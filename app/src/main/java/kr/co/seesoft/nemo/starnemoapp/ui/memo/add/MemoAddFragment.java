package kr.co.seesoft.nemo.starnemoapp.ui.memo.add;

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
import android.widget.CheckBox;
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

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.selection.Selection;
import androidx.recyclerview.selection.SelectionTracker;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.util.StringUtil;
import kr.co.seesoft.nemo.starnemoapp.R;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.ro.NemoCustomerMemoCodeListRO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.ro.NemoDeptCdNmListRO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.ro.NemoHospitalSearchRO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.ro.NemoVisitListRO;
import kr.co.seesoft.nemo.starnemoapp.ui.bagsend.BagSendFragment;
import kr.co.seesoft.nemo.starnemoapp.ui.bagsend.BagSendViewModel;
import kr.co.seesoft.nemo.starnemoapp.ui.bagsend.add.BagSendAddFragment;
import kr.co.seesoft.nemo.starnemoapp.ui.dialog.CustomCalendarDialog;
import kr.co.seesoft.nemo.starnemoapp.ui.dialog.CustomProgressDialog;
import kr.co.seesoft.nemo.starnemoapp.ui.dialog.SearchCustomerDialog;
import kr.co.seesoft.nemo.starnemoapp.ui.memo.MemoFragment;
import kr.co.seesoft.nemo.starnemoapp.ui.memo.MemoViewModel;
import kr.co.seesoft.nemo.starnemoapp.ui.transaction.agree.TransactionAgreeViewModel;
import kr.co.seesoft.nemo.starnemoapp.util.AndroidUtil;
import kr.co.seesoft.nemo.starnemoapp.util.Const;

public class MemoAddFragment extends Fragment {


    private MemoAddViewModel memoAddViewModel;
    private MemoViewModel memoViewModel;

    //셀렉박스 관련
    private Spinner spMemoItem;

    //셀렉박스 관련 끝

    private String strMemoItem;

    private Button btnSearch, btnAdd, btnCancel, btnSave;

    private TextView tvCustomer, tvSearchDate;
    private EditText etMemo;
    private LinearLayout raGroup;
    private CheckBox raItem01, raItem02, raItem03, raItem04;

    private ConstraintLayout constraintLayoutMemo;

    private CustomProgressDialog progressDialog;

    // Title bar button
    private ImageButton btnBack, btnHome;

    private Handler customerHandler;

    private NemoHospitalSearchRO selCustomer;

    private ArrayList<NemoCustomerMemoCodeListRO> codeList;

    //캘린더 팝업
    private CustomCalendarDialog calendarDialog;
    private Handler handlerCalendar;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        memoAddViewModel =
                ViewModelProviders.of(getActivity()).get(MemoAddViewModel.class);

        memoViewModel = ViewModelProviders.of(getActivity()).get(MemoViewModel.class);

        View root = inflater.inflate(R.layout.fragment_memo_add, container, false);

        initUI(root);
        init();
        return root;
    }

    private void initUI(View view) {


        btnSearch = (Button) view.findViewById(R.id.btnSearch);
        btnAdd = (Button) view.findViewById(R.id.btnAdd);
        btnCancel = (Button) view.findViewById(R.id.btnCancel);
        btnSave = (Button) view.findViewById(R.id.btnSave);

        tvCustomer = (TextView) view.findViewById(R.id.tvCustomer);
        tvSearchDate = (TextView) view.findViewById(R.id.tvSearchDate);

        etMemo = (EditText) view.findViewById(R.id.etMemo);

        constraintLayoutMemo = (ConstraintLayout) view.findViewById(R.id.constraintLayoutMemo);

        raGroup = (LinearLayout) view.findViewById(R.id.raGroup);
        raItem01 = (CheckBox) view.findViewById(R.id.raItem01);
        raItem02 = (CheckBox) view.findViewById(R.id.raItem02);
        raItem03 = (CheckBox) view.findViewById(R.id.raItem03);
        raItem04 = (CheckBox) view.findViewById(R.id.raItem04);

        btnBack = (ImageButton) view.findViewById(R.id.btnBack);
        btnHome = (ImageButton) view.findViewById(R.id.btnHome);

        spMemoItem = (Spinner) view.findViewById(R.id.spMemoItem);

    }

    private void init() {

        memoAddViewModel.setClear();

        btnHome.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_navigation_menu_to_home));


        Click click = new Click();

        btnBack.setOnClickListener(click);

        tvCustomer.setOnClickListener(click);

        btnSearch.setOnClickListener(click);
        btnAdd.setOnClickListener(click);
        btnCancel.setOnClickListener(click);
        btnSave.setOnClickListener(click);

        tvSearchDate.setOnClickListener(click);

        handlerCalendar = new CalendarHandler(Looper.getMainLooper());
        calendarDialog = new CustomCalendarDialog(getActivity(), handlerCalendar);


        progressDialog = new CustomProgressDialog(getContext());
        memoAddViewModel.getProgressFlag().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {

                if(aBoolean){
                    progressDialog.show();
                }else{
                    progressDialog.dismiss();
                }

            }
        });

        memoAddViewModel.getCodeList().observe(getViewLifecycleOwner(), new Observer<ArrayList<NemoCustomerMemoCodeListRO>>() {
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
                    spMemoItem.setAdapter(spinnerArrayAdapter);
                    spMemoItem.setOnItemSelectedListener(new SpMemoItemSelected());

                    codeList = codeListROS;
                }

            }
        });

        memoAddViewModel.getUpdateFlag().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {

                if(aBoolean){

                    memoViewModel.setSearchDate(memoAddViewModel.getSearchDate().getValue());
                    memoViewModel.getAPIMemoListData();

                    memoAddViewModel.setUpdateFlag(false);

                    Navigation.findNavController(getView()).popBackStack();
                }

            }
        });

        memoAddViewModel.getSearchDateYmd().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {

                tvSearchDate.setText(s);

            }
        });

        customerHandler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);

                AndroidUtil.log("customerHandler : " + msg.obj);

                selCustomer = (NemoHospitalSearchRO)msg.obj;


                tvCustomer.setText(selCustomer.getCustNm());

            }
        };

        etMemo.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

