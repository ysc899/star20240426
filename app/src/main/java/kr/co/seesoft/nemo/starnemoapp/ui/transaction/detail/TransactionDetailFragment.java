package kr.co.seesoft.nemo.starnemoapp.ui.transaction.detail;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.selection.ItemDetailsLookup;
import androidx.recyclerview.selection.Selection;
import androidx.recyclerview.selection.SelectionPredicates;
import androidx.recyclerview.selection.SelectionTracker;
import androidx.recyclerview.selection.StableIdKeyProvider;
import androidx.recyclerview.selection.StorageStrategy;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import kr.co.seesoft.nemo.starnemoapp.R;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.ro.NemoCustomerInfoRO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.ro.NemoHospitalSearchRO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.ro.NemoSalesTransactionListRO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.ro.NemoScheduleListRO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.ro.NemoVisitListRO;
import kr.co.seesoft.nemo.starnemoapp.ui.dialog.CustomProgressDialog;
import kr.co.seesoft.nemo.starnemoapp.ui.dialog.SignPadDialog;
import kr.co.seesoft.nemo.starnemoapp.ui.dialog.TransactionSignPadDialog;
import kr.co.seesoft.nemo.starnemoapp.ui.dialog.YearMonthPickerDialog;
import kr.co.seesoft.nemo.starnemoapp.ui.visitplanadd.VisitPlanAddFragment;
import kr.co.seesoft.nemo.starnemoapp.util.AndroidUtil;
import kr.co.seesoft.nemo.starnemoapp.util.Const;
import kr.co.seesoft.nemo.starnemoapp.util.DateUtil;

public class TransactionDetailFragment extends Fragment { //implements MainMenuActivity.OnBackPressedListener{


    private TransactionDetailViewModel transactionDetailViewModel;


    //리스트뷰 관련
    private RecyclerView rvViewList;
    private ListViewAdapter rvViewAdapter;
    private RecyclerView.LayoutManager rvViewLayoutManager;
    private SelectionTracker<Long> selection;

    private LinearLayout emptyView;

    Type visitPlanListType = new TypeToken<ArrayList<NemoSalesTransactionListRO>>() {
    }.getType();
    //리스트뷰 관련 끝

    //셀렉박스 관련
    private Spinner spSearchOption;

    /** 병원 정보 */
    private NemoHospitalSearchRO visitHospital;

    private String visitHospitalType;

    /**
     * 검색 방법 종류 (병원명, 병원 코드)
     */
    private int searchType;
    //셀렉박스 관련 끝

    private TextView tvHospitalName;

    private Button btnSign, btnPayment, btnPaymentList, btnPaymentSend, btnSearch;
    //private CheckBox cbAgree;

    private ImageView ivAgree;
    private TextView tvAgree;

    private EditText etSearchKeyWord;

    private CustomProgressDialog progressDialog;

    private TransactionSignPadDialog signPadDialog;
    private Handler signHandler;

    // Title bar button
    private ImageButton btnBack, btnHome;

    Gson gson = new Gson();

    private TextView tvStartDate, tvEndDate;

    private int pickerType = 1;

    String startYM, endYM;

    private NemoSalesTransactionListRO selItem = null;
    private NemoSalesTransactionListRO oldItem = null;

    private View oldView = null;
    private int oldColor = 0;

