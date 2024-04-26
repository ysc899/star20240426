package kr.co.seesoft.nemo.starnemoapp.ui.transaction.paysend;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
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

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.selection.Selection;
import androidx.recyclerview.selection.SelectionTracker;
import androidx.recyclerview.widget.RecyclerView;
import kr.co.seesoft.nemo.starnemoapp.R;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.ro.NemoCustomerInfoRO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.ro.NemoHospitalSearchRO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.ro.NemoVisitListRO;
import kr.co.seesoft.nemo.starnemoapp.ui.dialog.CustomProgressDialog;
import kr.co.seesoft.nemo.starnemoapp.ui.dialog.YearMonthPickerDialog;
import kr.co.seesoft.nemo.starnemoapp.ui.transaction.agree.TransactionAgreeViewModel;
import kr.co.seesoft.nemo.starnemoapp.util.AndroidUtil;
import kr.co.seesoft.nemo.starnemoapp.util.Const;

public class TransactionPaysendFragment extends Fragment { //implements MainMenuActivity.OnBackPressedListener{


    private TransactionPaysendViewModel transactionPaysendViewModel;

    /** 병원 정보 */
    private NemoHospitalSearchRO visitHospital;

    private Button btnCancel, btnSend;

    private CustomProgressDialog progressDialog;

    // Title bar button
    private ImageButton btnBack, btnHome;

    private TextView tvHospitalName;

    private TextView tvCustCd, tvJobYm;

    private EditText etCustEmalAddr;

    private String jobYM;

    Gson gson = new Gson();

    DatePickerDialog.OnDateSetListener yymmDialog = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth){
            AndroidUtil.log("YearMonthPicker : " +" year = " + year + ", month = " + monthOfYear + ", day = " + dayOfMonth);

            transactionPaysendViewModel.setJobYM(year + "-" + String.format("%02d", monthOfYear) );

        }
    };

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        transactionPaysendViewModel =
                ViewModelProviders.of(getActivity()).get(TransactionPaysendViewModel.class);
        View root = inflater.inflate(R.layout.fragment_transaction_paysend, container, false);

        initUI(root);
        init();
        return root;
    }

    private void initUI(View view) {

        tvHospitalName = (TextView) view.findViewById(R.id.tvHospitalName);

        tvCustCd = (TextView) view.findViewById(R.id.tvCustCd);
        tvJobYm = (TextView) view.findViewById(R.id.tvJobYm);

        etCustEmalAddr = (EditText) view.findViewById(R.id.etCustEmalAddr);

        btnCancel = (Button) view.findViewById(R.id.btnCancel);
        btnSend = (Button) view.findViewById(R.id.btnSend);

        btnBack = (ImageButton) view.findViewById(R.id.btnBack);
        btnHome = (ImageButton) view.findViewById(R.id.btnHome);

    }

    private void init() {


        btnHome.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_navigation_menu_to_home));


        Click click = new Click();

        tvJobYm.setOnClickListener(click);

        btnCancel.setOnClickListener(click);
        btnSend.setOnClickListener(click);

        btnBack.setOnClickListener(click);

        progressDialog = new CustomProgressDialog(getContext());
        transactionPaysendViewModel.getProgressFlag().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {

                if(aBoolean){
                    progressDialog.show();
                }else{
                    progressDialog.dismiss();
                }

            }
        });

        transactionPaysendViewModel.getUpdateFlag().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {

                if(aBoolean){
                    Navigation.findNavController(getView()).popBackStack();
                }

            }
        });

        visitHospital = (NemoHospitalSearchRO) getArguments().getSerializable("visit_hospital");

        AndroidUtil.log("visitHospital : " + visitHospital);

        transactionPaysendViewModel.setHospital(visitHospital);

        tvHospitalName.setText(visitHospital.getCustNm());


        transactionPaysendViewModel.getHospitalInfo().observe(getViewLifecycleOwner(), new Observer<NemoCustomerInfoRO>() {
            @Override
            public void onChanged(NemoCustomerInfoRO customerInfo) {
                setCustomerInfo(customerInfo);
            }
        });

        transactionPaysendViewModel.getJobYM().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {

                jobYM = s;

                String[] arrDisp = jobYM.split("-");

                String dispYm = arrDisp[0] + "년 " + arrDisp[1] + "월";

                tvJobYm.setText(dispYm);

            }
        });


    }

    public void setCustomerInfo(NemoCustomerInfoRO customerInfo)
    {

        tvCustCd.setText(customerInfo.getCustCd());
        etCustEmalAddr.setText(customerInfo.getCustEmalAddr());

    }


    public class Click implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {

                case R.id.tvJobYm:

                    YearMonthPickerDialog pd1 = new YearMonthPickerDialog();
                    pd1.setListener(yymmDialog);

                    AndroidUtil.log("jobYM : " + jobYM);

                    String[] arrStr = jobYM.split("-");

                    pd1.setValue(Integer.parseInt(arrStr[0]),Integer.parseInt(arrStr[1]));

                    pd1.show(getActivity().getSupportFragmentManager(), "YearMonthPicker");

                    break;

                case R.id.btnSend:

                    String email = etCustEmalAddr.getText().toString().trim();

                    if( StringUtils.isEmpty(email) )
                    {
                        AndroidUtil.toast(getContext(), "이메일을 입력 하세요.");
                        return;
                    }

                    transactionPaysendViewModel.apiSendBill(email);

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

}