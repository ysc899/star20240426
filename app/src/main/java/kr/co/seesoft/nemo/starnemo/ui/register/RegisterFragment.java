package kr.co.seesoft.nemo.starnemo.ui.register;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import kr.co.seesoft.nemo.starnemo.R;
import kr.co.seesoft.nemo.starnemo.api.ro.HospitalRegisterRO;
import kr.co.seesoft.nemo.starnemo.api.ro.VisitPlanRO;
import kr.co.seesoft.nemo.starnemo.db.vo.PictureVO;
import kr.co.seesoft.nemo.starnemo.nemoapi.ro.NemoImageInfoRO;
import kr.co.seesoft.nemo.starnemo.nemoapi.ro.NemoVisitListRO;
import kr.co.seesoft.nemo.starnemo.ui.camera.CameraMainActivity;
import kr.co.seesoft.nemo.starnemo.ui.dialog.CustomErrorDialog;
import kr.co.seesoft.nemo.starnemo.ui.dialog.CustomIngProgressDialog;
import kr.co.seesoft.nemo.starnemo.ui.dialog.CustomProgressDialog;
import kr.co.seesoft.nemo.starnemo.util.AndroidUtil;
import kr.co.seesoft.nemo.starnemo.util.Const;
import kr.co.seesoft.nemo.starnemo.util.DateUtil;


public class RegisterFragment extends Fragment {

    /** 방문 병원 정보 */
    private NemoVisitListRO visitHospital;
    /** 뷰모델 */
    private RegisterViewModel registerViewModel;
    /** context */
    private Context context;

    /** 버튼들 */
    private Button btnCamera, btnSend, btnScan, btnAlbum;
    /** 텍스트 뷰들 */
    private TextView tvRegisterManagerName, tvRegisterDate, tvHospitalName, tvTotalCount, tvNoSendCount, tvReceiptTotalCount, tvSendCount, tvScanInfo;
    /** 이미지 뷰 */
    private ImageView ivCall;

    /** 레이아웃 */
    private ConstraintLayout clFooterAction;

    /** 블루투스 통신 스레드 */
    protected ConnectedThread threadBluetooth;
    /** 블루투스 핸들러 */
    protected Handler handlerBluetooth;

    /** 접수된 환자 리스트 관련 */

    private RecyclerView rvRegisterList;
    private HospitalRegisterAdapter rvRegisterAdapter;
    private RecyclerView.LayoutManager rvRegisterLayoutManager;
    /** 접수된 환자 리스트 관련 끝 */

    private CustomIngProgressDialog progressDialog;

    private CustomErrorDialog errorDialog;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        registerViewModel =
                new ViewModelProvider(this).get(RegisterViewModel.class);

        View root = inflater.inflate(R.layout.fragment_register, container, false);

        this.context = getContext();
        initUI(root);
        init();

