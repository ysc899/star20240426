package kr.co.seesoft.nemo.starnemoapp.ui.bagsend;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.selection.Selection;
import androidx.recyclerview.selection.SelectionTracker;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import kr.co.seesoft.nemo.starnemoapp.R;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.ro.NemoBagSendListRO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.ro.NemoHospitalSearchRO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.ro.NemoVisitListRO;
import kr.co.seesoft.nemo.starnemoapp.ui.dialog.CustomCalendarDialog;
import kr.co.seesoft.nemo.starnemoapp.ui.dialog.CustomProgressDialog;
import kr.co.seesoft.nemo.starnemoapp.ui.transaction.TransactionViewModel;
import kr.co.seesoft.nemo.starnemoapp.ui.visitplan.VisitPlanFragment;
import kr.co.seesoft.nemo.starnemoapp.util.AndroidUtil;
import kr.co.seesoft.nemo.starnemoapp.util.Const;
import kr.co.seesoft.nemo.starnemoapp.util.DateUtil;

public class BagSendFragment extends Fragment {


    private BagSendViewModel bagSendViewModel;


    //리스트뷰 관련
    private RecyclerView rvViewList;
    private ListViewAdapter rvViewAdapter;
    private RecyclerView.LayoutManager rvViewLayoutManager;
    private SelectionTracker<Long> selection;
    private LinearLayout emptyView;

    Type adapterListType = new TypeToken<ArrayList<NemoBagSendListRO>>() {
    }.getType();
    //리스트뷰 관련 끝

    //캘린더 팝업
    private CustomCalendarDialog calendarDialog;
    private Handler handlerCalendar;

    private CustomProgressDialog progressDialog;

    // Title bar button
    private ImageButton btnBack, btnHome;

    private Button btnAdd;

    private TextView tvSearchDate;

    Gson gson = new Gson();


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        bagSendViewModel =
                ViewModelProviders.of(getActivity()).get(BagSendViewModel.class);
        View root = inflater.inflate(R.layout.fragment_bagsend, container, false);

