package kr.co.seesoft.nemo.starnemoapp.ui.register;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import kr.co.seesoft.nemo.starnemoapp.R;
import kr.co.seesoft.nemo.starnemoapp.db.vo.PictureVO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.ro.NemoCustomerInfoRO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.ro.NemoCustomerInfoRndRO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.ro.NemoCustomerRecpListRO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.ro.NemoCustomerRecpTestListRO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.ro.NemoScheduleListRO;
import kr.co.seesoft.nemo.starnemoapp.ui.camera.CameraMainActivity;
import kr.co.seesoft.nemo.starnemoapp.ui.dialog.CustomAlertDialog;
import kr.co.seesoft.nemo.starnemoapp.ui.dialog.CustomErrorDialog;
import kr.co.seesoft.nemo.starnemoapp.ui.dialog.CustomIngProgressDialog;
import kr.co.seesoft.nemo.starnemoapp.util.AndroidUtil;
import kr.co.seesoft.nemo.starnemoapp.util.Const;
import kr.co.seesoft.nemo.starnemoapp.util.DateUtil;

import static android.content.Context.MODE_PRIVATE;


public class RegisterRndFragment extends Fragment {

    /** 방문 병원 정보 */
    private NemoScheduleListRO visitHospital;
    private NemoCustomerInfoRndRO rndHospital;
    /** 뷰모델 */
    private RegisterRndViewModel registerRndViewModel;
    /** context */
    private Context context;

    /** 버튼들 */
    private Button btnCamera, btnSend, btnAlbum, btnScan;
    /** 텍스트 뷰들 */
    private TextView tvRprsNm, tvRegisterDate, tvHospitalName, tvTotalCount, tvNoSendCount, tvReceiptTotalCount, tvSendCount, tvScanInfo;
    private TextView tvCustTelno, tvAddr, tvPrjtNm, tvPrjtChgrNm, tvUisu, tvTakeManual;
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

    // Title bar button
    private ImageButton btnBack, btnHome;

    private CustomAlertDialog customAlertDialog;

    private DownloadManager mDownloadManager;
    private Long mDownloadQueueId;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        registerRndViewModel =
                new ViewModelProvider(getActivity()).get(RegisterRndViewModel.class);

        View root = inflater.inflate(R.layout.fragment_register_rnd, container, false);

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


        tvCustTelno = (TextView)view.findViewById(R.id.tvCustTelno);
        tvAddr = (TextView)view.findViewById(R.id.tvAddr);
        tvPrjtNm = (TextView)view.findViewById(R.id.tvPrjtNm);
        tvPrjtChgrNm = (TextView)view.findViewById(R.id.tvPrjtChgrNm);
        tvUisu = (TextView)view.findViewById(R.id.tvUisu);
        tvTakeManual = (TextView)view.findViewById(R.id.tvTakeManual);

        tvRegisterDate = (TextView)view.findViewById(R.id.tvRegisterDate);
        tvHospitalName = (TextView)view.findViewById(R.id.tvRegisterHospitalName);
        tvRprsNm = (TextView)view.findViewById(R.id.tvRprsNm);
        tvTotalCount =  (TextView)view.findViewById(R.id.tvRegisterTotalCount);
        tvNoSendCount = (TextView)view.findViewById(R.id.tvRegisterNoSendCount);

        tvReceiptTotalCount = (TextView)view.findViewById(R.id.tvRegisterReceiptTotalCount);
        tvSendCount = (TextView)view.findViewById(R.id.tvRegisterSendCount);
        tvScanInfo = (TextView)view.findViewById(R.id.tvRegisterScanInfo);

        ivCall = (ImageView) view.findViewById(R.id.ivRegisterCall);

