package wmlove.library;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Administrator on 2015/6/17.
 */
public class CalendarView extends RelativeLayout {

    /**
     * callback when dayview is selected
     */
    private DaySelectChangeListener mDaySelectChangeListener = new DaySelectChangeListener();

    /**
     * callback when current view change
     */
    private OnTimeChangeListener mOnTimeChangeListener = null;

    /**
     * container viewpager
     */
    private ViewPager mViewPager;

    /**
     * viewpager adapter
     */
    private CalendarViewAdapter mCalendarViewAdapter;

    /**
     * is calendar shown in weekview
     */
    private boolean inweek = false;

    /**
     * time flag
     */
    private int TimeFlag ;

    /**
     * content view
     */
    private List<TimeView> timeViewList = new ArrayList<>();

    /**
     * max calendar
     */
    private Calendar mCalendarMax = null;

    /**
     * min calendar
     */
    private Calendar mCalendarMin = null;

    /**
     * current calendar
     */
    private Calendar mCalendarCurrent = null;

    private List<DaySelectChangeListener> OnTimeChangeListenerList = new ArrayList<>();

    private class DaySelectChangeListener implements OnDaySelectChangeListener {

        @Override
        public void SelectChangeCallBack() {
            TimeView current = timeViewList.get(mViewPager.getCurrentItem());
            for (TimeView other : timeViewList) {
                if (other!=current){other.clearSelected();}
            }
        }
    }

    public CalendarView(Context context,boolean inweek) {
        super(context,null);
    }

    public CalendarView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray t = context.obtainStyledAttributes(attrs,R.styleable.CalendarView);
        inweek = t.getBoolean(R.styleable.CalendarView_viewinview,false);
        t.recycle();

        TimeFlag = getTimeFlag(inweek);

        LinearLayout root = new LinearLayout(context);
        root.setOrientation(LinearLayout.VERTICAL);

        addTimeView();

        LinearLayout title = new LinearLayout(context);
        title.setOrientation(LinearLayout.HORIZONTAL);
        Calendar calendar = CalendarUtils.getCalenar();
        CalendarUtils.setToFirstDayInWeek(calendar);
        SimpleDateFormat format = new SimpleDateFormat("EEE", Locale.ENGLISH);
        for (int i = 0; i < 7; i++) {
            TextView textView = new TextView(context);
            textView.setWidth(150);
            textView.setHeight(150);
            textView.setGravity(Gravity.CENTER);
            textView.setTextAlignment(TEXT_ALIGNMENT_CENTER);
            textView.setTextColor(Color.BLACK);
            textView.setText(format.format(calendar.getTime()));
            Log.i("TAG", format.format(calendar.getTime()));
            calendar.add(Calendar.DATE, 1);
            title.addView(textView);
        }
        Log.i("TAG", title.getChildCount() + "childcount");
        root.addView(title);

        mViewPager = new ViewPager(context);
        mCalendarViewAdapter = new CalendarViewAdapter();
        mViewPager.setAdapter(mCalendarViewAdapter);
        mViewPager.setCurrentItem(getIndex());
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                TimeView timeView = timeViewList.get(position);
                Date date = timeView.getDate();
                mOnTimeChangeListener.OnTimeChange(date);
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });

        root.addView(mViewPager, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        addView(root);
    }

    public void setOnTimeChangeListener(OnTimeChangeListener listener){
        if(listener!=null){
            this.mOnTimeChangeListener = listener;
        }
        else throw new NullPointerException("OnTimeChangeListener can not be null!");
    }

    public void setOnDaySelectListener(OnDaySelectListener listener){
        if(listener!=null){
            for(TimeView timeView : timeViewList){
                timeView.setOnDaySelectListener(listener);
            }
        }
        else throw new NullPointerException("OnDaySelectListener can not be null!");
    }

    public void setMaxDate(Calendar calendarMax){
        this.mCalendarMax = calendarMax;
        postInvalidate();
    }

    public void setMinDate(Calendar calendarMin){
        this.mCalendarMin = calendarMin;
        postInvalidate();
    }

    public void setSelectColor(int color){
        for(TimeView timeView : timeViewList){
            timeView.setSelectColor(color);
        }
    }

    public void setCurrentCalendar(Calendar currentCalendar){
        this.mCalendarCurrent = currentCalendar;
        postInvalidate();
    }
    
    private Calendar getMinCalendarDay(){
        if(mCalendarMin==null){
            int mindate = -2;
            Calendar min = CalendarUtils.getCalenar();
            min.add(Calendar.YEAR, mindate);
            return min;
        }
        return mCalendarMin;
    }

    private Calendar getMaxCalendarDay(){
        if(mCalendarMax==null){
            int maxdate = 2;
            Calendar max = CalendarUtils.getCalenar();
            max.add(Calendar.YEAR,maxdate);
            return max;
        }
        return mCalendarMax;
    }

    public int getTimeFlag(boolean inweek) {
        return inweek ? Calendar.WEEK_OF_YEAR : Calendar.MONTH;
    }

    private void addTimeView(){
        Calendar minCalendar = getMinCalendarDay();
        Calendar maxCalendar = getMaxCalendarDay();
        if(mCalendarCurrent==null){
            mCalendarCurrent = CalendarUtils.getCalenar();
        }
        CalendarUtils.copyTo(minCalendar,mCalendarCurrent);
        Log.i("TAG",minCalendar.getTime()+"minCalendar"+maxCalendar.getTime()+"maxCalendar"+mCalendarCurrent.getTime()+"mCalendarCurrent");
        while (CalendarUtils.isBefore(mCalendarCurrent, maxCalendar)){
            TimeView timeView = new TimeView(getContext(),mCalendarCurrent,TimeFlag);
            timeView.setOnDaySelectChangeListener(mDaySelectChangeListener);
            timeViewList.add(timeView);
            Log.i("TAG", mCalendarCurrent.get(Calendar.MONTH) + "");
            mCalendarCurrent.add(TimeFlag, 1);
        }
    }

    private int getIndex(){
        Calendar c = CalendarUtils.getCalenar();
        for(int i=0;i<timeViewList.size();i++){
            TimeView timeView = timeViewList.get(i);
            Calendar other = timeView.getTimeCalendar();
            if(c.get(Calendar.YEAR)==other.get(Calendar.YEAR)
                    &&c.get(Calendar.MONTH)==other.get(Calendar.MONTH)){
                Log.i("TAG",i+"i");
                        if(TimeFlag==Calendar.WEEK_OF_YEAR){
                            if(c.get(Calendar.WEEK_OF_YEAR)==other.get(Calendar.WEEK_OF_YEAR)){return i;}
                        }
                return i;
            }
        }
        return 0;
    }

    private class CalendarViewAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            return timeViewList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object o) {
            return view == o;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            TimeView timeView = timeViewList.get(position);
            Log.i("TAG",timeViewList.size()+"size");
            container.addView(timeView);
            return timeViewList.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(timeViewList.get(position));
        }
    }
}
