package kr.co.seesoft.nemo.starnemoapp.ui.transaction.payment;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.selection.SelectionTracker;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import kr.co.seesoft.nemo.starnemoapp.R;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.po.NemoSalesApprovalAddPO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.ro.NemoHospitalSearchRO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.ro.NemoSalesApprovalListRO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.ro.NemoSalesDepositListRO;
import kr.co.seesoft.nemo.starnemoapp.ui.dialog.ConfirmDialog;
import kr.co.seesoft.nemo.starnemoapp.ui.dialog.CustomAlertDialog;
import kr.co.seesoft.nemo.starnemoapp.ui.dialog.CustomIngProgressDialog;
import kr.co.seesoft.nemo.starnemoapp.ui.dialog.CustomProgressDialog;
import kr.co.seesoft.nemo.starnemoapp.ui.dialog.SignPadDialog;
import kr.co.seesoft.nemo.starnemoapp.ui.dialog.YearMonthPickerDialog;
import kr.co.seesoft.nemo.starnemoapp.ui.transaction.payoff.TransactionPayoffViewModel;
import kr.co.seesoft.nemo.starnemoapp.util.AndroidUtil;
import kr.co.seesoft.nemo.starnemoapp.util.Const;
import kr.co.seesoft.nemo.starnemoapp.util.c1.C1itPrint;
import service.vcat.smartro.com.vcat.SmartroVCatCallback;
import service.vcat.smartro.com.vcat.SmartroVCatInterface;

public class TransactionPaymentHistoryFragment extends Fragment {


    private TransactionPaymentHistoryViewModel transactionPaymentHistoryViewModel;


    //리스트뷰 관련
    private RecyclerView rvViewList;
    private ListViewAdapter rvViewAdapter;
    private RecyclerView.LayoutManager rvViewLayoutManager;
    private SelectionTracker<Long> selection;

    private LinearLayout emptyView;

    Type adapterListType = new TypeToken<ArrayList<NemoSalesApprovalListRO>>() {
    }.getType();
    //리스트뷰 관련 끝

    //셀렉박스 관련
    private Spinner spSearchOption;

    /** 병원 정보 */
    private NemoHospitalSearchRO visitHospital;

    /** context */
    private Context context;

    /**
     * 검색 방법 종류 (병원명, 병원 코드)
     */
    private int searchType;
    //셀렉박스 관련 끝

    private TextView tvHospitalName;

    private TextView textView26;

    private TextView tvCardNo, tvApprovalDate, tvApprovalNo, tvTotalAmount, tvInstallment, tvCardCompany;

    private TextView tvReceipt;
    private RadioGroup ragReceipt;

    private RadioButton raReceiptPrint, raReceiptOnePrint, raReceiptNotPrint;

    private Button btnPrintReceipt;

    private Button btnCancel, btnPaymentCancel;

    private ConstraintLayout constraintLayoutList;
    private ConstraintLayout conLayoutCard, conLayoutBank;
    private ConstraintLayout bottomButton;

    private EditText etSearchKeyWord;

    private CustomProgressDialog progressDialog;
    private CustomIngProgressDialog customIngProgressDialog;
    private CustomAlertDialog customAlertDialog;
    private SignPadDialog signPadDialog;
    private Handler signHandler;

    // Title bar button
    private ImageButton btnBack, btnHome;

    Gson gson = new Gson();

    private TextView tvSearchDate;

    private int pickerType = 1;

    private String searchYM;

    private NemoSalesApprovalListRO selItem = null;

    private ConfirmDialog confirmDialog;
    private Handler cancelHandler, reprintigHandler;

    ////////////////////////////////////////////////////////////////////////////////////
    // Smatro VCAT Start
    // 서비스 인스턴스
    private SmartroVCatInterface mSmartroVCatInterface = null;

    private boolean isVCATBindError = false;

    private boolean isVCATPermissionError = true;

    private JSONObject jsonResultLast = null;

    // Smatro VCAT End
    ////////////////////////////////////////////////////////////////////////////////////

    private int isReceiptPrint = 2;


