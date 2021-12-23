package kr.co.seesoft.nemo.starnemo.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import kr.co.seesoft.nemo.starnemo.R;


public class CustomPhotoThumDialog extends Dialog {

    private Context c;
    private ImageView iv;

    public CustomPhotoThumDialog(Context context){
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setCanceledOnTouchOutside(true);

        this.c = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dialog_photo_thum);
        iv = (ImageView) findViewById(R.id.ivDialogPhotoThum);
        iv.setOnClickListener(view -> {dismiss();});
    }


    @Override
    public void show() {
        super.show();
    }

    public void show(Uri uri){

        this.show();


        Glide.with(c)
//                .asBitmap()
                .load(uri)
                .fitCenter()
                .into(iv);

        new Handler(Looper.getMainLooper()).postDelayed(() -> {dismiss();}, 1000);
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }

}
