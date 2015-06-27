package wmlove.library;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/**
 * Created by Administrator on 2015/6/17.
 */
public class TimeView extends RelativeLayout implements View.OnClickListener{
    private static final int DEFAULT_DAY_IN_WEEK = 7;
    private int WEEK_IN_MONTH;
    private final int DEFAULT_WEEK_IN_MONTH = 6;
    private List<DayView> dayViewList = new ArrayList<>();
    private int time;
    private int timeflag = Calendar.MONTH;
    private boolean showotherday = false;
    private OnDaySelectListener mDaySelectCallBack;
    private TimeChangeListener mTimeChangeListener;

    private class TimeChangeListener implements OnTimeChangeListener{

        @Override
        public void OnTimeChange(int timeflag) {
            WEEK_IN_MONTH = (timeflag==Calendar.MONTH) ? 6 : 1;
            int timefla = timeflag;
            setDayView(timefla);
            postInvalidate();
        }
    }

    public void setDaySelectCallBack(OnDaySelectListener mDaySelectCallBack) {
        this.mDaySelectCallBack = mDaySelectCallBack;
    }

    public TimeView(Context context,int time,int timeflag) {
        super(context);
        WEEK_IN_MONTH = (timeflag==Calendar.MONTH) ? 6 : 1;
        showotherday = (timeflag==Calendar.WEEK_OF_YEAR) ? true : false;
        mTimeChangeListener = new TimeChangeListener();
        this.time = time;
        setUpView();
        setDayView(timeflag);
    }

    public TimeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TimeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private LinearLayout makeRow(){
        LinearLayout row = new LinearLayout(getContext());
        row.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,0,1f);
        row.setLayoutParams(params);
        return row;
    }

    private void setUpView() {
        LinearLayout root = new LinearLayout(getContext());
        root.setOrientation(LinearLayout.VERTICAL);
        LinearLayout row;
        for(int i=0;i<WEEK_IN_MONTH;i++){
            row = makeRow();
            for(int x=0;x<DEFAULT_DAY_IN_WEEK;x++){
                DayView day = new DayView(getContext());
                day.setOnClickListener(this);
                row.addView(day, new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1f));
                dayViewList.add(day);
            }
            root.addView(row);
        }
        addView(root, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));
    }

    private void setDayView(int timeflag){
        Calendar calendar = getWorkingCalendar();
        for(DayView dayView : dayViewList){
            dayView.setTimeRange(calendar);
            dayView.setDay(showotherday,calendar.get(timeflag)==time);
            calendar.add(Calendar.DATE,1);
        }
    }

    private Calendar getWorkingCalendar(){
        Calendar calendar = CalendarUtils.getCalenar();
        calendar.set(timeflag, time);
        Log.i("TAG", "time" +calendar.getTime());
        Log.i("TAG", calendar.get(timeflag) + "month");
        if(timeflag==Calendar.MONTH){CalendarUtils.setToFirstDayInMonth(calendar);}
        CalendarUtils.setToFirstDayInWeek(calendar);
        return calendar;
    }

    public int getTime(){
        return this.time;
    }

    public void setTimeFlag(int timeflag){
        this.timeflag = timeflag;
    }

    public OnTimeChangeListener getOnTimeChangeListener(){
        return mTimeChangeListener;
    }


    @Override
    public void onClick(View view) {
        Log.i("TAG", "Child");
        if(view instanceof DayView){
            clearSelected();
            DayView dayView = (DayView) view;
            dayView.setSelected(true);
            Toast.makeText(getContext(),dayView.getText(),Toast.LENGTH_SHORT).show();
            postInvalidate();
        }
        mDaySelectCallBack.SelectCallBack(TimeView.this);
    }

    public void clearSelected() {
        for(DayView other : dayViewList){
            other.setSelected(false);
        }
    }
}
