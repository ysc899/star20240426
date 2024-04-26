package kr.co.seesoft.nemo.starnemoapp.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;
import android.widget.CalendarView;

import androidx.annotation.NonNull;

import java.util.Date;

import kr.co.seesoft.nemo.starnemoapp.R;
import kr.co.seesoft.nemo.starnemoapp.util.Const;
import kr.co.seesoft.nemo.starnemoapp.util.DateUtil;


public class CustomCalendarDialog extends Dialog {

    private Context c;
    private CalendarView calendarView;
    private Handler handler;
    private Date date = null;

    public CustomCalendarDialog(Context context, Handler handler){
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

        setContentView(R.layout.dialog_calendar);
        initUI();
        init();
    }

    private void initUI(){
        calendarView = (CalendarView)findViewById(R.id.cvPopupCalendar);
    }
    private void init(){

        if(date != null){
            calendarView.setDate(date.getTime());
        }

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
                i1++;
                Message msg = new Message();
                msg.what = Const.HANDLER_CALENDAR;
                msg.obj = DateUtil.getDate(i + "-"+ i1 + "-"+ i2, "yyyy-MM-dd");

                handler.sendMessage(msg);
                dismiss();

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
