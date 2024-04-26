package kr.co.seesoft.nemo.starnemoapp.ui.customersupport.add;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
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
import kr.co.seesoft.nemo.starnemoapp.nemoapi.po.NemoBagSendAddPO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.po.NemoCustomerSupportPO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.ro.NemoCdNmListRO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.ro.NemoCustomerMemoCodeListRO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.ro.NemoCustomerSupportReceiptRO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.ro.NemoDeptCdNmListRO;
import kr.co.seesoft.nemo.starnemoapp.ui.bagsend.BagSendViewModel;
import kr.co.seesoft.nemo.starnemoapp.ui.bagsend.add.BagSendAddViewModel;
import kr.co.seesoft.nemo.starnemoapp.ui.customersupport.CustomerSupportFragment;
import kr.co.seesoft.nemo.starnemoapp.ui.customersupport.CustomerSupportViewModel;
import kr.co.seesoft.nemo.starnemoapp.ui.dialog.CustomAlertDialog;
import kr.co.seesoft.nemo.starnemoapp.ui.dialog.CustomCalendarDialog;
import kr.co.seesoft.nemo.starnemoapp.ui.dialog.CustomProgressDialog;
import kr.co.seesoft.nemo.starnemoapp.ui.dialog.HourMinutePickerDialog;
import kr.co.seesoft.nemo.starnemoapp.util.AndroidUtil;
import kr.co.seesoft.nemo.starnemoapp.util.Const;
import kr.co.seesoft.nemo.starnemoapp.util.DateUtil;

public class CustomerSupportAddFragment extends Fragment {


    private CustomerSupportAddViewModel customerSupportAddViewModel;
    private CustomerSupportViewModel customerSupportViewModel;


    //셀렉박스 관련
    private Spinner spCpbgArvlLocCntrNm;

    private TextView tvRecpDt, tvCustCd, tvCustNm, tvPatnNm;

    private EditText etRecpNo;

    private EditText etCustRqstIssu;

    private Button btnSearch, btnSave, btnCancel;

    private CustomProgressDialog progressDialog;

    // Title bar button
    private ImageButton btnBack, btnHome;

    private ArrayList<NemoCustomerMemoCodeListRO> codeList;

    //캘린더 팝업
    private CustomCalendarDialog calendarDialog;
    private Handler handlerCalendar;

    private String cpbgArvlLocCntrNm;
    private int cpbgArvlLocCntrPos;

    private Date addDate;

    private CustomAlertDialog customAlertDialog;

    private String searchRecpNo = "";
    private Date searchDate;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        customerSupportAddViewModel =
                ViewModelProviders.of(getActivity()).get(CustomerSupportAddViewModel.class);

        customerSupportViewModel = ViewModelProviders.of(getActivity()).get(CustomerSupportViewModel.class);

        View root = inflater.inflate(R.layout.fragment_customer_support_add, container, false);

