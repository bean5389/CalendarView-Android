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
    private final int DEFAULT_WEEK_IN_MONTH = 6;
    private List<DayView> dayViewList = new ArrayList<>();
    private int month;
    private boolean showotherday = false;
    private OnDaySelectListener mDaySelectCallBack;

    public void setDaySelectCallBack(OnDaySelectListener mDaySelectCallBack) {
        this.mDaySelectCallBack = mDaySelectCallBack;
    }

    public TimeView(Context context,int month) {
        super(context);
        this.month = month;
        setUpView();
        setDayView();
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
        for(int i=0;i<DEFAULT_WEEK_IN_MONTH;i++){
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

    private void setDayView(){
        Calendar calendar = CalendarUtils.getCalenar();
        calendar.set(Calendar.MONTH, month);
        Log.i("TAG", calendar.get(Calendar.MONTH)+"month");
        CalendarUtils.setToFirstDayInMonth(calendar);
        CalendarUtils.setToFirstDayInWeek(calendar);
        for(DayView dayView : dayViewList){
            dayView.setTimeRange(month);
            dayView.setDay(calendar.get(Calendar.DAY_OF_MONTH),showotherday,calendar.get(Calendar.MONTH)==month);
            calendar.add(Calendar.DATE,1);
        }
    }

    public int getMonth(){
        return this.month;
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