    DatePickerDialog.OnDateSetListener yymmDialog = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth){
            AndroidUtil.log("YearMonthPicker : " +"pickerType = " + pickerType +" year = " + year + ", month = " + monthOfYear + ", day = " + dayOfMonth);

            if(pickerType == 1)
            {
                transactionDetailViewModel.setStartYM(year + "-" + String.format("%02d", monthOfYear) );
            }
            else
            {
                transactionDetailViewModel.setEndYM(year + "-" + String.format("%02d", monthOfYear));
            }


        }
    };

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        transactionDetailViewModel =
                ViewModelProviders.of(getActivity()).get(TransactionDetailViewModel.class);
        View root = inflater.inflate(R.layout.fragment_transaction_detail, container, false);

        initUI(root);
        init();
        return root;
    }

    private void initUI(View view) {

        tvHospitalName = (TextView) view.findViewById(R.id.tvHospitalName);

        btnSign = (Button) view.findViewById(R.id.btnSign);
        btnPayment = (Button) view.findViewById(R.id.btnPayment);
        btnPaymentList = (Button) view.findViewById(R.id.btnPaymentList);
        btnPaymentSend = (Button) view.findViewById(R.id.btnPaymentSend);
        btnSearch = (Button) view.findViewById(R.id.btnSearch);

        //cbAgree = (CheckBox) view.findViewById(R.id.cbAgree);
        ivAgree = (ImageView) view.findViewById(R.id.ivAgree);
        tvAgree = (TextView) view.findViewById(R.id.tvAgree);

        rvViewList = (RecyclerView) view.findViewById(R.id.rvViewList);
        rvViewList.setHasFixedSize(true);

        emptyView = (LinearLayout) view.findViewById(R.id.emptyView);

        rvViewLayoutManager = new LinearLayoutManager(getActivity());
        rvViewList.setLayoutManager(rvViewLayoutManager);


        spSearchOption = (Spinner) view.findViewById(R.id.spVisitPlanAddSearchOption);
        etSearchKeyWord = (EditText) view.findViewById(R.id.etVisitPlanAddSearchWord);


        btnBack = (ImageButton) view.findViewById(R.id.btnBack);
        btnHome = (ImageButton) view.findViewById(R.id.btnHome);

        tvStartDate = (TextView) view.findViewById(R.id.tvStartDate);
        tvEndDate = (TextView) view.findViewById(R.id.tvEndDate);

    }

    private void init() {

        btnHome.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_navigation_menu_to_home));

        Click click = new Click();

        // 약관 동의
        ivAgree.setOnClickListener(click);
        tvAgree.setOnClickListener(click);

        // 카드 결제
        btnPayment.setOnClickListener(click);

        // 수금 내역
        btnPaymentList.setOnClickListener(click);

        // 청구서 발송
        btnPaymentSend.setOnClickListener(click);

        btnBack.setOnClickListener(click);

        signHandler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);

                switch (msg.what){
                    case Const.HANDLER_SIGN:

                        byte[] outArray = (byte[])msg.obj;

                        //AndroidUtil.log("Sing : " + outArray);

                        transactionDetailViewModel.apiSingImageAdd(outArray, selItem);

                        break;
                }
            }
        };

        signPadDialog = new TransactionSignPadDialog(getActivity(), signHandler);

        tvStartDate.setOnClickListener(click);
        tvEndDate.setOnClickListener(click);

        btnSearch.setOnClickListener(click);
        btnSign.setOnClickListener(click);


        visitHospital = (NemoHospitalSearchRO) getArguments().getSerializable("visit_hospital");

        AndroidUtil.log("visitHospital : " + visitHospital);

        visitHospitalType = (String) getArguments().getSerializable("visit_hospital_type");

        transactionDetailViewModel.setHospital(visitHospital);

        tvHospitalName.setText(visitHospital.getCustNm());

        transactionDetailViewModel.getHospitalInfo().observe(getViewLifecycleOwner(), new Observer<NemoCustomerInfoRO>() {
            @Override
            public void onChanged(NemoCustomerInfoRO customerInfo) {
                setCustomerInfo(customerInfo);
            }
        });


        transactionDetailViewModel.getStartYM().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {

                startYM = s;

                String[] arrDisp = startYM.split("-");

                String dispYm = arrDisp[0] + "년 " + arrDisp[1] + "월";

                tvStartDate.setText(dispYm);

            }
        });

        transactionDetailViewModel.getEndYM().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {

                endYM = s;

                String[] arrDisp = endYM.split("-");

                String dispYm = arrDisp[0] + "년 " + arrDisp[1] + "월";

                tvEndDate.setText(dispYm);
            }
        });



        progressDialog = new CustomProgressDialog(getContext());
        transactionDetailViewModel.getProgressFlag().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
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


        transactionDetailViewModel.getTransactionlList().observe(getViewLifecycleOwner(), new Observer<ArrayList<NemoSalesTransactionListRO>>() {
            @Override
            public void onChanged(ArrayList<NemoSalesTransactionListRO> transactionROS) {

//                AndroidUtil.log("onChange() :" + selItem);
                rvViewAdapter.setadapterLists(transactionROS);

                if( selItem != null )
                {
                    selItem = null;
                }

            }
        });

        checkRND();

    }

    public void checkRND()
    {

        if( "rnd".equals(visitHospitalType) )
        {
            ivAgree.setVisibility(View.GONE);
            tvAgree.setVisibility(View.GONE);

            btnSign.setVisibility(View.GONE);
            btnPaymentSend.setVisibility(View.GONE);
        }
    }

    public void setCustomerInfo(NemoCustomerInfoRO customerInfo)
    {

        String imgUrl = customerInfo.getImgUrl();

        if( imgUrl != null && imgUrl.length() > 0 )
        {
            ivAgree.setImageResource(R.drawable.n_check_on);

        }
        else
        {
            ivAgree.setImageResource(R.drawable.n_check_off);
        }

    }

    public ArrayList<NemoSalesTransactionListRO> visitPlanListCopy(ArrayList<NemoSalesTransactionListRO> fromList) {
        String copyJSON = gson.toJson(fromList);
        ArrayList<NemoSalesTransactionListRO> toList = gson.fromJson(copyJSON, visitPlanListType);
        return toList;
    }

    private void setListViewColor(View v,String color)
    {
        TextView o_tvJobYm, o_tvSprcSellAmt, o_tvRprcSellAmt, o_tvDpstAmt;
        TextView o_tvAdtnAmt, o_tvSubAmt, o_tvTmrqUcamt, o_tvBlclDt;

        o_tvJobYm = (TextView) v.findViewById(R.id.tvJobYm);
        o_tvSprcSellAmt = (TextView) v.findViewById(R.id.tvSprcSellAmt);
        o_tvRprcSellAmt = (TextView) v.findViewById(R.id.tvRprcSellAmt);
        o_tvDpstAmt = (TextView) v.findViewById(R.id.tvDpstAmt);

        o_tvAdtnAmt = (TextView) v.findViewById(R.id.tvAdtnAmt);
        o_tvSubAmt = (TextView) v.findViewById(R.id.tvSubAmt);
        o_tvTmrqUcamt = (TextView) v.findViewById(R.id.tvTmrqUcamt);
        o_tvBlclDt = (TextView) v.findViewById(R.id.tvBlclDt);

        o_tvJobYm.setTextColor(Color.parseColor(color));
        o_tvSprcSellAmt.setTextColor(Color.parseColor(color));
        o_tvRprcSellAmt.setTextColor(Color.parseColor(color));
        o_tvDpstAmt.setTextColor(Color.parseColor(color));

        o_tvAdtnAmt.setTextColor(Color.parseColor(color));
        o_tvSubAmt.setTextColor(Color.parseColor(color));
        o_tvTmrqUcamt.setTextColor(Color.parseColor(color));
        o_tvBlclDt.setTextColor(Color.parseColor(color));
    }

    protected void selectTask(NemoSalesTransactionListRO t){

        selItem = t;

    }


    public class Click implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {

                case R.id.tvStartDate:
                    pickerType = 1;

                    YearMonthPickerDialog pd1 = new YearMonthPickerDialog();
                    pd1.setListener(yymmDialog);

                    AndroidUtil.log("startYM : " + startYM);

                    String[] arrStr = startYM.split("-");

                    pd1.setValue(Integer.parseInt(arrStr[0]),Integer.parseInt(arrStr[1]));

                    pd1.show(getActivity().getSupportFragmentManager(), "YearMonthPicker");

                    break;

                case R.id.tvEndDate:
                    pickerType = 2;

                    YearMonthPickerDialog pd2 = new YearMonthPickerDialog();
                    pd2.setListener(yymmDialog);

                    String[] arrStr2 = endYM.split("-");

                    pd2.setValue(Integer.parseInt(arrStr2[0]),Integer.parseInt(arrStr2[1]));

                    pd2.show(getActivity().getSupportFragmentManager(), "YearMonthPicker");

                    break;

                case R.id.btnSearch:


                    transactionDetailViewModel.apiTransactionList();

                    break;

                case R.id.btnSign:

//                    if( selItem == null )
//                    {
//                        AndroidUtil.toast(getContext(),"해당 월을 목록에서 선택 하세요!");
//
//                        break;
//                    }

                    signPadDialog.show();

                    String today = DateUtil.getFormatString(new Date(), "yyyy.MM.dd");

                    String tvTmrqUcamt = transactionDetailViewModel.getAmtMonth();

                    String msg = today + " 기준, 당미수금액 " + tvTmrqUcamt + " 원";

                    signPadDialog.setMsg(msg);


                    break;

                case R.id.btnBack:

                    Navigation.findNavController(getView()).popBackStack();

                    break;

                case R.id.ivAgree:
                case R.id.tvAgree:

                    Bundle bundle = new Bundle();

                    bundle.putSerializable("visit_hospital", visitHospital);

                    Navigation.findNavController(getView()).navigate(R.id.action_visitPlanFragment_to_transactionAgreeFragment, bundle);

                    break;

                case R.id.btnPayment:

                    Bundle bundle1 = new Bundle();

                    bundle1.putSerializable("visit_hospital", visitHospital);

                    Navigation.findNavController(getView()).navigate(R.id.action_visitPlanFragment_to_transactionPaymentFragment, bundle1);

                    break;

                case R.id.btnPaymentList:

                    Bundle bundle2 = new Bundle();

                    bundle2.putSerializable("visit_hospital", visitHospital);

                    Navigation.findNavController(getView()).navigate(R.id.action_visitPlanFragment_to_transactionPayoffFragment, bundle2);

                    break;

                case R.id.btnPaymentSend:

                    Bundle bundle3 = new Bundle();

                    bundle3.putSerializable("visit_hospital", visitHospital);

                    Navigation.findNavController(getView()).navigate(R.id.action_visitPlanFragment_to_transactionPaysendFragment, bundle3);

                    break;

            }
        }
    }

    public class SpinnerSelected implements Spinner.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            searchType = i;
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {
            searchType = Const.SEARCH_NAME;
        }

    }

    public class ListViewAdapter extends RecyclerView.Adapter<ListViewAdapter.ListViewHolder> {

        private ArrayList<NemoSalesTransactionListRO> adapterLists;
        private SelectionTracker<Long> selectionTracker;

        private int selectedItemPosition = RecyclerView.NO_POSITION;

        public ListViewAdapter(ArrayList<NemoSalesTransactionListRO> adapterLists) {
            this.adapterLists = visitPlanListCopy(adapterLists);

            setHasStableIds(true);
        }

        public void setSelectionTracker(SelectionTracker<Long> selectionTracker) {
            this.selectionTracker = selectionTracker;
        }

        public NemoSalesTransactionListRO getItem(int position){
            return adapterLists.get(position);
        }

        public void setadapterLists(ArrayList<NemoSalesTransactionListRO> adapterLists) {

            selectedItemPosition = RecyclerView.NO_POSITION;

            this.adapterLists = visitPlanListCopy(adapterLists);

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
        public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View adapterView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_transaction_detail_layout, parent, false);

            ListViewHolder holder = new ListViewHolder(adapterView);

            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull ListViewHolder holder, @SuppressLint("RecyclerView") int position) {
            NemoSalesTransactionListRO t = this.adapterLists.get(position);

            holder.bindItem(t);

            if (position % 2 == 0) {
                holder.itemView.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), android.R.color.transparent));
            }
            else
            {
                holder.itemView.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.even_row_color));
            }

            // 다중 Select 인 경우
