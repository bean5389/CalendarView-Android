package wmlove.library;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by Administrator on 2015/6/17.
 */
public class CalendarView extends RelativeLayout implements View.OnTouchListener,AbsListView.OnScrollListener{
    private ViewPager mViewPager;
    private CalendarViewAdapter mCalendarViewAdapter;
    private GestureDetector mScrollGestureDetector;
    private DaySelectListener mDaySelectListener = new DaySelectListener();

    private boolean inweek = false;
    private int timeflag ;
    private final int DEFAULT_FLING_MIN_DISTANCE = 100;
    private final int DEFAULT_FLING_MIN_VELOCITY= 100;

    private List<TimeView> timeViewList = new ArrayList<>();
    private List<OnTimeChangeListener> OnTimeChangeListenerList = new ArrayList<>();



    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
//        if(motionEvent.getAction()==MotionEvent.ACTION_DOWN){
//            Log.i("TAG", "timechange");
//            inweek = !inweek;
//            timeflag = getTimeFlag(inweek);
//            for(OnTimeChangeListener listener : OnTimeChangeListenerList){
//                listener.OnTimeChange(timeflag);
//            }
//
//        }

        return mScrollGestureDetector.onTouchEvent(motionEvent);
    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int i) {

    }

    @Override
    public void onScroll(AbsListView absListView, int i, int i1, int i2) {

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }


    public int getTimeFlag(boolean inweek) {
        return inweek ? Calendar.WEEK_OF_YEAR : Calendar.MONTH;
    }

    private class DaySelectListener implements OnDaySelectListener {

        @Override
        public void SelectCallBack(TimeView timeView) {
            for (TimeView other : timeViewList) {
                if (other!=timeView){other.clearSelected();}
            }
//            Log.i("TAG", "timechange");
//            timeViewList.clear();
//            addTimeView();
//            inweek = false;
//            timeflag = getTimeFlag(inweek);
//            for(OnTimeChangeListener listener : OnTimeChangeListenerList){
//                listener.OnTimeChange(timeflag);
//            }
//            mViewPager.setCurrentItem(getIndex());
        }
    }

    public CalendarView(Context context) {
        super(context);
    }

    public CalendarView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray t = context.obtainStyledAttributes(attrs,R.styleable.CalendarView);
        inweek = t.getBoolean(R.styleable.CalendarView_viewinview,false);
        t.recycle();

        mScrollGestureDetector = new GestureDetector(context,new ScrollGestureDetectorListener());

        timeflag = getTimeFlag(inweek);

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
//        mViewPager.setOnTouchListener(this);
        mCalendarViewAdapter = new CalendarViewAdapter();
        mViewPager.setAdapter(mCalendarViewAdapter);
        mViewPager.setCurrentItem(getIndex());
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        root.addView(mViewPager, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        addView(root);
    }


    public CalendarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setViewInWeek(boolean viewInWeek){
        inweek = viewInWeek;
        timeflag = getTimeFlag(inweek);
        postInvalidate();
    }

    private void addTimeView(){
        Calendar c = CalendarUtils.getCalenar();
        int min;
        int max;
        c.add(timeflag,-5);
        min = c.get(timeflag);
        c.add(timeflag,10);
        max = c.get(timeflag);
        Calendar current = CalendarUtils.getCalenar();
        current.set(timeflag, min);
        OnTimeChangeListener listener;
        while (current.get(timeflag)<=max){
            TimeView timeView = new TimeView(getContext(),current.get(timeflag),timeflag);
            timeView.setDaySelectCallBack(mDaySelectListener);
            listener = timeView.getOnTimeChangeListener();
            OnTimeChangeListenerList.add(listener);
            Log.i("TAG", current.get(timeflag) + "current");
            timeViewList.add(timeView);
            current.add(timeflag,1);
        }
    }

    private int getIndex(){
        Calendar c = CalendarUtils.getCalenar();
        for(int i=0;i<timeViewList.size();i++){
            TimeView timeView = timeViewList.get(i);
            if(c.get(timeflag)==timeView.getTime()){
                return i;
            }
        }
        return 0;
    }

    private class ScrollGestureDetectorListener extends GestureDetector.SimpleOnGestureListener{
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if(e1.getY() - e2.getY() >= DEFAULT_FLING_MIN_DISTANCE && Math.abs(velocityY) >= DEFAULT_FLING_MIN_VELOCITY){
                Log.i("TAG", "timechange");
                inweek = true;
                timeflag = getTimeFlag(inweek);
                for(OnTimeChangeListener listener : OnTimeChangeListenerList){
                    listener.OnTimeChange(timeflag);
                }
            }else if(e2.getY() - e1.getY() >= DEFAULT_FLING_MIN_DISTANCE && Math.abs(velocityY) >= DEFAULT_FLING_MIN_VELOCITY){
                Log.i("TAG", "timechange");
                inweek = false;
                timeflag = getTimeFlag(inweek);
                for(OnTimeChangeListener listener : OnTimeChangeListenerList){
                    listener.OnTimeChange(timeflag);
                }
            }
            mViewPager.invalidate();
            postInvalidate();
            return true;
        }
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
