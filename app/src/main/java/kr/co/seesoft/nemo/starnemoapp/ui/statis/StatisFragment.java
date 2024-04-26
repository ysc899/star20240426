package kr.co.seesoft.nemo.starnemoapp.ui.statis;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Date;

import kr.co.seesoft.nemo.starnemoapp.R;
import kr.co.seesoft.nemo.starnemoapp.api.ro.VisitPlanRO;
import kr.co.seesoft.nemo.starnemoapp.ui.dialog.CustomCalendarDialog;
import kr.co.seesoft.nemo.starnemoapp.util.Const;

public class StatisFragment extends Fragment {


    private StatisViewModel statisViewModel;


    //리스트뷰 관련
    private RecyclerView rvPlanList;
    private StatisAdapter rvPlanAdapter;
    private RecyclerView.LayoutManager rvPlanLayoutManager;
    
    //리스트뷰 관련 끝

    //캘린더 팝업
    private CustomCalendarDialog calendarDialog;
    private Handler handler;

    private ImageButton btnDatePre, btnDateNext;
    private TextView tvStatisDate;



    Gson gson = new Gson();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        statisViewModel =
                ViewModelProviders.of(getActivity()).get(StatisViewModel.class);
        View root = inflater.inflate(R.layout.fragment_statis, container, false);

        initUI(root);
        init();
        return root;
    }

    private void initUI(View view) {

        btnDateNext = (ImageButton) view.findViewById(R.id.btnStatisNext);
        btnDatePre = (ImageButton) view.findViewById(R.id.btnStatisPre);

        tvStatisDate = (TextView) view.findViewById(R.id.tvStatisDate);

        rvPlanList = (RecyclerView) view.findViewById(R.id.rvStatisList);
        rvPlanList.setHasFixedSize(true);

        rvPlanLayoutManager = new LinearLayoutManager(getActivity());
        rvPlanList.setLayoutManager(rvPlanLayoutManager);
    }

    private void init() {
        statisViewModel.getVisPlanYmd().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                tvStatisDate.setText(s);
            }
        });


        Click click = new Click();

        btnDateNext.setOnClickListener(click);
        btnDatePre.setOnClickListener(click);

        tvStatisDate.setOnClickListener(click);

        handler = new StatisHandler(Looper.getMainLooper());
        calendarDialog = new CustomCalendarDialog(getActivity(), handler);

        rvPlanAdapter = new StatisAdapter(new ArrayList<>());

        rvPlanList.setAdapter(rvPlanAdapter);



        statisViewModel.getVisitPlanList().observe(getViewLifecycleOwner(), new Observer<ArrayList<VisitPlanRO>>() {
            @Override
            public void onChanged(ArrayList<VisitPlanRO> visitPlanROS) {
                rvPlanAdapter.setPlanLists(visitPlanROS);
            }
        });

    }


    private class StatisHandler extends Handler {
        public StatisHandler(@NonNull Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);

            switch (msg.what){
                case Const.HANDLER_CALENDAR:

                    Date selectDate = (Date)msg.obj;
                    statisViewModel.setVisPlanDate(selectDate);

                    break;

            }

        }
    }

    public class Click implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {

                case R.id.tvStatisDate:

                    calendarDialog.show(statisViewModel.getVisPlanDate().getValue());
                    break;
                case R.id.btnStatisNext:
                    statisViewModel.calcVisPlanDate(1);
                    break;
                case R.id.btnStatisPre:
                    statisViewModel.calcVisPlanDate(-1);
                    break;
            }
        }
    }

    public class StatisAdapter extends RecyclerView.Adapter<StatisAdapter.StatisViewHolder>{

        private ArrayList<VisitPlanRO> visitLists;

        public StatisAdapter(ArrayList<VisitPlanRO> visitLists) {
            this.visitLists = visitLists;
        }

        public ArrayList<VisitPlanRO> getPlanLists() {
            return visitLists;
        }

        public void setPlanLists(ArrayList<VisitPlanRO> visitLists) {
            this.visitLists = visitLists;
            notifyDataSetChanged();
        }



        @NonNull
        @Override
        public StatisViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View adapterView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_statis_layout, parent, false);

            StatisViewHolder holder = new StatisViewHolder(adapterView);

            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull StatisViewHolder holder, int position) {
            VisitPlanRO t = this.visitLists.get(position);
            holder.bindItem(t);
        }

        @Override
        public int getItemCount() {
            return this.visitLists.size();
        }



        public class StatisViewHolder extends RecyclerView.ViewHolder {

            public TextView tvOrder, tvCode, tvName, tvCount;
            public VisitPlanRO item = null;

            public StatisViewHolder(@NonNull View itemView) {
                super(itemView);
                tvOrder = (TextView) itemView.findViewById(R.id.tvStatisListOrder);
                tvCode = (TextView) itemView.findViewById(R.id.tvStatisListCode);
                tvName = (TextView) itemView.findViewById(R.id.tvStatisListName);
                tvCount = (TextView) itemView.findViewById(R.id.tvStatisListCount);

            }

            public void bindItem(VisitPlanRO t){
                tvOrder.setText(String.valueOf(t.order));
                tvCode.setText(t.hospitalCode);
                tvName.setText(t.hospitalName);
                tvCount.setText(String.valueOf(t.count));
                this.item = t;
            }
        }
    }
}