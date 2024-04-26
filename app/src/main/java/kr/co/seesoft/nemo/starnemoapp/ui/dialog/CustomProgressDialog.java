package kr.co.seesoft.nemo.starnemoapp.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Window;

import kr.co.seesoft.nemo.starnemoapp.R;


public class CustomProgressDialog extends Dialog {

    private Context c;

    public CustomProgressDialog(Context context){
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setCanceledOnTouchOutside(false);

        this.c = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dialog_progress);

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
