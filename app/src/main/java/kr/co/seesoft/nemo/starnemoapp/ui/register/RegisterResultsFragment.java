package kr.co.seesoft.nemo.starnemoapp.ui.register;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import kr.co.seesoft.nemo.starnemoapp.R;
import kr.co.seesoft.nemo.starnemoapp.db.vo.PictureVO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.ro.NemoCustomerInfoRO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.ro.NemoCustomerRecpListRO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.ro.NemoCustomerRecpTestListRO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.ro.NemoScheduleListRO;
import kr.co.seesoft.nemo.starnemoapp.ui.camera.CameraMainActivity;
import kr.co.seesoft.nemo.starnemoapp.ui.dialog.CustomAlertDialog;
import kr.co.seesoft.nemo.starnemoapp.ui.dialog.CustomErrorDialog;
import kr.co.seesoft.nemo.starnemoapp.ui.dialog.CustomIngProgressDialog;
import kr.co.seesoft.nemo.starnemoapp.ui.memo.MemoFragment;
import kr.co.seesoft.nemo.starnemoapp.util.AndroidUtil;
import kr.co.seesoft.nemo.starnemoapp.util.Const;
import kr.co.seesoft.nemo.starnemoapp.util.DateUtil;
import kr.co.seesoft.nemo.starnemoapp.util.GetByte;


public class RegisterResultsFragment extends Fragment {

    /** 검사 결과 정보 */
    private NemoCustomerRecpListRO visitResults;

    /** context */
    private Context context;

    private WebView webview;

    // Title bar button
    private ImageButton btnBack, btnHome;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_register_results, container, false);

        this.context = getContext();
        initUI(root);
        init();

        return root;
    }

    private void initUI(View view) {

        btnBack = (ImageButton) view.findViewById(R.id.btnBack);
        btnHome = (ImageButton) view.findViewById(R.id.btnHome);

        webview = (WebView) view.findViewById(R.id.webview);

        WebSettings webSettings = webview.getSettings();
        webSettings.setJavaScriptEnabled(true);

    }

    private void init() {

        btnHome.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_navigation_menu_to_home));

        visitResults = (NemoCustomerRecpListRO) getArguments().getSerializable("visit_results");


        Click click = new Click();

        btnBack.setOnClickListener(click);

        goUrl();
    }

    private void goUrl()
    {
        SharedPreferences sp = context.getSharedPreferences(AndroidUtil.TAG_SP, Context.MODE_PRIVATE);
        String userId = sp.getString(AndroidUtil.SP_LOGIN_ID, "");
        String userPw = sp.getString(AndroidUtil.SP_LOGIN_PW, "");

        StringBuilder sb = new StringBuilder();


        try {

//            sb.append("I_UID=");
//            sb.append(URLEncoder.encode(userId, "UTF-8"));
//            sb.append("&I_UPWD=");
//            sb.append(URLEncoder.encode(userPw, "UTF-8"));
//            sb.append("&I_DTLDAT=");
//            sb.append(URLEncoder.encode(visitResults.getRecpDt(), "UTF-8"));
//            sb.append("&I_DTLJNO=");
//            sb.append(URLEncoder.encode(visitResults.getRecpNo(), "UTF-8"));

            sb.append("I_UID=");
            sb.append(userId);
            sb.append("&I_UPWD=");
            sb.append(userPw);
            sb.append("&I_DTLDAT=");
            sb.append(visitResults.getRecpDt());
            sb.append("&I_DTLJNO=");
            sb.append(visitResults.getRecpNo());

            String data = sb.toString();

            AndroidUtil.log("POST DATA : " + data);

            webview.postUrl(Const.URL_REGISTER_RESULTS, GetByte.myGetBytes(data, "BASE64"));
        }
        catch (Exception ex)
        {
            AndroidUtil.log(ex.toString());
        }

    }


    public class Click implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {

                case R.id.btnBack:

                    Navigation.findNavController(getView()).popBackStack();

                    break;
            }
        }
    }

}
