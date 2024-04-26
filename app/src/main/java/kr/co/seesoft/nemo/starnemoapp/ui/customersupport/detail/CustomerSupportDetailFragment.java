package kr.co.seesoft.nemo.starnemoapp.ui.customersupport.detail;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import kr.co.seesoft.nemo.starnemoapp.R;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.po.NemoCustomerSupportPO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.ro.NemoCustomerMemoCodeListRO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.ro.NemoCustomerSupportListRO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.ro.NemoCustomerSupportReceiptRO;
import kr.co.seesoft.nemo.starnemoapp.ui.customersupport.CustomerSupportViewModel;
import kr.co.seesoft.nemo.starnemoapp.ui.customersupport.add.CustomerSupportAddViewModel;
import kr.co.seesoft.nemo.starnemoapp.ui.dialog.ConfirmDialog;
import kr.co.seesoft.nemo.starnemoapp.ui.dialog.CustomAlertDialog;
import kr.co.seesoft.nemo.starnemoapp.ui.dialog.CustomCalendarDialog;
import kr.co.seesoft.nemo.starnemoapp.ui.dialog.CustomProgressDialog;
import kr.co.seesoft.nemo.starnemoapp.util.AndroidUtil;
import kr.co.seesoft.nemo.starnemoapp.util.Const;
import kr.co.seesoft.nemo.starnemoapp.util.DateUtil;

public class CustomerSupportDetailFragment extends Fragment {


    private CustomerSupportDetailViewModel customerSupportDetailViewModel;
    private CustomerSupportViewModel customerSupportViewModel;


    //셀렉박스 관련
    private Spinner spCpbgArvlLocCntrNm;

    private TextView tvRecpDt, tvRecpNo, tvCustCd, tvCustNm, tvPatnNm, tvCustSuptPrcsIssu;

    private EditText etCustRqstIssu;

    private Button btnDelete, btnSave, btnCancel;

    private CustomProgressDialog progressDialog;

    // Title bar button
    private ImageButton btnBack, btnHome;

    private ArrayList<NemoCustomerMemoCodeListRO> codeList;

    private String cpbgArvlLocCntrNm;
    private int cpbgArvlLocCntrPos;

    private CustomAlertDialog customAlertDialog;
    private ConfirmDialog confirmDialog;
    private Handler deleteHandler, updateHandler;

    private NemoCustomerSupportListRO visitSupport;

    private Date searchDate;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        customerSupportDetailViewModel =
                ViewModelProviders.of(getActivity()).get(CustomerSupportDetailViewModel.class);

        customerSupportViewModel = ViewModelProviders.of(getActivity()).get(CustomerSupportViewModel.class);

        View root = inflater.inflate(R.layout.fragment_customer_support_detail, container, false);

