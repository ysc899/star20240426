package kr.co.seesoft.nemo.starnemoapp.util;

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

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import kr.co.seesoft.nemo.starnemoapp.R;

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

    public static final String SP_LOGIN_UPPR_DEPT = "loginUpprDept";

    public static final String SP_LOGIN_SESSION_INFO = "sessionInfo";

    public static final String SP_CAMERA_SOUND = "shutSound";

    public static final String SP_CAMERA_SOUND_TYPE = "shutSoundType";

    public static final String SP_CAMERA_THUM = "photoThum";

    public static final String SP_ERROR_AUTO_SEND = "errorAutoSend";

    public static final String SP_CAMERA_FLASH = "flash";

    // 결제 영수증 출력 옵션
    public static final String SP_CARD_PRINT_OPTION = "cardPrint";

    // 취소 영수증 출력 옵션
    public static final String SP_CANCEL_PRINT_OPTION = "cancelPrint";

    // VAT 포함옵션
    public static final String SP_CARD_VAT_OPTION = "cardVAT";

    // VOC 조회 일자
    public static final String SP_VOC_VIEW_DATE = "vocViewDate";


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

    /**
     * @param str 처리할 문자열
     * @return 스페이스가 제거된 문자열
     */
    public static String spaceRemove(final String str) {
        if (StringUtils.isNotEmpty(str)) {
            String target = str;
            String match2 = "\\s{2,}";
            target = target.replaceAll(match2, "");
            target = target.replaceAll("\\p{Z}", "");
            target.trim();
            return target;
        } else {
            return "";
        }

    }

    public static String getSearchHospitalType(final int index)
    {
        String rValue = "";

        if( index == 0)
            rValue = "custNm";
        else if( index == 1)
            rValue = "custCd";
        else if( index == 2)
            rValue = "chgrId";

        return rValue;
    }

    public static String getInstallmentType(final int index)
    {
        String rValue = "";

        if( index == 0)
            rValue = "0";
        else if( index == 1)
            rValue = "2";
        else if( index == 2)
            rValue = "3";
        else if( index == 3)
            rValue = "4";
        else if( index == 4)
            rValue = "5";
        else if( index == 5)
            rValue = "6";

        return rValue;
    }

    public static String getDepositType(final String inStr)
    {
        String rValue = "";

        if( "5".equals(inStr))
        {
            rValue = "신용카드";
        }
        else if( "6".equals(inStr))
        {
            rValue = "통장입금";
        }
        else if( "1".equals(inStr))
        {
            rValue = "현금";
        }
        else if( "2".equals(inStr))
        {
            rValue = "약속어음";
        }
        else if( "3".equals(inStr))
        {
            rValue = "가계수표";
        }
        else if( "4".equals(inStr))
        {
            rValue = "당좌수표";
        }

        return rValue;
    }

    public static String getApprovalType(final String inStr,final String type)
    {
        String rValue = "";

        if( "approval".equals(inStr))
        {
            if( "Y".equals(type))
                rValue = "승인취소";
            else
                rValue = "승인";
        }
        else
        {
            rValue = "취소";
        }

        return rValue;
    }

    public static String getItrnNm(final String inStr)
    {
        String rValue = "";

        if( "O".equals(inStr))
        {
            rValue = "OCS연동";
        }
        else if( "U".equals(inStr))
        {
            rValue = "유비랩";
        }
        else if( "E".equals(inStr))
        {
            rValue = "엑셀";
        }
        else if( "H".equals(inStr))
        {
            rValue = "수기";
        } else {
            rValue = inStr;
        }

        return rValue;
    }

    public static Date dispStringToDate(String inDate)
    {
        Date rVlaue = new Date();

        try {
            if( inDate != null && inDate.length() == 8)
            {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");

                rVlaue = formatter.parse(inDate);
            }
        }
        catch (Exception ex)
        {
            AndroidUtil.log(ex.toString());
        }

        return rVlaue;
    }

    public static String dispStringToTime(String inTime)
    {
        String rVlaue = "";

        if( inTime != null && inTime.length() == 4)
        {
            rVlaue = inTime.substring(0,2);
            rVlaue += ":";
            rVlaue += inTime.substring(2);
        }

        return rVlaue;
    }

    public static String dispDate(String inStr)
    {
        String rValue = nullToString(inStr,"");

        if( rValue.length() == 8)
        {
            rValue = rValue.substring(0,4) + "-" + rValue.substring(4,6) + "-" + rValue.substring(6);
        }

        return rValue;
    }

    public static String dispTime(String inTime)
    {
        String rVlaue = "";

        if( inTime != null && inTime.length() == 6)
        {
            rVlaue = inTime.substring(0,2);
            rVlaue += ":";
            rVlaue += inTime.substring(2,4);
            rVlaue += ":";
            rVlaue += inTime.substring(4);
        }

        return rVlaue;
    }

    public static String dispYYMMM(String inStr)
    {
        String rValue = nullToString(inStr,"");

        if( rValue.length() == 6)
        {
            rValue = rValue.substring(0,4) + "-" + rValue.substring(4);
        }

        return rValue;
    }

    public static String dispCurrency(long inLong)
    {
        String rValue = "0";

        try
        {
            rValue = NumberFormat.getInstance().format(inLong);
        }
        catch (Exception ex)
        {

        }

        return rValue;
    }

    public static String dispLongComma(long inLong)
    {
        String rValue = "0";

        try
        {
            String inStr = String.valueOf(inLong);

            rValue = inStr.replaceAll("\\B(?=(\\d{3})+(?!\\d))", ",");
        }
        catch (Exception ex)
        {

        }

        return rValue;
    }

    public static String nullToString(String regex, String replacement) {
        if (regex == null || "".equals(regex) || "null".equals(regex)) {
            return replacement;
        }
        return regex;
    }

    public static int nullToInteger(String str, int i) {
        int value = 0;
        if ("".equals(nullToString(str,""))) {
            value = i;
        } else {
            try {
                value = Integer.parseInt(str);
            } catch (Exception e) {
            }
        }
        return value;
    }

    public static long nullToLong(String str, long i) {
        long value = 0;
        if ("".equals(nullToString(str,""))) {
            value = i;
        } else {
            try {
                value = Long.parseLong(str);
            } catch (Exception e) {
            }
        }
        return value;
    }

}