        btnBack = (ImageButton) view.findViewById(R.id.btnBack);
        btnHome = (ImageButton) view.findViewById(R.id.btnHome);

    }

    private void init() {

        btnBack.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_navigation_menu_to_back));
        btnHome.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_navigation_menu_to_home));

        BtnClick click = new BtnClick();
        btnCamera.setOnClickListener(click);
        btnSend.setOnClickListener(click);
        btnScan.setOnClickListener(click);
        btnAlbum.setOnClickListener(click);

        tvTakeManual.setOnClickListener(click);

        tvHospitalName.setOnClickListener(click);

        progressDialog = new CustomIngProgressDialog(getContext());
        registerRndViewModel.getProgressFlag().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {

                if(aBoolean){
                    progressDialog.show();
                }else{
                    registerRndViewModel.setIsSendProcess(false);
                    progressDialog.dismiss();
                }

            }
        });

        errorDialog = new CustomErrorDialog(getContext());
        errorDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                registerRndViewModel.setSendErrorFlag(false);
                registerRndViewModel.setAutoSendErrorCount(0);
            }
        });

        registerRndViewModel.getSendErrorFlag().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean){
                    errorDialog.show();
                }
            }
        });


        registerRndViewModel.getSendEndFlag().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean){

                    customAlertDialog = new CustomAlertDialog(getContext());
                    customAlertDialog.show();
                    customAlertDialog.setMsg("전송이 완료 되었습니다.");

                    registerRndViewModel.setSendEndFlag(false);

                }
            }
        });


        registerRndViewModel.getHospitalInfo().observe(getViewLifecycleOwner(), new Observer<NemoCustomerInfoRndRO>() {
            @Override
            public void onChanged(NemoCustomerInfoRndRO visitPlanRO) {

                tvRprsNm.setText(visitPlanRO.getChgrNm());

                tvCustTelno.setText(visitPlanRO.getCustTelno());
                tvAddr.setText(visitPlanRO.getAddr());
                tvPrjtNm.setText(visitPlanRO.getPrjtNm());
                tvPrjtChgrNm.setText(visitPlanRO.getPrjtChgrNm());
                tvUisu.setText(visitPlanRO.getUisu());
                tvTakeManual.setText(visitPlanRO.getFileNm());

                rndHospital = visitPlanRO;

            }
        });


        registerRndViewModel.getRegisterDate().observe(getViewLifecycleOwner(), new Observer<Date>() {
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

        registerRndViewModel.getTotalCount().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                tvTotalCount.setText(integer + " 건");
            }
        });
        registerRndViewModel.getNoSendCount().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                tvNoSendCount.setText(integer + " 건");
            }
        });

        registerRndViewModel.getSendCount().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {

                tvSendCount.setText(integer + " 건");

                if( registerRndViewModel.getIsSendProcess().getValue() )
                {
                    if(registerRndViewModel.getProgressFlag().getValue()){

                        String text = "진행중 : "+ integer + " / " + registerRndViewModel.getTotalCount().getValue();

                        progressDialog.updateText(text);
                    }
                }
                else
                {
                    progressDialog.updateText("");
                }

            }
        });





        visitHospital = (NemoScheduleListRO) getArguments().getSerializable("visit_hospital");
        registerRndViewModel.setRegisterDate(DateUtil.getDate(getArguments().getString("register_day", DateUtil.getFormatString(new Date(), "yyyyMMdd")), "yyyyMMdd"));

        tvHospitalName.setText(visitHospital.getCustNm());

        registerRndViewModel.setHospital(visitHospital);
        registerRndViewModel.setNemoRepository();

        registerRndViewModel.getHospitalDatas().observe(getViewLifecycleOwner(), new Observer<List<PictureVO>>() {
            @Override
            public void onChanged(List<PictureVO> pictureVOS) {
                registerRndViewModel.setCount();
            }
        });



        handlerBluetooth = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);

                switch (msg.what) {
                    case Const.MESSAGE_READ:
                        String barcode = msg.obj.toString();
                        registerRndViewModel.scanHospitalRegisterBarcode(barcode);
                        rvRegisterAdapter.notifyDataSetChanged();
                        break;
                }

            }
        };


    }


    protected void sendData() {
        List<PictureVO> list = registerRndViewModel.getHospitalDatas().getValue();

//        AndroidUtil.log("list.size() : " + list.size());
        if(list == null || list.size() == 0){
            AndroidUtil.toast(getContext(), "전송할 데이터가 없습니다.");
            return;
        }else {
            if (registerRndViewModel.getNoSendCount().getValue() == 0) {
                AndroidUtil.toast(getContext(), "전송할 사진이 없습니다.");
            } else {
                registerRndViewModel.sendData();
            }
        }
    }

    private void downloadFile() {

        String downloadUrl = AndroidUtil.nullToString(rndHospital.getFileUrl(),"");

        AndroidUtil.log("downloadUrl : " + downloadUrl);

        if( downloadUrl.length() > 0 )
        {
            ///////////////////////////////////////////
            // HttpURLConnection 으로 다운로드
//            String outputFilePath = Environment.getExternalStoragePublicDirectory(
//                    Environment.DIRECTORY_DOWNLOADS + "/SeeStar") + "/" + rndHospital.getFileNm();
//
//            new DownloadFileTask().execute(downloadUrl,outputFilePath);


            ///////////////////////////////////////////
            // DownloadManager로 다운로드
            mDownloadManager = (DownloadManager) getContext().getSystemService(Context.DOWNLOAD_SERVICE);

            String outputFilePath = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS + "/SeeStar") + "/" + rndHospital.getFileNm();

            AndroidUtil.log("outputFilePath : " + outputFilePath);

            File outputFile = new File(outputFilePath);
            if (!outputFile.getParentFile().exists()) {
                outputFile.getParentFile().mkdirs();
            }

            Uri downloadUri = Uri.parse(downloadUrl);
            DownloadManager.Request request = new DownloadManager.Request(downloadUri);
            List<String> pathSegmentList = downloadUri.getPathSegments();
            request.setTitle("수거 메뉴얼 다운로드");
            request.setDescription("수거 메뉴얼 다운로드");
            request.setDestinationUri(Uri.fromFile(outputFile));
            request.setAllowedOverMetered(true);
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

            mDownloadQueueId = mDownloadManager.enqueue(request);

            getContext().registerReceiver(new DownloadReceiver(), new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

            AndroidUtil.toast(getContext(),"파일 다운로가 요청 되었습니다.");

        }

    }


    boolean mSendPressed = false;


    protected class BtnClick implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {

                case R.id.tvTakeManual:

                    downloadFile();

                    break;

                case R.id.ivRegisterCall:

                    AndroidUtil.log(registerRndViewModel.getHospitalInfo().getValue().getCustTelno());

                    Intent callIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+registerRndViewModel.getHospitalInfo().getValue().getCustTelno()));
                    startActivity(callIntent);

                    break;

                case R.id.btnRegisterCamera:

                    AndroidUtil.log("카메라 버튼 누름");
                    Intent intent = new Intent(context, CameraMainActivity.class);

                    intent.putExtra(Const.HOSPITAL_CD, registerRndViewModel.getHospitalCd());
                    intent.putExtra(Const.TODAY, registerRndViewModel.getRegisterDateYmd());
                    intent.putExtra(Const.GO_ALBUM, false);

                    startActivityForResult(intent, Const.START_CAMERA_ACTIVITY);
                    break;

                case R.id.btnRegisterSend:


                    AndroidUtil.log("전송 버튼 누름");

                    if(mSendPressed){
                        AndroidUtil.toast(context, "처리중 입니다.");
                        return;
                    }

                    btnSend.setEnabled(false);
                    mSendPressed = true;

                    Handler tempHandler = new Handler(Looper.myLooper()) {
                        @Override
                        public void handleMessage(@NonNull Message msg) {
                            super.handleMessage(msg);
                            mSendPressed = false;
                            btnSend.setEnabled(true);
                        }
                    };
                    tempHandler.sendEmptyMessageDelayed(0, 2000);