        initUI(root);
        init();
        return root;
    }

    private void initUI(View view) {


        btnDelete = (Button) view.findViewById(R.id.btnDelete);
        btnSave = (Button) view.findViewById(R.id.btnSave);
        btnCancel = (Button) view.findViewById(R.id.btnCancel);

        spCpbgArvlLocCntrNm = (Spinner) view.findViewById(R.id.spCpbgArvlLocCntrNm);

        tvRecpDt = (TextView) view.findViewById(R.id.tvRecpDt);
        tvCustCd = (TextView) view.findViewById(R.id.tvCustCd);
        tvCustNm = (TextView) view.findViewById(R.id.tvCustNm);
        tvPatnNm = (TextView) view.findViewById(R.id.tvPatnNm);

        tvRecpNo = (TextView) view.findViewById(R.id.tvRecpNo);
        tvCustSuptPrcsIssu = (TextView) view.findViewById(R.id.tvCustSuptPrcsIssu);

        etCustRqstIssu = (EditText) view.findViewById(R.id.etCustRqstIssu);

        btnBack = (ImageButton) view.findViewById(R.id.btnBack);
        btnHome = (ImageButton) view.findViewById(R.id.btnHome);

    }

    private void init() {

        btnHome.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_navigation_menu_to_home));


        Click click = new Click();

        btnBack.setOnClickListener(click);

        btnDelete.setOnClickListener(click);
        btnSave.setOnClickListener(click);
        btnCancel.setOnClickListener(click);

        progressDialog = new CustomProgressDialog(getContext());

        visitSupport = (NemoCustomerSupportListRO) getArguments().getSerializable("visit_support");
        searchDate = (Date) getArguments().getSerializable("search_date");

        AndroidUtil.log("visitSupport : " + visitSupport);

        customerSupportDetailViewModel.getProgressFlag().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {

                if(aBoolean){
                    progressDialog.show();
                }else{
                    progressDialog.dismiss();
                }

            }
        });

        customerSupportDetailViewModel.getUpdateFlag().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {

                if(aBoolean){

                    customerSupportViewModel.setListClear();

                    customerSupportDetailViewModel.setUpdateFlag(false);

                }

            }
        });

        customerSupportDetailViewModel.getDeleteFlag().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {

                if(aBoolean){
                    customerSupportViewModel.setListClear();

                    customerSupportDetailViewModel.setDeleteFlag(false);

                    Navigation.findNavController(getView()).popBackStack();
                }

            }
        });

        customerSupportDetailViewModel.getCodeList().observe(getViewLifecycleOwner(), new Observer<ArrayList<NemoCustomerMemoCodeListRO>>() {
            @Override
            public void onChanged(ArrayList<NemoCustomerMemoCodeListRO> codeListROS) {

                if( codeListROS.size() > 0 )
                {
                    String defaultTypeCd = visitSupport.getCustSuptUpdtRqstTypeCd();
                    int posSel = 0;

                    String[] branchs = new String[codeListROS.size()];

                    for(int i = 0 ; i < codeListROS.size(); i++ )
                    {
                        branchs[i] = codeListROS.get(i).getCmmnCdNm();

                        if( defaultTypeCd != null && defaultTypeCd.equals(codeListROS.get(i).getCmmnCd()))
                            posSel = i;
                    }

                    ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, branchs);
                    spinnerArrayAdapter.setDropDownViewResource(R.layout.adapter_spinner_row2_layout);
                    spCpbgArvlLocCntrNm.setAdapter(spinnerArrayAdapter);
                    spCpbgArvlLocCntrNm.setOnItemSelectedListener(new SpCpbgArvlLocCntrNmSelected());

                    // 기본 값 설정
                    spCpbgArvlLocCntrNm.setSelection(posSel);

                    codeList = codeListROS;
                }

            }
        });

        customerSupportDetailViewModel.getCpRecpDtYmd().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {

                tvRecpDt.setText(s);

            }
        });

        etCustRqstIssu.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                return false;
            }
        });

        tvCustSuptPrcsIssu.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                return false;
            }
        });

        deleteHandler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);

                switch (msg.what){
                    case Const.HANDLER_CONFIRM:

                        customerSupportDetailViewModel.apiDelete(visitSupport);

                        break;
                }
            }
        };

        updateHandler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);

                switch (msg.what){
                    case Const.HANDLER_CONFIRM:

                        doSaveApi();

                        break;
                }
            }
        };

        setDefaultValue();

    }

    private void setDefaultValue()
    {
        tvRecpDt.setText(visitSupport.getRecpDt());
        tvRecpNo.setText(visitSupport.getRecpNo());
        tvCustCd.setText(visitSupport.getCustCd());
        tvCustNm.setText(visitSupport.getCustNm());
        tvPatnNm.setText(visitSupport.getPatnNm());

        etCustRqstIssu.setText(visitSupport.getCustRqstIssu());

        tvCustSuptPrcsIssu.setText(visitSupport.getCustSuptPrcsIssu());

        String custSuptUpdtRqstStatCd = visitSupport.getCustSuptUpdtRqstStatCd();

        if( "R".equals(custSuptUpdtRqstStatCd))
        {
            spCpbgArvlLocCntrNm.setEnabled(true);
            //etCustRqstIssu.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        }
        else
        {
            spCpbgArvlLocCntrNm.setEnabled(false);
            //etCustRqstIssu.setInputType(InputType.TYPE_NULL);
            etCustRqstIssu.setKeyListener(null);

        }
    }

    private boolean checkEmpty()
    {
        boolean rValue = true;

        String stCustRqstIssu = etCustRqstIssu.getText().toString().trim();

        if(StringUtils.isEmpty(stCustRqstIssu))
        {
            AndroidUtil.toast(getContext(), "요청사항을 입력 하세요!");
            rValue = false;
        }



        return rValue;
    }

    private void doSave()
    {
        String custSuptUpdtRqstStatCd = visitSupport.getCustSuptUpdtRqstStatCd();

        if( !"R".equals(custSuptUpdtRqstStatCd))
        {
            AndroidUtil.toast(getContext(), "요청상태에서만 수정 가능 합니다.");
            return;
        }

        //AndroidUtil.log("getCpbgTrptDivCd() : " + getCpbgTrptDivCd());
        if( !checkEmpty() )
            return;

        confirmDialog = new ConfirmDialog(getActivity(), updateHandler);

        confirmDialog.show();
        confirmDialog.setMsg("수정 하시겠습니까?");

    }

    private void doSaveApi()
    {

        String stCustRqstIssu = etCustRqstIssu.getText().toString().trim();

        NemoCustomerSupportPO param = new NemoCustomerSupportPO();

        param.setRecpDt(visitSupport.getRecpDt());
        param.setRecpNo(visitSupport.getRecpNo());
        param.setRecpSno(visitSupport.getRecpSno());
        param.setCustSuptUpdtRqstTypeCd(getCustSuptUpdtRqstTypeCd());
        param.setCustRqstIssu(stCustRqstIssu);
        param.setCustSuptPrcsUserId("");
        param.setCustSuptPrcsDtm("");
        param.setCustSuptUpdtRqstStatCd(visitSupport.getCustSuptUpdtRqstStatCd());

        customerSupportDetailViewModel.apiSave(param);

    }

    private void doDelete()
    {
        String custSuptUpdtRqstStatCd = visitSupport.getCustSuptUpdtRqstStatCd();

        if( !"R".equals(custSuptUpdtRqstStatCd))
        {
            AndroidUtil.toast(getContext(), "요청상태에서만 삭제 가능 합니다.");
            return;
        }

        confirmDialog = new ConfirmDialog(getActivity(), deleteHandler);

        confirmDialog.show();
        confirmDialog.setMsg("삭제 하시겠습니까?");

    }

    protected String getCustSuptUpdtRqstTypeCd() {

        String rValue = "";

        rValue = customerSupportDetailViewModel.getCustSuptUpdtRqstTypeCd(cpbgArvlLocCntrPos);

        return rValue;

    }


    public class Click implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {

                case R.id.btnDelete:

                    doDelete();

                    break;

                case R.id.btnSave:
                    doSave();

                    break;

                case R.id.btnCancel:
                case R.id.btnBack:

                    Navigation.findNavController(getView()).popBackStack();

                    break;
            }
        }
    }

    public class SpCpbgArvlLocCntrNmSelected implements Spinner.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            cpbgArvlLocCntrNm = spCpbgArvlLocCntrNm.getItemAtPosition(i).toString();
            cpbgArvlLocCntrPos = i;
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {
            try {
                cpbgArvlLocCntrPos = 0;
                cpbgArvlLocCntrNm = spCpbgArvlLocCntrNm.getItemAtPosition(0).toString();
            }
            catch (Exception ex)
            {
                AndroidUtil.log(ex.getMessage());
            }
        }

    }

}