        initUI(root);
        init();
        return root;
    }

    private void initUI(View view) {


        btnSearch = (Button) view.findViewById(R.id.btnSearch);
        btnSave = (Button) view.findViewById(R.id.btnSave);
        btnCancel = (Button) view.findViewById(R.id.btnCancel);

        spCpbgArvlLocCntrNm = (Spinner) view.findViewById(R.id.spCpbgArvlLocCntrNm);

        tvRecpDt = (TextView) view.findViewById(R.id.tvRecpDt);
        tvCustCd = (TextView) view.findViewById(R.id.tvCustCd);
        tvCustNm = (TextView) view.findViewById(R.id.tvCustNm);
        tvPatnNm = (TextView) view.findViewById(R.id.tvPatnNm);

        etRecpNo = (EditText) view.findViewById(R.id.etRecpNo);

        etCustRqstIssu = (EditText) view.findViewById(R.id.etCustRqstIssu);

        btnBack = (ImageButton) view.findViewById(R.id.btnBack);
        btnHome = (ImageButton) view.findViewById(R.id.btnHome);

    }

    private void init() {

        btnHome.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_navigation_menu_to_home));


        Click click = new Click();

        btnBack.setOnClickListener(click);

        btnSearch.setOnClickListener(click);
        btnSave.setOnClickListener(click);
        btnCancel.setOnClickListener(click);

        tvRecpDt.setOnClickListener(click);

        handlerCalendar = new CalendarHandler(Looper.getMainLooper());
        calendarDialog = new CustomCalendarDialog(getActivity(), handlerCalendar);

        progressDialog = new CustomProgressDialog(getContext());

        customerSupportAddViewModel.clear();

        addDate = (Date) getArguments().getSerializable("add_date");

        searchDate = addDate;

        AndroidUtil.log("addDate : " + addDate);

        customerSupportAddViewModel.setCpRecpDt(addDate);

        customerSupportAddViewModel.getProgressFlag().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {

                if(aBoolean){
                    progressDialog.show();
                }else{
                    progressDialog.dismiss();
                }

            }
        });

        customerSupportAddViewModel.getUpdateFlag().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {

                if(aBoolean){

                    //customerSupportViewModel.setSearchDate(new Date());
                    customerSupportViewModel.setClear();

                    customerSupportAddViewModel.setUpdateFlag(false);

                    Navigation.findNavController(getView()).popBackStack();
                }

            }
        });

        customerSupportAddViewModel.getCodeList().observe(getViewLifecycleOwner(), new Observer<ArrayList<NemoCustomerMemoCodeListRO>>() {
            @Override
            public void onChanged(ArrayList<NemoCustomerMemoCodeListRO> codeListROS) {

                if( codeListROS.size() > 0 )
                {
                    String[] branchs = new String[codeListROS.size()];

                    for(int i = 0 ; i < codeListROS.size(); i++ )
                    {
                        branchs[i] = codeListROS.get(i).getCmmnCdNm();
                    }

                    ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, branchs);
                    spinnerArrayAdapter.setDropDownViewResource(R.layout.adapter_spinner_row2_layout);
                    spCpbgArvlLocCntrNm.setAdapter(spinnerArrayAdapter);
                    spCpbgArvlLocCntrNm.setOnItemSelectedListener(new SpCpbgArvlLocCntrNmSelected());

                    codeList = codeListROS;
                }

            }
        });

        customerSupportAddViewModel.getCpRecpDtYmd().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {

                tvRecpDt.setText(s);

            }
        });

        customerSupportAddViewModel.getReceiptInfo().observe(getViewLifecycleOwner(), new Observer<NemoCustomerSupportReceiptRO>() {
            @Override
            public void onChanged(NemoCustomerSupportReceiptRO t) {

                //AndroidUtil.log("load NemoCustomerSupportReceiptRO- : " + t);

                if( t != null )
                {
                    tvCustCd.setText(AndroidUtil.nullToString(t.getCustCd(),""));
                    tvCustNm.setText(AndroidUtil.nullToString(t.getCustNm(),""));
                    tvPatnNm.setText(AndroidUtil.nullToString(t.getPatnNm(),""));

                    searchRecpNo = t.getRecpNo();

                    searchDate = customerSupportAddViewModel.getCpRecpDt().getValue();
                }
                else
                {
                    tvCustCd.setText("");
                    tvCustNm.setText("");
                    tvPatnNm.setText("");

                    searchRecpNo = "";

                    customAlertDialog = new CustomAlertDialog(getContext());
                    customAlertDialog.show();
                    customAlertDialog.setMsg("유효하지 않은 접수번호입니다.");
                }

            }
        });



        etCustRqstIssu.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                return false;
            }
        });



    }

    private boolean checkEmpty()
    {
        boolean rValue = true;

        String stetRecpNo = etRecpNo.getText().toString().trim();
        String stCustCd = tvCustCd.getText().toString().trim();
        String stCustRqstIssu = etCustRqstIssu.getText().toString().trim();

        if( !StringUtils.isEmpty(searchRecpNo) && !searchRecpNo.equals(stetRecpNo))
        {
            AndroidUtil.toast(getContext(), "접수번호를 조회 하세요!");
            rValue = false;
        }
        else if(!DateUtil.getFormatString(customerSupportAddViewModel.getCpRecpDt().getValue(),"yyyyMMdd").equals(DateUtil.getFormatString(searchDate,"yyyyMMdd")))
        {
            AndroidUtil.toast(getContext(), "접수번호를 조회 하세요!");
            rValue = false;
        }
        else if(StringUtils.isEmpty(stCustCd))
        {
            AndroidUtil.toast(getContext(), "접수번호를 조회 하세요!");
            rValue = false;
        }
        else if(StringUtils.isEmpty(stCustRqstIssu))
        {
            AndroidUtil.toast(getContext(), "요청사항을 입력 하세요!");
            rValue = false;
        }



        return rValue;
    }

    private void doSave()
    {
        //AndroidUtil.log("getCpbgTrptDivCd() : " + getCpbgTrptDivCd());
        if( !checkEmpty() )
            return;

        String recpDt = DateUtil.getFormatString(searchDate,"yyyyMMdd");
        String stCustRqstIssu = etCustRqstIssu.getText().toString().trim();

        NemoCustomerSupportPO param = new NemoCustomerSupportPO();

        param.setRecpDt(recpDt);
        param.setRecpNo(searchRecpNo);
        param.setRecpSno("");
        param.setCustSuptUpdtRqstTypeCd(getCustSuptUpdtRqstTypeCd());
        param.setCustRqstIssu(stCustRqstIssu);
        param.setCustSuptPrcsUserId("");
        param.setCustSuptPrcsDtm("");
        param.setCustSuptUpdtRqstStatCd("R");

        customerSupportAddViewModel.apiSave(param);



    }

    protected String getCustSuptUpdtRqstTypeCd() {

        String rValue = "";

        rValue = customerSupportAddViewModel.getCustSuptUpdtRqstTypeCd(cpbgArvlLocCntrPos);

        return rValue;

    }

    private void getCustomerInfo()
    {
        String stetRecpNo = etRecpNo.getText().toString().trim();

        if(StringUtils.isEmpty(stetRecpNo))
        {
            AndroidUtil.toast(getContext(), "접수번호를 입력 하세요!");
            return;
        }

        customerSupportAddViewModel.getCustomerInfo(stetRecpNo);

    }


    public class Click implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {

                case R.id.tvRecpDt:

                    calendarDialog.show(customerSupportAddViewModel.getCpRecpDt().getValue());
                    break;

                case R.id.btnSearch:

                    getCustomerInfo();

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

                    customerSupportAddViewModel.setCpRecpDt(selectDate);

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