package kr.co.seesoft.nemo.starnemoapp.ui.visitplan;

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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
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
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import kr.co.seesoft.nemo.starnemoapp.R;
import kr.co.seesoft.nemo.starnemoapp.db.vo.PictureVO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.ro.NemoScheduleListRO;
import kr.co.seesoft.nemo.starnemoapp.ui.dialog.CustomCalendarDialog;
import kr.co.seesoft.nemo.starnemoapp.ui.dialog.CustomProgressDialog;
import kr.co.seesoft.nemo.starnemoapp.util.AndroidUtil;
import kr.co.seesoft.nemo.starnemoapp.util.Const;
import kr.co.seesoft.nemo.starnemoapp.util.DateUtil;

public class VisitPlanFragment extends Fragment {


    private VisitPlanViewModel visitPlanViewModel;


    //리스트뷰 관련
    private RecyclerView rvPlanList;
    private VisitPlanAdapter rvPlanAdapter;
    private RecyclerView.LayoutManager rvPlanLayoutManager;
    private ItemTouchHelper itemTouchHelper;
    protected ItemTouchHelperCallback rvPlanToucherHelper;

    private LinearLayout emptyView;

    Type visitPlanListType = new TypeToken<ArrayList<NemoScheduleListRO>>() {
    }.getType();
    //리스트뷰 관련 끝

    //캘린더 팝업
    private CustomCalendarDialog calendarDialog;
    private Handler handler;


    private Button btnAdd, btnEdit, btnCancel, btnSave, btnCopy, btnSend;
    private ImageButton btnDatePre, btnDateNext;
    private TextView tvVisitPlanDate;

    //편집 플래그
    private boolean editFlag = false;
    //복사 플래그
    private boolean copyFlag = false;
    private ArrayList<NemoScheduleListRO> copyTempList;

    private CustomProgressDialog progressDialog;

    // Title bar button
    private ImageButton btnBack, btnHome;

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
        btnCancel = (Button) view.findViewById(R.id.btnVisitPlanCancel);
        btnSave = (Button) view.findViewById(R.id.btnVisitPlanSave);
        btnCopy = (Button) view.findViewById(R.id.btnVisitPlanCopy);
        btnSend = (Button) view.findViewById(R.id.btnVisitPlanSend);
        btnDateNext = (ImageButton) view.findViewById(R.id.btnVisitPlanNext);
        btnDatePre = (ImageButton) view.findViewById(R.id.btnVisitPlanPre);

        tvVisitPlanDate = (TextView) view.findViewById(R.id.tvVisitPlanDate);

        rvPlanList = (RecyclerView) view.findViewById(R.id.rvVisitPlanList);
        rvPlanList.setHasFixedSize(true);

        emptyView = (LinearLayout) view.findViewById(R.id.emptyView);

        rvPlanLayoutManager = new LinearLayoutManager(getActivity());
        rvPlanList.setLayoutManager(rvPlanLayoutManager);

