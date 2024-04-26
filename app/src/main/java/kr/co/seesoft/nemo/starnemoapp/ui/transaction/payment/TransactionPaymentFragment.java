package kr.co.seesoft.nemo.starnemoapp.ui.transaction.payment;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.selection.Selection;
import androidx.recyclerview.selection.SelectionTracker;
import androidx.recyclerview.widget.RecyclerView;
import kr.co.seesoft.nemo.starnemoapp.BuildConfig;
import kr.co.seesoft.nemo.starnemoapp.R;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.po.NemoSalesApprovalAddPO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.ro.NemoHospitalSearchRO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.ro.NemoSalesTransactionListRO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.ro.NemoVisitListRO;
import kr.co.seesoft.nemo.starnemoapp.ui.dialog.ConfirmDialog;
import kr.co.seesoft.nemo.starnemoapp.ui.dialog.CustomAlertDialog;
import kr.co.seesoft.nemo.starnemoapp.ui.dialog.CustomIngProgressDialog;
import kr.co.seesoft.nemo.starnemoapp.ui.dialog.CustomProgressDialog;
import kr.co.seesoft.nemo.starnemoapp.ui.dialog.SignPadDialog;
import kr.co.seesoft.nemo.starnemoapp.ui.transaction.agree.TransactionAgreeViewModel;
import kr.co.seesoft.nemo.starnemoapp.ui.visitplanadd.VisitPlanAddFragment;
import kr.co.seesoft.nemo.starnemoapp.util.AndroidUtil;
import kr.co.seesoft.nemo.starnemoapp.util.Const;
import kr.co.seesoft.nemo.starnemoapp.util.c1.C1itPrint;
import service.vcat.smartro.com.vcat.SmartroVCatCallback;
import service.vcat.smartro.com.vcat.SmartroVCatInterface;

public class TransactionPaymentFragment extends Fragment {


    private TransactionPaymentViewModel transactionPaymentViewModel;

    /** context */
    private Context context;

    /** 병원 정보 */
    private NemoHospitalSearchRO visitHospital;

    private Spinner spInstallment;

    private int installmentType;

    private TextView textView12;

    private TextView tvHospitalName;

    private TextView tvTmrqSellAmt, tvBfmmRqesUcamt, tvBlclAmt, tvTmrqUcamt;

    private EditText etPayments;

    private Button btnCancel, btnPayment , btnPaymentHistory;

    private CustomProgressDialog progressDialog;
    private CustomIngProgressDialog customIngProgressDialog;
    private CustomAlertDialog customAlertDialog;
    private SignPadDialog signPadDialog;
    private Handler signHandler;

    // Title bar button
    private ImageButton btnBack, btnHome;

    private ConstraintLayout conLayoutCard;

    private TextView tvCardNo, tvApprovalDate, tvApprovalNo, tvTotalAmount, tvInstallment, tvCardCompany;

    private Button btnPrintReceipt;

    private RadioGroup ragVAT, ragReceipt;
    private RadioButton raVATInclude, raVATNotInclude, raReceiptPrint, raReceiptOnePrint, raReceiptNotPrint;

    private boolean isVATInclude = false;
    private int isReceiptPrint = 2;

    Gson gson = new Gson();

    private ConfirmDialog confirmDialog;
    private Handler paymentHandler;


    ////////////////////////////////////////////////////////////////////////////////////
    // Smatro VCAT Start
    // 서비스 인스턴스
    private SmartroVCatInterface mSmartroVCatInterface = null;

    private boolean isVCATBindError = false;

    private boolean isVCATPermissionError = true;

    private JSONObject jsonResultLast = null;

    // Smatro VCAT End
    ////////////////////////////////////////////////////////////////////////////////////

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        transactionPaymentViewModel =
                ViewModelProviders.of(getActivity()).get(TransactionPaymentViewModel.class);

        View root = inflater.inflate(R.layout.fragment_transaction_payment, container, false);

        this.context = getContext();

