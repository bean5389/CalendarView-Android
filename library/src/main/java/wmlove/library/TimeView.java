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

    /**
     * default day int a week
     */
    private static final int DEFAULT_DAY_IN_WEEK = 7;

    /**
     * week in a month
     */
    private int WEEK_IN_MONTH;

    /**
     *
     */
    private int mTimeflag;

    /**
     * is show this other day in this month view
     */
    private boolean showOtherDay;

    /**
     * default week in a month
     */
    private final int DEFAULT_WEEK_IN_MONTH = 6;

    /**
     *
     */
    private List<DayView> dayViewList;

    /**
     * current calendar
     */
    private Calendar mCalendarCurrent = null;

    /**
     * call back when dayview is selected
     */
    private OnDaySelectListener mDaySelectCallBack;

    /**
     * call back when dayview change selected state
     */
    private OnDaySelectChangeListener mOnDaySelectChangeListener;


    public TimeView(Context context,Calendar calendarCurrent,int timeflag) {
        super(context);
        /**肺都气炸了*/
        mCalendarCurrent = CalendarUtils.getCalenar();
        CalendarUtils.copyTo(calendarCurrent, mCalendarCurrent);

        showOtherDay = false;
        mTimeflag = timeflag;
        dayViewList = new ArrayList<>();

        WEEK_IN_MONTH = (mTimeflag==Calendar.MONTH) ? 6 : 1;
        Log.i("TAG1",WEEK_IN_MONTH+"");
        showOtherDay = (mTimeflag==Calendar.WEEK_OF_YEAR) ? true : false;
        Log.i("TAG1",showOtherDay+"");
        setUpView();
        setDayView(mTimeflag);
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
            dayView.setDay(showOtherDay, calendar.get(timeflag) == mCalendarCurrent.get(timeflag));
            calendar.add(Calendar.DATE, 1);
            Log.i("Date",calendar.getTime()+"");
        }
    }

    private Calendar getWorkingCalendar(){
        Calendar calendar = CalendarUtils.getCalenar();
        CalendarUtils.copyTo(mCalendarCurrent, calendar);
        Log.i("TAG","CalendarMonth" + calendar.get(Calendar.MONTH));
        Log.i("TAG", "time" +calendar.getTime());
        Log.i("TAG", calendar.get(mTimeflag) + "month");
        if(mTimeflag==calendar.MONTH){CalendarUtils.setToFirstDayInMonth(calendar);}
        CalendarUtils.setToFirstDayInWeek(calendar);
        Log.i("TAG", "time" + calendar.getTime());
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
        this.mTimeflag = timeflag;
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

    public void setOnDaySelectListener(OnDaySelectListener mDaySelectCallBack) {
        this.mDaySelectCallBack = mDaySelectCallBack;
    }

    public void setOnDaySelectChangeListener(OnDaySelectChangeListener onDaySelectChangeListener){
        this.mOnDaySelectChangeListener = onDaySelectChangeListener;
    }
}
