package kr.co.seesoft.nemo.starnemoapp.ui.register.voc;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

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
import androidx.recyclerview.selection.ItemDetailsLookup;
import androidx.recyclerview.selection.SelectionTracker;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import kr.co.seesoft.nemo.starnemoapp.R;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.ro.NemoCustomerInfoRO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.ro.NemoCustomerMemoListRO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.ro.NemoCustomerVOCListRO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.ro.NemoHospitalSearchRO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.ro.NemoSalesTransactionListRO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.ro.NemoScheduleListRO;
import kr.co.seesoft.nemo.starnemoapp.ui.PaginationScrollListener;
import kr.co.seesoft.nemo.starnemoapp.ui.dialog.CustomCalendarDialog;
import kr.co.seesoft.nemo.starnemoapp.ui.dialog.CustomProgressDialog;
import kr.co.seesoft.nemo.starnemoapp.ui.dialog.SignPadDialog;
import kr.co.seesoft.nemo.starnemoapp.ui.dialog.YearMonthPickerDialog;
import kr.co.seesoft.nemo.starnemoapp.ui.memo.MemoFragment;
import kr.co.seesoft.nemo.starnemoapp.ui.memo.add.MemoAddFragment;
import kr.co.seesoft.nemo.starnemoapp.ui.transaction.detail.TransactionDetailViewModel;
import kr.co.seesoft.nemo.starnemoapp.util.AndroidUtil;
import kr.co.seesoft.nemo.starnemoapp.util.Const;

public class VOCFragment extends Fragment { //implements MainMenuActivity.OnBackPressedListener{


    private VOCViewModel vocViewModel;


    //리스트뷰 관련
    private RecyclerView rvViewList;
    private ListViewAdapter rvViewAdapter;
    private RecyclerView.LayoutManager rvViewLayoutManager;
    private SelectionTracker<Long> selection;

    private LinearLayout emptyView;

    Type adapterListType = new TypeToken<ArrayList<NemoCustomerVOCListRO>>() {
    }.getType();
    //리스트뷰 관련 끝


    /** 병원 정보 */
    private NemoScheduleListRO visitHospital;

    private TextView tvHospitalName;

    private CustomProgressDialog progressDialog;

    // Title bar button
    private ImageButton btnBack, btnHome;

    private TextView tvSearchDate;

    Gson gson = new Gson();

    //캘린더 팝업
    private CustomCalendarDialog calendarDialog;
    private Handler handlerCalendar;

    private int currentPage = 1; // 초기 페이지

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        vocViewModel =
                ViewModelProviders.of(getActivity()).get(VOCViewModel.class);
        View root = inflater.inflate(R.layout.fragment_register_voc, container, false);