        return root;
    }

    private void initUI(View view) {
        btnCamera = (Button) view.findViewById(R.id.btnRegisterCamera);
        btnSend = (Button) view.findViewById(R.id.btnRegisterSend);
        btnScan = (Button) view.findViewById(R.id.btnRegisterScan);
        btnAlbum = (Button) view.findViewById(R.id.btnRegisterAlbum);



        tvRegisterDate = (TextView)view.findViewById(R.id.tvRegisterDate);
        tvHospitalName = (TextView)view.findViewById(R.id.tvRegisterHospitalName);
        tvRegisterManagerName = (TextView)view.findViewById(R.id.tvRegisterManagerName);
        tvTotalCount =  (TextView)view.findViewById(R.id.tvRegisterTotalCount);
        tvNoSendCount = (TextView)view.findViewById(R.id.tvRegisterNoSendCount);

        tvReceiptTotalCount = (TextView)view.findViewById(R.id.tvRegisterReceiptTotalCount);
        tvSendCount = (TextView)view.findViewById(R.id.tvRegisterSendCount);
        tvScanInfo = (TextView)view.findViewById(R.id.tvRegisterScanInfo);

        ivCall = (ImageView) view.findViewById(R.id.ivRegisterCall);


        rvRegisterList = (RecyclerView) view.findViewById(R.id.rvRegisterPatientList);
        rvRegisterList.setHasFixedSize(true);

        rvRegisterLayoutManager = new LinearLayoutManager(getActivity());
        rvRegisterList.setLayoutManager(rvRegisterLayoutManager);




/*
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float density = getResources().getDisplayMetrics().density;
        String densityString = String.format("%.2f", density);
        float dpHeight = outMetrics.heightPixels / density;
        float dpWidth = outMetrics.widthPixels / density;

        //dp별 layout 별도 적용
//        AndroidUtil.log("dpHeight : : "+dpHeight+"  dpWidth : "+dpWidth+"  density : "+density +"String.format(\"%.2f\", density) : "+densityString);





        clFooterAction  = (ConstraintLayout) view.findViewById(R.id.clFooterAction);
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) clFooterAction.getLayoutParams();
//        layoutParams.bottomMargin = 112;

        AndroidUtil.log("layoutParams.bottomMargin : " + layoutParams.bottomMargin);

        if(StringUtils.startsWith(densityString, "2.6")){
            AndroidUtil.log("if 2.6");
            layoutParams.bottomMargin = 147;
        }else if(StringUtils.startsWith(densityString, "2.8")){
            AndroidUtil.log("if 2.8");
            layoutParams.bottomMargin = 400;
        }


        clFooterAction.setLayoutParams(layoutParams);
*/
    }

    private void init() {
        BtnClick click = new BtnClick();
        btnCamera.setOnClickListener(click);
        btnSend.setOnClickListener(click);
        btnScan.setOnClickListener(click);
        btnAlbum.setOnClickListener(click);

        ivCall.setOnClickListener(click);

        tvHospitalName.setOnClickListener(click);

        progressDialog = new CustomIngProgressDialog(getContext());
        registerViewModel.getProgressFlag().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {

                if(aBoolean){
                    progressDialog.show();
                }else{
                    progressDialog.dismiss();
                }

            }
        });

        errorDialog = new CustomErrorDialog(getContext());
        errorDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                registerViewModel.setSendErrorFlag(false);
                registerViewModel.setAutoSendErrorCount(0);
            }
        });

        registerViewModel.getSendErrorFlag().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean){
                    errorDialog.show();
                }
            }
        });


        registerViewModel.getSendEndFlag().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean){
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setMessage("전송이 완료 되었습니다.")
                            .setCancelable(true)
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // FIRE ZE MISSILES!
                                    dialog.dismiss();
                                }
                            })
                            .setOnDismissListener(new DialogInterface.OnDismissListener() {
                                @Override
                                public void onDismiss(DialogInterface dialogInterface) {
                                    registerViewModel.setSendEndFlag(false);
                                    registerViewModel.getApiHospitalImageInfo();
                                }
                            })

                    ;
                    // Create the AlertDialog object and return it
                    builder.create();
                    builder.show();
                }
            }
        });


        registerViewModel.getHospitalInfo().observe(getViewLifecycleOwner(), new Observer<NemoVisitListRO>() {
            @Override
            public void onChanged(NemoVisitListRO visitPlanRO) {
                tvHospitalName.setText(visitPlanRO.getHospitalName());
                tvRegisterManagerName.setText(visitPlanRO.getManagerName());

                registerViewModel.getApiHospitalImageInfo();

                AndroidUtil.log("업무 화면 진입 : " +visitPlanRO.toString());

                if(StringUtils.isNotEmpty(visitPlanRO.getHospitalTel())){
                    ivCall.setVisibility(View.VISIBLE);
                }else{
                    ivCall.setVisibility(View.INVISIBLE);
                }

//                AndroidUtil.log(visitPlanRO);


            }
        });
        registerViewModel.getRegisterDate().observe(getViewLifecycleOwner(), new Observer<Date>() {
            @Override
            public void onChanged(Date date) {

                String sDate = DateUtil.getFormatString(date, "yyyyMMdd");
                String tDate = DateUtil.getFormatString(new Date(), "yyyyMMdd");
                if(sDate.equals(tDate)){
                    btnCamera.setVisibility(View.VISIBLE);
                    btnSend.setVisibility(View.VISIBLE);
                }else{
                    btnCamera.setVisibility(View.INVISIBLE);
                    btnSend.setVisibility(View.INVISIBLE);
                }

                tvRegisterDate.setText(DateUtil.getFormatString(date, "yyyy년 MM월 dd일"));
            }
        });

        registerViewModel.getTotalCount().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                tvTotalCount.setText(integer + " 건");
            }
        });
        registerViewModel.getNoSendCount().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                tvNoSendCount.setText(integer + " 건");
            }
        });

        registerViewModel.getSendCount().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {

                tvSendCount.setText(integer + " 건");
                if(registerViewModel.getProgressFlag().getValue()){

                    String text = "진행중 : "+ integer + " / " + registerViewModel.getTotalCount().getValue();

                    progressDialog.updateText(text);
                }
            }
        });


        registerViewModel.getTotalScanCount().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                //스캔 카운트 변경은 여기


                tvScanInfo.setText(registerViewModel.getScanCount().getValue() +" / " + integer);