    DatePickerDialog.OnDateSetListener yymmDialog = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth){
            AndroidUtil.log("YearMonthPicker : " +"pickerType = " + pickerType +" year = " + year + ", month = " + monthOfYear + ", day = " + dayOfMonth);

            transactionPaymentHistoryViewModel.setSearchYM(year + "-" + String.format("%02d", monthOfYear) );

            transactionPaymentHistoryViewModel.apiSalesApprovalList();


        }
    };

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        transactionPaymentHistoryViewModel =
                ViewModelProviders.of(getActivity()).get(TransactionPaymentHistoryViewModel.class);
        View root = inflater.inflate(R.layout.fragment_transaction_payment_history, container, false);

        this.context = getContext();

        initUI(root);
        init();
        return root;
    }

    private void initUI(View view) {

        tvHospitalName = (TextView) view.findViewById(R.id.tvHospitalName);

        tvCardNo = (TextView) view.findViewById(R.id.tvCardNo);
        tvApprovalDate = (TextView) view.findViewById(R.id.tvApprovalDate);
        tvApprovalNo = (TextView) view.findViewById(R.id.tvApprovalNo);
        tvTotalAmount = (TextView) view.findViewById(R.id.tvTotalAmount);
        tvInstallment = (TextView) view.findViewById(R.id.tvInstallment);
        tvCardCompany = (TextView) view.findViewById(R.id.tvCardCompany);

        textView26 = (TextView) view.findViewById(R.id.textView26);

        btnPrintReceipt = (Button) view.findViewById(R.id.btnPrintReceipt);

        tvReceipt = (TextView) view.findViewById(R.id.tvReceipt);
        ragReceipt = (RadioGroup) view.findViewById(R.id.ragReceipt);

        raReceiptPrint = (RadioButton) view.findViewById(R.id.raReceiptPrint);
        raReceiptOnePrint = (RadioButton) view.findViewById(R.id.raReceiptOnePrint);
        raReceiptNotPrint = (RadioButton) view.findViewById(R.id.raReceiptNotPrint);

        constraintLayoutList = (ConstraintLayout) view.findViewById(R.id.constraintLayoutList);
        conLayoutCard = (ConstraintLayout) view.findViewById(R.id.conLayoutCard);
        conLayoutBank = (ConstraintLayout) view.findViewById(R.id.conLayoutBank);
        bottomButton = (ConstraintLayout) view.findViewById(R.id.bottomButton);

        btnCancel = (Button) view.findViewById(R.id.btnCancel);
        btnPaymentCancel = (Button) view.findViewById(R.id.btnPaymentCancel);

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
        btnPaymentCancel.setOnClickListener(click);

        btnPrintReceipt.setOnClickListener(click);

        raReceiptPrint.setOnClickListener(click);
        raReceiptOnePrint.setOnClickListener(click);
        raReceiptNotPrint.setOnClickListener(click);

        btnBack.setOnClickListener(click);

        getDefault();

        visitHospital = (NemoHospitalSearchRO) getArguments().getSerializable("visit_hospital");

        AndroidUtil.log("visitHospital : " + visitHospital);

        transactionPaymentHistoryViewModel.setHospital(visitHospital);

        tvHospitalName.setText(visitHospital.getCustNm());

        transactionPaymentHistoryViewModel.getSearchYM().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {

                searchYM = s;

                String[] arrDisp = searchYM.split("-");

                String dispYm = arrDisp[0] + "년 " + arrDisp[1] + "월";

                tvSearchDate.setText(dispYm);

            }
        });


        progressDialog = new CustomProgressDialog(getContext());
        transactionPaymentHistoryViewModel.getProgressFlag().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
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

        transactionPaymentHistoryViewModel.getAdapterlList().observe(getViewLifecycleOwner(), new Observer<ArrayList<NemoSalesApprovalListRO>>() {
            @Override
            public void onChanged(ArrayList<NemoSalesApprovalListRO> visitPlanROS) {
                rvViewAdapter.setadapterLists(visitPlanROS);
                reset();
            }
        });

        cancelHandler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);

                switch (msg.what){
                    case Const.HANDLER_CONFIRM:

                        doPaymentCancel();

                        break;
                }
            }
        };

        reprintigHandler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);

                switch (msg.what){
                    case Const.HANDLER_CONFIRM:

                        reprintingReceipt();

                        break;
                }
            }
        };

        signHandler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);

                switch (msg.what){
                    case Const.HANDLER_SIGN:

                        byte[] outArray = (byte[])msg.obj;

                        //AndroidUtil.log("Sing : " + outArray);
                        saveSignImage(outArray);

                        break;

                    case Const.HANDLER_SIGN_CANCEL:

                        try {
                            mSmartroVCatInterface.cancelService();
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }

                        break;
                }
            }
        };

        reset();

        /////////////////////////////////////////////////////////////////////////
        // VCAT 서비스 Bind
        bindVCATService();

    }

    private void getDefault()
    {
        SharedPreferences sp = context.getSharedPreferences(AndroidUtil.TAG_SP, Context.MODE_PRIVATE);

        isReceiptPrint = sp.getInt(AndroidUtil.SP_CANCEL_PRINT_OPTION, 2);

        AndroidUtil.log("isReceiptPrint : " + isReceiptPrint);

        ragReceipt.clearCheck();

        if( isReceiptPrint == 1 )
        {
            raReceiptOnePrint.setChecked(true);
            raReceiptPrint.setChecked(false);
            raReceiptNotPrint.setChecked(false);
        }
        else if( isReceiptPrint == 2 )
        {
            raReceiptOnePrint.setChecked(false);
            raReceiptPrint.setChecked(true);
            raReceiptNotPrint.setChecked(false);
        }
        else
        {
            raReceiptOnePrint.setChecked(false);
            raReceiptPrint.setChecked(false);
            raReceiptNotPrint.setChecked(true);
        }


    }

    private void setDefault()
    {
        SharedPreferences sp = context.getSharedPreferences(AndroidUtil.TAG_SP, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        editor.putInt(AndroidUtil.SP_CANCEL_PRINT_OPTION, isReceiptPrint);

        editor.commit();
    }

    private void closeProgress()
    {
        if (customIngProgressDialog != null)
        {
            customIngProgressDialog.dismiss();
            customIngProgressDialog = null;
        }
    }

    private void showProgress(String strCaption)
    {
        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                if (customIngProgressDialog != null) {
                    closeProgress();
                }

                try
                {
                    customIngProgressDialog = new CustomIngProgressDialog(getContext());
                    customIngProgressDialog.show();

                    customIngProgressDialog.updateText(strCaption);

                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
    }

    private void closeSignPadDialog()
    {
        if (signPadDialog != null)
        {
            signPadDialog.dismiss();
            signPadDialog = null;
        }
    }

    private void showAlertDialog(String strText)
    {
        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                customAlertDialog = new CustomAlertDialog(getContext());
                customAlertDialog.show();
                customAlertDialog.setMsg(strText);
            }
        });
    }

    private void showSignUiDialog()
    {
        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                if (signPadDialog != null) {
                    closeSignPadDialog();
                }

                try
                {
                    signPadDialog = new SignPadDialog(getActivity(), signHandler);
                    signPadDialog.setCancelable(false);
                    signPadDialog.show();

                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
    }

    private void saveSignImage(byte[] outArray)
    {
        try {

            JSONObject jobjPath = new JSONObject();

            String data = Base64.encodeToString(outArray, 0, outArray.length, Base64.DEFAULT);

            jobjPath.put("sign-data", data);

            AndroidUtil.log("postExtraData : " + jobjPath);

            mSmartroVCatInterface.postExtraData(jobjPath.toString());

        }
        catch (Exception ex)
        {
            ex.printStackTrace();

            AndroidUtil.log(ex.toString());
        }
    }

    private void showResult(String card_no,String approval_no,String approval_date,String total_amount, String installment, String card_company)
    {

        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                conLayoutCard.setVisibility(View.VISIBLE);

                try {
                    tvCardNo.setText(card_no);
                    tvApprovalDate.setText(approval_date);
                    tvApprovalNo.setText(approval_no);
                    tvTotalAmount.setText(AndroidUtil.dispCurrency(Long.parseLong(total_amount)));

                    String dispInstallment = "일시불";

                    if( !"0".equals( installment ))
                    {
                        dispInstallment = installment + " 개월";
                    }

                    tvInstallment.setText(dispInstallment);

                    //tvCardCompany.setText(card_company);
                }
                catch (Exception ex)
                {
                    AndroidUtil.log(ex.toString());
                }
            }
        });

    }

    private void doCancel()
    {
        confirmDialog = new ConfirmDialog(getActivity(), cancelHandler);

        confirmDialog.show();
        confirmDialog.setMsg("결제 승인을 취소 하시겠습니까?");
    }

    /**
     * 결제 취소 처리
     */
    private void doPaymentCancel()
    {
        if( isVCATBindError || isVCATPermissionError )
        {
            AndroidUtil.toast(getContext(),"카드 모듈 연결 실패!");
            return;
        }

        try {

            JSONObject jsonInput = new JSONObject();

            jsonInput.put("type", Const.VCAT_TRAN_TYPE);

            // No card
            // pg-tran-seq 값을 전달받지 못해 처리 불가능 함
            //jsonInput.put("deal", Const.VCAT_TRAN_PAYMENT_NO_CARD_CANCEL);

            // 결제 카드가 있는경우
            jsonInput.put("deal", Const.VCAT_TRAN_PAYMENT_CANCEL);

            String pgTranSeq = selItem.getTradSno();
            String approvalNo = selItem.getAprvNo();
            String approvalDate = selItem.getAprvDt();

            AndroidUtil.log("pg-tran-seq : " + pgTranSeq );
            AndroidUtil.log("approval-no : " + approvalNo );
            AndroidUtil.log("approval-date : " + approvalDate );

            // PG 거래 일련번호
            jsonInput.put("pg-tran-seq", pgTranSeq);
            // PG 거래 일련번호
            jsonInput.put("approval-no", approvalNo);
            // PG 거래 일련번호
            jsonInput.put("approval-date", approvalDate);

            long payment = selItem.getAprvAmt();
            long vat = selItem.getTax();

            AndroidUtil.log("VCAT 결제금액 : " + payment );
            AndroidUtil.log("VCAT VAT : " + vat );

            // 부가세
            jsonInput.put("surtax", vat);

            // Tip
            jsonInput.put("tip", "0");

            // 결제 금액
            jsonInput.put("total-amount", payment);

            // CAT-ID와 사업자 번호
            jsonInput.put("cat-id", Const.VCAT_CAT_ID);
            jsonInput.put("business-no", Const.VCAT_BIZ_NO);

            // 가맹형태
            jsonInput.put("member-type", Const.VCAT_MEM_TYPE);

            // 서버 정보
            jsonInput.put("van-comm", Const.vcatServerType);
            jsonInput.put("pg-comm", Const.vcatPgServerType);
            jsonInput.put("security-comm", Const.vcatKeyServerType);

            /**
             * 거래 속성을 추가 합니다.
             */
            JSONArray jsonArray = new JSONArray();

            jsonArray.put("attr-include-sign-bmp-buffer");

            jsonInput.put("attribute", jsonArray);


            AndroidUtil.log("VCAT : =============================== <REQUEST> ===================================");
            AndroidUtil.log("VCAT : " + jsonInput.toString());
            AndroidUtil.log("VCAT : =============================================================================");
            getVCatInterface().executeService(jsonInput.toString(), new SmartroVCatCallback.Stub()
            {
                @Override
                public void onServiceEvent(String strEventJSON)
                {
                    AndroidUtil.log("VCAT : =============================== <EVENT> ====================================");
                    AndroidUtil.log("VCAT : " + strEventJSON);
                    AndroidUtil.log("VCAT : ============================================================================");
                    try
                    {
                        final JSONObject jsonEvent = new JSONObject(strEventJSON);

                        if (jsonEvent.has("event"))
                        {
                            if (jsonEvent.getString("event").equals("status"))
                            {
                                showProgress(jsonEvent.getString("description"));
                            }
                            else if (jsonEvent.getString("event").equals("prompt")) {
                                if (jsonEvent.getString("description").equals("sign")) {
                                    showSignUiDialog();
                                }
                            }

//                            else if (jsonEvent.getString("event").equals("sign-draw"))
//                            {
//                                showSignDialog();
//                                drawSignPosition(jsonEvent.getInt("x"), jsonEvent.getInt("y"));
//                            }
//                            else if (jsonEvent.getString("event").equals("sign-end"))
//                            {
//                                closeSignDialog();
//                            }
//                            else if (jsonEvent.getString("event").equals("prompt"))
//                            {
//                                if (jsonEvent.getString("description").equals("sign"))
//                                {
//                                    showSignUiDialog();
//                                }
//                                else if (jsonEvent.getString("description").equals("pay-type"))
//                                {
//                                    String strPayName = "알 수 없음";
//
//                                    // 페이 이름을 맵핑 테이블에서 가져온다.
//                                    for (String strItem : mhmPayTypePerPrefix.keySet())
//                                    {
//                                        if (mhmPayTypePerPrefix.get(strItem).equals(jsonEvent.getString("pay-type")))
//                                        {
//                                            strPayName = strItem;
//                                            break;
//                                        }
//                                    }
//
//                                    // 페이 종류를 확인하고, 필요 시 직접 선택한다.
//                                    showYesOrNoDialog("페이 종류 확인", "[" + strPayName + "] (이)가 맞습니까?",
//                                            "맞음", new DialogInterface.OnClickListener()
//                                            {
//                                                @Override
//                                                public void onClick(DialogInterface dialogInterface, int i)
//                                                {
//                                                    JSONObject jsonPost = new JSONObject();
//
//                                                    try
//                                                    {
//                                                        jsonPost.put("pay-type", jsonEvent.getString("pay-type"));
//                                                        getVCatInterface().postExtraData(jsonPost.toString());
//                                                    }
//                                                    catch (Exception e)
//                                                    {
//                                                        e.printStackTrace();
//                                                    }
//                                                }
//                                            },
//                                            "아님(직접 선택)", new DialogInterface.OnClickListener()
//                                            {
//                                                @Override
//                                                public void onClick(DialogInterface dialogInterface, int i)
//                                                {
//                                                    showPopupSpinner("페이 타입을 선택해 주세요.", mlstPayType, new DialogInterface.OnClickListener()
//                                                    {
//                                                        @Override
//                                                        public void onClick(DialogInterface dialogInterface, int i)
//                                                        {
//                                                            JSONObject jsonPost = new JSONObject();
//
//                                                            try
//                                                            {
//                                                                jsonPost.put("pay-type", mhmPayTypePerPrefix.get(mlstPayType.get(i)));
//                                                                getVCatInterface().postExtraData(jsonPost.toString());
//                                                            }
//                                                            catch (Exception e)
//                                                            {
//                                                                e.printStackTrace();
//                                                            }
//                                                        }
//                                                    });
//                                                }
//                                            }
//                                    );
//                                }
//                                else if (jsonEvent.getString("description").equals("card-no"))
//                                {
//                                    String cardNo = jsonEvent.getString("card-no");
//
//                                    // 페이 종류를 확인하고, 필요 시 직접 선택한다.
//                                    showYesOrNoDialog("거래 계속 진행 여부?", "[" + cardNo + "] (이)가 맞습니까?",
//                                            "진행", new DialogInterface.OnClickListener()
//                                            {
//                                                @Override
//                                                public void onClick(DialogInterface dialogInterface, int i)
//                                                {
//                                                    JSONObject jsonPost = new JSONObject();
//
//                                                    try
//                                                    {
//                                                        jsonPost.put("go-next-step", "y");
//                                                        getVCatInterface().postExtraData(jsonPost.toString());
//                                                    }
//                                                    catch (Exception e)
//                                                    {
//                                                        e.printStackTrace();
//                                                    }
//                                                }
//                                            },
//                                            "중단", new DialogInterface.OnClickListener()
//                                            {
//                                                @Override
//                                                public void onClick(DialogInterface dialogInterface, int i)
//                                                {
//                                                    JSONObject jsonPost = new JSONObject();
//
//                                                    try
//                                                    {
//                                                        jsonPost.put("go-next-step", "n");
//                                                        getVCatInterface().postExtraData(jsonPost.toString());
//                                                    }
//                                                    catch (Exception e)
//                                                    {
//                                                        e.printStackTrace();
//                                                    }
//                                                }
//                                            }
//                                    );
//                                }
//                                else if (jsonEvent.getString("description").equals("aid-selection"))
//                                {
//                                    JSONArray list = jsonEvent.getJSONArray("appendix");
//
//                                    List<String> aidList = new ArrayList<>();
//
//                                    for(int i = 0; i < list.length(); i++)
//                                    {
//                                        aidList.add(list.getString(i));
//                                    }
//
//                                    showPopupSpinner("어플리케이션을 선택해 주세요", aidList, new DialogInterface.OnClickListener()
//                                    {
//                                        @Override
//                                        public void onClick(DialogInterface dialogInterface, int i)
//                                        {
//                                            JSONObject jsonPost = new JSONObject();
//
//                                            try
//                                            {
//                                                jsonPost.put("aid-selection", aidList.get(i));
//                                                getVCatInterface().postExtraData(jsonPost.toString());
//                                            }
//                                            catch (Exception e)
//                                            {
//                                                e.printStackTrace();
//                                            }
//                                        }
//                                    });
//                                }
//                            }
                        }
                    }
                    catch (Exception e)
                    {
                        closeProgress();

                        e.printStackTrace();
                    }
                }

                @Override
                public void onServiceResult(String strResultJSON)
                {
                    AndroidUtil.log("VCAT : =============================== <RESULT> ====================================");
                    AndroidUtil.log("VCAT : " + strResultJSON);
                    AndroidUtil.log("VCAT : ============================================================================");

                    closeProgress();

                    try
                    {
                        int iResult;
                        final JSONObject jsonResult = new JSONObject(strResultJSON);

                        iResult = Integer.parseInt(jsonResult.getString("service-result"));
                        if (iResult == 0)
                        {
                            if (jsonResult.getString("response-code").equals("00"))
                            {
                                String card_no = "";
                                String approval_no = "";
                                String approval_date = "";
                                String total_amount = "";
                                String installment = "";
                                String card_company = "";

                                if (jsonResult.has("card-no"))
                                {
                                    card_no = jsonResult.getString("card-no");
                                }

                                //승인 일시를 설정 합니다.
                                if (jsonResult.has("approval-date") && jsonResult.has("approval-no"))
                                {
                                    approval_no = jsonResult.getString("approval-no");
                                    approval_date = AndroidUtil.dispDate(jsonResult.getString("approval-date"));

                                    if (jsonResult.has("approval-time") )
                                    {
                                        approval_date += " " + AndroidUtil.dispTime(jsonResult.getString("approval-time"));
                                    }
                                }

                                // 승인 금액
                                if (jsonResult.has("total-amount"))
                                {
                                    total_amount = jsonResult.getString("total-amount");
                                }

                                // 할부
                                if (jsonResult.has("installment"))
                                {
                                    installment = jsonResult.getString("installment");
                                }

                                // 카드사
                                if (jsonResult.has("issuer-info"))
                                {
                                    card_company = jsonResult.getString("issuer-info");
                                }

                                // 결제 결과 출력
                                showResult(card_no,approval_no,approval_date,total_amount,installment,card_company);

                                // 프린트
                                if( isReceiptPrint != 0 )
                                {
                                    printingReceipt(jsonResult, true);
                                }

                                AndroidUtil.log("결과 전송");

                                // 결제 결과 전송
                                addApproval(jsonResult);

                                jsonResultLast = jsonResult;


                                //빌키 거래 일때만 사용 합니다.
//                                if (mStrTranType.equals("bill-key") && jsonResult.has("bill-key"))
//                                {
//                                    setBillKeyValue(jsonResult.getString("bill-key"));
//                                }

                                // 전자영수증 발급을 요청
//                                if (((CheckBox) findViewById(R.id.chkbox_issuing_ereceipt)).isChecked())
//                                {
//                                    issuingEReceipt(jsonResult);
//                                }
//                                else
//                                {
//                                    if (((CheckBox) findViewById(R.id.chkbox_printing)).isChecked())
//                                    {
//                                        CheckBox checkBox = findViewById(R.id.chkbox_printing_for_b201);
//
//                                        printingExample(jsonResult, checkBox.isChecked());
//                                    }
//                                }
                            }
                            else
                            {
                                showAlertDialog("거래 거절! [" + jsonResult.get("display-msg") + "]");
                            }
                        }
                        else
                        {

                            if (signPadDialog != null) {
                                closeSignPadDialog();
                            }

                            if (jsonResult.has("service-description"))
                            {
                                showAlertDialog("오류 : " + jsonResult.get("service-description") + " (" + iResult + ")");
                            }
                            else
                            {
                                showAlertDialog("오류 :" + jsonResult.get("service-result"));
                            }
                        }
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }

                }
            });

        }
        catch (Exception ex)
        {
            AndroidUtil.log("VCAT : " + ex.toString());
        }




    }

    private void addApproval(JSONObject jsonResult)
    {
        NemoSalesApprovalAddPO param = new NemoSalesApprovalAddPO();

        String aprvDt = "";                  /* 승인일자 approval-date */
        String custCd = "";                  /* 고객코드 */
        String userId = "";                  /* 담당자아이디 */
        long aprvAmt = 0;                   /* 승인금액 total-amount */
        String devcNm = "";                  /* 장비명 device-name */
        String devcCertInfo = "";            /* 장비인증정보 device-auth-info */
        String devcCertVer = "";             /* 장비인증버전 device-auth-ver */
        String devcPrtNo = "";               /* 장비제품번호 device-serial */
        String afsronNm = "";                /* 가맹점주명 business-owner-name */
        String afsrNm = "";                  /* 가맹점명 business-name */
        String afsrTelno = "";               /* 가맹점전화번호 business-phone-no */
        String afsrAddr = "";                /* 가맹점주소 business-address */
        String scrnMesg = "";                /* 화면메시지 display-msg */
        String pmntAprvCd = "";              /* 결제승인코드 response-code */
        String aprvNo = "";                  /* 승인번호 approval-no */
        String crdNo = "";                   /* 카드번호 card-no */
        String tradUniqNo = "";              /* 거래고유번호 unique-no */
        String ccmpnAftNo = "";              /* 카드사가맹번호 merchant-no */
        String aqirInfo = "";                /* 매입사정보 acquire-info */
        String issrInfo = "";                /* 발급사정보 issuer-info */
        String rciptTitl = "";               /* 영수증제목 receipt-title */
        String rciptCont = "";               /* 영수증내용 receipt-msg */
        String tradSno = "";                 /* 거래일련번호 pg-tran-seq */
        String scrnIndiMesg = "";            /* 화면표시메시지 display-msg */
        String srvcOprtRslt = "";            /* 서비스작동결과 service-result */
        String errCont = "";                 /* 오류내용 service-description */
        String aprvRqstSno = "";             /* 승인요청일련번호 van-tran-seq */
        String blkeyEftnsYn = "";            /* 빌키유효성여부 enable-bill-key */
        String addtInfo = "";                /* 추가정보 filler1 */

        String aprvRqstType = "";
        String tradType = "";
        long tax = 0;
        long srvchr = 0;
        String instmMmCnt = "";
        String devc = "";
        String afsr = ""; /* 필드매핑 못 찾음 */
        String bizrno = "";
        String afsrForm = "";
        String vanCmmuSetp = "";
        String pgCmmuSetp = "";
        String scrtyCmmuSetp = "";
        String devcSwVer = "";
        String sgntImgPath = "";
        String sgntBmpPath = "";
        String aprvTm = "";

        try {
            if (jsonResult.has("approval-date"))
            {
                aprvDt = jsonResult.getString("approval-date");
            }

            if (jsonResult.has("total-amount"))
            {
                aprvAmt = Long.parseLong(jsonResult.getString("total-amount"));
            }

            if (jsonResult.has("device-name"))
            {
                devcNm = jsonResult.getString("device-name");
            }

            if (jsonResult.has("device-auth-info"))
            {
                devcCertInfo = jsonResult.getString("device-auth-info");
            }

            if (jsonResult.has("device-auth-ver"))
            {
                devcCertVer = jsonResult.getString("device-auth-ver");
            }

            if (jsonResult.has("device-serial"))
            {
                devcPrtNo = jsonResult.getString("device-serial");
            }

            if (jsonResult.has("business-owner-name"))
            {
                afsronNm = jsonResult.getString("business-owner-name");
            }

            if (jsonResult.has("business-name"))
            {
                afsrNm = jsonResult.getString("business-name");
            }

            if (jsonResult.has("business-phone-no"))
            {
                afsrTelno = jsonResult.getString("business-phone-no");
            }

            if (jsonResult.has("business-address"))
            {
                afsrAddr = jsonResult.getString("business-address");
            }

            if (jsonResult.has("display-msg"))
            {
                scrnMesg = jsonResult.getString("display-msg");
            }

            if (jsonResult.has("response-code"))
            {
                pmntAprvCd = jsonResult.getString("response-code");
            }

            if (jsonResult.has("approval-no"))
            {
                aprvNo = jsonResult.getString("approval-no");
            }

            if (jsonResult.has("card-no"))
            {
                crdNo = jsonResult.getString("card-no");
            }

            if (jsonResult.has("unique-no"))
            {
                tradUniqNo = jsonResult.getString("unique-no");
            }

            if (jsonResult.has("merchant-no"))
            {
                ccmpnAftNo = jsonResult.getString("merchant-no");
            }

            if (jsonResult.has("acquire-info"))
            {
                aqirInfo = jsonResult.getString("acquire-info");
            }

            if (jsonResult.has("issuer-info"))
            {
                issrInfo = jsonResult.getString("issuer-info");
            }

            if (jsonResult.has("receipt-title"))
            {
                rciptTitl = jsonResult.getString("receipt-title");
            }

            if (jsonResult.has("receipt-msg"))
            {
                rciptCont = jsonResult.getString("receipt-msg");
            }

            if (jsonResult.has("pg-tran-seq"))
            {
                tradSno = jsonResult.getString("pg-tran-seq");
            }

            if (jsonResult.has("display-msg"))
            {
                scrnIndiMesg = jsonResult.getString("display-msg");
            }

            if (jsonResult.has("service-result"))
            {
                srvcOprtRslt = jsonResult.getString("service-result");
            }

            if (jsonResult.has("service-description"))
            {
                errCont = jsonResult.getString("service-description");
            }

            if (jsonResult.has("van-tran-seq"))
            {
                aprvRqstSno = jsonResult.getString("van-tran-seq");
            }

            if (jsonResult.has("enable-bill-key"))
            {
                blkeyEftnsYn = jsonResult.getString("enable-bill-key");
            }

            if (jsonResult.has("filler1"))
            {
                addtInfo = jsonResult.getString("filler1");
            }

            if (jsonResult.has("type"))
            {
                aprvRqstType = jsonResult.getString("type");
            }

            if (jsonResult.has("deal"))
            {
                tradType = jsonResult.getString("deal");
            }

            if (jsonResult.has("surtax"))
            {
                tax = Long.parseLong(jsonResult.getString("surtax"));
            }

            if (jsonResult.has("tip"))
            {
                srvchr = Long.parseLong(jsonResult.getString("tip"));
            }

            if (jsonResult.has("installment"))
            {
                instmMmCnt = jsonResult.getString("installment");
            }

            if (jsonResult.has("cat-id"))
            {
                devc = jsonResult.getString("cat-id");
            }

            if (jsonResult.has("business-no"))
            {
                bizrno = jsonResult.getString("business-no");
            }

            if (jsonResult.has("member-type"))
            {
                afsrForm = jsonResult.getString("member-type");
            }

            if (jsonResult.has("van-comm"))
            {
                vanCmmuSetp = jsonResult.getString("van-comm");
            }

            if (jsonResult.has("pg-comm"))
            {
                pgCmmuSetp = jsonResult.getString("pg-comm");
            }

            if (jsonResult.has("security-comm"))
            {
                scrtyCmmuSetp = jsonResult.getString("security-comm");
            }

            if (jsonResult.has("device-sw-version"))
            {
                devcSwVer = jsonResult.getString("device-sw-version");
            }

            if (jsonResult.has("sign-img-path"))
            {
                sgntImgPath = jsonResult.getString("sign-img-path");
            }

            if (jsonResult.has("sign-bmp-path"))
            {
                sgntBmpPath = jsonResult.getString("sign-bmp-path");
            }

            if (jsonResult.has("approval-time"))
            {
                aprvTm = jsonResult.getString("approval-time");
            }

            param.setAprvDt(aprvDt);
            param.setAprvAmt(aprvAmt);
            param.setDevcNm(devcNm);
            param.setDevcCertInfo(devcCertInfo);
            param.setDevcCertVer(devcCertVer);
            param.setDevcPrtNo(devcPrtNo);
            param.setAfsronNm(afsronNm);
            param.setAfsrNm(afsrNm);
            param.setAfsrTelno(afsrTelno);
            param.setAfsrAddr(afsrAddr);
            param.setScrnMesg(scrnMesg);
            param.setPmntAprvCd(pmntAprvCd);
            param.setAprvNo(aprvNo);
            param.setCrdNo(crdNo);
            param.setTradUniqNo(tradUniqNo);
            param.setCcmpnAftNo(ccmpnAftNo);
            param.setAqirInfo(aqirInfo);
            param.setIssrInfo(issrInfo);
            param.setRciptTitl(rciptTitl);
            param.setRciptCont(rciptCont);
            param.setTradSno(tradSno);
            param.setScrnIndiMesg(scrnIndiMesg);
            param.setSrvcOprtRslt(srvcOprtRslt);
            param.setErrCont(errCont);
            param.setAprvRqstSno(aprvRqstSno);
            param.setBlkeyEftnsYn(blkeyEftnsYn);
            param.setAddtInfo(addtInfo);

            param.setAprvRqstType(aprvRqstType);
            param.setTradType(tradType);
            param.setTax(tax);
            param.setSrvchr(srvchr);
            param.setInstmMmCnt(instmMmCnt);
            param.setDevc(devc);
            param.setAfsr(afsr);
            param.setBizrno(bizrno);
            param.setAfsrForm(afsrForm);
            param.setVanCmmuSetp(vanCmmuSetp);
            param.setPgCmmuSetp(pgCmmuSetp);
            param.setScrtyCmmuSetp(scrtyCmmuSetp);
            param.setDevcSwVer(devcSwVer);
            param.setSgntImgPath(sgntImgPath);
            param.setSgntBmpPath(sgntBmpPath);
            param.setAprvTm(aprvTm);


            transactionPaymentHistoryViewModel.apiAddApproval(param);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            AndroidUtil.log(ex.toString());
        }




    }

    ////////////////////////////////////////////////////////////////////////////////////
    // Smatro VCAT Start
    private void bindVCATService()
    {
        /** 호출 관련 인텐트 값 입력
         */
        Intent intentTemp = new Intent(Const.VCAT_SERVER_ACTION);
        intentTemp.setPackage(Const.VCAT_SERVER_PACKAGE);
        /** 서비스 바인드 처리
         */
        if(getContext().bindService(intentTemp, mServiceConnectionExample, Context.BIND_AUTO_CREATE) == false)
        {
            AndroidUtil.log("VCAT : bindService Fail!!!");

            // Bind 오류시 실행
            // awakeVCATService() 실행 후 다시 bind 함
            if( !isVCATBindError )
            {
                isVCATBindError = true;
                awakeVCATService();
            }
            else
            {
                AndroidUtil.toast(getContext(), "카드 모듈 연결 실패!");
            }

        }
    }

    private void awakeVCATService()
    {
        String schemeParam = "smartroapp://vcatscheme?";
        schemeParam += "manage=awake";

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(schemeParam));

        startActivityForResult(intent, Const.VCAT_SCHEME);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        if (resultCode == Const.VCAT_SCHEME)
        {
            if (data != null)
            {
                if (data.hasExtra("result"))
                {
                    if (data.getStringExtra("result").equals("0000"))
                    {
                        AndroidUtil.log("VCAT : 프로그램이 실행되었습니다.");
                    }
                }
            }

            bindVCATService();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    /** 서비스 바인드-언바인드 관련 인라인 메써드
     */
    private final ServiceConnection mServiceConnectionExample = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mSmartroVCatInterface = SmartroVCatInterface.Stub.asInterface(service);

            try {
                connectedWithService();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name)
        {
            disconnectedWithService();
        }
    };

    protected void connectedWithService()
    {
        AndroidUtil.log("VCAT : 서비스가 연결되었습니다.");

        // Bind 성공후 Permission 요청
        vcatPermission();
    }

    protected void disconnectedWithService()
    {
        AndroidUtil.log("VCAT : 서비스 연결이 해제 되었습니다.");
        mSmartroVCatInterface = null;
    }

    private void vcatPermission()
    {

        JSONObject jsonInput = new JSONObject();

        try
        {
            jsonInput.put("service", "function");
            jsonInput.put("service-manage", "grant-permission");

            AndroidUtil.log( "VCAT : =============================== <REQUEST> ===================================");
            AndroidUtil.log( "VCAT : " + jsonInput.toString());
            AndroidUtil.log( "VCAT : =============================================================================");
            getVCatInterface().executeService(jsonInput.toString(), new SmartroVCatCallback.Stub() {
                @Override
                public void onServiceEvent(String strEventJSON) { }

                @Override
                public void onServiceResult(String strResultJSON) {

                    AndroidUtil.log("VCAT : =============================== <RESULT> ===================================");
                    AndroidUtil.log("VCAT : " + strResultJSON);
                    AndroidUtil.log("VCAT : ============================================================================");

                    try {
                        int iResult;
                        JSONObject jsonResult = new JSONObject(strResultJSON);

                        iResult = Integer.valueOf(jsonResult.getString("service-result"));
                        if (iResult == 0) {
                            AndroidUtil.log("VCAT : 권한 부여 성공!");

                            isVCATPermissionError = false;
                        }
                        else {
                            AndroidUtil.log("VCAT : 오류 코드 :" + jsonResult.get("service-result"));
                        }
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        catch (Exception e)
        {
            AndroidUtil.log(e.toString());
        }

    }

    protected SmartroVCatInterface getVCatInterface()
    {
        return mSmartroVCatInterface;
    }

    /** 실물 영수증 예제 인쇄 함수
     *
     * @param jsonData - 인쇄 관련 데이터
     */
    private void printingReceipt(JSONObject jsonData, boolean isB201)
    {
        try
        {
            byte[] baCut = {0x1b, 0x6d};                    //영수증 컷팅 명령(EPSON 프린터 호환)
            byte[] ba2XHeight = {0x1d, 0x21, 0x01};         //글자 확대 명령(EPSON 프린터 호환)
            byte[] baNormal = {0x1d, 0x21, 0x00};           //기본 글자 명령(EPSON 프린터 호환)
            byte[] baRasterBitImg = {0x1d, 0x76, 0x30};     //레스터 비트맵 출력(서명 이미지)
            byte[] baBarCode = {0x1d, 0x6b};                //바코드출력
            ArrayList<Byte> alPrintingStream = new ArrayList<>();
            StringBuilder sbContents;

            long lSurtax = 0, lTip = 0, lTotalAmount, lMinus = 0;

            for (int i = 0; i < isReceiptPrint ; i++)
            {
                sbContents = new StringBuilder();

                //영수증 타이틀
                lTotalAmount = Long.parseLong(jsonData.getString("total-amount"));

                if (jsonData.has("surtax") && jsonData.getString("surtax").length() > 0)
                {
                    lSurtax = Long.parseLong(jsonData.getString("surtax"));
                }
                if (jsonData.has("tip") && jsonData.getString("tip").length() > 0)
                {
                    lTip = Long.parseLong(jsonData.getString("tip"));
                }

                if (jsonData.getString("type").equals("credit"))
                {
                    if (jsonData.getString("deal").equals("approval"))
                    {
                        sbContents.append(new String(ba2XHeight)).append("신용 승인 매출 전표\n").append(new String(baNormal));
                        lMinus = 1;
                    }
                    else if (jsonData.getString("deal").equals("cancellation"))
                    {
                        sbContents.append(new String(ba2XHeight)).append("신용 취소 매출 전표\n").append(new String(baNormal));
                        lMinus = -1;
                    }
                    else if (jsonData.getString("deal").equals("partial-cancel"))
                    {
                        sbContents.append(new String(ba2XHeight)).append("신용 부분 취소 매출 전표\n").append(new String(baNormal));
                        lMinus = -1;
                    }
                    else if (jsonData.getString("deal").equals("no-card-cancel"))
                    {
                        sbContents.append(new String(ba2XHeight)).append("신용 무 카드 취소 매출 전표\n").append(new String(baNormal));
                        lMinus = -1;
                    }
                }
                else if (jsonData.getString("type").equals("cash"))
                {
                    if (jsonData.getString("deal").equals("approval"))
                    {
                        sbContents.append(new String(ba2XHeight)).append("현금영수증 승인 매출 전표\n").append(new String(baNormal));
                        lMinus = 1;
                    }
                    else if (jsonData.getString("deal").equals("cancellation"))
                    {
                        sbContents.append(new String(ba2XHeight)).append("현금영수증 취소 매출 전표\n").append(new String(baNormal));
                        lMinus = -1;
                    }
                }
//                else if (jsonData.getString("type").equals("pay"))
//                {
//                    String strPayName = "간편 결제";
//
//                    // 페이 이름을 맵핑 테이블에서 가져온다.
//                    for (String strItem : mhmPayTypePerPrefix.keySet())
//                    {
//                        if (mhmPayTypePerPrefix.get(strItem).equals(jsonData.getString("pay-type")))
//                        {
//                            strPayName = strItem;
//                            break;
//                        }
//                    }
//
//                    if (jsonData.getString("deal").equals("approval"))
//                    {
//                        sbContents.append(new String(ba2XHeight)).append("[").append(strPayName).append("] 승인 매출 전표\n").append(new String(baNormal));
//                        lMinus = 1;
//                    }
//                    else if (jsonData.getString("deal").equals("cancellation") || jsonData.getString("deal").equals("cancel"))
//                    {
//                        sbContents.append(new String(ba2XHeight)).append("[").append(strPayName).append("]  취소 매출 전표\n").append(new String(baNormal));
//                        lMinus = -1;
//                    }
//                }

                //영수증 내용
                sbContents.append("--------------------------------\n");
                sbContents.append(String.format("가 맹 점 명: %s\n", jsonData.getString("business-name")));
                sbContents.append(String.format("대  표  자 : %s\n", jsonData.getString("business-owner-name")));
                sbContents.append(String.format("사업자 번호: %s\n", jsonData.getString("business-no")));

                String afsrTel = "";
                if (jsonData.has("business-phone-no"))
                {
                    afsrTel = jsonData.getString("business-phone-no");
                }

                sbContents.append(String.format("사업장 전화: %s\n", afsrTel));
                sbContents.append(String.format("사업장 주소: %.32s\n", jsonData.getString("business-address")));
                sbContents.append("--------------------------------\n");
                sbContents.append(String.format("단말기 번호: %s\n", jsonData.getString("cat-id")));
                if (jsonData.has("card-no"))
                {
                    sbContents.append(String.format("카 드 번 호: %s\n", jsonData.getString("card-no")));
                }
                sbContents.append(String.format("승 인 번 호: %s\n", jsonData.getString("approval-no")));
                sbContents.append(String.format("승 인 날 짜: %s\n", jsonData.getString("approval-date")));
                sbContents.append(String.format("승 인 시 간: %s\n", jsonData.getString("approval-time")));
                sbContents.append("--------------------------------\n");
                sbContents.append(String.format("물 품 가 액: %17s원\n", AndroidUtil.dispLongComma((lTotalAmount - lSurtax - lTip) * lMinus)));
                if (lSurtax > 0)
                {
                    sbContents.append(String.format("부   가  세: %17s원\n", AndroidUtil.dispLongComma(lSurtax * lMinus)));
                }
                if (lTip > 0)
                {
                    sbContents.append(String.format("봉   사  료: %17s원\n", AndroidUtil.dispLongComma(lTip * lMinus)));
                }

                // 할부 개월
                String installment = "0";
                if (jsonData.has("installment"))
                {
                    installment = jsonData.getString("installment");

                    if( !"0".equals(installment))
                    {
                        sbContents.append(String.format("할 부 개 월: %15s개월\n", installment));
                    }
                }

                sbContents.append(String.format("합 계 금 액: %17s원\n", AndroidUtil.dispLongComma(lTotalAmount * lMinus)));
                sbContents.append("--------------------------------\n");
                sbContents.append("감사합니다.\n\n");

                //문자열을 바이트 배열로 바꾼 뒤 프린팅 스트림에 저장한다.
                for (byte bItem : sbContents.toString().getBytes("KSC5601"))
                {
                    alPrintingStream.add(bItem);
                }

                //서명 이미지 버퍼가 있다면, 출력한다.
                if (jsonData.has("sign-bmp-buffer"))
                {
                    JSONArray jaValue = new JSONArray(jsonData.getString("sign-bmp-buffer"));
                    int iWidth = 128 / 8;
                    int iHeight = 64;

                    if (jaValue.length() == 1086)
                    {
                        //"고객 서명" 문자열 출력
                        for (byte bItem : ("** 고객 서명 **\n").getBytes("KSC5601"))
                        {
                            alPrintingStream.add(bItem);
                        }

                        if(isB201 == true)
                        {
                            byte[] baData = new byte[1086];

                            for (int j = 0; j < 1086; j++)
                            {
                                baData[j] = (byte) (jaValue.getInt(j) & 0x000000FF);
                            }

                            BitmapFactory.Options options = new BitmapFactory.Options();
                            options.inScaled = false;
                            Bitmap bitmap = BitmapFactory.decodeByteArray(baData, 0, 1086, options);

//                            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.sign, options);

                            byte[] decode = new C1itPrint(0).Print(bitmap, 1);

                            if(decode!=null)
                            {
                                alPrintingStream.add((byte)0x1C);

                                for (int k = 0; k < decode.length; k++)
                                {
                                    alPrintingStream.add(decode[k]);
                                }

                                alPrintingStream.add((byte)0x1C);
                            }
                        }
                        else
                        {
                            //비트맵 출력 명령을 넣는다. [GS v 0]
                            for (byte bItem : baRasterBitImg)
                            {
                                alPrintingStream.add(bItem);
                            }

                            //비트맵 출력 파라미터를 넣는다. [ m xL xH yL yH ]
                            /*
                               0, 48 Normal         180 DPI 180 DPI - 일반
                               1, 49 Double-width   180 DPI 90 DPI  - 가로 2배 확대
                               2, 50 Double-height  90 DPI 180 DPI  - 세로 2배 확대
                               3, 51 Quadruple      90 DPI 90 DPI   - 가로,세로 2배 확대 (4배 확대)
                             */
                            //4배 확대 사용
                            alPrintingStream.add((byte) 51);
                            //가로 사이즈 128픽셀
                            alPrintingStream.add((byte) (iWidth % 256));
                            alPrintingStream.add((byte) (iWidth / 256));
                            //세로 사이즈 64픽셀
                            alPrintingStream.add((byte) (iHeight % 256));
                            alPrintingStream.add((byte) (iHeight / 256));
                            /* Data1 ~ DataK
                            - 비트맵 헤더 62바이트 제외하고 비트맵 버퍼를 만든다.
                             */
                            byte[] baData = new byte[1024];
                            /* 비트맵 이미지를 출력용으로 바꾸기 위해 반전하면서 버퍼에 복사한다.
                             */
                            for (int iBmp = 0; iBmp < 1024; iBmp++)
                            {
                                baData[iBmp] = (byte) (jaValue.getInt(iBmp + 62) ^ 0xFF);
                            }

                            byte[] baOrder = new byte[1024];
                            /* 비트맵 이미지를 Y축 기준으로 역으로 정렬한다.
                             */
                            for (int iY = (iHeight - 1); iY >= 0; iY--)
                            {
                                for (int iX = 0; iX < iWidth; iX++)
                                {
                                    baOrder[iX + (Math.abs(iY - (iHeight - 1)) * iWidth)] = baData[iX + (iY * iWidth)];
                                }
                            }

                            //비트맵 출력 데이터를 넣는다. [ d1....dk ]
                            for (int iBmp = 0; iBmp < 1024; iBmp++)
                            {
                                alPrintingStream.add(baOrder[iBmp]);
                            }
                        }
                    }
                }

                //바코드를 출력한다.
                if(isB201 == false)
                {
                    //"바코드 (승인번호:nnnnnn)" 문자열 출력
                    for (byte bItem : ("\n** 바코드(승인번호:"+ jsonData.getString("approval-no") +") **\n").getBytes("KSC5601"))
                    {
                        alPrintingStream.add(bItem);
                    }

                    for (byte bItem : baBarCode)
                    {
                        alPrintingStream.add(bItem);
                    }
                }
                /*
                    'Founction A'
                    0 UPC-A
                    1 UPC-E
                    2 JAN13
                    3 JAN8
                    4 CODE39
                    5 ITF
                    6 CODEBAR
                 */
                //바코드 타입을 넣는다. [ m ] (CODE128)
                alPrintingStream.add((byte)73);
                //바코드 길이를 넣는다. [ n ]
                alPrintingStream.add((byte)jsonData.getString("approval-no").length());
                //바코드 출력 데이터를 넣는다. [ d1....dk + NUL(0) ]
                for (byte bItem : (jsonData.getString("approval-no")).getBytes())
                {
                    alPrintingStream.add(bItem);
                }

                //공란을 넣는다.
                for (byte bItem : ("\n\n\n\n\n").getBytes())
                {
                    alPrintingStream.add(bItem);
                }

                //영수증 컷팅한다.
                for (byte bItem : baCut)
                {
                    alPrintingStream.add(bItem);
                }
            }

            JSONObject JsonPrint = new JSONObject();
            JSONArray JsonPrintContents = new JSONArray();

            for (byte bItem : alPrintingStream)
            {
                JsonPrintContents.put(bItem);
            }

            JsonPrint.put("service", "printing");

            /****************************************************************
             * 중요!! 프린터 필요 시, 하기 사항을 유의해 주세요.
             ****************************************************************/
            JsonPrint.put("printer", "printer-comm1");    //"printer-comm1"만 사용하며, "printer-comm1" ~ "printer-comm16"까지 동일하게 설정 저장하고 사용 가능합니다.
            JsonPrint.put("contents", JsonPrintContents);        //바이트 값을 JSONArray 에 Integer 배열 형태로 집어넣어줘야 함.

            AndroidUtil.log("VCAT : =============================== <REQUEST> ===================================");
            AndroidUtil.log("VCAT : " + JsonPrint.toString());
            AndroidUtil.log("VCAT : =============================================================================");
            getVCatInterface().executeService(JsonPrint.toString(), new SmartroVCatCallback.Stub()
            {
                @Override
                public void onServiceEvent(String strEventJSON)
                {
                    try
                    {
                        final JSONObject jsonEvent = new JSONObject(strEventJSON);

                        if (jsonEvent.has("event"))
                        {
                            if (jsonEvent.getString("event").equals("status"))
                            {
                                showProgress(jsonEvent.getString("description"));
                            }
                        }
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onServiceResult(String strResultJSON)
                {
                    AndroidUtil.log("VCAT : =============================== <RESULT> ====================================");
                    AndroidUtil.log("VCAT : " + strResultJSON);
                    AndroidUtil.log("VCAT : ============================================================================");

                    try
                    {
                        JSONObject jsonResult = new JSONObject(strResultJSON);

                        closeProgress();
                        if (Integer.parseInt(jsonResult.getString("service-result")) == 0)
                        {
                            showAlertDialog("출력 완료!\r\n인쇄가 완료되었습니다.");
                        }
                        else
                        {
                            showAlertDialog("출력 오류..\n오류 코드 :" + jsonResult.get("service-result"));
                        }
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void doReprinting()
    {
        confirmDialog = new ConfirmDialog(getActivity(), reprintigHandler);

        confirmDialog.show();
        confirmDialog.setMsg("영수증을 재출력 하시겠습니까?");
    }

    private void reprintingReceipt()
    {
        try
        {
            byte[] baCut = {0x1b, 0x6d};                    //영수증 컷팅 명령(EPSON 프린터 호환)
            byte[] ba2XHeight = {0x1d, 0x21, 0x01};         //글자 확대 명령(EPSON 프린터 호환)
            byte[] baNormal = {0x1d, 0x21, 0x00};           //기본 글자 명령(EPSON 프린터 호환)
            byte[] baRasterBitImg = {0x1d, 0x76, 0x30};     //레스터 비트맵 출력(서명 이미지)
            byte[] baBarCode = {0x1d, 0x6b};                //바코드출력
            ArrayList<Byte> alPrintingStream = new ArrayList<>();
            StringBuilder sbContents;

            long lSurtax = 0, lTip = 0, lTotalAmount, lMinus = 0;

            for (int i = 0; i < 1; i++)
            {
                sbContents = new StringBuilder();

                //영수증 타이틀
                lTotalAmount = selItem.getAprvAmt();
                lSurtax = selItem.getTax();
                lTip = selItem.getSrvchr();

                String approvalType = selItem.getAprvRqstType();

                if ("credit".equals(approvalType))
                {
                    String tradType = selItem.getTradType();

                    if ("approval".equals(tradType))
                    {
                        sbContents.append(new String(ba2XHeight)).append("신용 승인 매출 전표\n").append(new String(baNormal));
                        lMinus = 1;
                    }
                    else if ("cancellation".equals(tradType))
                    {
                        sbContents.append(new String(ba2XHeight)).append("신용 취소 매출 전표\n").append(new String(baNormal));
                        lMinus = -1;
                    }
                    else if ("partial-cancel".equals(tradType))
                    {
                        sbContents.append(new String(ba2XHeight)).append("신용 부분 취소 매출 전표\n").append(new String(baNormal));
                        lMinus = -1;
                    }
                    else if ("no-card-cancel".equals(tradType))
                    {
                        sbContents.append(new String(ba2XHeight)).append("신용 무 카드 취소 매출 전표\n").append(new String(baNormal));
                        lMinus = -1;
                    }
                }

                String afsrNm = selItem.getAfsrNm();
                String afsronNm = selItem.getAfsronNm();
                String bizrno = selItem.getBizrno();
                String afsrTelno = AndroidUtil.nullToString(selItem.getAfsrTelno(),"");
                String afsrAddr = selItem.getAfsrAddr();

                String devc = selItem.getDevc();
                String crdNo = selItem.getCrdNo();
                String aprvNo = selItem.getAprvNo();
                String aprvDt = selItem.getAprvDt();
                String aprvTm = selItem.getAprvTm();

                //영수증 내용
                sbContents.append("--------------------------------\n");
                sbContents.append(String.format("가 맹 점 명: %s\n", afsrNm));
                sbContents.append(String.format("대  표  자 : %s\n", afsronNm));
                sbContents.append(String.format("사업자 번호: %s\n", bizrno));

                sbContents.append(String.format("사업장 전화: %s\n", afsrTelno));
                sbContents.append(String.format("사업장 주소: %.32s\n", afsrAddr));
                sbContents.append("--------------------------------\n");

                sbContents.append(String.format("단말기 번호: %s\n", devc));
                sbContents.append(String.format("카 드 번 호: %s\n", crdNo));
                sbContents.append(String.format("승 인 번 호: %s\n", aprvNo));
                sbContents.append(String.format("승 인 날 짜: %s\n", aprvDt));
                sbContents.append(String.format("승 인 시 간: %s\n", aprvTm));
                sbContents.append("--------------------------------\n");
                sbContents.append(String.format("물 품 가 액: %17s원\n", AndroidUtil.dispLongComma(((lTotalAmount - lSurtax - lTip)) * lMinus)));
                if (lSurtax > 0)
                {
                    sbContents.append(String.format("부   가  세: %17s원\n", AndroidUtil.dispLongComma(lSurtax * lMinus)));
                }
                if (lTip > 0)
                {
                    sbContents.append(String.format("봉   사  료: %17s원\n", AndroidUtil.dispLongComma(lTip * lMinus)));
                }


                // 할부 개월
                String installment = selItem.getInstmMmCnt();
                if (installment != null && !"0".equals( installment ))
                {
                    sbContents.append(String.format("할 부 개 월: %15s개월\n", installment));
                }


                sbContents.append(String.format("합 계 금 액: %17s원\n", AndroidUtil.dispLongComma(lTotalAmount * lMinus)));
                sbContents.append("--------------------------------\n");
                sbContents.append("감사합니다.\n\n");

                //문자열을 바이트 배열로 바꾼 뒤 프린팅 스트림에 저장한다.
                for (byte bItem : sbContents.toString().getBytes("KSC5601"))
                {
                    alPrintingStream.add(bItem);
                }



                //공란을 넣는다.
                for (byte bItem : ("\n\n\n\n\n").getBytes())
                {
                    alPrintingStream.add(bItem);
                }

                //영수증 컷팅한다.
                for (byte bItem : baCut)
                {
                    alPrintingStream.add(bItem);
                }
            }

            JSONObject JsonPrint = new JSONObject();
            JSONArray JsonPrintContents = new JSONArray();

            for (byte bItem : alPrintingStream)
            {
                JsonPrintContents.put(bItem);
            }

            JsonPrint.put("service", "printing");

            /****************************************************************
             * 중요!! 프린터 필요 시, 하기 사항을 유의해 주세요.
             ****************************************************************/
            JsonPrint.put("printer", "printer-comm1");    //"printer-comm1"만 사용하며, "printer-comm1" ~ "printer-comm16"까지 동일하게 설정 저장하고 사용 가능합니다.
            JsonPrint.put("contents", JsonPrintContents);        //바이트 값을 JSONArray 에 Integer 배열 형태로 집어넣어줘야 함.

            AndroidUtil.log("VCAT : =============================== <REQUEST> ===================================");
            AndroidUtil.log("VCAT : " + JsonPrint.toString());
            AndroidUtil.log("VCAT : =============================================================================");
            getVCatInterface().executeService(JsonPrint.toString(), new SmartroVCatCallback.Stub()
            {
                @Override
                public void onServiceEvent(String strEventJSON)
                {

                    AndroidUtil.log("VCAT : =============================== <EVENT> ====================================");
                    AndroidUtil.log("VCAT : " + strEventJSON);
                    AndroidUtil.log("VCAT : ============================================================================");

                    try
                    {
                        final JSONObject jsonEvent = new JSONObject(strEventJSON);

                        if (jsonEvent.has("event"))
                        {
                            if (jsonEvent.getString("event").equals("status"))
                            {
                                showProgress(jsonEvent.getString("description"));
                            }
                        }
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onServiceResult(String strResultJSON)
                {
                    AndroidUtil.log("VCAT : =============================== <RESULT> ====================================");
                    AndroidUtil.log("VCAT : " + strResultJSON);
                    AndroidUtil.log("VCAT : ============================================================================");

                    try
                    {
                        JSONObject jsonResult = new JSONObject(strResultJSON);

                        closeProgress();
                        if (Integer.parseInt(jsonResult.getString("service-result")) == 0)
                        {
                            showAlertDialog("출력 완료!\r\n인쇄가 완료되었습니다.");
                        }
                        else
                        {
                            showAlertDialog("출력 오류..\n오류 코드 :" + jsonResult.get("service-result"));
                        }
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }





    // Smatro VCAT End
    ////////////////////////////////////////////////////////////////////////////////////

    public ArrayList<NemoSalesApprovalListRO> adapterListCopy(ArrayList<NemoSalesApprovalListRO> fromList) {
        String copyJSON = gson.toJson(fromList);
        ArrayList<NemoSalesApprovalListRO> toList = gson.fromJson(copyJSON, adapterListType);
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

        tvCardNo.setText("");
        tvInstallment.setText("");
        tvApprovalDate.setText("");
        tvApprovalNo.setText("");
        tvTotalAmount.setText("");

        btnPaymentCancel.setVisibility(View.GONE);

        btnPrintReceipt.setVisibility(View.GONE);

        getDefault();
    }

    protected void selectTask(NemoSalesApprovalListRO t){

        selItem = t;

        ConstraintLayout.LayoutParams params1 = (ConstraintLayout.LayoutParams) constraintLayoutList.getLayoutParams();

        params1.bottomToTop = R.id.conLayoutCard;

        constraintLayoutList.setLayoutParams(params1);

        conLayoutCard.setVisibility(View.VISIBLE);

        tvCardNo.setText(t.getCrdNo());
        //할부개월

        String dispInstallment = "일시불";

        if( t.getInstmMmCnt() != null && !"0".equals( t.getInstmMmCnt() ))
        {
            dispInstallment = t.getInstmMmCnt() + " 개월";
        }

        tvInstallment.setText(dispInstallment);

        //승인일시
        tvApprovalDate.setText(t.getAprvDtm());
        // 승인번호
        tvApprovalNo.setText(t.getAprvNo());
        // 승인금액
        tvTotalAmount.setText(AndroidUtil.dispCurrency(t.getAprvAmt()));
        // 카드종류
        tvCardCompany.setText(AndroidUtil.nullToString(t.getIssrInfo(),""));

        if( "approval".equals(t.getTradType()))
        {
            if( "N".equals(t.getCanceled()) )
            {
                btnPaymentCancel.setVisibility(View.VISIBLE);
                tvReceipt.setVisibility(View.VISIBLE);
                ragReceipt.setVisibility(View.VISIBLE);

            }
            else
            {
                btnPaymentCancel.setVisibility(View.GONE);
                tvReceipt.setVisibility(View.GONE);
                ragReceipt.setVisibility(View.GONE);

            }
            tvInstallment.setVisibility(View.VISIBLE);
            textView26.setVisibility(View.VISIBLE);

        }
        else
        {
            btnPaymentCancel.setVisibility(View.GONE);
            tvReceipt.setVisibility(View.GONE);
            ragReceipt.setVisibility(View.GONE);

            tvInstallment.setVisibility(View.GONE);
            textView26.setVisibility(View.GONE);
        }

        jsonResultLast = null;

        btnPrintReceipt.setVisibility(View.VISIBLE);


    }


    public class Click implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {

                case R.id.tvSearchDate:

                    YearMonthPickerDialog pd1 = new YearMonthPickerDialog();
                    pd1.setListener(yymmDialog);

                    AndroidUtil.log("searchYM : " + searchYM);

                    String[] arrStr = searchYM.split("-");

                    pd1.setValue(Integer.parseInt(arrStr[0]),Integer.parseInt(arrStr[1]));

                    pd1.show(getActivity().getSupportFragmentManager(), "YearMonthPicker");

                    break;

                case R.id.raReceiptPrint:
                    // 영수증 출력
                    isReceiptPrint = 2;
                    setDefault();
                    break;
                case R.id.raReceiptOnePrint:
                    // 영수증 1장 출력
                    isReceiptPrint = 1;
                    setDefault();
                    break;
                case R.id.raReceiptNotPrint:
                    // 영수증 미출력
                    isReceiptPrint = 0;
                    setDefault();
                    break;

                case R.id.btnPaymentCancel:

                    // 승인 취소
                    doCancel();

                    break;

                case R.id.btnPrintReceipt:

                    doReprinting();

                    break;

                case R.id.btnCancel:
                    //이전 화면으로 가기
                    Navigation.findNavController(getView()).popBackStack();

                    break;

                case R.id.btnBack:

                    Navigation.findNavController(getView()).popBackStack();

                    break;
            }
        }
    }

    public class ListViewAdapter extends RecyclerView.Adapter<ListViewAdapter.ListViewHolder> {

        private ArrayList<NemoSalesApprovalListRO> adapterLists;
        private SelectionTracker<Long> selectionTracker;

        private int selectedItemPosition = RecyclerView.NO_POSITION;

        public ListViewAdapter(ArrayList<NemoSalesApprovalListRO> adapterLists) {
            this.adapterLists = adapterListCopy(adapterLists);

            setHasStableIds(true);
        }

        public void setSelectionTracker(SelectionTracker<Long> selectionTracker) {
            this.selectionTracker = selectionTracker;
        }

        public NemoSalesApprovalListRO getItem(int position){
            return adapterLists.get(position);
        }

        public void setadapterLists(ArrayList<NemoSalesApprovalListRO> adapterLists) {

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

            View adapterView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_transaction_payment_layout, parent, false);

            ListViewHolder holder = new ListViewHolder(adapterView);

            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull ListViewHolder holder, @SuppressLint("RecyclerView") int position) {
            NemoSalesApprovalListRO t = this.adapterLists.get(position);

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

            public NemoSalesApprovalListRO item = null;

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

            public void bindItem(NemoSalesApprovalListRO t) {
                tvRegDtm.setText(AndroidUtil.dispDate(t.getAprvDtm()));
                tvBlclAmt.setText(AndroidUtil.dispCurrency(t.getAprvAmt()));

                tvDpstTypeNm.setText(AndroidUtil.getApprovalType(t.getTradType(),t.getCanceled()));
                this.item = t;
            }

        }
    }

}