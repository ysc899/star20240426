package kr.co.seesoft.nemo.starnemoapp.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CalendarView;

import com.kyanogen.signatureview.SignatureView;

import java.io.ByteArrayOutputStream;
import java.util.Date;

import kr.co.seesoft.nemo.starnemoapp.R;
import kr.co.seesoft.nemo.starnemoapp.util.AndroidUtil;
import kr.co.seesoft.nemo.starnemoapp.util.Const;


public class SignPadDialog extends Dialog {

    private Context c;
    private CalendarView calendarView;
    private Handler handler;
    private Date date = null;

    private Button btnConfirm, btnCancel;

    SignatureView signatureView;

    public SignPadDialog(Context context, Handler handler){
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setCanceledOnTouchOutside(true);

        this.c = context;
        this.handler = handler;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dialog_sign_pad);
        initUI();
        init();
    }

    private void initUI(){

        btnConfirm = findViewById(R.id.btn_confirm);
        btnCancel = findViewById(R.id.btn_cancel);

        signatureView = (SignatureView) findViewById(R.id.signature_view);

    }
    private  void init(){

        btnCancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                signatureView.clearCanvas();

                Message msg = new Message();
                msg.what = Const.HANDLER_SIGN_CANCEL;

                handler.sendMessage(msg);

                dismiss();
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {


                if(!signatureView.isBitmapEmpty())
                {
                    Bitmap bitmap = signatureView.getSignatureBitmap();

                    ByteArrayOutputStream outArray = new ByteArrayOutputStream();

                    bitmap.compress(Bitmap.CompressFormat.PNG,100,outArray);

                    Message msg = new Message();
                    msg.what = Const.HANDLER_SIGN;
                    msg.obj = outArray.toByteArray();

                    handler.sendMessage(msg);

                    signatureView.clearCanvas();

                    dismiss();
                }
                else
                {
                    AndroidUtil.toast(getContext(),"사인후 전자 서명 가능 합니다.");
                }



            }
        });


    }


    @Override
    public void show() {
        super.show();
    }

    public void show(Date d){
        this.date = d;
        super.show();

    }

    @Override
    public void dismiss() {
        super.dismiss();
    }
}
