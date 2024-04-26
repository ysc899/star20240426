package kr.co.seesoft.nemo.starnemoapp.ui.transaction.agree;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kyanogen.signatureview.SignatureView;

import java.io.ByteArrayOutputStream;
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
import kr.co.seesoft.nemo.starnemoapp.util.AndroidUtil;
import kr.co.seesoft.nemo.starnemoapp.util.Const;

public class TransactionAgreeFragment extends Fragment {


    private TransactionAgreeViewModel transactionAgreeViewModel;

    /** 병원 정보 */
    private NemoHospitalSearchRO visitHospital;

    private Button btnClear, btnSign;

    private CustomProgressDialog progressDialog;

    // Title bar button
    private ImageButton btnBack, btnHome;

    private TextView tvBizrNo, tvCustNm, tvAddr, tvRprsNm, tvAgreeDate, tvTermsConDate;
    private TextView tvTerms;

    SignatureView signatureView;
    ImageView imSignatureView;

    Gson gson = new Gson();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        transactionAgreeViewModel =
                ViewModelProviders.of(getActivity()).get(TransactionAgreeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_transaction_agree, container, false);

        initUI(root);
        init();
        return root;
    }

    private void initUI(View view) {

        tvBizrNo = (TextView) view.findViewById(R.id.tvBizrNo);
        tvCustNm = (TextView) view.findViewById(R.id.tvCustNm);
        tvAddr = (TextView) view.findViewById(R.id.tvAddr);
        tvRprsNm = (TextView) view.findViewById(R.id.tvRprsNm);
        tvAgreeDate = (TextView) view.findViewById(R.id.tvAgreeDate);
        tvTermsConDate = (TextView) view.findViewById(R.id.tvTermsConDate);
        tvTerms = (TextView) view.findViewById(R.id.tvTerms);

        btnClear = (Button) view.findViewById(R.id.btnClear);
        btnSign = (Button) view.findViewById(R.id.btnSign);

        btnBack = (ImageButton) view.findViewById(R.id.btnBack);
        btnHome = (ImageButton) view.findViewById(R.id.btnHome);

        signatureView = (SignatureView) view.findViewById(R.id.signature_view);
        imSignatureView = (ImageView) view.findViewById(R.id.imSignature_view);
    }

    private void init() {


        btnHome.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_navigation_menu_to_home));


        Click click = new Click();

        btnBack.setOnClickListener(click);

        btnClear.setOnClickListener(click);
        btnSign.setOnClickListener(click);

        visitHospital = (NemoHospitalSearchRO) getArguments().getSerializable("visit_hospital");

        AndroidUtil.log("visitHospital : " + visitHospital);

        transactionAgreeViewModel.setHospital(visitHospital);

        progressDialog = new CustomProgressDialog(getContext());
        transactionAgreeViewModel.getProgressFlag().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {

                if(aBoolean){
                    progressDialog.show();
                }else{
                    progressDialog.dismiss();
                }

            }
        });

        transactionAgreeViewModel.getHospitalInfo().observe(getViewLifecycleOwner(), new Observer<NemoCustomerInfoRO>() {
            @Override
            public void onChanged(NemoCustomerInfoRO customerInfo) {
                setCustomerInfo(customerInfo);
            }
        });

    }

    public void setCustomerInfo(NemoCustomerInfoRO customerInfo)
    {

        tvBizrNo.setText(customerInfo.getBizrNo());
        tvCustNm.setText(customerInfo.getCustNm());
        tvAddr.setText(customerInfo.getAddr());
        tvRprsNm.setText(customerInfo.getRprsNm());
        tvAgreeDate.setText(customerInfo.getCsntDtm());
        tvTermsConDate.setText(AndroidUtil.dispDate(customerInfo.getRvsnDt()));
        tvTerms.setText(Html.fromHtml(customerInfo.getTrmsCont()));

        String imgUrl = customerInfo.getImgUrl();

        if( imgUrl != null && imgUrl.length() > 0 )
        {
            imSignatureView.setVisibility(View.VISIBLE);
            signatureView.setVisibility(View.GONE);

            AndroidUtil.log("imgUrl : " + imgUrl);

            try {
                Glide.with(this)
                        .load(imgUrl)
                        .into(imSignatureView);
            }
            catch (Exception ex)
            {
                AndroidUtil.toast(getContext(),"사인정보 로드 오류");

                imSignatureView.setVisibility(View.GONE);
                signatureView.setVisibility(View.VISIBLE);


                AndroidUtil.log(ex.toString());
            }

        }
        else
        {
            imSignatureView.setVisibility(View.GONE);
            signatureView.setVisibility(View.VISIBLE);
        }

    }

    public class Click implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {

                case R.id.btnBack:

                    Navigation.findNavController(getView()).popBackStack();

                    break;

                case R.id.btnClear:

                    imSignatureView.setVisibility(View.GONE);
                    signatureView.setVisibility(View.VISIBLE);

                    signatureView.clearCanvas();
                    break;

                case R.id.btnSign:

//                    AndroidUtil.log("signatureView.isBitmapEmpty() : " + signatureView.isBitmapEmpty());

                    if(!signatureView.isBitmapEmpty())
                    {
                        Bitmap bitmap = signatureView.getSignatureBitmap();

                        ByteArrayOutputStream outArray = new ByteArrayOutputStream();

                        bitmap.compress(Bitmap.CompressFormat.PNG,100,outArray);

                        transactionAgreeViewModel.apiSingImageAdd(outArray.toByteArray());
                    }
                    else
                    {
                        AndroidUtil.toast(getContext(),"사인후 약관 동의 가능 합니다.");
                    }



                    break;
            }
        }
    }

}