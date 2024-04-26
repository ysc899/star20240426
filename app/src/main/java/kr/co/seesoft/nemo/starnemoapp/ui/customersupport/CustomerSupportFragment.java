package kr.co.seesoft.nemo.starnemoapp.ui.customersupport;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.selection.SelectionTracker;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import kr.co.seesoft.nemo.starnemoapp.R;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.ro.NemoCustomerMemoListRO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.ro.NemoCustomerSupportListRO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.ro.NemoHospitalSearchRO;
import kr.co.seesoft.nemo.starnemoapp.ui.PaginationScrollListener;
import kr.co.seesoft.nemo.starnemoapp.ui.customer.CustomerViewModel;
import kr.co.seesoft.nemo.starnemoapp.ui.dialog.CustomCalendarDialog;
import kr.co.seesoft.nemo.starnemoapp.ui.dialog.CustomProgressDialog;
import kr.co.seesoft.nemo.starnemoapp.ui.memo.MemoFragment;
import kr.co.seesoft.nemo.starnemoapp.util.AndroidUtil;
import kr.co.seesoft.nemo.starnemoapp.util.Const;

public class CustomerSupportFragment extends Fragment {


    private CustomerSupportViewModel customerSupportViewModel;


    //리스트뷰 관련
    private RecyclerView rvViewList;
    private ListViewAdapter rvViewAdapter;
    private RecyclerView.LayoutManager rvViewLayoutManager;
    private SelectionTracker<Long> selection;

    private LinearLayout emptyView;

    Type adapterListType = new TypeToken<ArrayList<NemoCustomerSupportListRO>>() {
    }.getType();
    //리스트뷰 관련 끝

    //캘린더 팝업
    private CustomCalendarDialog calendarDialog;
    private Handler handlerCalendar;

    private Button btnAdd;

    private TextView tvSearchDate;

    private CustomProgressDialog progressDialog;

    // Title bar button
    private ImageButton btnBack, btnHome;

    Gson gson = new Gson();

    private int currentPage = 1; // 초기 페이지

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        customerSupportViewModel =
                ViewModelProviders.of(getActivity()).get(CustomerSupportViewModel.class);
        View root = inflater.inflate(R.layout.fragment_customer_support, container, false);

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

            //AndroidUtil.log("receivedData : " + value);

            if( "true".equals( value ))
            {
                customerSupportViewModel.setClear();
            }

