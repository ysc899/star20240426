package kr.co.seesoft.nemo.starnemoapp.ui.bagsend.add;

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
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.selection.SelectionTracker;
import androidx.recyclerview.widget.RecyclerView;
import kr.co.seesoft.nemo.starnemoapp.R;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.po.NemoBagSendAddPO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.ro.NemoBagSendListRO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.ro.NemoCdNmListRO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.ro.NemoDeptCdNmListRO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.ro.NemoHospitalSearchRO;
import kr.co.seesoft.nemo.starnemoapp.ui.bagsend.BagSendFragment;
import kr.co.seesoft.nemo.starnemoapp.ui.bagsend.BagSendViewModel;
import kr.co.seesoft.nemo.starnemoapp.ui.dialog.CustomCalendarDialog;
import kr.co.seesoft.nemo.starnemoapp.ui.dialog.CustomProgressDialog;
import kr.co.seesoft.nemo.starnemoapp.ui.dialog.HourMinutePickerDialog;
import kr.co.seesoft.nemo.starnemoapp.ui.dialog.YearMonthPickerDialog;
import kr.co.seesoft.nemo.starnemoapp.ui.visitplanadd.VisitPlanAddFragment;
import kr.co.seesoft.nemo.starnemoapp.util.AndroidUtil;
import kr.co.seesoft.nemo.starnemoapp.util.Const;
import kr.co.seesoft.nemo.starnemoapp.util.DateUtil;

public class BagSendAddFragment extends Fragment {


    private BagSendAddViewModel bagSendAddViewModel;
    private BagSendViewModel bagSendViewModel;


    //셀렉박스 관련
    private Spinner spCpbgArvlLocCntrNm, spCpbgTrptDivNm, spCpbgSndvhclCmpNm;

    private TextView tvCpbgSendLocCntrNm;

    private TextView tvCpbgSendDt, tvCpbgSendTm, tvCpbgArvlPragDt, tvCpbgArvlPragTm;

    private EditText etCpbgQty; //, etEtcQty;

    private EditText etCpbgSndvhclNo, etCpbgInvcNo, etCpbgSendCont; //, etSenderNm

    private Button btnCancel, btnSave, btnSend;

    private int calendarOpenType;
    private int hmPickerType;

    private CustomProgressDialog progressDialog;

    // Title bar button
    private ImageButton btnBack, btnHome;

    //캘린더 팝업
    private CustomCalendarDialog calendarDialog;
    private CustomCalendarDialog calendarDialog1;
    private Handler handlerCalendar;

    private String cpbgSendTm, cpbgArvlPragTm;

    private String cpbgSendDeptNm, cpbgArvlLocCntrNm, cpbgTrptDivNm, cpbgSndvhclCmpNm;
    private int cpbgSendDeptPos, cpbgArvlLocCntrPos, cpbgTrptDivPos, cpbgSndvhclCmpPos;

    private String deptCode;

    private Date visitDate;