//                tvReceiptTotalCount.setText(integer + " 건");
            }
        });
        registerViewModel.getScanCount().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                tvScanInfo.setText(integer +" / " + registerViewModel.getTotalScanCount().getValue());
            }
        });
//        registerViewModel.getNoScanCount().observe(getViewLifecycleOwner(), new Observer<Integer>() {
//            @Override
//            public void onChanged(Integer integer) {
//                tvScanInfo.setText(integer + " 건");
//            }
//        });


        visitHospital = (NemoVisitListRO) getArguments().getSerializable("visit_hospital");
        registerViewModel.setHospitalInfo(visitHospital);
        registerViewModel.setRegisterDate(DateUtil.getDate(getArguments().getString("register_day", DateUtil.getFormatString(new Date(), "yyyyMMdd")), "yyyyMMdd"));
        registerViewModel.setNemoRepository();

        //스타 시스템 데이터 조회
        registerViewModel.setHospitalRegisterList();

        registerViewModel.getHospitalDatas().observe(getViewLifecycleOwner(), new Observer<List<PictureVO>>() {
            @Override
            public void onChanged(List<PictureVO> pictureVOS) {
                registerViewModel.setCount();
            }
        });

        registerViewModel.getHospitalImageInfo().observe(getViewLifecycleOwner(), new Observer<NemoImageInfoRO>() {
            @Override
            public void onChanged(NemoImageInfoRO nemoImageInfoRO) {

                tvReceiptTotalCount.setText(nemoImageInfoRO.getMainCount() + " 건");
            }
        });


        handlerBluetooth = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);

                switch (msg.what) {
                    case Const.MESSAGE_READ:
                        String barcode = msg.obj.toString();
                        registerViewModel.scanHospitalRegisterBarcode(barcode);
                        rvRegisterAdapter.notifyDataSetChanged();
                        break;
                }

            }
        };


        rvRegisterAdapter = new HospitalRegisterAdapter(new ArrayList<>());
        rvRegisterList.setAdapter(rvRegisterAdapter);

        registerViewModel.getHospitalRegisterList().observe(getViewLifecycleOwner(), new Observer<ArrayList<HospitalRegisterRO>>() {
            @Override
            public void onChanged(ArrayList<HospitalRegisterRO> hospitalRegisterROS) {
                rvRegisterAdapter.setRegisterList(hospitalRegisterROS);
            }
        });


    }


    protected void sendData() {
        List<PictureVO> list = registerViewModel.getHospitalDatas().getValue();
        if(list == null || list.size() == 0){
            AndroidUtil.toast(getContext(), "전송할 데이터가 없습니다.");
            return;
        }else {
            if (registerViewModel.getNoSendCount().getValue() == 0) {
                AndroidUtil.toast(getContext(), "전송할 사진이 없습니다.");
            } else {
                registerViewModel.sendData();
            }
        }
    }

    protected class BtnClick implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {

                case R.id.ivRegisterCall:

                    AndroidUtil.log(registerViewModel.getHospitalInfo().getValue().getHospitalTel());

                    Intent callIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+registerViewModel.getHospitalInfo().getValue().getHospitalTel()));
                    startActivity(callIntent);

                    break;

                case R.id.btnRegisterCamera:

                    AndroidUtil.log("카메라 버튼 누름");
                    Intent intent = new Intent(context, CameraMainActivity.class);

                    intent.putExtra(Const.HOSPITAL_CD, registerViewModel.getHospitalCd());
                    intent.putExtra(Const.TODAY, registerViewModel.getRegisterDateYmd());
                    intent.putExtra(Const.GO_ALBUM, false);

                    startActivityForResult(intent, Const.START_CAMERA_ACTIVITY);
                    break;

                case R.id.btnRegisterSend:
                    AndroidUtil.log("전송 버튼 누름");

                    if(registerViewModel.getHospitalRegisterList().getValue().size() > 0){
                        Bundle bundle = new Bundle();

                        bundle.putString("register_day", DateUtil.getFormatString(registerViewModel.getRegisterDate().getValue(), "yyyyMMdd"));
                        bundle.putSerializable("visit_hospital", registerViewModel.getHospitalInfo().getValue());
                        bundle.putInt("sst", registerViewModel.getSstCount());
                        bundle.putInt("edta", registerViewModel.getEdtaCount());
                        bundle.putInt("urine", registerViewModel.getUrineCount());
                        bundle.putInt("other", registerViewModel.getOtherCount());

                        Navigation.findNavController(getView()).navigate(R.id.action_registerFragment_to_takingOverFragment, bundle);

                    }else {
                        sendData();
                    }
                    break;

                case R.id.btnRegisterScan:
                    Intent enBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enBtIntent, Const.START_BLUETOOTH);
                    break;

                case R.id.btnRegisterAlbum:
                    Intent albumIntent = new Intent(context, CameraMainActivity.class);

                    albumIntent.putExtra(Const.HOSPITAL_CD, registerViewModel.getHospitalCd());
                    albumIntent.putExtra(Const.TODAY, registerViewModel.getRegisterDateYmd());
                    albumIntent.putExtra(Const.GO_ALBUM, true);

                    startActivityForResult(albumIntent, Const.START_CAMERA_ACTIVITY);

                    break;

                case R.id.tvRegisterHospitalName:

