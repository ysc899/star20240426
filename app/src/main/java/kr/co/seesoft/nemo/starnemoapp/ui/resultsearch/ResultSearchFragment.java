package kr.co.seesoft.nemo.starnemoapp.ui.resultsearch;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import kr.co.seesoft.nemo.starnemoapp.R;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.ro.NemoCustomerRecpListRO;
import kr.co.seesoft.nemo.starnemoapp.util.AndroidUtil;
import kr.co.seesoft.nemo.starnemoapp.util.Const;
import kr.co.seesoft.nemo.starnemoapp.util.GetByte;


public class ResultSearchFragment extends Fragment {

    /** context */
    private Context context;

    private WebView webview;

    // Title bar button
    private ImageButton btnBack, btnHome;

    private TextView textTitle;

    String callType = "";


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

        textTitle = (TextView) view.findViewById(R.id.textTitle);

        WebSettings webSettings = webview.getSettings();
        webSettings.setJavaScriptEnabled(true);

    }

    private void init() {

        btnHome.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_navigation_menu_to_home));

        callType = (String) getArguments().getString("callType");


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
//            sb.append("&I_DTLDAT=");
//            sb.append(visitResults.getRecpDt());
//            sb.append("&I_DTLJNO=");
//            sb.append(visitResults.getRecpNo());

            String data = sb.toString();

            AndroidUtil.log("callType : " + callType);
            AndroidUtil.log("POST DATA : " + data);

            // 항목 조회 call
            if( "1".equals(callType))
            {
                textTitle.setText("항목 조회");
                webview.postUrl(Const.URL_ITEM_SEARCH, GetByte.myGetBytes(data, "BASE64"));
            }
            // 결과 조회 call
            else if( "2".equals(callType))
            {
                textTitle.setText("결과 조회");
                webview.postUrl(Const.URL_RESULTS_SEARCH, GetByte.myGetBytes(data, "BASE64"));
            }


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