        initUI(root);
        init();
        return root;
    }

    private void initUI(View view) {

        tvHospitalName = (TextView) view.findViewById(R.id.tvHospitalName);

        tvTmrqSellAmt = (TextView) view.findViewById(R.id.tvTmrqSellAmt);
        tvBfmmRqesUcamt = (TextView) view.findViewById(R.id.tvBfmmRqesUcamt);
        tvBlclAmt = (TextView) view.findViewById(R.id.tvBlclAmt);
        tvTmrqUcamt = (TextView) view.findViewById(R.id.tvTmrqUcamt);

        tvCardNo = (TextView) view.findViewById(R.id.tvCardNo);
        tvApprovalDate = (TextView) view.findViewById(R.id.tvApprovalDate);
        tvApprovalNo = (TextView) view.findViewById(R.id.tvApprovalNo);
        tvTotalAmount = (TextView) view.findViewById(R.id.tvTotalAmount);
        tvInstallment = (TextView) view.findViewById(R.id.tvInstallment);
        tvCardCompany = (TextView) view.findViewById(R.id.tvCardCompany);

        btnPrintReceipt = (Button) view.findViewById(R.id.btnPrintReceipt);

        etPayments = (EditText) view.findViewById(R.id.etPayments);

        conLayoutCard = (ConstraintLayout) view.findViewById(R.id.conLayoutCard);

        btnCancel = (Button) view.findViewById(R.id.btnCancel);
        btnPayment = (Button) view.findViewById(R.id.btnPayment);
        btnPaymentHistory = (Button) view.findViewById(R.id.btnPaymentHistory);

        spInstallment = (Spinner) view.findViewById(R.id.spInstallment);
        textView12 = (TextView) view.findViewById(R.id.textView12);

        ragVAT = (RadioGroup) view.findViewById(R.id.ragVAT);
        ragReceipt = (RadioGroup) view.findViewById(R.id.ragReceipt);

        raVATInclude = (RadioButton) view.findViewById(R.id.raVATInclude);
        raVATNotInclude = (RadioButton) view.findViewById(R.id.raVATNotInclude);
        raReceiptPrint = (RadioButton) view.findViewById(R.id.raReceiptPrint);
        raReceiptOnePrint = (RadioButton) view.findViewById(R.id.raReceiptOnePrint);
        raReceiptNotPrint = (RadioButton) view.findViewById(R.id.raReceiptNotPrint);

        btnBack = (ImageButton) view.findViewById(R.id.btnBack);
        btnHome = (ImageButton) view.findViewById(R.id.btnHome);

    }

    private void init() {



        getDefault();


        btnHome.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_navigation_menu_to_home));

        etPayments.addTextChangedListener(new NumberTextWatcher(etPayments));




        Click click = new Click();

        btnBack.setOnClickListener(click);

        btnPayment.setOnClickListener(click);
        btnCancel.setOnClickListener(click);
        btnPaymentHistory.setOnClickListener(click);
        btnPrintReceipt.setOnClickListener(click);

        raVATInclude.setOnClickListener(click);
        raVATNotInclude.setOnClickListener(click);
        raReceiptPrint.setOnClickListener(click);
        raReceiptOnePrint.setOnClickListener(click);
        raReceiptNotPrint.setOnClickListener(click);

        AndroidUtil.log("TransactionPayment init() : " + raReceiptPrint.isChecked());
        AndroidUtil.log("TransactionPayment init() : " + raReceiptNotPrint.isChecked());

        ArrayAdapter<CharSequence> spAdapter = ArrayAdapter.createFromResource(getContext(), R.array.month_options, R.layout.adapter_spinner_row_layout);
        spAdapter.setDropDownViewResource(R.layout.adapter_spinner_row2_layout);

        spInstallment.setAdapter(spAdapter);
        spInstallment.setOnItemSelectedListener(new SpinnerSelected());


        visitHospital = (NemoHospitalSearchRO) getArguments().getSerializable("visit_hospital");

        tvHospitalName.setText(visitHospital.getCustNm());

        transactionPaymentViewModel.setHospital(visitHospital);


        progressDialog = new CustomProgressDialog(getContext());
        transactionPaymentViewModel.getProgressFlag().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {

                if(aBoolean){
                    progressDialog.show();
                }else{
                    progressDialog.dismiss();
                }

            }
        });


        transactionPaymentViewModel.getTransactionlList().observe(getViewLifecycleOwner(), new Observer<ArrayList<NemoSalesTransactionListRO>>() {
            @Override
            public void onChanged(ArrayList<NemoSalesTransactionListRO> transactionROS) {

                if( transactionROS.size() > 0 )
                {
                    NemoSalesTransactionListRO t = transactionROS.get(0);

                    tvTmrqSellAmt.setText(AndroidUtil.dispCurrency(t.getTmrqSellAmt()));
                    tvBfmmRqesUcamt.setText(AndroidUtil.dispCurrency(t.getBfmmRqesUcamt()));
                    tvBlclAmt.setText(AndroidUtil.dispCurrency(t.getBlclAmt()));
                    tvTmrqUcamt.setText(AndroidUtil.dispCurrency(t.getTmrqUcamt()));

                    // 결제 금액 설정
                    etPayments.setText(AndroidUtil.dispCurrency(t.getBlclGoalAmt()));

                }
                else
                {
                    tvTmrqSellAmt.setText("0");
                    tvBfmmRqesUcamt.setText("0");
                    tvBlclAmt.setText("0");
                    tvTmrqUcamt.setText("0");
                }

            }
        });

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

        paymentHandler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);

                switch (msg.what){
                    case Const.HANDLER_CONFIRM:

                        doPayment();

                        break;
                }
            }
        };

        /////////////////////////////////////////////////////////////////////////
        // VCAT 서비스 Bind
        bindVCATService();

    }


    private void getDefault()
    {
        SharedPreferences sp = context.getSharedPreferences(AndroidUtil.TAG_SP, Context.MODE_PRIVATE);

        isReceiptPrint = sp.getInt(AndroidUtil.SP_CARD_PRINT_OPTION, 2);
        isVATInclude = sp.getBoolean(AndroidUtil.SP_CARD_VAT_OPTION, false);

        AndroidUtil.log("isReceiptPrint : " + isReceiptPrint);

        ragVAT.clearCheck();
        ragReceipt.clearCheck();

        // 영수증 1장 출력
        if( isReceiptPrint == 1 )
        {
            raReceiptOnePrint.setChecked(true);
            raReceiptPrint.setChecked(false);
            raReceiptNotPrint.setChecked(false);
        }
        // 영수증 2장 출력
        else if( isReceiptPrint == 2 )
        {
            raReceiptOnePrint.setChecked(false);
            raReceiptPrint.setChecked(true);
            raReceiptNotPrint.setChecked(false);
        }
        // 영수증 출력 안함
        else
        {
            raReceiptOnePrint.setChecked(false);
            raReceiptPrint.setChecked(false);
            raReceiptNotPrint.setChecked(true);
        }

        if( isVATInclude )
        {
            raVATInclude.setChecked(true);
            raVATNotInclude.setChecked(false);
        }
        else
        {
            raVATInclude.setChecked(false);
            raVATNotInclude.setChecked(true);
        }


    }

    private void setDefault()
    {
        SharedPreferences sp = context.getSharedPreferences(AndroidUtil.TAG_SP, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        editor.putInt(AndroidUtil.SP_CARD_PRINT_OPTION, isReceiptPrint);
        editor.putBoolean(AndroidUtil.SP_CARD_VAT_OPTION, isVATInclude);

        editor.commit();
    }

    @Override
    public void onResume() {
        super.onResume();
        // RadioButton 초기화
        ragVAT.clearCheck();
        ragReceipt.clearCheck();

        getDefault();

        etPayments.setText("");
        spInstallment.setSelection(0);
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

    private void dispReprinting()
    {
        getActivity().runOnUiThread(new Runnable() {
            public void run() {

                try
                {
                    btnPrintReceipt.setVisibility(View.VISIBLE);

                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
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

                    tvCardCompany.setText(card_company);
                }
                catch (Exception ex)
                {
                    AndroidUtil.log(ex.toString());
                }
            }
        });

    }

    private void checkPayment()
    {
        String payments = etPayments.getText().toString().trim().replaceAll(",","");

        if(StringUtils.isEmpty(payments))
        {
            AndroidUtil.toast(getContext(),"결제금액을 입력 하세요!");

            return;
        }

        if( isVATInclude )
        {
            confirmDialog = new ConfirmDialog(getActivity(), paymentHandler);

            confirmDialog.show();
            confirmDialog.setMsg("부가세 포함 승인하시겠습니까?");
        }
        else
        {
            doPayment();
        }


    }

    /**
     * 결제 처리
     */
    private void doPayment()
    {
        if( isVCATBindError || isVCATPermissionError )
        {
            AndroidUtil.toast(getContext(),"카드 모듈 연결 실패!");
            return;
        }

        String payments = etPayments.getText().toString().trim().replaceAll(",","");


        try {

            JSONObject jsonInput = new JSONObject();

            jsonInput.put("type", Const.VCAT_TRAN_TYPE);
            jsonInput.put("deal", Const.VCAT_TRAN_PAYMENT);

            long payment = Long.parseLong(payments);
            long vat = 0;

            if( isVATInclude )
            {
                vat = (long) (payment * 0.1);
            }

            AndroidUtil.log("VCAT 결제금액 : " + payment );
            AndroidUtil.log("VCAT VAT : " + vat );

            // 부가세
            jsonInput.put("surtax", vat);

            // Tip
            jsonInput.put("tip", "0");

            // 결제 금액
            jsonInput.put("total-amount", payments);

            String installment = AndroidUtil.getInstallmentType(installmentType);

            // 할부 개월
            jsonInput.put("installment", installment);

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


                                dispReprinting();

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


            transactionPaymentViewModel.apiAddApproval(param);
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

    private void setExchangeKey()
    {
        try {
            final JSONObject jsonInput = new JSONObject();

            jsonInput.put("service", "function");
            jsonInput.put("cat-id", Const.VCAT_CAT_ID);              //단말기 번호
            jsonInput.put("business-no", Const.VCAT_BIZ_NO);         //사업자 번호
            jsonInput.put("device-manage", "exchange-key");

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
                        JSONObject jsonResult = new JSONObject(strEventJSON);

                        if (jsonResult.has("event"))
                        {
                            if (jsonResult.getString("event").equals("status"))
                            {
                                //showProgress(jsonResult.getString("description"));

                                AndroidUtil.log("VCAT exchange-key : " + jsonResult.getString("description"));

                            }
                        }
                    }
                    catch (Exception e) {
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
                        int iResult;
                        JSONObject jsonResult = new JSONObject(strResultJSON);

                        iResult = Integer.valueOf(jsonResult.getString("service-result"));
                        if (iResult == 0)
                        {
                            Iterator<String> ittKeys = jsonResult.keys();
                            StringBuilder sbTemp = new StringBuilder();
                            String strKey;

                            while (ittKeys.hasNext())
                            {
                                strKey = ittKeys.next();
                                sbTemp.append(String.format("%s : %s \n", strKey, jsonResult.get(strKey)));
                            }
                            AndroidUtil.log("VCAT exchange-key : " + sbTemp.toString());
                        }
                        else
                        {
                            AndroidUtil.toast(context, "서비스 실행 오류 코드 :" + jsonResult.get("service-description"));
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
            AndroidUtil.log(ex.toString());
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

    private void setCustomSignUI()
    {
        try {
            JSONObject setJsonRequest = new JSONObject();

            setJsonRequest.put("service", "setting");

            setJsonRequest.put("additional-device", "customui");

            AndroidUtil.log( "VCAT : =============================== <REQUEST> ===================================");
            AndroidUtil.log("VCAT : " + setJsonRequest.toString());
            AndroidUtil.log( "VCAT : =============================================================================");
            getVCatInterface().executeService(setJsonRequest.toString(), new SmartroVCatCallback.Stub()
            {
                @Override
                public void onServiceEvent(String strEventJSON)
                {

                }

                @Override
                public void onServiceResult(String strResultJSON)
                {
                    closeProgress();
                    AndroidUtil.log("VCAT : =============================== <RESULT> ===================================");
                    AndroidUtil.log("VCAT : " + strResultJSON);
                    AndroidUtil.log("VCAT : ============================================================================");

                    try
                    {
                        int iResult;
                        JSONObject jsonResult = new JSONObject(strResultJSON);

                        iResult = Integer.valueOf(jsonResult.getString("service-result"));
                        if (iResult == 0)
                        {
                            AndroidUtil.log("카드 모듈 연결 성공");

                            setExchangeKey();
                        }
                        else
                        {
                            isVCATBindError = false;
                            AndroidUtil.log("카드 모듈 연결 실패!");
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
            AndroidUtil.log(ex.toString());
        }
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

                            setCustomSignUI();
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

            AndroidUtil.log("isReceiptPrint : " + isReceiptPrint);

            for (int i = 0; i < isReceiptPrint; i++)
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





    // Smatro VCAT End
    ////////////////////////////////////////////////////////////////////////////////////


    public class Click implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btnPayment:

                    checkPayment();

                    break;

                case R.id.btnPaymentHistory:

                    Bundle bundle = new Bundle();

                    bundle.putSerializable("visit_hospital", visitHospital);

                    Navigation.findNavController(getView()).navigate(R.id.action_to_transactionPaymentHistoryFragment, bundle);

                    break;

                case R.id.btnPrintReceipt:

                    printingReceipt(jsonResultLast, true);

                    break;

                case R.id.raVATInclude:
                    // VAT 포함
                    isVATInclude = true;
                    setDefault();
                    break;
                case R.id.raVATNotInclude:
                    // VAT 미포함
                    isVATInclude = false;
                    setDefault();
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

                case R.id.btnCancel:
                case R.id.btnBack:

                    Navigation.findNavController(getView()).popBackStack();

                    break;
            }
        }
    }

    public class SpinnerSelected implements Spinner.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            installmentType = i;
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {
            installmentType = 0;
        }

    }



    public class NumberTextWatcher implements TextWatcher {

        private final DecimalFormat decimalFormat = new DecimalFormat("#,###");
        private final EditText editText;
        private String previousText = "";

        NumberTextWatcher(EditText editText) {
            this.editText = editText;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int start, int before, int count) {
            // 이전 텍스트 저장
            previousText = charSequence.toString();
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
            // 입력된 텍스트에 천 원 단위로 콤마 추가
            String newText = charSequence.toString();
            if (newText.length() > 0 && !newText.equals(previousText)) {
                String formattedText = formatNumber(newText);
                editText.removeTextChangedListener(this);
                editText.setText(formattedText);
                editText.setSelection(formattedText.length());
                editText.addTextChangedListener(this);

                if( newText.length() > 0 )
                {
                    if( Integer.parseInt(newText.replaceAll(",", "")) >= 50000)
                    {
                        spInstallment.setVisibility(View.VISIBLE);
                        textView12.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        spInstallment.setVisibility(View.GONE);
                        textView12.setVisibility(View.GONE);
                    }
                }
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {
            // 작업 완료 후 추가적인 작업이 필요한 경우
        }

        private String formatNumber(String number) {
            try {
                // 숫자에서 콤마 제거
                String cleanString = number.replaceAll(",", "");

                // 천 원 단위로 콤마 추가
                double parsed = Double.parseDouble(cleanString);
                return decimalFormat.format(parsed);
            } catch (NumberFormatException e) {
                e.printStackTrace();
                return number; // 예외 발생 시 원래 텍스트 반환
            }
        }
    }

}