        initUI(root);
        init();
        return root;
    }

    private void initUI(View view) {

        tvHospitalName = (TextView) view.findViewById(R.id.tvHospitalName);

        tvSearchDate = (TextView) view.findViewById(R.id.tvSearchDate);

        rvViewList = (RecyclerView) view.findViewById(R.id.rvViewList);
        rvViewList.setHasFixedSize(true);

        emptyView = (LinearLayout) view.findViewById(R.id.emptyView);

        rvViewLayoutManager = new LinearLayoutManager(getActivity());
        rvViewList.setLayoutManager(rvViewLayoutManager);


        btnBack = (ImageButton) view.findViewById(R.id.btnBack);
        btnHome = (ImageButton) view.findViewById(R.id.btnHome);

    }

    private void init() {

        btnHome.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_navigation_menu_to_home));

        Click click = new Click();

        tvSearchDate.setOnClickListener(click);

        btnBack.setOnClickListener(click);

        handlerCalendar = new CalendarHandler(Looper.getMainLooper());
        calendarDialog = new CustomCalendarDialog(getActivity(), handlerCalendar);


        visitHospital = (NemoScheduleListRO) getArguments().getSerializable("visit_hospital");

        AndroidUtil.log("visitHospital : " + visitHospital);

        vocViewModel.setHospital(visitHospital);

        tvHospitalName.setText(visitHospital.getCustNm());


        rvViewAdapter = new ListViewAdapter(new ArrayList<>());

        rvViewList.setAdapter(rvViewAdapter);


        vocViewModel.getVocList().observe(getViewLifecycleOwner(), new Observer<ArrayList<NemoCustomerVOCListRO>>() {
            @Override
            public void onChanged(ArrayList<NemoCustomerVOCListRO> memoListROS) {
                rvViewAdapter.setadapterLists(memoListROS);
            }
        });


        vocViewModel.getSearchDateYmd().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {

                tvSearchDate.setText(s);

            }
        });


        progressDialog = new CustomProgressDialog(getContext());
        vocViewModel.getProgressFlag().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {

                if(aBoolean){
                    progressDialog.show();
                }else{
                    progressDialog.dismiss();
                }

            }
        });

        rvViewList.addOnScrollListener(new PaginationScrollListener((LinearLayoutManager)rvViewLayoutManager) {
            @Override
            public void loadNextPage(int page) {

                currentPage++;
                AndroidUtil.log("loadNextPage : " + currentPage);
                vocViewModel.setCurrentPage(currentPage);

                vocViewModel.apiVOCList();
            }
        });

    }


    public ArrayList<NemoCustomerVOCListRO> adapterListCopy(ArrayList<NemoCustomerVOCListRO> fromList) {
        String copyJSON = gson.toJson(fromList);
        ArrayList<NemoCustomerVOCListRO> toList = gson.fromJson(copyJSON, adapterListType);
        return toList;
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

                    currentPage = 1;
                    vocViewModel.setCurrentPage(currentPage);

                    Date selectDate = (Date)msg.obj;
                    vocViewModel.setSearchDate(selectDate);

                    vocViewModel.apiVOCList();

                    break;

            }

        }
    }


    public class Click implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {

                case R.id.tvSearchDate:

                    calendarDialog.show(vocViewModel.getSearchDate().getValue());
                    break;

                case R.id.btnBack:

                    Navigation.findNavController(getView()).popBackStack();

                    break;
            }
        }
    }

    public class ListViewAdapter extends RecyclerView.Adapter<ListViewAdapter.ListViewHolder> {

        private ArrayList<NemoCustomerVOCListRO> adapterLists;
        private SelectionTracker<Long> selectionTracker;

        public ListViewAdapter(ArrayList<NemoCustomerVOCListRO> planLists) {
            this.adapterLists = adapterListCopy(planLists);

            setHasStableIds(true);
        }

        public void setSelectionTracker(SelectionTracker<Long> selectionTracker) {
            this.selectionTracker = selectionTracker;
        }

        public NemoCustomerVOCListRO getItem(int position){
            return adapterLists.get(position);
        }

        public void setadapterLists(ArrayList<NemoCustomerVOCListRO> adapterLists) {
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

            View adapterView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_register_voc_layout, parent, false);

            ListViewAdapter.ListViewHolder holder = new ListViewAdapter.ListViewHolder(adapterView);

            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
            NemoCustomerVOCListRO t = this.adapterLists.get(position);

            holder.bindItem(t);

        }

        @Override
        public int getItemCount() {
            return this.adapterLists.size();
        }

        public class ListViewHolder extends RecyclerView.ViewHolder {

            public TextView tvCustInqrCont, tvCustCnslDtm, tvCllrAnswCont;

            public View v;

            public NemoCustomerVOCListRO item = null;

            public ListViewHolder(@NonNull View itemView) {
                super(itemView);
                v = itemView;
                tvCustInqrCont = (TextView) itemView.findViewById(R.id.tvCustInqrCont);
                tvCustCnslDtm = (TextView) itemView.findViewById(R.id.tvCustCnslDtm);
                tvCllrAnswCont = (TextView) itemView.findViewById(R.id.tvCllrAnswCont);

            }

            public void bindItem(NemoCustomerVOCListRO t) {
                tvCustInqrCont.setText(AndroidUtil.nullToString(t.getCustInqrCont(),""));

                tvCustCnslDtm.setText(AndroidUtil.nullToString(t.getCustCnslDtm(),""));

                tvCllrAnswCont.setText(AndroidUtil.nullToString(t.getCllrAnswCont(),""));

                this.item = t;
            }

        }
    }

}