//            holder.tvSprcSellAmt.setText(NumberFormat.getInstance().format(t.getSprcSellAmt()));
//            holder.tvRprcSellAmt.setText(NumberFormat.getInstance().format(t.getRprcSellAmt()));
//            holder.tvDpstAmt.setText(NumberFormat.getInstance().format(t.getDpstAmt()));
//
//            holder.tvAdtnAmt.setText(NumberFormat.getInstance().format(t.getAdtnAmt()));
//            holder.tvSubAmt.setText(NumberFormat.getInstance().format(t.getSubAmt()));
//            holder.tvTmrqUcamt.setText(NumberFormat.getInstance().format(t.getTmrqUcamt()));
//            holder.tvBlclDt.setText(t.getBlclDt());
//
//            if( t.getSgntImgUrl() != null && t.getSgntImgUrl().length() > 0 )
//            {
//                holder.signYNLeft.setVisibility(View.VISIBLE);
//            }
//
//            holder.setSelectiontracker(selectionTracker);





            // 선택된 항목인 경우 텍스트 색상 변경
//            if (position == selectedItemPosition) {
//                holder.itemView.setBackgroundResource(android.R.color.darker_gray);
//
//                setListViewColor(holder.itemView,"#ffffff");
//
//            } else {
//                // 선택되지 않은 항목의 경우 원래의 텍스트 색상으로 설정
//                if (position % 2 == 0) {
//                    holder.itemView.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), android.R.color.transparent));
//                }
//                else
//                {
//                    holder.itemView.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.even_row_color));
//                }
//
//                setListViewColor(holder.itemView,"#000000");
//            }
//
//            if( !"rnd".equals(visitHospitalType) )
//            {
//                holder.itemView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        // 선택된 항목의 위치 갱신
//                        int previousSelectedItem = selectedItemPosition;
//
//                        if( t.getSgntImgUrl() != null && t.getSgntImgUrl().length() > 0 )
//                        {
//                            AndroidUtil.toast(getContext(),"전자서명을 완료한 월 입니다.");
//                        }
//                        else
//                        {
//                            selectedItemPosition = position;
//
//                            // 기존에 선택된 항목의 텍스트 색상을 원래대로 변경
//                            notifyItemChanged(previousSelectedItem);
//
//                            // 새로 선택된 항목의 텍스트 색상을 변경
//                            notifyItemChanged(selectedItemPosition);
//
//                            selectTask(t);
//
//                        }
//                    }
//                });
//            }

        }

        @Override
        public int getItemCount() {
            return this.adapterLists.size();
        }

        public class ListViewHolder extends RecyclerView.ViewHolder {

            public TextView tvJobYm, tvSprcSellAmt, tvRprcSellAmt, tvDpstAmt;

            public TextView tvAdtnAmt, tvSubAmt, tvTmrqUcamt, tvBlclDt;

            public LinearLayout signYNLeft;

            public View v;

            public NemoSalesTransactionListRO item = null;

            public ListViewHolder(@NonNull View itemView) {
                super(itemView);
                v = itemView;

                tvJobYm = (TextView) itemView.findViewById(R.id.tvJobYm);
                tvSprcSellAmt = (TextView) itemView.findViewById(R.id.tvSprcSellAmt);
                tvRprcSellAmt = (TextView) itemView.findViewById(R.id.tvRprcSellAmt);
                tvDpstAmt = (TextView) itemView.findViewById(R.id.tvDpstAmt);

                tvAdtnAmt = (TextView) itemView.findViewById(R.id.tvAdtnAmt);
                tvSubAmt = (TextView) itemView.findViewById(R.id.tvSubAmt);
                tvTmrqUcamt = (TextView) itemView.findViewById(R.id.tvTmrqUcamt);
                tvBlclDt = (TextView) itemView.findViewById(R.id.tvBlclDt);

                signYNLeft = (LinearLayout) itemView.findViewById(R.id.signYNLeft);

            }

            public void bindItem(NemoSalesTransactionListRO t) {

                tvJobYm.setText(AndroidUtil.dispYYMMM(t.getJobYm()));
                tvSprcSellAmt.setText(AndroidUtil.dispCurrency(t.getSprcSellAmt()));
                tvRprcSellAmt.setText(AndroidUtil.dispCurrency(t.getRprcSellAmt()));
                tvDpstAmt.setText(AndroidUtil.dispCurrency(t.getDpstAmt()));

                tvAdtnAmt.setText(AndroidUtil.dispCurrency(t.getAdtnAmt()));
                tvSubAmt.setText(AndroidUtil.dispCurrency(t.getSubAmt()));
                tvTmrqUcamt.setText(AndroidUtil.dispCurrency(t.getTmrqUcamt()));
                tvBlclDt.setText(AndroidUtil.dispDate(t.getBlclDt()));

//                AndroidUtil.log("t.getSgntImgUrl() : " + t.getSgntImgUrl());

                if( t.getSgntImgUrl() != null && t.getSgntImgUrl().length() > 0 )
                {
                    signYNLeft.setVisibility(View.VISIBLE);
                }
                else
                {
                    signYNLeft.setVisibility(View.GONE);
                }

                this.item = t;
            }

            public ListViewLookUp.ItemDetails<Long> getItemDetails() {
                return new ItemDetailsLookup.ItemDetails<Long>() {
                    @Override
                    public int getPosition() {
                        return getAdapterPosition();
                    }

                    @Nullable
                    @Override
                    public Long getSelectionKey() {
                        return getItemId();
                    }
                };
            }

            public void setSelectiontracker(SelectionTracker<Long> selectionTracker){

//                if(selectionTracker != null && selectionTracker.isSelected((long) getAdapterPosition())){
//                    v.setBackgroundResource(android.R.color.darker_gray);
//
//                    tvJobYm.setTextColor(Color.parseColor("#ffffff"));
//                    tvSprcSellAmt.setTextColor(Color.parseColor("#ffffff"));
//                    tvRprcSellAmt.setTextColor(Color.parseColor("#ffffff"));
//                    tvDpstAmt.setTextColor(Color.parseColor("#ffffff"));
//
//                    tvAdtnAmt.setTextColor(Color.parseColor("#ffffff"));
//                    tvSubAmt.setTextColor(Color.parseColor("#ffffff"));
//                    tvTmrqUcamt.setTextColor(Color.parseColor("#ffffff"));
//                    tvBlclDt.setTextColor(Color.parseColor("#ffffff"));
//                    AndroidUtil.toast(getContext(),"선택됨");
//                }else{
//                    v.setBackgroundResource(android.R.color.white);
//
//                    tvJobYm.setTextColor(Color.parseColor("#000000"));
//                    tvSprcSellAmt.setTextColor(Color.parseColor("#000000"));
//                    tvRprcSellAmt.setTextColor(Color.parseColor("#000000"));
//                    tvDpstAmt.setTextColor(Color.parseColor("#000000"));
//
//                    tvAdtnAmt.setTextColor(Color.parseColor("#000000"));
//                    tvSubAmt.setTextColor(Color.parseColor("#000000"));
//                    tvTmrqUcamt.setTextColor(Color.parseColor("#000000"));
//                    tvBlclDt.setTextColor(Color.parseColor("#000000"));
//                    AndroidUtil.toast(getContext(),"선택 안됨");
//                }

            }

        }
    }

    public class ListViewLookUp extends ItemDetailsLookup<Long> {

        private RecyclerView view;

        public ListViewLookUp(RecyclerView view) {
            this.view = view;
        }

        @Nullable
        @Override
        public ItemDetails<Long> getItemDetails(@NonNull MotionEvent e) {
            View v = view.findChildViewUnder(e.getX(), e.getY());
            if (v != null) {
                TransactionDetailFragment.ListViewAdapter.ListViewHolder holder = (TransactionDetailFragment.ListViewAdapter.ListViewHolder) view.getChildViewHolder(v);
                return holder.getItemDetails();
            }
            return null;
        }
    }

}