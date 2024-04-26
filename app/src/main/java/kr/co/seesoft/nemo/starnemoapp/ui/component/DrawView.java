package kr.co.seesoft.nemo.starnemoapp.ui.component;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import kr.co.seesoft.nemo.starnemoapp.util.AndroidUtil;

public class DrawView extends View {

    private static final float MINP = 0.25f;
    private static final float MAXP = 0.75f;
    private Bitmap mBitmap;
    private Canvas mCanvas;
    private Paint mPaint;
    private Path mPath;
    private Paint mBitmapPaint;

    private boolean oldDraw = false;
    private int onOldDrawCount = 0;


    private List<Path> drawPathList = new ArrayList<>();

    public DrawView(Context c) {
        super(c);
        mPath = new Path();
        mBitmapPaint = new Paint(Paint.DITHER_FLAG);
    }
    public DrawView(Context c, Paint mPaint) {
        super(c);
        mPath = new Path();
        mBitmapPaint = new Paint(Paint.DITHER_FLAG);
        this.mPaint = mPaint;
    }

    public DrawView(Context c , AttributeSet att){
        super(c , att);
        mPath = new Path();
        mBitmapPaint = new Paint(Paint.DITHER_FLAG);

    }

    public DrawView(Context c , AttributeSet att , int ref){
        super(c , att , ref);
        mPath = new Path();
        mBitmapPaint = new Paint(Paint.DITHER_FLAG);
    }
    public DrawView(Context c , AttributeSet att , int ref, Paint mPaint){
        super(c , att , ref);
        mPath = new Path();
        mBitmapPaint = new Paint(Paint.DITHER_FLAG);
        this.mPaint = mPaint;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);

    }
    @Override
    protected void onDraw(Canvas canvas) {

        if(oldDraw){
            if(onOldDrawCount== 0){
                onOldDrawCount++;
            }else if(onOldDrawCount == 1) {
                this.setBackgroundColor(Color.parseColor("#F4EFF4"));
                oldDraw = false;
            }
        }

        canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);

        for(Path item : drawPathList){
            canvas.drawPath(item, mPaint);
        }


    }
    private float mX, mY;
    private static final float TOUCH_TOLERANCE = 4;
    private void touch_start(float x, float y) {
        Path tempPath = new Path();
        drawPathList.add(tempPath);

        mPath = tempPath;

        mPath.reset();
        mPath.moveTo(x, y);
        mX = x;
        mY = y;
    }
    private void touch_move(float x, float y) {
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            mPath.quadTo(mX, mY, (x + mX)/2, (y + mY)/2);
            mX = x;
            mY = y;
        }
    }
    private void touch_up() {
        mPath.lineTo(mX, mY);
    }

    public void setOldDraw(){
        this.oldDraw = true;
    }

    /**
     * 그리기 초기화
     */
    public void setDrawClear() {
        this.setBackgroundColor(Color.parseColor("#F4EFF4"));
        this.oldDraw = false;
        drawPathList.clear();
        drawPathList = null;
        drawPathList = new ArrayList<>();
        invalidate();
    }

    /**
     * 그리기 이전
     */
    public void setDrawUndo(){

        drawPathList.remove(drawPathList.size()-1);
        invalidate();

    }

    /**
     * @return 그린 횟수 리턴
     */
    public int getDrawSize(){
        return drawPathList.size();
    }

    public boolean saveDraw(File file){

        if(!oldDraw) {
            if (getDrawSize() == 0) {
                AndroidUtil.toast(getContext(), "사인이 없습니다.");
                return false;
            }
        }

        if(file != null || file.length() > 0){
            file.delete();
        }


        Bitmap b = Bitmap.createBitmap(this.getWidth(), this.getHeight(), Bitmap.Config.ARGB_8888 );
        b.eraseColor(Color.WHITE);

        Canvas c = new Canvas(b);
        this.draw(c);

        try(FileOutputStream fos = new FileOutputStream(file)) {
            b.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();

        } catch (IOException e) {
            AndroidUtil.log("IOException", e);
//            e.printStackTrace();
            return false;
        }

        b.recycle();

        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touch_start(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                touch_move(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                touch_up();
                invalidate();
                break;
        }
        return true;
    }


}