            getArguments().clear();
        }

        btnHome.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_navigation_menu_to_home));


        Click click = new Click();

        tvSearchDate.setOnClickListener(click);
        btnAdd.setOnClickListener(click);

        btnBack.setOnClickListener(click);

        progressDialog = new CustomProgressDialog(getContext());

        handlerCalendar = new CalendarHandler(Looper.getMainLooper());
        calendarDialog = new CustomCalendarDialog(getActivity(), handlerCalendar);

        customerSupportViewModel.getProgressFlag().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
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

        customerSupportViewModel.getCustomerSupportList().observe(getViewLifecycleOwner(), new Observer<ArrayList<NemoCustomerSupportListRO>>() {
            @Override
            public void onChanged(ArrayList<NemoCustomerSupportListRO> customerSupportListROS) {
                rvViewAdapter.setadapterLists(customerSupportListROS);
            }
        });

        rvViewList.addOnScrollListener(new PaginationScrollListener((LinearLayoutManager)rvViewLayoutManager) {
            @Override
            public void loadNextPage(int page) {

                int currentPage = customerSupportViewModel.getCurrentPage();

                currentPage++;
                AndroidUtil.log("loadNextPage : " + currentPage);
                customerSupportViewModel.setCurrentPage(currentPage);

                customerSupportViewModel.getAPICustomerSupportListData();
            }
        });

        customerSupportViewModel.getSearchDateYmd().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {

                tvSearchDate.setText(s);

            }
        });

    }

    public ArrayList<NemoCustomerSupportListRO> adapterListCopy(ArrayList<NemoCustomerSupportListRO> fromList) {
        String copyJSON = gson.toJson(fromList);
        ArrayList<NemoCustomerSupportListRO> toList = gson.fromJson(copyJSON, adapterListType);
        return toList;
    }


    protected void moveTask(NemoCustomerSupportListRO t){

        Bundle bundle = new Bundle();

        bundle.putSerializable("visit_support", t);
        bundle.putSerializable("search_date", customerSupportViewModel.getSearchDate().getValue());

        Navigation.findNavController(getView()).navigate(R.id.action_to_customerSupportDetailFragment, bundle);

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

                    customerSupportViewModel.setCurrentPage(1);
                    customerSupportViewModel.setSearchDate(selectDate);

                    //AndroidUtil.log("Call HANDLER_CALENDAR : " + selectDate);

                    break;

            }

        }
    }


    public class Click implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.tvSearchDate:

                    calendarDialog.show(customerSupportViewModel.getSearchDate().getValue());
                    break;


                case R.id.btnAdd:

                    Bundle bundle = new Bundle();

                    bundle.putSerializable("add_date", customerSupportViewModel.getSearchDate().getValue());

                    Navigation.findNavController(getView()).navigate(R.id.action_to_customerSupportAddFragment,bundle);

                    break;

                case R.id.btnBack:

                    Navigation.findNavController(getView()).popBackStack();

                    break;
            }
        }
    }

    public class ListViewAdapter extends RecyclerView.Adapter<ListViewAdapter.ListViewHolder> {

        private ArrayList<NemoCustomerSupportListRO> adapterLists;
        private SelectionTracker<Long> selectionTracker;

        public ListViewAdapter(ArrayList<NemoCustomerSupportListRO> planLists) {
            this.adapterLists = adapterListCopy(planLists);

            setHasStableIds(true);
        }

        public void setSelectionTracker(SelectionTracker<Long> selectionTracker) {
            this.selectionTracker = selectionTracker;
        }

        public NemoCustomerSupportListRO getItem(int position){
            return adapterLists.get(position);
        }

        public void setadapterLists(ArrayList<NemoCustomerSupportListRO> adapterLists) {
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

            View adapterView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_customer_support_layout, parent, false);

            ListViewAdapter.ListViewHolder holder = new ListViewAdapter.ListViewHolder(adapterView);

            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull ListViewAdapter.ListViewHolder holder, int position) {
            NemoCustomerSupportListRO t = this.adapterLists.get(position);

            holder.bindItem(t);

        }

        @Override
        public int getItemCount() {
            return this.adapterLists.size();
        }

        public class ListViewHolder extends RecyclerView.ViewHolder {

            public TextView tvRegDt, tvRecpDt, tvRecpNo, tvPatnNm, tvCustSuptUpdtRqstStatNm;

            public View v;

            public NemoCustomerSupportListRO item = null;

            public ListViewHolder(@NonNull View itemView) {
                super(itemView);
                v = itemView;
                tvRegDt = (TextView) itemView.findViewById(R.id.tvRegDt);
                tvRecpDt = (TextView) itemView.findViewById(R.id.tvRecpDt);
                tvRecpNo = (TextView) itemView.findViewById(R.id.tvRecpNo);
                tvPatnNm = (TextView) itemView.findViewById(R.id.tvPatnNm);
                tvCustSuptUpdtRqstStatNm = (TextView) itemView.findViewById(R.id.tvCustSuptUpdtRqstStatNm);

                itemView.setOnClickListener(view -> {
                    moveTask(item);
                });
            }

            public void bindItem(NemoCustomerSupportListRO t) {
                tvRegDt.setText(AndroidUtil.nullToString(t.getRegDt(),""));
                tvRecpDt.setText(AndroidUtil.dispDate(t.getRecpDt()));
                tvRecpNo.setText(AndroidUtil.nullToString(t.getRecpNo(),""));
                tvPatnNm.setText(AndroidUtil.nullToString(t.getPatnNm(),""));

                tvCustSuptUpdtRqstStatNm.setText(AndroidUtil.nullToString(t.getCustSuptUpdtRqstStatNm(),""));

                this.item = t;
            }

        }
    }

}