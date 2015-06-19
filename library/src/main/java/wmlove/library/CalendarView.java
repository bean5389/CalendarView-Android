package wmlove.library;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
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
public class CalendarView extends RelativeLayout implements View.OnClickListener{
    private ViewPager mViewPager;
    private CalendarViewAdapter mCalendarViewAdapter;
    private List<TimeView> timeViewList = new ArrayList<>();
    private DaySelectListener mDaySelectListener = new DaySelectListener();

    private class DaySelectListener implements OnDaySelectListener {

        @Override
        public void SelectCallBack(TimeView timeView) {
            for (TimeView other : timeViewList) {
                if (other!=timeView){other.clearSelected();}
            }
        }
    }

    public CalendarView(Context context) {
        super(context);
    }

    public CalendarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LinearLayout root = new LinearLayout(context);
        root.setOrientation(LinearLayout.VERTICAL);

        addTimeView();

        LinearLayout title = new LinearLayout(context);
        title.setOrientation(LinearLayout.HORIZONTAL);
        Calendar calendar = CalendarUtils.getCalenar();
        CalendarUtils.setToFirstDayInWeek(calendar);
        SimpleDateFormat format = new SimpleDateFormat("EEE", Locale.ENGLISH);
        for(int i=0;i<7;i++){
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
        Log.i("TAG", title.getChildCount()+"childcount");
        root.addView(title);

        mViewPager = new ViewPager(context);
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
        root.addView(mViewPager,new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        addView(root);
    }

    public CalendarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void addTimeView(){
        Calendar c = CalendarUtils.getCalenar();
        int min;
        int max;
        c.add(Calendar.MONTH,-5);
        min = c.get(Calendar.MONTH);
        c.add(Calendar.MONTH,10);
        max = c.get(Calendar.MONTH);
        Calendar current = CalendarUtils.getCalenar();
        current.set(Calendar.MONTH, min);
        while (current.get(Calendar.MONTH)<=max){
            TimeView timeView = new TimeView(getContext(),current.get(Calendar.MONTH));
            timeView.setOnClickListener(this);
            timeView.setDaySelectCallBack(mDaySelectListener);
            Log.i("TAG", current.get(Calendar.MONTH) + "current");
            timeViewList.add(timeView);
            current.add(Calendar.MONTH,1);
        }
    }

    private int getIndex(){
        Calendar c = CalendarUtils.getCalenar();
        for(int i=0;i<timeViewList.size();i++){
            TimeView timeView = timeViewList.get(i);
            if(c.get(Calendar.MONTH)==timeView.getMonth()){
                return i;
            }
        }
        return 0;
    }

    @Override
    public void onClick(View view) {
        Log.i("TAG", "Container");
        if(view instanceof TimeView) {

            TimeView timeView = (TimeView) view;
            timeView.onClick(view);
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
