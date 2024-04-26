package kr.co.seesoft.nemo.starnemoapp.ui.dashboard;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import kr.co.seesoft.nemo.starnemoapp.R;
import kr.co.seesoft.nemo.starnemoapp.db.vo.PictureVO;
import kr.co.seesoft.nemo.starnemoapp.ui.camera.CameraMainActivity;
import kr.co.seesoft.nemo.starnemoapp.util.AndroidUtil;
import kr.co.seesoft.nemo.starnemoapp.util.Const;
import kr.co.seesoft.nemo.starnemoapp.util.DateUtil;


public class DashboardFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;

    private Context context;

    private Button btnCamera, btnSend, btnScan;


    protected ConnectedThread threadBluetooth;
    protected Handler handlerBluetooth;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);


        this.context = getContext();
        initUI(root);
        init();
        return root;
    }

    private void initUI(View view) {
        btnCamera = (Button) view.findViewById(R.id.btnCamera);
        btnSend = (Button) view.findViewById(R.id.btnSend);
        btnScan = (Button) view.findViewById(R.id.btnScan);

        final TextView tvTotalCount = view.findViewById(R.id.tvTotalCount);
        final TextView tvSendCount = view.findViewById(R.id.tvSendCount);
        final TextView tvNoSendCount = view.findViewById(R.id.tvNoSendCount);
        dashboardViewModel.getTotalCount().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                tvTotalCount.setText("총 : " + integer + " 건");
            }
        });
        dashboardViewModel.getSendCount().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                tvSendCount.setText("전송 : " + integer + " 건");
            }
        });
        dashboardViewModel.getNoSendCount().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                tvNoSendCount.setText("미전송  : " + integer + " 건");
            }
        });
    }

    private void init() {
        BtnClick click = new BtnClick();
        btnCamera.setOnClickListener(click);
        btnSend.setOnClickListener(click);
        btnScan.setOnClickListener(click);

        dashboardViewModel.setHospitalCd("1234561");
        dashboardViewModel.setToday(DateUtil.getFormatString(new Date(), "yyyyMMdd"));
        dashboardViewModel.setNemoRepository();

        dashboardViewModel.getHospitalDatas().observe(getViewLifecycleOwner(), new Observer<List<PictureVO>>() {
            @Override
            public void onChanged(List<PictureVO> pictureVOS) {
                dashboardViewModel.setCount();
            }
        });


        //블루투스 관련
//        AndroidUtil.log("----------------- 블루투스 -----------------");
//        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
//        if(bluetoothAdapter == null){
//            AndroidUtil.log("지원 안됨");
//            AndroidUtil.toast(context, "지원 안됨");
//        }else{
//            AndroidUtil.log("되넹");
//            AndroidUtil.toast(context, "되넹");
//        }
//        AndroidUtil.log("----------------- 블루투스 -----------------");


        handlerBluetooth = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);

                switch (msg.what) {
                    case Const.MESSAGE_READ:
                        AndroidUtil.log("------------ 핸들러 ---------------------");
                        AndroidUtil.log(msg.obj.toString());
                        AndroidUtil.log("------------ 핸들러 ---------------------");
                        AndroidUtil.toastShort(getContext(), msg.obj.toString());


                        break;
                }

            }
        };

    }   

    protected void sendData() {

        List<PictureVO> list = dashboardViewModel.getHospitalDatas().getValue();

        if (list != null) {

            list.stream().filter(t -> {
                return !t.sendFlag;
            }).forEach(t -> {
                AndroidUtil.log(t.toString());

                File f = new File(t.filePath);
                AndroidUtil.log(f.getName());

                try {
                    byte[] fileContent = FileUtils.readFileToByteArray(f);
                    String imageBase64 = Base64.getEncoder().encodeToString(fileContent);
//                    AndroidUtil.log(imageBase64);

                    t.sendFlag = true;
                    dashboardViewModel.updateHospitalPicData(t);

                } catch (IOException e) {
                    AndroidUtil.log("에러에러");
                    e.printStackTrace();
                }
            });


        }


    }

    protected class BtnClick implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {

                case R.id.btnCamera:

                    Intent intent = new Intent(context, CameraMainActivity.class);

                    intent.putExtra(Const.HOSPITAL_CD, dashboardViewModel.getHospitalCd());
                    intent.putExtra(Const.TODAY, dashboardViewModel.getToday());

                    startActivityForResult(intent, Const.START_CAMERA_ACTIVITY);
                    break;

                case R.id.btnSend:
                    sendData();
                    break;

                case R.id.btnScan:
                    Intent enBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enBtIntent, Const.START_BLUETOOTH);
                    break;
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

//        //이부분이 필요한가 모르겠넹;;;
//        AndroidUtil.log("*****************************************");
//        AndroidUtil.log("requestCode : "+ requestCode + "   resultCode : " + resultCode);
//        AndroidUtil.log("*****************************************");


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
//            while (true) {
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

}