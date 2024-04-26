package kr.co.seesoft.nemo.starnemoapp.ui.transaction.payoff;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
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
import kr.co.seesoft.nemo.starnemoapp.nemoapi.ro.NemoHospitalSearchRO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.ro.NemoSalesDepositListRO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.ro.NemoSalesTransactionListRO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.ro.NemoVisitListRO;
import kr.co.seesoft.nemo.starnemoapp.ui.dialog.CustomProgressDialog;
import kr.co.seesoft.nemo.starnemoapp.ui.dialog.SignPadDialog;
import kr.co.seesoft.nemo.starnemoapp.ui.dialog.YearMonthPickerDialog;
import kr.co.seesoft.nemo.starnemoapp.ui.dialog.YearPickerDialog;
import kr.co.seesoft.nemo.starnemoapp.ui.transaction.detail.TransactionDetailViewModel;
import kr.co.seesoft.nemo.starnemoapp.util.AndroidUtil;
import kr.co.seesoft.nemo.starnemoapp.util.Const;

public class TransactionPayoffFragment extends Fragment {


    private TransactionPayoffViewModel transactionPayoffViewModel;


    //리스트뷰 관련
    private RecyclerView rvViewList;
    private ListViewAdapter rvViewAdapter;
    private RecyclerView.LayoutManager rvViewLayoutManager;
    private SelectionTracker<Long> selection;

    private LinearLayout emptyView;

    Type adapterListType = new TypeToken<ArrayList<NemoSalesDepositListRO>>() {
    }.getType();
    //리스트뷰 관련 끝

    //셀렉박스 관련
    private Spinner spSearchOption;

    /** 병원 정보 */
    private NemoHospitalSearchRO visitHospital;

    /**
     * 검색 방법 종류 (병원명, 병원 코드)
     */
    private int searchType;
    //셀렉박스 관련 끝

    private TextView tvHospitalName;

    private TextView tvCardNo, tvInstallment, tvApprovalDate, tvApprovalNo, tvCardPay;
    private TextView tvBankName, tvBankNo, tvBankDate, tvBankPayment, tvBankEtc;

    private Button btnCancel;

    private ConstraintLayout constraintLayoutList;
    private ConstraintLayout conLayoutCard, conLayoutBank;
    private ConstraintLayout bottomButton;

    private EditText etSearchKeyWord;

    private CustomProgressDialog progressDialog;

    // Title bar button
    private ImageButton btnBack, btnHome;

    Gson gson = new Gson();

    private TextView tvSearchDate;

    private int pickerType = 1;

    private String searchYM;

    private NemoSalesDepositListRO selItem = null;


