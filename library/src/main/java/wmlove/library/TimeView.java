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
import java.util.Date;
import java.util.List;


/**
 * Created by Administrator on 2015/6/17.
 */
public class TimeView extends RelativeLayout implements View.OnClickListener{
    private static final int DEFAULT_DAY_IN_WEEK = 7;
    private Calendar mCalendarCurrent = null;
    private int WEEK_IN_MONTH;
    private final int DEFAULT_WEEK_IN_MONTH = 6;
    private List<DayView> dayViewList = new ArrayList<>();
    private int timeflag = Calendar.MONTH;
    private boolean showOtherDay = false;
    private OnDaySelectListener mDaySelectCallBack;
    private OnDaySelectChangeListener mOnDaySelectChangeListener;

    public void setOnDaySelectListener(OnDaySelectListener mDaySelectCallBack) {
        this.mDaySelectCallBack = mDaySelectCallBack;
    }

    public void setOnDaySelectChangeListener(OnDaySelectChangeListener onDaySelectChangeListener){
        this.mOnDaySelectChangeListener = onDaySelectChangeListener;
    }

    public TimeView(Context context,Calendar calendarCurrent,int timeflag) {
        super(context);
        /**肺都气炸了*/
        this.timeflag = timeflag;
        mCalendarCurrent = CalendarUtils.getCalenar();
        CalendarUtils.copyTo(calendarCurrent,mCalendarCurrent);

        WEEK_IN_MONTH = (timeflag==Calendar.MONTH) ? 6 : 1;
        showOtherDay = (timeflag==Calendar.WEEK_OF_YEAR) ? true : false;
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
        int size = SizeUtils.getSize(getContext());
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
                row.addView(day, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));
                dayViewList.add(day);
            }
            root.addView(row);
        }
        addView(root);
    }

    private void setDayView(int timeflag){
        Calendar calendar = getWorkingCalendar();
        for(DayView dayView : dayViewList){
            dayView.setTimeRange(calendar);
            dayView.setDay(showOtherDay, calendar.get(timeflag) == mCalendarCurrent.get(timeflag));
            calendar.add(Calendar.DAY_OF_MONTH,1);
            Log.i("day",String.valueOf(calendar.get(Calendar.DATE)));
        }
    }

    private Calendar getWorkingCalendar(){
        Calendar calendar = CalendarUtils.getCalenar();
        CalendarUtils.copyTo(mCalendarCurrent, calendar);
        Log.i("TAG","CalendarMonth"+calendar.get(Calendar.MONTH));
        Log.i("TAG1", "time1" +calendar.getTime());
//        Log.i("TAG", calendar.get(timeflag) + "month");
        if(timeflag==Calendar.MONTH){
            CalendarUtils.setToFirstDayInMonth(calendar);
        }
        CalendarUtils.setToFirstDayInWeek(calendar);
        Log.i("TAG1", "time3" +calendar.getTime());
        return calendar;
    }

    public Calendar getTimeCalendar(){
        Log.i("TAG",mCalendarCurrent.getTime()+"Timecurrent");
        return this.mCalendarCurrent;
    }

    public Date getDate(){
        return this.mCalendarCurrent.getTime();
    }

    public void setSelectColor(int color){
        for(DayView dayView : dayViewList){
            dayView.setSelectColor(color);
        }
    }

    public void setTimeFlag(int timeflag){
        this.timeflag = timeflag;
    }


    @Override
    public void onClick(View view) {
        String time = null;
        Date date = null;
        if(view instanceof DayView){
            clearSelected();
            DayView dayView = (DayView) view;
            dayView.setSelected(true);
            Toast.makeText(getContext(),dayView.getText(),Toast.LENGTH_SHORT).show();
            time = dayView.getTime();
            date = dayView.getDate();
            postInvalidate();
        }
        mDaySelectCallBack.OnDaySelect(time,date);
        mOnDaySelectChangeListener.SelectChangeCallBack();
    }

    public void clearSelected() {
        for(DayView other : dayViewList){
            other.setSelected(false);
        }
    }
}
