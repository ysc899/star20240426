package kr.co.seesoft.nemo.starnemoapp.ui.takingover;


import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.Date;
import java.util.List;

import kr.co.seesoft.nemo.starnemoapp.R;
import kr.co.seesoft.nemo.starnemoapp.api.ro.HospitalRegisterRO;
import kr.co.seesoft.nemo.starnemoapp.db.vo.TakingOverVO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.ro.NemoVisitListRO;
import kr.co.seesoft.nemo.starnemoapp.ui.dialog.CustomDrawDialog;
import kr.co.seesoft.nemo.starnemoapp.ui.register.RegisterViewModel;
import kr.co.seesoft.nemo.starnemoapp.util.AndroidUtil;
import kr.co.seesoft.nemo.starnemoapp.util.Const;
import kr.co.seesoft.nemo.starnemoapp.util.DateUtil;


public class TakingOverFragment extends Fragment {

    /** 방문 병원 정보 */
    private NemoVisitListRO visitHospital;
    /** 뷰모델 */
    private TakingOverViewModel takingOverViewModel;
    /** 부모 뷰모델 */
    private RegisterViewModel parentViewModel;



    /** context */
    private Context context;

    /** 버튼들 */
    private Button btnTakingOverCancel, btnTakingOverTakingSign, btnTakingOverTakeSign, btnTakingOverComplete;
    /** 텍스트 뷰들 */
    private TextView tvTakingOverDate, tvTakingOverHospitalName;
    /** 텍스트 인풋 */
    private EditText etTakingOverTemp, etTakingOverSST, etTakingOverEDTA, etTakingOverUrine, etTakingOverBio, etTakingOverOther, etTakingOverIssue;

    //그리기 관련
    private Handler drawHandler;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        takingOverViewModel =
                new ViewModelProvider(this).get(TakingOverViewModel.class);


        parentViewModel = ViewModelProviders.of(getActivity()).get(RegisterViewModel.class);


        View root = inflater.inflate(R.layout.fragment_takingover, container, false);


