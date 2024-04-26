package kr.co.seesoft.nemo.starnemoapp.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

import kr.co.seesoft.nemo.starnemoapp.R;


public class CustomIngProgressDialog extends Dialog {

    private Context c;
    private TextView tv;

    public CustomIngProgressDialog(Context context){
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setCanceledOnTouchOutside(false);

        this.c = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dialog_ing_progress);
        tv = (TextView) findViewById(R.id.tvImgProgress);
        tv.setText("진행중 입니다.");

    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }

    @Override
    public void show() {
        super.show();
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }

    public void updateText(final String text){
        this.tv.setText(text);
    }
}