        initUI(root);
        init();
        return root;
    }

    private void initUI(View view) {

        rvViewList = (RecyclerView) view.findViewById(R.id.rvViewList);
        rvViewList.setHasFixedSize(true);

        rvViewLayoutManager = new LinearLayoutManager(getActivity());
        rvViewList.setLayoutManager(rvViewLayoutManager);

        emptyView = (LinearLayout) view.findViewById(R.id.emptyView);

        tvSearchDate = (TextView) view.findViewById(R.id.tvSearchDate);

        btnBack = (ImageButton) view.findViewById(R.id.btnBack);
        btnHome = (ImageButton) view.findViewById(R.id.btnHome);

        btnAdd = (Button) view.findViewById(R.id.btnAdd);

    }

    private void init() {


        btnHome.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_navigation_menu_to_home));


        Click click = new Click();

        tvSearchDate.setOnClickListener(click);
        btnAdd.setOnClickListener(click);

        btnBack.setOnClickListener(click);

        handlerCalendar = new CalendarHandler(Looper.getMainLooper());
        calendarDialog = new CustomCalendarDialog(getActivity(), handlerCalendar);

        progressDialog = new CustomProgressDialog(getContext());
        bagSendViewModel.getProgressFlag().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {

                if(aBoolean){
                    progressDialog.show();
                }else{
                    progressDialog.dismiss();
                }

            }
        });


        rvViewAdapter = new ListViewAdapter(new ArrayList<>());

        rvViewList.setAdapter(rvViewAdapter);


        bagSendViewModel.getBagSendList().observe(getViewLifecycleOwner(), new Observer<ArrayList<NemoBagSendListRO>>() {
            @Override
            public void onChanged(ArrayList<NemoBagSendListRO> bagSendListROS) {
                rvViewAdapter.setPlanLists(bagSendListROS);
            }
        });

        bagSendViewModel.getSearchDateYmd().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {

                tvSearchDate.setText(s);

            }
        });

    }

    public ArrayList<NemoBagSendListRO> adapterListCopy(ArrayList<NemoBagSendListRO> fromList) {
        String copyJSON = gson.toJson(fromList);
        ArrayList<NemoBagSendListRO> toList = gson.fromJson(copyJSON, adapterListType);
        return toList;
    }

    protected void moveTask(NemoBagSendListRO t){

        Bundle bundle = new Bundle();

        bundle.putSerializable("visit_bagsend", t);

        Navigation.findNavController(getView()).navigate(R.id.action_visitPlanFragment_to_bagsendDetailFragment, bundle);

    }

    private class CalendarHandler extends Handler {
        public CalendarHandler(@NonNull Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);

            switch (msg.what){
                case Const.HANDLER_CALENDAR:

                    Date selectDate = (Date)msg.obj;
                    bagSendViewModel.setSearchDate(selectDate);

                    break;

            }

        }
    }


    public class Click implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {

                case R.id.tvSearchDate:

                    calendarDialog.show(bagSendViewModel.getSearchDate().getValue());
                    break;


                case R.id.btnAdd:

                    Bundle bundle = new Bundle();

                    bundle.putSerializable("visit_date", bagSendViewModel.getSearchDate().getValue());

                    Navigation.findNavController(getView()).navigate(R.id.action_visitPlanFragment_to_bagsendAddFragment, bundle);

                    break;

                case R.id.btnBack:

                    Navigation.findNavController(getView()).popBackStack();

                    break;
            }
        }
    }

    public class ListViewAdapter extends RecyclerView.Adapter<ListViewAdapter.ListViewHolder> {

        private ArrayList<NemoBagSendListRO> planLists;
        private SelectionTracker<Long> selectionTracker;

        public ListViewAdapter(ArrayList<NemoBagSendListRO> planLists) {
            this.planLists = adapterListCopy(planLists);

            setHasStableIds(true);
        }

        public void setSelectionTracker(SelectionTracker<Long> selectionTracker) {
            this.selectionTracker = selectionTracker;
        }

        public NemoBagSendListRO getItem(int position){
            return planLists.get(position);
        }

        public void setPlanLists(ArrayList<NemoBagSendListRO> planLists) {
            this.planLists = adapterListCopy(planLists);

            if (planLists.isEmpty()) {
                emptyView.setVisibility(View.VISIBLE);
                rvViewList.setVisibility(View.GONE);
            } else {
                emptyView.setVisibility(View.GONE);
                rvViewList.setVisibility(View.VISIBLE);
            }

            notifyDataSetChanged();
        }


        @Override
        public long getItemId(int position) {
            return position;
        }

        @NonNull
        @Override
        public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View adapterView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_bagsend_layout, parent, false);

            ListViewHolder holder = new ListViewHolder(adapterView);

            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
            NemoBagSendListRO t = this.planLists.get(position);

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

        public class ListViewHolder extends RecyclerView.ViewHolder {

            public TextView tvCpbgSendDt, tvCpbgArvlLocCntrNm, tvSenderNm , tvCpbgTrptDivNm;

            public View v;

            public NemoBagSendListRO item = null;

            public ListViewHolder(@NonNull View itemView) {
                super(itemView);
                v = itemView;
                tvCpbgSendDt = (TextView) itemView.findViewById(R.id.tvCpbgSendDt);
                tvCpbgArvlLocCntrNm = (TextView) itemView.findViewById(R.id.tvCpbgArvlLocCntrNm);
                tvSenderNm = (TextView) itemView.findViewById(R.id.tvSenderNm);
                tvCpbgTrptDivNm = (TextView) itemView.findViewById(R.id.tvCpbgTrptDivNm);

                itemView.setOnClickListener(view -> {
                    moveTask(item);
                });
            }

            public void bindItem(NemoBagSendListRO t) {

                AndroidUtil.log("getCpbgSendDt() : " + t.getCpbgSendDt());

                tvCpbgSendDt.setText(AndroidUtil.dispDate(t.getCpbgSendDt()));
                tvCpbgArvlLocCntrNm.setText(AndroidUtil.nullToString(t.getCpbgArvlLocCntrNm(),""));
                tvSenderNm.setText(AndroidUtil.nullToString(t.getSenderNm(),""));
                tvCpbgTrptDivNm.setText(AndroidUtil.nullToString(t.getCpbgTrptDivNm(),""));

                this.item = t;
            }

        }
    }

}