        btnBack = (ImageButton) view.findViewById(R.id.btnBack);
        btnHome = (ImageButton) view.findViewById(R.id.btnHome);
    }

    private void init() {

        Bundle receivedData = getArguments();
        if (receivedData != null) {
            String value = receivedData.getString("fromMenu");

            AndroidUtil.log("receivedData : " + value);

            if( "true".equals( value ))
            {
                visitPlanViewModel.setClear();
            }

            getArguments().clear();
        }


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
        btnBack.setOnClickListener(click);

        btnAdd.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_visitPlanFragment_to_visitPlanAddFragment));
        btnEdit.setOnClickListener(click);
        btnCancel.setOnClickListener(click);
        btnSave.setOnClickListener(click);
        btnCopy.setOnClickListener(click);
        btnSend.setOnClickListener(click);
        btnDateNext.setOnClickListener(click);
        btnDatePre.setOnClickListener(click);

        btnHome.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_navigation_menu_to_home));

        tvVisitPlanDate.setOnClickListener(click);

        handler = new VisitPlanHandler(Looper.getMainLooper());
        calendarDialog = new CustomCalendarDialog(getActivity(), handler);

        rvPlanAdapter = new VisitPlanAdapter(new ArrayList<>());

        rvPlanList.setAdapter(rvPlanAdapter);

        rvPlanToucherHelper = new ItemTouchHelperCallback(rvPlanAdapter);

        itemTouchHelper = new ItemTouchHelper(rvPlanToucherHelper);
        itemTouchHelper.attachToRecyclerView(rvPlanList);

        visitPlanViewModel.getVisitPlanList().observe(getViewLifecycleOwner(), new Observer<ArrayList<NemoScheduleListRO>>() {
            @Override
            public void onChanged(ArrayList<NemoScheduleListRO> visitPlanROS) {
                rvPlanAdapter.setPlanLists(visitPlanROS);

                //visitPlanViewModel.dispList();
            }
        });

        visitPlanViewModel.getPictureDatas().observe(getViewLifecycleOwner(), new Observer<List<PictureVO>>() {
            @Override
            public void onChanged(List<PictureVO> pictureVOS) {
                //AndroidUtil.log("안오지??");
                rvPlanAdapter.notifyDataSetChanged();
            }
        });



    }


    protected void moveTask(NemoScheduleListRO t){
        if(editFlag){
            return;
        }

        if(DateUtil.getFormatString(visitPlanViewModel.getVisPlanDate().getValue(), "yyyyMMdd").equals(DateUtil.getFormatString(new Date(), "yyyyMMdd"))) {

            if(!visitPlanViewModel.getUpdateFlag().getValue()) {
                Bundle bundle = new Bundle();

                bundle.putString("register_day", DateUtil.getFormatString(visitPlanViewModel.getVisPlanDate().getValue(), "yyyyMMdd"));
                bundle.putSerializable("visit_hospital", t);

                if( "Y".equals(t.getRndYn()))
                {
                    if( t.getRndTakePlanUkeyid() == null || t.getRndTakePlanUkeyid().length() == 0 )
                    {
                        AndroidUtil.toast(getContext(),"rndTakePlanUkeyid가 없습니다.");
                        return;
                    }
                    Navigation.findNavController(getView()).navigate(R.id.action_visitPlanFragment_to_registerRndFragment, bundle);
                }
                else
                {
                    Navigation.findNavController(getView()).navigate(R.id.action_visitPlanFragment_to_registerFragment, bundle);
                }
            }else{
                AndroidUtil.toast(getContext(), "추가 편집된 병원 정보가 있습니다. 저장 후 다시 시도하세요.");
            }
        }else{
//            AndroidUtil.toast(getContext(), "오늘 날짜만 의뢰지 접수가 가능 합니다.");
            Bundle bundle = new Bundle();

            bundle.putString("register_day", DateUtil.getFormatString(visitPlanViewModel.getVisPlanDate().getValue(), "yyyyMMdd"));
            bundle.putSerializable("visit_hospital", t);

            if( "Y".equals(t.getRndYn()))
            {
                if( t.getRndTakePlanUkeyid() == null || t.getRndTakePlanUkeyid().length() == 0 )
                {
                    AndroidUtil.toast(getContext(),"rndTakePlanUkeyid가 없습니다.");
                    return;
                }
                Navigation.findNavController(getView()).navigate(R.id.action_visitPlanFragment_to_registerRndFragment, bundle);
            }
            else
            {
                Navigation.findNavController(getView()).navigate(R.id.action_visitPlanFragment_to_registerFragment, bundle);
            }
        }

    }

    public ArrayList<NemoScheduleListRO> visitPlanListCopy(ArrayList<NemoScheduleListRO> fromList){
        String copyJSON = gson.toJson(fromList);
        ArrayList<NemoScheduleListRO> toList = gson.fromJson(copyJSON, visitPlanListType);
        toList.sort(NemoScheduleListRO::compareTo);
        return toList;
    }

    public void finishEdit(){
        rvPlanToucherHelper.setTouchFlag(false);
        btnCopy.setVisibility(View.VISIBLE);
//        btnSend.setVisibility(View.GONE);
        btnAdd.setVisibility(View.VISIBLE);
        btnDatePre.setVisibility(View.VISIBLE);
        btnDateNext.setVisibility(View.VISIBLE);
        btnCancel.setVisibility(View.GONE);
        btnSave.setVisibility(View.GONE);
        btnEdit.setVisibility(View.VISIBLE);
        editFlag = false;

    }

    public void finishCopy(){
        btnCopy.setVisibility(View.VISIBLE);
//        btnSend.setVisibility(View.GONE);
        btnAdd.setVisibility(View.VISIBLE);
        btnDatePre.setVisibility(View.VISIBLE);
        btnDateNext.setVisibility(View.VISIBLE);
        btnCancel.setVisibility(View.GONE);
        btnSave.setVisibility(View.GONE);
        btnEdit.setVisibility(View.VISIBLE);

    }

    public void setBeforeDateMode(){
        rvPlanToucherHelper.setTouchFlag(false);
//        btnSend.setVisibility(View.GONE);
        btnAdd.setVisibility(View.GONE);
        btnEdit.setVisibility(View.GONE);
        btnSave.setVisibility(View.GONE);

        if(!copyFlag){
            btnCopy.setVisibility(View.VISIBLE);
        }
        else
        {
            btnCopy.setVisibility(View.GONE);
        }


    }

    public void setAfterDateMode(){
        finishEdit();
    }

    private void showDateModel()
    {
        Date sDate = visitPlanViewModel.getVisPlanDate().getValue();
        Date tDate = DateUtil.getDate(DateUtil.getFormatString(new Date(), "yyyyMMdd"),"yyyyMMdd");

        long sTime = sDate.getTime();
        long tTime = tDate.getTime();

        if(sTime < tTime){
            setBeforeDateMode();
        }else{
            setAfterDateMode();
        }
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
//                    btnSend.setVisibility(View.GONE);
                    btnAdd.setVisibility(View.GONE);
                    btnDatePre.setVisibility(View.GONE);
                    btnDateNext.setVisibility(View.GONE);
                    btnCancel.setVisibility(View.VISIBLE);
                    btnSave.setVisibility(View.VISIBLE);

                    rvPlanAdapter.setPlanLists(visitPlanViewModel.getVisitPlanList().getValue());

                    break;

                case R.id.btnVisitPlanCancel:


                    AndroidUtil.log("editFlag : " + editFlag);


                    // Edit 모드인 경우
                    if( editFlag ) {

                        rvPlanAdapter.setPlanLists(visitPlanViewModel.getVisitPlanList().getValue());
//                        visitPlanViewModel.getAPIVisPlanListData();

                        finishEdit();

                    }
                    // 붙여 넣기 모드
                    else
                    {
                        copyTempList = new ArrayList<NemoScheduleListRO>();
                        //visitPlanViewModel.setVisPlanListData(copyTempList);

                        visitPlanViewModel.getUpdateFlag().setValue(true);

                        copyFlag = false;

                        visitPlanViewModel.setVisPlanDate(visitPlanViewModel.getVisPlanDate().getValue());

                        finishCopy();
                    }



                    break;

                case R.id.btnVisitPlanCopy:

                    if(copyFlag){

//                        btnSend.setVisibility(View.VISIBLE);

                        visitPlanViewModel.setVisPlanListData(copyTempList);

                        visitPlanViewModel.getUpdateFlag().setValue(true);

                        AndroidUtil.toast(getContext(), "붙여넣기 완료");
                        btnCopy.setText("복사");

                        btnEdit.setVisibility(View.GONE);
                        btnCopy.setVisibility(View.GONE);
//                        btnSend.setVisibility(View.GONE);
                        btnAdd.setVisibility(View.GONE);
                        btnDatePre.setVisibility(View.GONE);
                        btnDateNext.setVisibility(View.GONE);
                        btnCancel.setVisibility(View.VISIBLE);
                        btnSave.setVisibility(View.VISIBLE);

                        copyFlag = false;

                    }else {

//                        btnSend.setVisibility(View.GONE);

                        copyTempList = visitPlanListCopy(visitPlanViewModel.getVisitPlanList().getValue());
                        AndroidUtil.toast(getContext(), "복사 되었습니다.");
                        btnCopy.setText("붙여넣기");

                        copyFlag = true;

                        showDateModel();

                    }

                    //copyFlag = !copyFlag;

                    break;

                case R.id.btnVisitPlanSend:

                    visitPlanViewModel.sendPlanListData();
                    break;


                case R.id.btnVisitPlanSave:

                    // Edit 모드인 경우
                    if( editFlag )
                    {
                        finishEdit();

                        ArrayList<NemoScheduleListRO> editVisitPlanList = rvPlanAdapter.getPlanLists();

                        visitPlanViewModel.setEditVisPlanListData(visitPlanListCopy(editVisitPlanList));

                        visitPlanViewModel.setUpdateFlag(true);

                        visitPlanViewModel.updatePlanListData();
                    }
                    // 붙여 넣기 모드
                    else
                    {
                        //visitPlanViewModel.sendPlanListData();
                        visitPlanViewModel.sendBeforeDeleteAll();
                    }

                    finishEdit();


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

                case R.id.btnBack:

                    Navigation.findNavController(getView()).popBackStack();

                    break;
            }
        }
    }

    public class VisitPlanAdapter extends RecyclerView.Adapter<VisitPlanAdapter.VisitPlanViewHolder> implements ItemTouchHelperListener {

        private ArrayList<NemoScheduleListRO> planLists;

        public VisitPlanAdapter(ArrayList<NemoScheduleListRO> planLists) {
            this.planLists = visitPlanListCopy(planLists);

        }

        public ArrayList<NemoScheduleListRO> getPlanLists() {
            return planLists;
        }

        public void setPlanLists(ArrayList<NemoScheduleListRO> planLists) {
            this.planLists=  visitPlanListCopy(planLists);

            if (planLists.isEmpty()) {
                emptyView.setVisibility(View.VISIBLE);
                rvPlanList.setVisibility(View.GONE);
            } else {
                emptyView.setVisibility(View.GONE);
                rvPlanList.setVisibility(View.VISIBLE);
            }

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
            NemoScheduleListRO t = this.planLists.get(position);
            holder.bindItem(t);

            if (position % 2 == 0) {
                holder.itemView.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), android.R.color.transparent));
            }
            else
            {
                holder.itemView.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.even_row_color));
            }
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

            AndroidUtil.log("tmpOrder : " + tmpOrder);

            this.planLists.sort(NemoScheduleListRO::compareTo);
            notifyItemMoved(fromPosition, toPosition);
            notifyItemChanged(fromPosition);
            notifyItemChanged(toPosition);
            return true;
        }

        @Override
        public void onItemSwip(int position) {
            this.planLists.remove(position);

            notifyItemRemoved(position);

            int count = 1;
            for (NemoScheduleListRO t : planLists) {
                t.setOrder(count++);
            }

            notifyDataSetChanged();
        }





        public class VisitPlanViewHolder extends RecyclerView.ViewHolder {

            public View v;
            public TextView tvOrder, tvCode, tvName, tvCount;
            public ImageView tvSort , tvRnd;
            public LinearLayout lSendLeft, lSendNoLeft;
            public NemoScheduleListRO item = null;

            public VisitPlanViewHolder(@NonNull View itemView) {
                super(itemView);
                tvOrder = (TextView) itemView.findViewById(R.id.tvVisitPlanListOrder);
                tvCode = (TextView) itemView.findViewById(R.id.tvVisitPlanListCode);
                tvName = (TextView) itemView.findViewById(R.id.tvVisitPlanListName);
                tvCount = (TextView) itemView.findViewById(R.id.tvVisitPlanListCount);

                tvSort = (ImageView) itemView.findViewById(R.id.icon_sort);
                tvRnd = (ImageView) itemView.findViewById(R.id.icon_rnd);

                lSendLeft = (LinearLayout) itemView.findViewById(R.id.lSendLeft);
                lSendNoLeft = (LinearLayout) itemView.findViewById(R.id.lSendNoLeft);

                v = itemView;

                itemView.setOnClickListener(view -> {
                    moveTask(item);
                });
            }

            public void bindItem(NemoScheduleListRO t){
                tvOrder.setText(String.valueOf(t.getOrder()));
                tvCode.setText(t.getCustCd());
                tvName.setText(t.getCustNm());
                tvCount.setText(String.valueOf(0));
                this.item = t;

                if( "Y".equals(t.getRndYn()) )
                {
                    tvRnd.setVisibility(View.VISIBLE);
                }
                else
                {
                    tvRnd.setVisibility(View.GONE);
                }

                if(visitPlanViewModel.getPictureDatas().getValue() != null) {
                    List<PictureVO> pDatas = visitPlanViewModel.getPictureDatas().getValue();

                    //AndroidUtil.log("hospitalYmdDatas : " + pDatas);

                    List<PictureVO> hList = pDatas.stream().filter(it -> {
                        return it.hospitalKey.equals(t.getCustCd());
                    }).collect(Collectors.toList());

//                    String backgroundColorString ="#5CB85C";

                    if(hList.size() > 0){
                        int noSendCount = (int)hList.stream().filter(it ->{return !it.sendFlag;}).count();

                        AndroidUtil.log("noSendCount : " + noSendCount);

                        if(noSendCount == 0){
                            lSendLeft.setVisibility(View.VISIBLE);
                            lSendNoLeft.setVisibility(View.GONE);
//                            backgroundColorString = "#5CB85C";
                        }else{
                            lSendLeft.setVisibility(View.GONE);
                            lSendNoLeft.setVisibility(View.VISIBLE);
//                            backgroundColorString = "#F0AD4E";
                        }

//                        this.v.setBackgroundColor(Color.parseColor(backgroundColorString));
//                        this.tvOrder.setBackgroundColor(Color.parseColor(backgroundColorString));
//                        this.tvCode.setBackgroundColor(Color.parseColor(backgroundColorString));
//                        this.tvName.setBackgroundColor(Color.parseColor(backgroundColorString));
//                        this.tvCount.setBackgroundColor(Color.parseColor(backgroundColorString));
//                        this.tvOrder.setTextColor(Color.WHITE);
//                        this.tvCode.setTextColor(Color.WHITE);
//                        this.tvName.setTextColor(Color.WHITE);
//                        this.tvCount.setTextColor(Color.WHITE);
                    }
                    else
                    {
                        lSendLeft.setVisibility(View.GONE);
                        lSendNoLeft.setVisibility(View.GONE);
                    }

                    AndroidUtil.log(t.getCustNm() + " : " + hList.size());

                }

                if( editFlag == true )
                {
                    tvSort.setVisibility(View.VISIBLE);
                }
                else
                {
                    tvSort.setVisibility(View.GONE);
                }



            }
        }
    }
}