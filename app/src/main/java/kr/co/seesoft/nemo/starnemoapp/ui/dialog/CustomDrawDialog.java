package kr.co.seesoft.nemo.starnemoapp.ui.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.BlurMaskFilter;
import android.graphics.EmbossMaskFilter;
import android.graphics.MaskFilter;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import java.io.File;

import kr.co.seesoft.nemo.starnemoapp.R;
import kr.co.seesoft.nemo.starnemoapp.ui.component.DrawView;


public class CustomDrawDialog extends DialogFragment {

    private Handler handler;

    //그리기 관련
    private FrameLayout flDrawLayout;
    private DrawView drawView;
    private String title;
    private File  file;
    private int mode;

//    public CustomDrawDialog(Handler handler) {
//        this.handler = handler;
//    }

    public static CustomDrawDialog newInstance(String title, Handler h, File f, int mode){
        CustomDrawDialog dialog = new CustomDrawDialog();
        dialog.handler = h;
        dialog.title = title;
        dialog.file = f;
        dialog.mode = mode;
        return dialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AlertDialog);

        LayoutInflater inflater = requireActivity().getLayoutInflater();

        View v = inflater.inflate(R.layout.dialog_draw, null);
        initUI(v);
        init();


        setCancelable(false);
         AlertDialog dialog = builder.setTitle(title)
                 .setCancelable(false)
                 .setView(v)
                 .setPositiveButton("확인", null)
                 .setNeutralButton("초기화", null)
                 .setNegativeButton("취소", null)
                 .create();


         dialog.setOnShowListener(new DialogInterface.OnShowListener() {
             @Override
             public void onShow(DialogInterface dialogInterface) {

                 ((AlertDialog)dialogInterface).getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                     @Override
                     public void onClick(View view) {
                         boolean isSave = drawView.saveDraw(file);

                         if(isSave){
                            String filePath = file.getAbsolutePath();
                            Message msg = new Message();
                            msg.what = mode;
                            msg.obj = filePath;
                            handler.sendMessage(msg);

                            dialogInterface.dismiss();
                         }
                     }
                 });
                 ((AlertDialog)dialogInterface).getButton(AlertDialog.BUTTON_NEUTRAL).setOnClickListener(new View.OnClickListener() {
                     @Override
                     public void onClick(View view) {
                         drawView.setDrawClear();

                     }
                 });
             }
         });

        return dialog;
    }



    private void initUI(View v){
        flDrawLayout = (FrameLayout) v.findViewById(R.id.flDraw);
    }
    private  void init(){

        //그리기 초기화 관련
        Paint mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(0xFF000000);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(5);
        MaskFilter mEmboss = new EmbossMaskFilter(new float[] {1,1,1}, 0.4f, 6, 3.5f);
        MaskFilter mBlur = new BlurMaskFilter(8, BlurMaskFilter.Blur.NORMAL);
        drawView = new DrawView(getContext(), mPaint);

        if(file != null && file.length() > 0){
            drawView.setBackground(Drawable.createFromPath(file.getAbsolutePath()));
//            drawView.setBackground(file);
            drawView.setOldDraw();

        }

        flDrawLayout.addView(drawView);

    }





    @Override
    public void dismiss() {
        super.dismiss();
    }
}
