package wmlove.library;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Administrator on 2015/6/17.
 */
public class CalendarView extends RelativeLayout{
    private ViewPager mViewPager;
    private CalendarViewAdapter mCalendarViewAdapter;
    private List<TimeView> timeViewList = new ArrayList<>();

    public CalendarView(Context context) {
        super(context);

    }

    public CalendarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        addTimeView();
        mViewPager = new ViewPager(context);
        mCalendarViewAdapter = new CalendarViewAdapter();
        mViewPager.setAdapter(mCalendarViewAdapter);
        mViewPager.setCurrentItem(getIndex());
        addView(mViewPager);
        TextView t = new TextView(context);
        t.setText("das qwd");
        addView(t);
    }

    public CalendarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void addTimeView(){
        Calendar c = CalendarUtils.getCalenar();
        int min;
        int max;
        c.add(Calendar.MONTH,-1);
        min = c.get(Calendar.MONTH);
        c.add(Calendar.MONTH,2);
        max = c.get(Calendar.MONTH);
        Calendar current = CalendarUtils.getCalenar();
        current.set(Calendar.MONTH, min);
        while (current.get(Calendar.MONTH)<=max){
            TimeView timeView = new TimeView(getContext());
            timeView.setMonth(current.get(Calendar.MONTH));
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
            return timeView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);
        }
    }
}
