package kr.co.seesoft.nemo.starnemoapp.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kyanogen.signatureview.SignatureView;

import java.io.ByteArrayOutputStream;
import java.util.Date;

import kr.co.seesoft.nemo.starnemoapp.R;
import kr.co.seesoft.nemo.starnemoapp.util.AndroidUtil;
import kr.co.seesoft.nemo.starnemoapp.util.Const;


public class ConfirmDialog extends Dialog {

    private Context c;
    private CalendarView calendarView;
    private Handler handler;

    private Button btnConfirm, btnCancel;
    private TextView tvMsg;

    private Object returnObj;

    public ConfirmDialog(Context context, Handler handler){
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setCanceledOnTouchOutside(true);

        this.c = context;
        this.handler = handler;

        returnObj = null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dialog_confirm);
        initUI();
        init();
    }

    private void initUI(){

        tvMsg = findViewById(R.id.tvMsg);

        btnConfirm = findViewById(R.id.btn_confirm);
        btnCancel = findViewById(R.id.btn_cancel);

    }
    private  void init(){

        btnCancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                Message msg = new Message();
                msg.what = Const.HANDLER_CONFIRM;
                msg.obj = returnObj;

                handler.sendMessage(msg);

                dismiss();
            }
        });


    }

    public void setMsg(String msg)
    {
        tvMsg.setText(msg);
    }

    public void setReturnObj(Object msg)
    {
        returnObj = msg;
    }

    public void setBtnCancelGone()
    {
        btnCancel.setVisibility(View.GONE);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
        params.weight = 1.0f;
        params.gravity = Gravity.CENTER;

        btnConfirm.setLayoutParams(params);
    }


    @Override
    public void show() {
        super.show();
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }
}