    DatePickerDialog.OnDateSetListener yymmDialog = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth){
            AndroidUtil.log("YearMonthPicker : " +"pickerType = " + pickerType +" year = " + year + ", month = " + monthOfYear + ", day = " + dayOfMonth);

            transactionPayoffViewModel.setSearchYM(year + "");

            transactionPayoffViewModel.apiSalesDepositList();


        }
    };

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        transactionPayoffViewModel =
                ViewModelProviders.of(getActivity()).get(TransactionPayoffViewModel.class);
        View root = inflater.inflate(R.layout.fragment_transaction_payoff, container, false);

        initUI(root);
        init();
        return root;
    }

    private void initUI(View view) {

        tvHospitalName = (TextView) view.findViewById(R.id.tvHospitalName);

        tvCardNo = (TextView) view.findViewById(R.id.tvCardNo);
        tvInstallment = (TextView) view.findViewById(R.id.tvInstallment);
        tvApprovalDate = (TextView) view.findViewById(R.id.tvApprovalDate);
        tvApprovalNo = (TextView) view.findViewById(R.id.tvApprovalNo);
        tvCardPay = (TextView) view.findViewById(R.id.tvCardPay);

        tvBankName = (TextView) view.findViewById(R.id.tvBankName);
        tvBankNo = (TextView) view.findViewById(R.id.tvBankNo);
        tvBankDate = (TextView) view.findViewById(R.id.tvBankDate);
        tvBankPayment = (TextView) view.findViewById(R.id.tvBankPayment);
        tvBankEtc = (TextView) view.findViewById(R.id.tvBankEtc);

        constraintLayoutList = (ConstraintLayout) view.findViewById(R.id.constraintLayoutList);
        conLayoutCard = (ConstraintLayout) view.findViewById(R.id.conLayoutCard);
        conLayoutBank = (ConstraintLayout) view.findViewById(R.id.conLayoutBank);
        bottomButton = (ConstraintLayout) view.findViewById(R.id.bottomButton);

        btnCancel = (Button) view.findViewById(R.id.btnCancel);

        rvViewList = (RecyclerView) view.findViewById(R.id.rvViewList);
        rvViewList.setHasFixedSize(true);

        emptyView = (LinearLayout) view.findViewById(R.id.emptyView);

        rvViewLayoutManager = new LinearLayoutManager(getActivity());
        rvViewList.setLayoutManager(rvViewLayoutManager);


        spSearchOption = (Spinner) view.findViewById(R.id.spVisitPlanAddSearchOption);
        etSearchKeyWord = (EditText) view.findViewById(R.id.etVisitPlanAddSearchWord);


        btnBack = (ImageButton) view.findViewById(R.id.btnBack);
        btnHome = (ImageButton) view.findViewById(R.id.btnHome);

        tvSearchDate = (TextView) view.findViewById(R.id.tvSearchDate);

    }

    private void init() {


        btnHome.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_navigation_menu_to_home));

        Click click = new Click();

        tvSearchDate.setOnClickListener(click);

        btnCancel.setOnClickListener(click);

        btnBack.setOnClickListener(click);

        visitHospital = (NemoHospitalSearchRO) getArguments().getSerializable("visit_hospital");

        AndroidUtil.log("visitHospital : " + visitHospital);

        transactionPayoffViewModel.setHospital(visitHospital);

        tvHospitalName.setText(visitHospital.getCustNm());

        transactionPayoffViewModel.getSearchYM().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {

                searchYM = s;

                //String[] arrDisp = searchYM.split("-");

                //String dispYm = arrDisp[0] + "년 " + arrDisp[1] + "월";

                String dispYm = s + "년 ";

                tvSearchDate.setText(dispYm);

            }
        });


        progressDialog = new CustomProgressDialog(getContext());
        transactionPayoffViewModel.getProgressFlag().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
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

        transactionPayoffViewModel.getAdapterlList().observe(getViewLifecycleOwner(), new Observer<ArrayList<NemoSalesDepositListRO>>() {
            @Override
            public void onChanged(ArrayList<NemoSalesDepositListRO> visitPlanROS) {
                rvViewAdapter.setadapterLists(visitPlanROS);
                reset();
            }
        });

    }

    public ArrayList<NemoSalesDepositListRO> adapterListCopy(ArrayList<NemoSalesDepositListRO> fromList) {
        String copyJSON = gson.toJson(fromList);
        ArrayList<NemoSalesDepositListRO> toList = gson.fromJson(copyJSON, adapterListType);
        return toList;
    }

    private void setListViewColor(View v,String color)
    {
        TextView tvRegDtm, tvBlclAmt, tvDpstTypeNm;

        tvRegDtm = (TextView) v.findViewById(R.id.tvRegDtm);
        tvBlclAmt = (TextView) v.findViewById(R.id.tvBlclAmt);
        tvDpstTypeNm = (TextView) v.findViewById(R.id.tvDpstTypeNm);

        tvRegDtm.setTextColor(Color.parseColor(color));
        tvBlclAmt.setTextColor(Color.parseColor(color));
        tvDpstTypeNm.setTextColor(Color.parseColor(color));
    }

    private void reset()
    {

        ConstraintLayout.LayoutParams params1 = (ConstraintLayout.LayoutParams) constraintLayoutList.getLayoutParams();

        params1.bottomToTop = R.id.bottomButton;

        constraintLayoutList.setLayoutParams(params1);

        conLayoutCard.setVisibility(View.GONE);
        conLayoutBank.setVisibility(View.GONE);

        tvCardNo.setText("");
        tvInstallment.setText("");
        tvApprovalDate.setText("");
        tvApprovalNo.setText("");
        tvCardPay.setText("");

        tvBankName.setText("");
        tvBankNo.setText("");
        tvBankDate.setText("");
        tvBankPayment.setText("");
        tvBankEtc.setText("");

    }

    protected void selectTask(NemoSalesDepositListRO t){

        selItem = t;

        if( "5".equals( t.getDpstTypeCd()))
        {
            ConstraintLayout.LayoutParams params1 = (ConstraintLayout.LayoutParams) constraintLayoutList.getLayoutParams();

            params1.bottomToTop = R.id.conLayoutCard;

            constraintLayoutList.setLayoutParams(params1);

            conLayoutCard.setVisibility(View.VISIBLE);
            conLayoutBank.setVisibility(View.GONE);

            tvCardNo.setText(t.getCrcdNo());
            //할부개월
            tvInstallment.setText(t.getInsMm());
            //승인일시
            tvApprovalDate.setText(t.getAprvDtm());
            // 승인번호
            tvApprovalNo.setText(t.getCrcdAprvNo());
            // 승인금액
            tvCardPay.setText(AndroidUtil.dispCurrency(t.getAprvAmt()));

        }
        else
        {

            ConstraintLayout.LayoutParams params1 = (ConstraintLayout.LayoutParams) constraintLayoutList.getLayoutParams();

            params1.bottomToTop = R.id.conLayoutBank;

            constraintLayoutList.setLayoutParams(params1);

            conLayoutCard.setVisibility(View.GONE);
            conLayoutBank.setVisibility(View.VISIBLE);

            // 입금은행
            tvBankName.setText(t.getBankNm());
            // 입금계좌
            tvBankNo.setText(t.getBankAcctno());
            // 입금일시
            tvBankDate.setText(t.getDpstDtm());
            // 입금금액
            tvBankPayment.setText(AndroidUtil.dispCurrency(t.getDpstAmt()));
            // 내역
            tvBankEtc.setText(t.getSumr());

        }

    }


    public class Click implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {

                case R.id.tvSearchDate:

                    YearPickerDialog pd1 = new YearPickerDialog();
                    pd1.setListener(yymmDialog);

                    AndroidUtil.log("searchYM : " + searchYM);

                    pd1.setValue(Integer.parseInt(searchYM));

                    pd1.show(getActivity().getSupportFragmentManager(), "YearMonthPicker");

                    break;

                case R.id.btnCancel:
                    //이전 화면으로 가기
                    Navigation.findNavController(getView()).popBackStack();

                    break;

                case R.id.btnPaymentCancel:

                    // 승인 취소


                    break;

                case R.id.btnBack:

                    Navigation.findNavController(getView()).popBackStack();

                    break;
            }
        }
    }

    public class ListViewAdapter extends RecyclerView.Adapter<ListViewAdapter.ListViewHolder> {

        private ArrayList<NemoSalesDepositListRO> adapterLists;
        private SelectionTracker<Long> selectionTracker;

        private int selectedItemPosition = RecyclerView.NO_POSITION;

        public ListViewAdapter(ArrayList<NemoSalesDepositListRO> adapterLists) {
            this.adapterLists = adapterListCopy(adapterLists);

            setHasStableIds(true);
        }

        public void setSelectionTracker(SelectionTracker<Long> selectionTracker) {
            this.selectionTracker = selectionTracker;
        }

        public NemoSalesDepositListRO getItem(int position){
            return adapterLists.get(position);
        }

        public void setadapterLists(ArrayList<NemoSalesDepositListRO> adapterLists) {

            selectedItemPosition = RecyclerView.NO_POSITION;

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
        public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View adapterView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_transaction_payoff_layout, parent, false);

            ListViewHolder holder = new ListViewHolder(adapterView);

            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull ListViewHolder holder, @SuppressLint("RecyclerView") int position) {
            NemoSalesDepositListRO t = this.adapterLists.get(position);

            holder.bindItem(t);

            // 선택된 항목인 경우 텍스트 색상 변경
            if (position == selectedItemPosition) {
                holder.itemView.setBackgroundResource(android.R.color.darker_gray);

                setListViewColor(holder.itemView,"#ffffff");

            } else {
                // 선택되지 않은 항목의 경우 원래의 텍스트 색상으로 설정
                if (position % 2 == 0) {
                    holder.itemView.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), android.R.color.transparent));
                }
                else
                {
                    holder.itemView.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.even_row_color));
                }

                setListViewColor(holder.itemView,"#000000");
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // 선택된 항목의 위치 갱신
                    int previousSelectedItem = selectedItemPosition;

                    selectedItemPosition = position;

                    // 기존에 선택된 항목의 텍스트 색상을 원래대로 변경
                    notifyItemChanged(previousSelectedItem);

                    // 새로 선택된 항목의 텍스트 색상을 변경
                    notifyItemChanged(selectedItemPosition);

                    selectTask(t);
                }
            });


        }

        @Override
        public int getItemCount() {
            return this.adapterLists.size();
        }

        public class ListViewHolder extends RecyclerView.ViewHolder {

            public TextView tvRegDtm, tvBlclAmt, tvDpstTypeNm;

            public View v;

            public NemoSalesDepositListRO item = null;

            public ListViewHolder(@NonNull View itemView) {
                super(itemView);
                v = itemView;
                tvRegDtm = (TextView) itemView.findViewById(R.id.tvRegDtm);
                tvBlclAmt = (TextView) itemView.findViewById(R.id.tvBlclAmt);
                tvDpstTypeNm = (TextView) itemView.findViewById(R.id.tvDpstTypeNm);

//                itemView.setOnClickListener(view -> {
//                    selectTask(item,v);
//                });
            }

            public void bindItem(NemoSalesDepositListRO t) {
                tvRegDtm.setText(AndroidUtil.dispDate(t.getBlclDt()));
                tvBlclAmt.setText(AndroidUtil.dispCurrency(t.getBlclAmt()));

                tvDpstTypeNm.setText(AndroidUtil.getDepositType(t.getDpstTypeCd()));
                this.item = t;
            }

        }
    }

}