//                    Bundle bundle = new Bundle();
//
//                    bundle.putString("register_day", DateUtil.getFormatString(registerViewModel.getRegisterDate().getValue(), "yyyyMMdd"));
//                    bundle.putSerializable("visit_hospital", registerViewModel.getHospitalInfo().getValue());
//                    bundle.putInt("sst", registerViewModel.getSstCount());
//                    bundle.putInt("edta", registerViewModel.getEdtaCount());
//                    bundle.putInt("urine", registerViewModel.getUrineCount());
//                    bundle.putInt("other", registerViewModel.getOtherCount());
//
//                    Navigation.findNavController(getView()).navigate(R.id.action_registerFragment_to_takingOverFragment, bundle);

                    break;
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(threadBluetooth != null){
            //블루투스 연결 종료
            threadBluetooth.interrupt();
            threadBluetooth.cancel();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {


        switch (requestCode) {
            case Const.START_CAMERA_ACTIVITY:

                break;

            case Const.START_BLUETOOTH:
                if (resultCode == Activity.RESULT_OK) {

                    BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
                    Set<BluetoothDevice> devices = adapter.getBondedDevices();
                    if (devices.size() > 0) {
                        for (BluetoothDevice device : devices) {

                            if (device.getName().startsWith("SN")) {
                                AndroidUtil.log("블루투스~" + device.getType());

                                if (threadBluetooth != null) {
                                    threadBluetooth.interrupt();
                                    threadBluetooth.cancel();
                                    threadBluetooth = null;
                                    SystemClock.sleep(1000);
                                }

                                try {
                                    BluetoothSocket socket = createBluetoothSocket(device);
                                    socket.connect();
                                    threadBluetooth = new ConnectedThread(socket);
                                    threadBluetooth.start();

                                } catch (IOException e) {
                                    AndroidUtil.log("생성 에러  : ", e);
                                    adapter.cancelDiscovery();
                                }
                            }
                        }


                    } else {
                        AndroidUtil.toast(context, "등록된 스캐너가 없습니다.");
                    }
                } else {
                    AndroidUtil.toast(context, "블루투스 활성화가 필요 합니다.");
                }

                break;

        }

        super.onActivityResult(requestCode, resultCode, data);

    }


    private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {
        if (Build.VERSION.SDK_INT >= 10) {
            try {
                final Method m = device.getClass().getMethod("createInsecureRfcommSocketToServiceRecord", new Class[]{UUID.class});
                return (BluetoothSocket) m.invoke(device, Const.BT_UUID);
            } catch (Exception e) {
                AndroidUtil.log("Could not create Insecure RFComm Connection", e);
            }
        }
        return device.createRfcommSocketToServiceRecord(Const.BT_UUID);
    }


    private class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket) {
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
                AndroidUtil.log("소켓 연결 에러 : ", e);
            }
            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            AndroidUtil.log("블루투스 런~~" + mmSocket.isConnected());

            byte[] mmBuffer = new byte[1024];
            int numBytes;
            while (!isInterrupted()) {
                try {

                    numBytes = mmInStream.available();
                    if (numBytes != 0) {
                        SystemClock.sleep(100);
                        numBytes = mmInStream.available();
                        numBytes = mmInStream.read(mmBuffer, 0, numBytes);

                        String code = StringUtils.toEncodedString(ArrayUtils.subarray(mmBuffer, 0, numBytes), Charset.forName("UTF-8")).replaceAll("[^0-9]", "");

                        Message readMsg = handlerBluetooth.obtainMessage(
                                Const.MESSAGE_READ, code);
                        readMsg.sendToTarget();
                    }


                } catch (IOException e) {
                    AndroidUtil.log("리드 에러 : " + e);
                }
            }
        }

        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                AndroidUtil.log("닫기 에러 :", e);
            }
        }
    }

    public class HospitalRegisterAdapter extends RecyclerView.Adapter<HospitalRegisterAdapter.RegisterPatientViewHolder>{

        private ArrayList<HospitalRegisterRO> registerList;

        public HospitalRegisterAdapter(ArrayList<HospitalRegisterRO> registerList) {
            this.registerList = registerList;
        }

        public ArrayList<HospitalRegisterRO> getRegisterList() {
            return registerList;
        }

        public void setRegisterList(ArrayList<HospitalRegisterRO> registerList) {
            this.registerList = registerList;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public HospitalRegisterAdapter.RegisterPatientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View adapterView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_register_patient_layout, parent, false);

            HospitalRegisterAdapter.RegisterPatientViewHolder holder = new HospitalRegisterAdapter.RegisterPatientViewHolder(adapterView);

            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull HospitalRegisterAdapter.RegisterPatientViewHolder holder, int position) {
            HospitalRegisterRO t = this.registerList.get(position);
            holder.bindItem(t);
        }

        @Override
        public int getItemCount() {
            return this.registerList.size();
        }


        public class RegisterPatientViewHolder extends RecyclerView.ViewHolder {

            public View v;
            public TextView tvOrder, tvId, tvName, tvSpecimen;
            public HospitalRegisterRO item = null;

            public RegisterPatientViewHolder(@NonNull View itemView) {
                super(itemView);
                tvOrder = (TextView) itemView.findViewById(R.id.tvRegisterOrder);
                tvId = (TextView) itemView.findViewById(R.id.tvRegisterId);
                tvName = (TextView) itemView.findViewById(R.id.tvRegisterName);
                tvSpecimen = (TextView) itemView.findViewById(R.id.tvRegisterSpecimen);
                v = itemView;
            }

            public void bindItem(HospitalRegisterRO t){
                tvOrder.setText(String.valueOf(t.order));
                tvId.setText(t.registerId);
                tvName.setText(t.patientName);
                tvSpecimen.setText(StringUtils.joinWith(", ",t.specimenList));
                if(t.scanFlag){
                    v.setBackgroundColor(Color.parseColor("#f0a1b8"));
                    tvOrder.setTextColor(Color.WHITE);
                    tvId.setTextColor(Color.WHITE);
                    tvName.setTextColor(Color.WHITE);
                    tvSpecimen.setTextColor(Color.WHITE);

                }else{
                    v.setBackgroundResource(android.R.color.white);
                    tvOrder.setTextColor(Color.parseColor("#999999"));
                    tvId.setTextColor(Color.parseColor("#999999"));
                    tvName.setTextColor(Color.parseColor("#999999"));
                    tvSpecimen.setTextColor(Color.parseColor("#999999"));
                }
                this.item = t;
            }
        }
    }

}