//                if( v.getId() == R.id.etMemo )
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

    private void setDispRadio()
    {
        try {

            int index = spMemoItem.getSelectedItemPosition();

            NemoCustomerMemoCodeListRO obj = codeList.get(index);

            if( obj.getItemCd01() != null && obj.getItemCd01().length() > 0 )
            {
                //ConstraintLayout.LayoutParams params1 = (ConstraintLayout.LayoutParams) constraintLayoutMemo.getLayoutParams();

                //params1.topMargin = 720; //px 단위

                //constraintLayoutMemo.setLayoutParams(params1);

                raItem01.setText(AndroidUtil.nullToString(obj.getItemCd01(),""));
                raItem02.setText(AndroidUtil.nullToString(obj.getItemCd02(),""));
                raItem03.setText(AndroidUtil.nullToString(obj.getItemCd03(),""));
                raItem04.setText(AndroidUtil.nullToString(obj.getItemCd04(),""));

                raItem01.setChecked(false);
                raItem02.setChecked(false);
                raItem03.setChecked(false);
                raItem04.setChecked(false);

                raGroup.setVisibility(View.VISIBLE);

            }
            else
            {

                //ConstraintLayout.LayoutParams params1 = (ConstraintLayout.LayoutParams) constraintLayoutMemo.getLayoutParams();

                //params1.topMargin = 620;

                //constraintLayoutMemo.setLayoutParams(params1);

                raGroup.setVisibility(View.GONE);
            }

        }
        catch (Exception ex)
        {
            AndroidUtil.log(ex.toString());
        }


    }

    private String getRadio()
    {
        String rValue = "";

        try {

            int index = spMemoItem.getSelectedItemPosition();

            NemoCustomerMemoCodeListRO obj = codeList.get(index);

            if( raItem01.isChecked() )
            {
                rValue = AndroidUtil.nullToString(obj.getItemCd01(),"") + "  ";
            }
            if( raItem02.isChecked() )
            {
                rValue += AndroidUtil.nullToString(obj.getItemCd02(),"") + "  ";
            }
            if( raItem03.isChecked() )
            {
                rValue += AndroidUtil.nullToString(obj.getItemCd03(),"") + "  ";
            }
            if( raItem04.isChecked() )
            {
                rValue += AndroidUtil.nullToString(obj.getItemCd04(),"") + "  ";
            }

        }
        catch (Exception ex)
        {
            AndroidUtil.log(ex.toString());
        }

        return rValue;
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
                    memoAddViewModel.setSearchDate(selectDate);

                    break;

            }

        }
    }

    private void memoSave()
    {
        if(selCustomer == null)
        {
            AndroidUtil.toast(getContext(),"고객을 검색 후 선택 하세요!");

            return;
        }

        String memo = etMemo.getText().toString().trim();

        if(StringUtils.isEmpty(memo))
        {
            AndroidUtil.toast(getContext(),"메모를 입력 하세요!");

            return;
        }

        memoAddViewModel.apiSendMemo(selCustomer,memo);
    }


    public class Click implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {

                case R.id.tvSearchDate:

                    calendarDialog.show(memoAddViewModel.getSearchDate().getValue());
                    break;

                case R.id.tvCustomer:
                case R.id.btnSearch:

                    SearchCustomerDialog searchCustomerDialog = SearchCustomerDialog.newInstance(customerHandler);

                    searchCustomerDialog.show(getChildFragmentManager(),"SearchCustomer");

                    break;

                case R.id.btnAdd:

                    if( strMemoItem.length() > 0 )
                    {
                        String orgMemo = etMemo.getText().toString();

                        String addMemo = "";

                        if(orgMemo.length() > 0)
                        {
                            addMemo += "\n";
                        }

                        addMemo += strMemoItem + " - \n";

                        String detailItem = getRadio();

                        if( detailItem.length() > 0 )
                        {
                            addMemo += detailItem + "\n";
                        }

                        etMemo.setText(orgMemo + addMemo);

                        etMemo.setSelection(etMemo.getText().length());
                        etMemo.requestFocus();
                    }

                    break;

                case R.id.btnSave:

                    memoSave();

                    break;

                case R.id.btnCancel:
                case R.id.btnBack:

                    Navigation.findNavController(getView()).popBackStack();

                    break;
            }
        }
    }

    public class SpMemoItemSelected implements Spinner.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            strMemoItem = spMemoItem.getItemAtPosition(i).toString();

            setDispRadio();
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {
            try {
                strMemoItem = spMemoItem.getItemAtPosition(0).toString();

                setDispRadio();
            }
            catch (Exception ex)
            {
                AndroidUtil.log(ex.getMessage());
            }
        }

    }




}