//                    new Timer().schedule(new TimerTask() {
//                        @Override
//                        public void run() {
//                            mSendPressed = false;
//                            btnSend.setEnabled(true);
//                        }
//                    }, 1000);

                    AndroidUtil.log("전송 프로세스 시작");


//                    if(registerViewModel.getHospitalRegisterList().getValue().stream().filter(it -> it.scanFlag).count() > 0){
//                        Bundle bundle = new Bundle();
//
//                        bundle.putString("register_day", DateUtil.getFormatString(registerViewModel.getRegisterDate().getValue(), "yyyyMMdd"));
//                        bundle.putSerializable("visit_hospital", registerViewModel.getHospitalInfo().getValue());
//                        bundle.putInt("sst", registerViewModel.getSstCount());
//                        bundle.putInt("edta", registerViewModel.getEdtaCount());
//                        bundle.putInt("urine", registerViewModel.getUrineCount());
//                        bundle.putInt("other", registerViewModel.getOtherCount());
//
//
//                        List<HospitalRegisterRO> sacnList = registerViewModel.getHospitalRegisterList().getValue().stream().filter(it -> it.scanFlag).collect(Collectors.toList());
//
//                        if(sacnList.size() <= 0){
//                            AndroidUtil.toast(context, "스캔된 내용이 없습니다.");
//                            return;
//                        }
//
//                        Gson gson = new Gson();
//
////                        AndroidUtil.log("**********************************************");
////                        AndroidUtil.log(gson.toJson(sacnList));
////                        AndroidUtil.log("**********************************************");
//
//                        bundle.putString("scanList", gson.toJson(sacnList));
//
//                        Navigation.findNavController(getView()).navigate(R.id.action_registerFragment_to_takingOverFragment, bundle);
//
//                    }else {
//                        sendData();
//                    }

                    sendData();

                    break;

                case  R.id.btnRegisterScan:
                    Intent enBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enBtIntent, Const.START_BLUETOOTH);
                    break;

                case R.id.btnRegisterAlbum:
                    Intent albumIntent = new Intent(context, CameraMainActivity.class);

                    albumIntent.putExtra(Const.HOSPITAL_CD, registerRndViewModel.getHospitalCd());
                    albumIntent.putExtra(Const.TODAY, registerRndViewModel.getRegisterDateYmd());
                    albumIntent.putExtra(Const.GO_ALBUM, true);

                    startActivityForResult(albumIntent, Const.START_CAMERA_ACTIVITY);

                    break;

                case R.id.tvRegisterHospitalName:

                    Bundle bundle1 = new Bundle();

                    bundle1.putSerializable("visit_hospital_cd", visitHospital.getCustCd());
                    bundle1.putSerializable("visit_hospital_nm", visitHospital.getCustNm());

                    Navigation.findNavController(getView()).navigate(R.id.action_customerDetailFragment, bundle1);

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

                    boolean isScanDevice = false;

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

                                    isScanDevice = true;

                                } catch (IOException e) {
                                    AndroidUtil.log("생성 에러  : ", e);
                                    adapter.cancelDiscovery();
                                }
                            }
                        }

                        if( !isScanDevice )
                        {
                            AndroidUtil.toast(context, "등록된 스캐너가 없습니다.");
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

        private ArrayList<NemoCustomerRecpListRO> registerList;

        public HospitalRegisterAdapter(ArrayList<NemoCustomerRecpListRO> registerList) {
            this.registerList = registerList;
        }

        public ArrayList<NemoCustomerRecpListRO> getRegisterList() {
            return registerList;
        }

        public void setRegisterList(ArrayList<NemoCustomerRecpListRO> registerList) {
            this.registerList = registerList;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public RegisterPatientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View adapterView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_register_patient_layout, parent, false);

            RegisterPatientViewHolder holder = new RegisterPatientViewHolder(adapterView);

            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull RegisterPatientViewHolder holder, int position) {
            NemoCustomerRecpListRO t = this.registerList.get(position);
            holder.bindItem(t);
        }

        @Override
        public int getItemCount() {
            return this.registerList.size();
        }


        public class RegisterPatientViewHolder extends RecyclerView.ViewHolder {

            public View v;
            public TextView tvOrder, tvId, tvName,tvRecpNo , tvSpecimen;
            public NemoCustomerRecpListRO item = null;

            public RegisterPatientViewHolder(@NonNull View itemView) {
                super(itemView);
                tvOrder = (TextView) itemView.findViewById(R.id.tvRegisterOrder);
                tvId = (TextView) itemView.findViewById(R.id.tvRegisterId);
                tvName = (TextView) itemView.findViewById(R.id.tvRegisterName);
                tvRecpNo = (TextView) itemView.findViewById(R.id.tvRecpNo);
                tvSpecimen = (TextView) itemView.findViewById(R.id.tvRegisterSpecimen);
                v = itemView;
            }

            public void bindItem(NemoCustomerRecpListRO t){
                tvOrder.setText(String.valueOf(t.getOrder()));
                tvId.setText(t.getRecpNo());
                tvName.setText(t.getPatnNm());

                ArrayList<NemoCustomerRecpTestListRO> testList = t.getTestCodeList();

                String respNo = "";
                String respName = "";

                for( int i = 0 ; i < testList.size() ; i++ )
                {
                    if( i > 0 )
                    {
                        respNo += "\n";
                        respName += "\n";
                    }
                    respNo += testList.get(i).getTstCd();
                    respName += testList.get(i).getTstNm();
                }

                tvRecpNo.setText(respNo);
                tvSpecimen.setText(respName);

                this.item = t;
            }
        }
    }

    private class DownloadReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            long receivedId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);

            // 다운로드 완료 여부 확인
            if (mDownloadQueueId == receivedId) {

                int status = getDownloadStatus(context, receivedId);

                // 다운로드 성공 여부 확인
                if (status == DownloadManager.STATUS_SUCCESSFUL) {
                    AndroidUtil.toast(getContext(),"수거 메뉴얼 다운로드 완료");
                } else {
                    // 다운로드 실패
                    AndroidUtil.toast(getContext(),"수거 메뉴얼 다운로드 실패!");
                }
            }
        }

        private int getDownloadStatus(Context context, long downloadId) {
            DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);

            DownloadManager.Query query = new DownloadManager.Query();
            query.setFilterById(downloadId);

            try (Cursor cursor = downloadManager.query(query)) {
                if (cursor != null && cursor.moveToFirst()) {
                    return cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
                }
            }

            return DownloadManager.STATUS_FAILED;
        }
    }


    private class DownloadFileTask extends AsyncTask<String, Void, Boolean> {

        private String outputFilePath;

        @Override
        protected Boolean doInBackground(String... params) {
            String fileUrl = params[0];
            outputFilePath = params[1];
            try {
                // 파일 다운로드 수행
                downloadFile(fileUrl);
                return true;
            } catch (IOException e) {
                AndroidUtil.log("파일을 다운로드 할 수 없습니다.");
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (result) {
                // 다운로드 성공 시 메시지 표시
                AndroidUtil.toast(getContext(),"수거 메뉴얼 다운로드 완료");
            } else {
                // 다운로드 실패 시 메시지 표시
                AndroidUtil.toast(getContext(),"수거 메뉴얼 다운로드 실패!");
            }
        }

        private void downloadFile(String fileUrl) throws IOException {
            URL url = new URL(fileUrl);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            try {

                urlConnection.setConnectTimeout(5000); // 5초 동안 연결을 시도
                urlConnection.setReadTimeout(10000); // 10초 동안 데이터를 읽는 시간 제한

                int responseCode = urlConnection.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) {

                    // 파일 저장 스트림 열기
                    try (InputStream inputStream = urlConnection.getInputStream();
                         FileOutputStream outputStream = new FileOutputStream(outputFilePath)) {

                        byte[] buffer = new byte[1024];
                        int bytesRead;

                        while ((bytesRead = inputStream.read(buffer)) != -1) {
                            outputStream.write(buffer, 0, bytesRead);
                        }
                    }
                } else {
                    // 서버 응답이 HTTP_OK가 아닌 경우 오류 처리
                    throw new IOException("서버 접속 오류");
                }
            } finally {
                urlConnection.disconnect();
            }
        }
    }

}