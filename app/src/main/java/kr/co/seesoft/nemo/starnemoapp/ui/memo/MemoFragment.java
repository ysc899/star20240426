package kr.co.seesoft.nemo.starnemoapp.ui.memo;

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
import kr.co.seesoft.nemo.starnemoapp.nemoapi.ro.NemoCustomerMemoListRO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.ro.NemoHospitalSearchRO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.ro.NemoSalesTransactionListRO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.ro.NemoVisitListRO;
import kr.co.seesoft.nemo.starnemoapp.ui.bagsend.BagSendFragment;
import kr.co.seesoft.nemo.starnemoapp.ui.bagsend.BagSendViewModel;
import kr.co.seesoft.nemo.starnemoapp.ui.dialog.CustomCalendarDialog;
import kr.co.seesoft.nemo.starnemoapp.ui.dialog.CustomProgressDialog;
import kr.co.seesoft.nemo.starnemoapp.ui.transaction.TransactionViewModel;
import kr.co.seesoft.nemo.starnemoapp.ui.transaction.detail.TransactionDetailFragment;
import kr.co.seesoft.nemo.starnemoapp.util.AndroidUtil;
import kr.co.seesoft.nemo.starnemoapp.util.Const;

public class MemoFragment extends Fragment {


    private MemoViewModel memoViewModel;


    //리스트뷰 관련
    private RecyclerView rvViewList;
    private ListViewAdapter rvViewAdapter;
    private RecyclerView.LayoutManager rvViewLayoutManager;
    private SelectionTracker<Long> selection;

    private LinearLayout emptyView;

    Type adapterListType = new TypeToken<ArrayList<NemoCustomerMemoListRO>>() {
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
        memoViewModel =
                ViewModelProviders.of(getActivity()).get(MemoViewModel.class);
        View root = inflater.inflate(R.layout.fragment_memo, container, false);

        initUI(root);
        init();
        return root;
    }

    private void initUI(View view) {

        rvViewList = (RecyclerView) view.findViewById(R.id.rvViewList);
        rvViewList.setHasFixedSize(true);

        emptyView = (LinearLayout) view.findViewById(R.id.emptyView);

        rvViewLayoutManager = new LinearLayoutManager(getActivity());
        rvViewList.setLayoutManager(rvViewLayoutManager);

        tvSearchDate = (TextView) view.findViewById(R.id.tvSearchDate);

        btnBack = (ImageButton) view.findViewById(R.id.btnBack);
        btnHome = (ImageButton) view.findViewById(R.id.btnHome);

        btnAdd = (Button) view.findViewById(R.id.btnAdd);

    }

    private void init() {

        Bundle receivedData = getArguments();
        if (receivedData != null) {
            String value = receivedData.getString("fromMenu");

            AndroidUtil.log("receivedData : " + value);

            if( "true".equals( value ))
            {
                memoViewModel.setClear();
            }

            getArguments().clear();
        }


        btnHome.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_navigation_menu_to_home));


        MemoFragment.Click click = new MemoFragment.Click();

        tvSearchDate.setOnClickListener(click);
        btnAdd.setOnClickListener(click);

        btnBack.setOnClickListener(click);

        handlerCalendar = new MemoFragment.CalendarHandler(Looper.getMainLooper());
        calendarDialog = new CustomCalendarDialog(getActivity(), handlerCalendar);

        progressDialog = new CustomProgressDialog(getContext());
        memoViewModel.getProgressFlag().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
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


        memoViewModel.getMemoList().observe(getViewLifecycleOwner(), new Observer<ArrayList<NemoCustomerMemoListRO>>() {
            @Override
            public void onChanged(ArrayList<NemoCustomerMemoListRO> memoListROS) {
                rvViewAdapter.setadapterLists(memoListROS);
            }
        });

        memoViewModel.getSearchDateYmd().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {

                tvSearchDate.setText(s);

            }
        });

    }

    public ArrayList<NemoCustomerMemoListRO> adapterListCopy(ArrayList<NemoCustomerMemoListRO> fromList) {
        String copyJSON = gson.toJson(fromList);
        ArrayList<NemoCustomerMemoListRO> toList = gson.fromJson(copyJSON, adapterListType);
        return toList;
    }

    protected void moveTask(NemoCustomerMemoListRO t){

        Bundle bundle = new Bundle();

        bundle.putSerializable("visit_memo", t);

        AndroidUtil.log("NemoCustomerMemoListRO : " + t);

        Navigation.findNavController(getView()).navigate(R.id.action_to_memoDetailFragment, bundle);

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
                    memoViewModel.setSearchDate(selectDate);

                    break;

            }

        }
    }


    public class Click implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {

                case R.id.tvSearchDate:

                    calendarDialog.show(memoViewModel.getSearchDate().getValue());
                    break;


                case R.id.btnAdd:

                    Navigation.findNavController(getView()).navigate(R.id.action_to_memoAddFragment);

                    break;

                case R.id.btnBack:

                    Navigation.findNavController(getView()).popBackStack();

                    break;
            }
        }
    }

    public class ListViewAdapter extends RecyclerView.Adapter<ListViewAdapter.ListViewHolder> {

        private ArrayList<NemoCustomerMemoListRO> adapterLists;
        private SelectionTracker<Long> selectionTracker;

        public ListViewAdapter(ArrayList<NemoCustomerMemoListRO> planLists) {
            this.adapterLists = adapterListCopy(planLists);

            setHasStableIds(true);
        }

        public void setSelectionTracker(SelectionTracker<Long> selectionTracker) {
            this.selectionTracker = selectionTracker;
        }

        public NemoCustomerMemoListRO getItem(int position){
            return adapterLists.get(position);
        }

        public void setadapterLists(ArrayList<NemoCustomerMemoListRO> adapterLists) {
            this.adapterLists = adapterListCopy(adapterLists);

            if (adapterLists.isEmpty()) {
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
        public ListViewAdapter.ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View adapterView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_memo_layout, parent, false);

            ListViewAdapter.ListViewHolder holder = new ListViewAdapter.ListViewHolder(adapterView);

            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
            NemoCustomerMemoListRO t = this.adapterLists.get(position);

            holder.bindItem(t);

        }

        @Override
        public int getItemCount() {
            return this.adapterLists.size();
        }

        public class ListViewHolder extends RecyclerView.ViewHolder {

            public TextView tvCustNm, tvUpdtDtm, tvMemo;

            public View v;

            public NemoCustomerMemoListRO item = null;

            public ListViewHolder(@NonNull View itemView) {
                super(itemView);
                v = itemView;
                tvCustNm = (TextView) itemView.findViewById(R.id.tvCustNm);
                tvUpdtDtm = (TextView) itemView.findViewById(R.id.tvUpdtDtm);
                tvMemo = (TextView) itemView.findViewById(R.id.tvMemo);

                itemView.setOnClickListener(view -> {
                    moveTask(item);
                });
            }

            public void bindItem(NemoCustomerMemoListRO t) {
                tvCustNm.setText(AndroidUtil.nullToString(t.getCustNm(),""));

                String updtDtm = t.getUpdtDtm();

                if( updtDtm != null && updtDtm.indexOf(".") > 0)
                {
                    updtDtm = updtDtm.substring(0,updtDtm.indexOf("."));
                }

                tvUpdtDtm.setText(updtDtm);
                tvMemo.setText(AndroidUtil.nullToString(t.getMemo(),""));

                this.item = t;
            }

        }
    }

}