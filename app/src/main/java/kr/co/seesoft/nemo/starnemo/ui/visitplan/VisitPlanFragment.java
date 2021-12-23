package kr.co.seesoft.nemo.starnemo.ui.visitplan;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import kr.co.seesoft.nemo.starnemo.R;
import kr.co.seesoft.nemo.starnemo.api.ro.VisitPlanRO;
import kr.co.seesoft.nemo.starnemo.db.vo.PictureVO;
import kr.co.seesoft.nemo.starnemo.nemoapi.ro.NemoVisitListRO;
import kr.co.seesoft.nemo.starnemo.ui.dialog.CustomCalendarDialog;
import kr.co.seesoft.nemo.starnemo.ui.dialog.CustomProgressDialog;
import kr.co.seesoft.nemo.starnemo.util.AndroidUtil;
import kr.co.seesoft.nemo.starnemo.util.Const;
import kr.co.seesoft.nemo.starnemo.util.DateUtil;

public class VisitPlanFragment extends Fragment {


    private VisitPlanViewModel visitPlanViewModel;


    //리스트뷰 관련
    private RecyclerView rvPlanList;
    private VisitPlanAdapter rvPlanAdapter;
    private RecyclerView.LayoutManager rvPlanLayoutManager;
    private ItemTouchHelper itemTouchHelper;
    protected ItemTouchHelperCallback rvPlanToucherHelper;

    Type visitPlanListType = new TypeToken<ArrayList<NemoVisitListRO>>() {
    }.getType();
    //리스트뷰 관련 끝

    //캘린더 팝업
    private CustomCalendarDialog calendarDialog;
    private Handler handler;


    private Button btnAdd, btnEdit, btnCencel, btnSave, btnCopy, btnSend;
    private ImageButton btnDatePre, btnDateNext;
    private TextView tvVisitPlanDate;

    //편집 플래그
    private boolean editFlag = false;
    //복사 플래그
    private boolean copyFlag = false;
    private ArrayList<NemoVisitListRO> copyTempList;

    private CustomProgressDialog progressDialog;

    Gson gson = new Gson();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        visitPlanViewModel =
                ViewModelProviders.of(getActivity()).get(VisitPlanViewModel.class);
        View root = inflater.inflate(R.layout.fragment_visit_plan, container, false);