        this.context = getContext();
        initUI(root);
        init();
        return root;
    }

    private void initUI(View view) {
        btnTakingOverCancel = (Button) view.findViewById(R.id.btnTakingOverCancel);
        btnTakingOverTakingSign = (Button) view.findViewById(R.id.btnTakingOverTakingSign);
        btnTakingOverTakeSign = (Button) view.findViewById(R.id.btnTakingOverTakeSign);
        btnTakingOverComplete = (Button) view.findViewById(R.id.btnTakingOverComplete);


        tvTakingOverDate = (TextView)view.findViewById(R.id.tvTakingOverDate);
        tvTakingOverHospitalName = (TextView)view.findViewById(R.id.tvTakingOverHospitalName);

        etTakingOverTemp = (EditText) view.findViewById(R.id.etTakingOverTemp);
        etTakingOverSST = (EditText) view.findViewById(R.id.etTakingOverSST);
        etTakingOverEDTA = (EditText) view.findViewById(R.id.etTakingOverEDTA);
        etTakingOverUrine = (EditText) view.findViewById(R.id.etTakingOverUrine);
        etTakingOverBio = (EditText) view.findViewById(R.id.etTakingOverBio);
        etTakingOverOther = (EditText) view.findViewById(R.id.etTakingOverOther);
        etTakingOverIssue = (EditText) view.findViewById(R.id.etTakingOverIssue);

    }

    private void init() {
        BtnClick click = new BtnClick();
        btnTakingOverCancel.setOnClickListener(click);
        btnTakingOverTakingSign.setOnClickListener(click);
        btnTakingOverTakeSign.setOnClickListener(click);
        btnTakingOverComplete.setOnClickListener(click);


        visitHospital = (NemoVisitListRO) getArguments().getSerializable("visit_hospital");
        takingOverViewModel.setHospitalInfo(visitHospital);
        takingOverViewModel.setRegisterDate(DateUtil.getDate(getArguments().getString("register_day", DateUtil.getFormatString(new Date(), "yyyyMMdd")), "yyyyMMdd"));
//        takingOverViewModel.setNemoRepository();

        takingOverViewModel.getRegisterDate().observe(getViewLifecycleOwner(), new Observer<Date>() {
            @Override
            public void onChanged(Date date) {
                tvTakingOverDate.setText(DateUtil.getFormatString(date, "yyyy년 MM월 dd일"));
            }
        });

        takingOverViewModel.getHospitalInfo().observe(getViewLifecycleOwner(), new Observer<NemoVisitListRO>() {
            @Override
            public void onChanged(NemoVisitListRO visitPlanRO) {
                tvTakingOverHospitalName.setText(visitPlanRO.getHospitalName());
            }
        });

        int sst = getArguments().getInt("sst");
        int edta = getArguments().getInt("edta");
        int urine = getArguments().getInt("urine");
        int other = getArguments().getInt("other");

        TakingOverVO vo = new TakingOverVO();
        vo.sst = sst;
        vo.edta = edta;
        vo.urine = urine;
        vo.other = other;

        String scanJson = getArguments().getString("scanList", "");

        Gson gson = new Gson();

        List<HospitalRegisterRO> scanList = gson.fromJson(scanJson, new TypeToken<List<HospitalRegisterRO>>(){}.getType());

        takingOverViewModel.setScanList(scanList);


        takingOverViewModel.setTakingOverInfo(vo);

        takingOverViewModel.getTakingOverInfo().observe(getViewLifecycleOwner(), new Observer<TakingOverVO>() {
            @Override
            public void onChanged(TakingOverVO takingOverVO) {
                if(takingOverVO.temperature > 0f) {
                    etTakingOverTemp.setText(String.valueOf(takingOverVO.temperature));
                }
                if(takingOverVO.sst > 0) {
                    etTakingOverSST.setText(String.valueOf(takingOverVO.sst));
                }
                if(takingOverVO.edta > 0) {
                    etTakingOverEDTA.setText(String.valueOf(takingOverVO.edta));
                }
                if(takingOverVO.urine > 0) {
                    etTakingOverUrine.setText(String.valueOf(takingOverVO.urine));
                }
                if(takingOverVO.bio > 0) {
                    etTakingOverBio.setText(String.valueOf(takingOverVO.bio));
                }
                if(takingOverVO.other > 0) {
                    etTakingOverOther.setText(String.valueOf(takingOverVO.other));
                }
                etTakingOverIssue.setText(takingOverVO.issue);
            }
        });

        takingOverViewModel.getSuccessFinsishFlag().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {

                if(aBoolean){

                    AndroidUtil.log("true 처리 전 : " + parentViewModel.getClearDataFlag().getValue() );
                    
                    parentViewModel.getClearDataFlag().setValue(true);

                    AndroidUtil.log("true 처리 후 : " + parentViewModel.getClearDataFlag().getValue() );
                    
                    Navigation.findNavController(getView()).popBackStack();

                    AndroidUtil.log("화면 종료");
                }

            }
        });


        drawHandler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);

                switch (msg.what){
                    case Const.DRAW_MODE_TAKING:

                        String takingFilePath = msg.obj.toString();
                        File takingFile = new File(takingFilePath);
                        if(takingFile.length() > 0){
                            takingOverViewModel.setTakingSignFile(takingFilePath);
                        }

                        break;
                    case Const.DRAW_MODE_TAKE:
                        String takeFilePath = msg.obj.toString();
                        File takeFile = new File(takeFilePath);
                        if(takeFile.length() > 0){
                            takingOverViewModel.setTakeSignFile(takeFilePath);
                        }
                        break;
                }
            }
        };
    }

    protected void openDialog(int mode){
        String title;
        String fileName = takingOverViewModel.getRegisterDateYmd() + "_" + takingOverViewModel.getHospitalCd() +"_"+ String.valueOf(System.currentTimeMillis())+"_";
        if(mode == Const.DRAW_MODE_TAKING){
            title = "인계 사인";
            fileName += "taking.png";
        }else{
            title = "인수 사인";
            fileName += "take.png";
        }

        String defaultFilePath =  getContext().getExternalFilesDir(getContext().getResources().getString(R.string.app_name)).getAbsolutePath();

        File pFile = new File( defaultFilePath+ File.separator + takingOverViewModel.getRegisterDateYmd() + File.separator + takingOverViewModel.getHospitalCd());
        if(!pFile.exists()){
            pFile.mkdirs();
        }

        File saveFile = new File(pFile,  fileName);

        CustomDrawDialog customDrawDialog = CustomDrawDialog.newInstance(title, drawHandler, saveFile, mode);
        customDrawDialog.show(getChildFragmentManager(), "taking");

    }



    protected class BtnClick implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.btnTakingOverTakingSign:
                    openDialog(Const.DRAW_MODE_TAKING);
                    break;
                case R.id.btnTakingOverTakeSign:
                     openDialog(Const.DRAW_MODE_TAKE);
                   break;
                case R.id.btnTakingOverComplete:

                    TakingOverVO item = takingOverViewModel.getTakingOverVO();
                    if(StringUtils.isEmpty(etTakingOverTemp.getText().toString())){
                        AndroidUtil.toast(getContext(), "온도는 필수 입니다.");
                        return;
                    }

                    item.temperature = Float.valueOf(etTakingOverTemp.getText().toString());
                    item.sst = AndroidUtil.textToInt(etTakingOverSST.getText().toString());
                    item.edta = AndroidUtil.textToInt(etTakingOverEDTA.getText().toString());
                    item.urine = AndroidUtil.textToInt(etTakingOverUrine.getText().toString());
                    item.bio = AndroidUtil.textToInt(etTakingOverBio.getText().toString());
                    item.other = AndroidUtil.textToInt(etTakingOverOther.getText().toString());

                    if((item.sst + item.edta + item.urine + item.bio + item.other) == 0){
                        AndroidUtil.toast(getContext(), "인수된 검체 수 가 없습니다.");
                        return;
                    }

                    item.issue = etTakingOverIssue.getText().toString();

                    if(StringUtils.isEmpty(item.takeOverPath)){
                        AndroidUtil.toast(getContext(), "인수 사인이 없습니다.");
                        return;
                    }
                    if(StringUtils.isEmpty(item.takingOverPath)){
                        AndroidUtil.toast(getContext(), "인계 사인이 없습니다.");
                        return;
                    }


                    takingOverViewModel.sendTakingOverInfo(item);
                    AndroidUtil.toast(getContext(), "저장되었습니다.");

                    break;

                case R.id.btnTakingOverCancel:

                    Navigation.findNavController(getView()).popBackStack();

                    break;


            }


        }
    }
}