    DatePickerDialog.OnDateSetListener hhmmDialog = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int hour, int minute, int dayOfMonth){
            AndroidUtil.log("HourMinutePicker : " +"hmPickerType = " + hmPickerType +" hour = " + hour + ", minute = " + minute);

            if(hmPickerType == 1)
            {
                bagSendAddViewModel.setCpbgSendTm(String.format("%02d", hour) + ":" + String.format("%02d", minute) );
            }
            else
            {
                bagSendAddViewModel.setCpbgArvlPragTm(String.format("%02d", hour) + ":" + String.format("%02d", minute));
            }


        }
    };

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        bagSendAddViewModel =
                ViewModelProviders.of(getActivity()).get(BagSendAddViewModel.class);

        bagSendViewModel = ViewModelProviders.of(getActivity()).get(BagSendViewModel.class);

        View root = inflater.inflate(R.layout.fragment_bagsend_add, container, false);

        initUI(root);
        init();
        return root;
    }

    private void initUI(View view) {


        btnCancel = (Button) view.findViewById(R.id.btnCancel);
        btnSave = (Button) view.findViewById(R.id.btnSave);
        btnSend = (Button) view.findViewById(R.id.btnSend);

        tvCpbgSendLocCntrNm = view.findViewById(R.id.tvCpbgSendLocCntrNm);
        spCpbgArvlLocCntrNm = (Spinner) view.findViewById(R.id.spCpbgArvlLocCntrNm);
        spCpbgTrptDivNm = (Spinner) view.findViewById(R.id.spCpbgTrptDivNm);
        spCpbgSndvhclCmpNm = (Spinner) view.findViewById(R.id.spCpbgSndvhclCmpNm);

        tvCpbgSendDt = (TextView) view.findViewById(R.id.tvCpbgSendDt);
        tvCpbgSendTm = (TextView) view.findViewById(R.id.tvCpbgSendTm);
        tvCpbgArvlPragDt = (TextView) view.findViewById(R.id.tvCpbgArvlPragDt);
        tvCpbgArvlPragTm = (TextView) view.findViewById(R.id.tvCpbgArvlPragTm);

        etCpbgQty = (EditText) view.findViewById(R.id.etCpbgQty);
        //etEtcQty = (EditText) view.findViewById(R.id.etEtcQty);

        etCpbgSndvhclNo = (EditText) view.findViewById(R.id.etCpbgSndvhclNo);
        etCpbgInvcNo = (EditText) view.findViewById(R.id.etCpbgInvcNo);
        //etSenderNm = (EditText) view.findViewById(R.id.etSenderNm);
        etCpbgSendCont = (EditText) view.findViewById(R.id.etCpbgSendCont);



        btnBack = (ImageButton) view.findViewById(R.id.btnBack);
        btnHome = (ImageButton) view.findViewById(R.id.btnHome);

    }

    private void init() {

        SharedPreferences sp = getContext().getSharedPreferences(AndroidUtil.TAG_SP, Context.MODE_PRIVATE);
        deptCode = sp.getString(AndroidUtil.SP_LOGIN_DEPT, "");


        btnHome.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_navigation_menu_to_home));


        Click click = new Click();

        btnBack.setOnClickListener(click);

        btnCancel.setOnClickListener(click);
        btnSave.setOnClickListener(click);
        btnSend.setOnClickListener(click);

        tvCpbgSendDt.setOnClickListener(click);
        tvCpbgSendTm.setOnClickListener(click);

        tvCpbgArvlPragDt.setOnClickListener(click);
        tvCpbgArvlPragTm.setOnClickListener(click);

        handlerCalendar = new CalendarHandler(Looper.getMainLooper());
        calendarDialog = new CustomCalendarDialog(getActivity(), handlerCalendar);
        calendarDialog1 = new CustomCalendarDialog(getActivity(), handlerCalendar);

        progressDialog = new CustomProgressDialog(getContext());

        visitDate = (Date) getArguments().getSerializable("visit_date");

        AndroidUtil.log("visitDate : " + visitDate);

        bagSendAddViewModel.getProgressFlag().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {

                if(aBoolean){
                    progressDialog.show();
                }else{
                    progressDialog.dismiss();
                }

            }
        });

        bagSendAddViewModel.getUpdateFlag().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {

                if(aBoolean){

                    bagSendViewModel.setSearchDate(visitDate);
                    bagSendViewModel.getAPIBagSendListData();

                    bagSendAddViewModel.setUpdateFlag(false);

                    Navigation.findNavController(getView()).popBackStack();
                }

            }
        });

        bagSendAddViewModel.getBranchList().observe(getViewLifecycleOwner(), new Observer<ArrayList<NemoDeptCdNmListRO>>() {
            @Override
            public void onChanged(ArrayList<NemoDeptCdNmListRO> branchListROS) {

                String cpbgSendDeptNm = "";

                if( branchListROS.size() > 0 )
                {
                    String[] branchs = new String[branchListROS.size()];

                    int posIndex = 0;

                    for(int i = 0 ; i < branchListROS.size(); i++ )
                    {
                        branchs[i] = branchListROS.get(i).getDeptNm();
                        String dispCode = branchListROS.get(i).getDeptCd();

                        if( deptCode.equals(dispCode))
                        {
                            cpbgSendDeptNm = branchListROS.get(i).getDeptNm();
                        }
                    }


                }

                tvCpbgSendLocCntrNm.setText(cpbgSendDeptNm);

            }
        });

        bagSendAddViewModel.getCenterList().observe(getViewLifecycleOwner(), new Observer<ArrayList<NemoDeptCdNmListRO>>() {
            @Override
            public void onChanged(ArrayList<NemoDeptCdNmListRO> centerListROS) {

                if( centerListROS.size() > 0 )
                {
                    String[] branchs = new String[centerListROS.size()];

                    for(int i = 0 ; i < centerListROS.size(); i++ )
                    {
                        branchs[i] = centerListROS.get(i).getDeptNm();
                    }

                    ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, branchs);
                    spinnerArrayAdapter.setDropDownViewResource(R.layout.adapter_spinner_row2_layout); // android.R.layout.simple_spinner_dropdown_item
                    spCpbgArvlLocCntrNm.setAdapter(spinnerArrayAdapter);
                    spCpbgArvlLocCntrNm.setOnItemSelectedListener(new SpCpbgArvlLocCntrNmSelected());
                }

            }
        });

        bagSendAddViewModel.getTransportationList().observe(getViewLifecycleOwner(), new Observer<ArrayList<NemoCdNmListRO>>() {
            @Override
            public void onChanged(ArrayList<NemoCdNmListRO> listROS) {

                if( listROS.size() > 0 )
                {
                    String[] branchs = new String[listROS.size()];

                    for(int i = 0 ; i < listROS.size(); i++ )
                    {
                        branchs[i] = listROS.get(i).getName();
                    }

                    ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, branchs);
                    spinnerArrayAdapter.setDropDownViewResource(R.layout.adapter_spinner_row2_layout); // android.R.layout.simple_spinner_dropdown_item
                    spCpbgTrptDivNm.setAdapter(spinnerArrayAdapter);
                    spCpbgTrptDivNm.setOnItemSelectedListener(new SpCpbgTrptDivNmSelected());
                }

            }
        });

        bagSendAddViewModel.getTransportCompanyList().observe(getViewLifecycleOwner(), new Observer<ArrayList<NemoCdNmListRO>>() {
            @Override
            public void onChanged(ArrayList<NemoCdNmListRO> listROS) {

                if( listROS.size() > 0 )
                {
                    String[] branchs = new String[listROS.size()];

                    for(int i = 0 ; i < listROS.size(); i++ )
                    {
                        branchs[i] = listROS.get(i).getName();
                    }

                    ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, branchs);
                    spinnerArrayAdapter.setDropDownViewResource(R.layout.adapter_spinner_row2_layout); // android.R.layout.simple_spinner_dropdown_item
                    spCpbgSndvhclCmpNm.setAdapter(spinnerArrayAdapter);
                    spCpbgSndvhclCmpNm.setOnItemSelectedListener(new SpCpbgSndvhclCmpNmSelected());
                }

            }
        });

        bagSendAddViewModel.getCpbgSendDtYmd().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {

                tvCpbgSendDt.setText(s);

            }
        });

        bagSendAddViewModel.getCpbgArvlPragDtYmd().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {

                tvCpbgArvlPragDt.setText(s);

            }
        });

        bagSendAddViewModel.getCpbgSendTm().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {

                tvCpbgSendTm.setText(s);

                cpbgSendTm = s;

            }
        });

        bagSendAddViewModel.getCpbgArvlPragTm().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {

                tvCpbgArvlPragTm.setText(s);

                cpbgArvlPragTm = s;

            }
        });

        etCpbgSendCont.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

