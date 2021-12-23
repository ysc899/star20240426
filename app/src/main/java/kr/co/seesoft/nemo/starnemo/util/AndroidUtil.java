package kr.co.seesoft.nemo.starnemo.util;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.os.Handler;
import android.widget.Toast;

import org.apache.commons.lang3.StringUtils;

import kr.co.seesoft.nemo.starnemo.R;

/**
 * 공통 사용할 유틸
 */
public class AndroidUtil {

    /** SharedPreferences Name 테크 */
    public static final String TAG_SP = "starNemo";

    /** sp 시큐어 코드 테그 */
    public static final String SP_SECURE_TAG = "secure";

    public static final String SP_LOGIN_AUTO = "loginAuto";

    public static final String SP_LOGIN_ID = "loginId";

    public static final String SP_LOGIN_PW = "loginPw";

    public static final String SP_LOGIN_DEPT = "loginDept";

    public static final String SP_LOGIN_SESSION_INFO = "sessionInfo";

    public static final String SP_CAMERA_SOUND = "shutSound";

    public static final String SP_CAMERA_SOUND_TYPE = "shutSoundType";

    public static final String SP_CAMERA_THUM = "photoThum";

    public static final String SP_ERROR_AUTO_SEND = "errorAutoSend";

    public static final String SP_CAMERA_FLASH = "flash";


    public static final String LOG_TAG = "hopalt";

    public static void log(final Object obj){
        LogWrapper.v(LOG_TAG, obj.toString());
    }

    public static void log(final String msg){
//        Log.i(LOG_TAG, msg);
        LogWrapper.v(LOG_TAG, msg);
    }
    public static void log(final String msg, final Throwable tr){
//        Log.i(LOG_TAG, msg);
        tr.printStackTrace();
        LogWrapper.v(LOG_TAG, msg, tr);
    }

    public static void toast(final Context context, final String msg){
        AndroidUtil.log(msg);
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }
    public static void toastShort(final Context context, final String msg){
        AndroidUtil.log(msg);
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * @param context context
     * @return app 버전 정보
     */
    public static String getVersionName(final Context context){
        String version = "";
        PackageInfo packageInfo;

        if(context == null){
            return version;
        }
        try{
            packageInfo = context.getApplicationContext()
                    .getPackageManager()
                    .getPackageInfo(context.getApplicationContext().getPackageName(), 0);
            version = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            log("버전 조회 에러 ",  e);
        }

        return version;
    }

    /**
     * 확인 다이얼로그
     * @param context context
     * @param title 타이틀
     * @param message 메시지
     */
    public static void showAlert(final Context context, final String title, final String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }

    public static final int ALERT_CALLBACK_OK = 300;
    public static final int ALERT_CALLBACK_CANCEL = 3001;

    public static void showAlert(final Context context, final String title, final String message, final String btnMsg, final Handler callback){
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialog);

        builder.setTitle(title);
        builder.setMessage(message);

        builder.setPositiveButton(btnMsg, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                callback.sendEmptyMessage(ALERT_CALLBACK_OK);
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }


    public static void copyText(final Context context, final String label, final String t){
        ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText(label, t);
        clipboardManager.setPrimaryClip(clipData);

        toast(context, label + "이 복사되었습니다.");
    }

    public static int textToInt(final String str){
        if(StringUtils.isEmpty(str)){
            return 0;
        }else{
            return Integer.valueOf(str);
        }
    }

    public static boolean isConnectNetwork(final Context context){

        boolean result = false;

        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if(cm != null){
            NetworkCapabilities capabilities = cm.getNetworkCapabilities(cm.getActiveNetwork());
            if(capabilities != null){
                result = capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                        || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR);
            }

        }

        return result;

    }

}