        initUI(root);
        init();
        return root;
    }

    private void initUI(View view) {

        btnAdd = (Button) view.findViewById(R.id.btnVisitPlanAdd);
        btnEdit = (Button) view.findViewById(R.id.btnVisitPlanEdit);
        btnCencel = (Button) view.findViewById(R.id.btnVisitPlanCancel);
        btnSave = (Button) view.findViewById(R.id.btnVisitPlanSave);
        btnCopy = (Button) view.findViewById(R.id.btnVisitPlanCopy);
        btnSend = (Button) view.findViewById(R.id.btnVisitPlanSend);
        btnDateNext = (ImageButton) view.findViewById(R.id.btnVisitPlanNext);
        btnDatePre = (ImageButton) view.findViewById(R.id.btnVisitPlanPre);

        tvVisitPlanDate = (TextView) view.findViewById(R.id.tvVisitPlanDate);

        rvPlanList = (RecyclerView) view.findViewById(R.id.rvVisitPlanList);
        rvPlanList.setHasFixedSize(true);

        rvPlanLayoutManager = new LinearLayoutManager(getActivity());
        rvPlanList.setLayoutManager(rvPlanLayoutManager);
    }

    private void init() {
        visitPlanViewModel.getVisPlanYmd().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                tvVisitPlanDate.setText(s);

                Date sDate = visitPlanViewModel.getVisPlanDate().getValue();
                Date tDate = DateUtil.getDate(DateUtil.getFormatString(new Date(), "yyyyMMdd"),"yyyyMMdd");

                long sTime = sDate.getTime();
                long tTime = tDate.getTime();
//
//                AndroidUtil.log("sDate : " + sTime);
//                AndroidUtil.log("tDate : " + tTime);

                if(sTime < tTime){
                    setBeforeDateMode();
                }else{
                    setAfterDateMode();
                }

                visitPlanViewModel.setNemoRepository(getViewLifecycleOwner());

            }
        });

        progressDialog = new CustomProgressDialog(getContext());

        progressDialog = new CustomProgressDialog(getContext());
        visitPlanViewModel.getProgressFlag().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {

                if(aBoolean){
                    progressDialog.show();
                }else{
                    progressDialog.dismiss();
                }

            }
        });


        Click click = new Click();
        btnAdd.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_visitPlanFragment_to_visitPlanAddFragment));
        btnEdit.setOnClickListener(click);
        btnCencel.setOnClickListener(click);
        btnSave.setOnClickListener(click);
        btnCopy.setOnClickListener(click);
        btnSend.setOnClickListener(click);
        btnDateNext.setOnClickListener(click);
        btnDatePre.setOnClickListener(click);

        tvVisitPlanDate.setOnClickListener(click);

        handler = new VisitPlanHandler(Looper.getMainLooper());
        calendarDialog = new CustomCalendarDialog(getActivity(), handler);

        rvPlanAdapter = new VisitPlanAdapter(new ArrayList<>());

        rvPlanList.setAdapter(rvPlanAdapter);

        rvPlanToucherHelper = new ItemTouchHelperCallback(rvPlanAdapter);

        itemTouchHelper = new ItemTouchHelper(rvPlanToucherHelper);
        itemTouchHelper.attachToRecyclerView(rvPlanList);

        visitPlanViewModel.getVisitPlanList().observe(getViewLifecycleOwner(), new Observer<ArrayList<NemoVisitListRO>>() {
            @Override
            public void onChanged(ArrayList<NemoVisitListRO> visitPlanROS) {
                rvPlanAdapter.setPlanLists(visitPlanROS);
            }
        });

        visitPlanViewModel.getPictureDatas().observe(getViewLifecycleOwner(), new Observer<List<PictureVO>>() {
            @Override
            public void onChanged(List<PictureVO> pictureVOS) {
                AndroidUtil.log("안오지??");
                rvPlanAdapter.notifyDataSetChanged();
            }
        });

    }

    protected void moveTask(NemoVisitListRO t){
        if(editFlag){
            return;
        }

        if(DateUtil.getFormatString(visitPlanViewModel.getVisPlanDate().getValue(), "yyyyMMdd").equals(DateUtil.getFormatString(new Date(), "yyyyMMdd"))) {

            if(!visitPlanViewModel.getUpdateFlag().getValue()) {
                Bundle bundle = new Bundle();

                bundle.putString("register_day", DateUtil.getFormatString(visitPlanViewModel.getVisPlanDate().getValue(), "yyyyMMdd"));
                bundle.putSerializable("visit_hospital", t);

                Navigation.findNavController(getView()).navigate(R.id.action_visitPlanFragment_to_registerFragment, bundle);
            }else{
                AndroidUtil.toast(getContext(), "추가 편집된 병원 정보가 있습니다. 전송을 눌러주세요.");
            }
        }else{
//            AndroidUtil.toast(getContext(), "오늘 날짜만 의뢰지 접수가 가능 합니다.");
            Bundle bundle = new Bundle();

            bundle.putString("register_day", DateUtil.getFormatString(visitPlanViewModel.getVisPlanDate().getValue(), "yyyyMMdd"));
            bundle.putSerializable("visit_hospital", t);

            Navigation.findNavController(getView()).navigate(R.id.action_visitPlanFragment_to_registerFragment, bundle);

        }

    }

    public ArrayList<NemoVisitListRO> visitPlanListCopy(ArrayList<NemoVisitListRO> fromList){
        String copyJSON = gson.toJson(fromList);
        ArrayList<NemoVisitListRO> toList = gson.fromJson(copyJSON, visitPlanListType);
        toList.sort(NemoVisitListRO::compareTo);
        return toList;
    }

    public void finishEdit(){
        rvPlanToucherHelper.setTouchFlag(false);
        btnCopy.setVisibility(View.VISIBLE);
        btnSend.setVisibility(View.VISIBLE);
        btnAdd.setVisibility(View.VISIBLE);
        btnDatePre.setVisibility(View.VISIBLE);
        btnDateNext.setVisibility(View.VISIBLE);
        btnCencel.setVisibility(View.GONE);
        btnSave.setVisibility(View.GONE);
        btnEdit.setVisibility(View.VISIBLE);
        editFlag = false;
    }

    public void setBeforeDateMode(){
        rvPlanToucherHelper.setTouchFlag(false);
        btnSend.setVisibility(View.GONE);
        btnAdd.setVisibility(View.GONE);
        btnEdit.setVisibility(View.GONE);
    }

    public void setAfterDateMode(){
        finishEdit();
    }


    private class VisitPlanHandler extends Handler {
        public VisitPlanHandler(@NonNull Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);

            switch (msg.what){
                case Const.HANDLER_CALENDAR:

                    Date selectDate = (Date)msg.obj;
                    visitPlanViewModel.setVisPlanDate(selectDate);

                    break;

            }

        }
    }

    public class Click implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {

                case R.id.btnVisitPlanEdit:

                    editFlag = true;
                    rvPlanToucherHelper.setTouchFlag(true);

                    btnEdit.setVisibility(View.GONE);
                    btnCopy.setVisibility(View.GONE);
                    btnSend.setVisibility(View.GONE);
                    btnAdd.setVisibility(View.GONE);
                    btnDatePre.setVisibility(View.GONE);
                    btnDateNext.setVisibility(View.GONE);
                    btnCencel.setVisibility(View.VISIBLE);
                    btnSave.setVisibility(View.VISIBLE);
                    break;

                case R.id.btnVisitPlanCancel:
                    finishEdit();

                    rvPlanAdapter.setPlanLists(visitPlanViewModel.getVisitPlanList().getValue());

                    break;

                case R.id.btnVisitPlanCopy:

                    if(copyFlag){

                        visitPlanViewModel.setVisPlanListData(copyTempList);

                        visitPlanViewModel.getUpdateFlag().setValue(true);

                        AndroidUtil.toast(getContext(), "붙여넣기 완료");
                        btnCopy.setText("복사");

                    }else {
                        copyTempList = visitPlanListCopy(visitPlanViewModel.getVisitPlanList().getValue());
                        AndroidUtil.toast(getContext(), "복사 되었습니다.");
                        btnCopy.setText("붙여넣기");
                    }

                    copyFlag = !copyFlag;

                    break;

                case R.id.btnVisitPlanSend:

                    visitPlanViewModel.sendPlanListData();
                    break;


                case R.id.btnVisitPlanSave:
                    finishEdit();

                    ArrayList<NemoVisitListRO> editVisitPlanList = rvPlanAdapter.getPlanLists();

                    visitPlanViewModel.setEditVisPlanListData(visitPlanListCopy(editVisitPlanList));

                    visitPlanViewModel.setUpdateFlag(true);

                    break;
                case R.id.tvVisitPlanDate:
                    if(editFlag){
                        AndroidUtil.toast(getContext(), "편집 모드 종료 후 사용하세요");
                        return;
                    }
                    calendarDialog.show(visitPlanViewModel.getVisPlanDate().getValue());
                    break;
                case R.id.btnVisitPlanNext:
                    visitPlanViewModel.calcVisPlanDate(1);
                    break;
                case R.id.btnVisitPlanPre:
                    visitPlanViewModel.calcVisPlanDate(-1);
                    break;
            }
        }
    }

    public class VisitPlanAdapter extends RecyclerView.Adapter<VisitPlanAdapter.VisitPlanViewHolder> implements ItemTouchHelperListener {

        private ArrayList<NemoVisitListRO> planLists;

        public VisitPlanAdapter(ArrayList<NemoVisitListRO> planLists) {
            this.planLists = visitPlanListCopy(planLists);

        }

        public ArrayList<NemoVisitListRO> getPlanLists() {
            return planLists;
        }

        public void setPlanLists(ArrayList<NemoVisitListRO> planLists) {
            this.planLists=  visitPlanListCopy(planLists);
            notifyDataSetChanged();
        }



        @NonNull
        @Override
        public VisitPlanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View adapterView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_visit_plan_layout, parent, false);

            VisitPlanViewHolder holder = new VisitPlanViewHolder(adapterView);

            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull VisitPlanViewHolder holder, int position) {
            NemoVisitListRO t = this.planLists.get(position);
            holder.bindItem(t);
        }

        @Override
        public int getItemCount() {
            return this.planLists.size();
        }

        @Override
        public boolean onItemMove(int fromPosition, int toPosition) {

            int tmpOrder = this.planLists.get(fromPosition).getOrder();
            this.planLists.get(fromPosition).setOrder( this.planLists.get(toPosition).getOrder());
            this.planLists.get(toPosition).setOrder(tmpOrder);

            this.planLists.sort(NemoVisitListRO::compareTo);
            notifyItemMoved(fromPosition, toPosition);
            notifyItemChanged(fromPosition);
            notifyItemChanged(toPosition);
            return true;
        }

        @Override
        public void onItemSwip(int position) {
            this.planLists.remove(position);
            notifyItemRemoved(position);
        }

        public class VisitPlanViewHolder extends RecyclerView.ViewHolder {

            public View v;
            public TextView tvOrder, tvCode, tvName, tvCount;
            public NemoVisitListRO item = null;

            public VisitPlanViewHolder(@NonNull View itemView) {
                super(itemView);
                tvOrder = (TextView) itemView.findViewById(R.id.tvVisitPlanListOrder);
                tvCode = (TextView) itemView.findViewById(R.id.tvVisitPlanListCode);
                tvName = (TextView) itemView.findViewById(R.id.tvVisitPlanListName);
                tvCount = (TextView) itemView.findViewById(R.id.tvVisitPlanListCount);
                v = itemView;

                itemView.setOnClickListener(view -> {
                    moveTask(item);
                });
            }

            public void bindItem(NemoVisitListRO t){
                tvOrder.setText(String.valueOf(t.getOrder()));
                tvCode.setText(t.getHospitalCode());
                tvName.setText(t.getHospitalName());
                tvCount.setText(String.valueOf(0));
                this.item = t;

                this.v.setBackgroundColor(Color.WHITE);
                this.tvOrder.setBackgroundColor(Color.WHITE);
                this.tvCode.setBackgroundColor(Color.WHITE);
                this.tvName.setBackgroundColor(Color.WHITE);
                this.tvCount.setBackgroundColor(Color.WHITE);
                this.tvOrder.setTextColor(Color.parseColor("#999999"));
                this.tvCode.setTextColor(Color.parseColor("#999999"));
                this.tvName.setTextColor(Color.parseColor("#999999"));
                this.tvCount.setTextColor(Color.parseColor("#999999"));


                if(visitPlanViewModel.getPictureDatas().getValue() != null) {
                    List<PictureVO> pDatas = visitPlanViewModel.getPictureDatas().getValue();
                    List<PictureVO> hList = pDatas.stream().filter(it -> {
                        return it.hospitalKey.equals(t.getHospitalCode());
                    }).collect(Collectors.toList());

                    String backgroundColorString ="#5CB85C";

                    if(hList.size() > 0){
                        int noSendCount = (int)hList.stream().filter(it ->{return !it.sendFlag;}).count();
                        if(noSendCount == 0){
                            backgroundColorString = "#5CB85C";
                        }else{
                            backgroundColorString = "#F0AD4E";
                        }

                        this.v.setBackgroundColor(Color.parseColor(backgroundColorString));
                        this.tvOrder.setBackgroundColor(Color.parseColor(backgroundColorString));
                        this.tvCode.setBackgroundColor(Color.parseColor(backgroundColorString));
                        this.tvName.setBackgroundColor(Color.parseColor(backgroundColorString));
                        this.tvCount.setBackgroundColor(Color.parseColor(backgroundColorString));
                        this.tvOrder.setTextColor(Color.WHITE);
                        this.tvCode.setTextColor(Color.WHITE);
                        this.tvName.setTextColor(Color.WHITE);
                        this.tvCount.setTextColor(Color.WHITE);
                    }

                    AndroidUtil.log(t.getHospitalName() + " : " + hList.size());

                }

            }
        }
    }
}