//                if( v.getId() == R.id.etCpbgSendCont )
//                {
//                    v.getParent().requestDisallowInterceptTouchEvent(true);
//
//                    switch (event.getAction() & MotionEvent.ACTION_MASK)
//                    {
//                        case MotionEvent.ACTION_UP:
//                            v.getParent().requestDisallowInterceptTouchEvent(false);
//                            break;
//                    }
//                }

                return false;
            }
        });


    }

    private boolean checkEmpty()
    {
        boolean rValue = true;

        String cpbgQty = etCpbgQty.getText().toString().trim();
        String cpbgSndvhclNo = etCpbgSndvhclNo.getText().toString().trim();
        String cpbgInvcNo = etCpbgInvcNo.getText().toString().trim();

        if(StringUtils.isEmpty(cpbgQty))
        {
            AndroidUtil.toast(getContext(), "행낭수량을 입력 하세요!");
            rValue = false;
        }
//        else if(StringUtils.isEmpty(cpbgSndvhclNo))
//        {
//            AndroidUtil.toast(getContext(), "차량번호를 입력 하세요!");
//            rValue = false;
//        }
//        else if(StringUtils.isEmpty(cpbgInvcNo))
//        {
//            AndroidUtil.toast(getContext(), "송장번호를 입력 하세요!");
//            rValue = false;
//        }

        Date drptDt = bagSendAddViewModel.getCpbgSendDt().getValue();
        Date arvDt = bagSendAddViewModel.getCpbgArvlPragDt().getValue();

        long drptTime = drptDt.getTime();
        long arvDtTime = arvDt.getTime();

        if( arvDtTime < drptTime )
        {
            AndroidUtil.toast(getContext(), "출발일자가 도착일자 보다 클 수 없습니다!");
            rValue = false;
        }
        else if( arvDtTime == drptTime )
        {
            String sendTm = bagSendAddViewModel.getCpbgSendTm().getValue().replace(":","");
            String arvlTm = bagSendAddViewModel.getCpbgArvlPragTm().getValue().replace(":","");

            try {
                if( Integer.parseInt(sendTm) >= Integer.parseInt(arvlTm) )
                {
                    AndroidUtil.toast(getContext(), "출발시간이 도착시간 보다 크거나 동일 할 수 없습니다!");
                    rValue = false;
                }
            }
            catch (Exception ex){}
        }



        return rValue;
    }

    private void bagSave(int type)
    {

        //AndroidUtil.log("getCpbgTrptDivCd() : " + getCpbgTrptDivCd());
        if( !checkEmpty() )
            return;

        String cpbgQty = etCpbgQty.getText().toString().trim();
        //String etcQty = etEtcQty.getText().toString().trim();
        String cpbgSndvhclNo = etCpbgSndvhclNo.getText().toString().trim();
        String cpbgInvcNo = etCpbgInvcNo.getText().toString().trim();
        //String senderNm = etSenderNm.getText().toString().trim();
        String cpbgSendCont = etCpbgSendCont.getText().toString().trim();

//        if(StringUtils.isEmpty(etcQty))
//        {
//            etcQty = "0";
//        }

        NemoBagSendAddPO param = new NemoBagSendAddPO();

        param.setCpbgSendDeptCd(deptCode);
        param.setCpbgArvlLocCntrCd(getCpbgArvlLocCntrCd());
        param.setCpbgQty(cpbgQty);
        param.setCpbgSendCont(cpbgSendCont);
        param.setCpbgTrptDivCd(getCpbgTrptDivCd());
        param.setCpbgInvcNo(cpbgInvcNo);
        param.setCpbgSndvhclNo(cpbgSndvhclNo);
        param.setCpbgSndvhclCmpNm(cpbgSndvhclCmpNm);

        param.setCpbgSendDt(DateUtil.getFormatString(visitDate, "yyyyMMdd"));

        if( type == 1)
        {
            param.setCpbgMvntDivCd("N");
        }
        else
        {
            param.setCpbgMvntDivCd("S");
        }


        bagSendAddViewModel.apiBagSend(param);

    }

    protected String getCpbgArvlLocCntrCd() {

        String rValue = "";

        rValue = bagSendAddViewModel.getCpbgArvlLocCntrCd(cpbgArvlLocCntrPos);

        return rValue;

    }

    protected String getCpbgTrptDivCd() {

        String rValue = "";

        rValue = bagSendAddViewModel.getCpbgTrptDivCd(cpbgTrptDivPos);

        return rValue;

    }


    public class Click implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {

                case R.id.tvCpbgSendDt:

                    calendarOpenType = 1;

                    calendarDialog.show(bagSendAddViewModel.getCpbgSendDt().getValue());
                    break;

                case R.id.tvCpbgSendTm:

                    hmPickerType = 1;

                    HourMinutePickerDialog pd1 = new HourMinutePickerDialog();
                    pd1.setListener(hhmmDialog);

                    String[] arrStr = cpbgSendTm.split(":");

                    pd1.setValue(Integer.parseInt(arrStr[0]),Integer.parseInt(arrStr[1]));

                    pd1.show(getActivity().getSupportFragmentManager(), "HourMinutePicker");


                    break;

                case R.id.tvCpbgArvlPragDt:

                    calendarOpenType = 2;

                    calendarDialog1.show(bagSendAddViewModel.getCpbgArvlPragDt().getValue());
                    break;

                case R.id.tvCpbgArvlPragTm:

                    hmPickerType = 2;

                    HourMinutePickerDialog pd2 = new HourMinutePickerDialog();
                    pd2.setListener(hhmmDialog);

                    String[] arrStr2 = cpbgArvlPragTm.split(":");

                    pd2.setValue(Integer.parseInt(arrStr2[0]),Integer.parseInt(arrStr2[1]));

                    pd2.show(getActivity().getSupportFragmentManager(), "HourMinutePicker");


                    break;

                case R.id.btnSave:

                    bagSave(1);

                    break;

                case R.id.btnSend:

                    bagSave(2);

                    break;

                case R.id.btnCancel:

                    Navigation.findNavController(getView()).popBackStack();

                    break;


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

                    if( calendarOpenType == 1)
                    {
                        bagSendAddViewModel.setCpbgSendDt(selectDate);
                    }
                    else
                    {
                        bagSendAddViewModel.setCpbgArvlPragDt(selectDate);
                    }

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

    public class SpCpbgTrptDivNmSelected implements Spinner.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            cpbgTrptDivNm = spCpbgTrptDivNm.getItemAtPosition(i).toString();
            cpbgTrptDivPos = i;
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {
            try {
                cpbgTrptDivPos = 0;
                cpbgTrptDivNm = spCpbgTrptDivNm.getItemAtPosition(0).toString();
            }
            catch (Exception ex)
            {
                AndroidUtil.log(ex.getMessage());
            }
        }

    }

    public class SpCpbgSndvhclCmpNmSelected implements Spinner.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            cpbgSndvhclCmpNm = spCpbgSndvhclCmpNm.getItemAtPosition(i).toString();
            cpbgSndvhclCmpPos = i;
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {
            try {
                cpbgSndvhclCmpPos = 0;
                cpbgSndvhclCmpNm = spCpbgSndvhclCmpNm.getItemAtPosition(0).toString();
            }
            catch (Exception ex)
            {
                AndroidUtil.log(ex.getMessage());
            }
        }

    }
}