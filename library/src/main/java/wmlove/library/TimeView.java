package wmlove.library;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Administrator on 2015/6/17.
 */
public class TimeView extends RelativeLayout{
    private static final int DEFAULT_DAY_IN_WEEK = 7;
    private final int DEFAULT_ROW_HEIGHT = 50;
    private final int DEFAULT_WEEK_IN_MONTH = 1;
    private List<DayView> dayViewList = new ArrayList<>();
    private int month;

    public TimeView(Context context) {
        super(context);
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
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        row.setLayoutParams(params);
        return row;
    }

    private void setUpView() {
        LinearLayout root = new LinearLayout(getContext());
        root.setOrientation(LinearLayout.HORIZONTAL);
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        root.setLayoutParams(params);

        LinearLayout row;
        for(int i=0;i<DEFAULT_WEEK_IN_MONTH;i++){
            row = makeRow();
            for(int x=0;x<DEFAULT_DAY_IN_WEEK;x++){
                DayView day = new DayView(getContext());
                row.addView(day,new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 1f));
                dayViewList.add(day);
            }
            root.addView(row);
        }
        addView(root);
    }

    private void setDayView(){
        Calendar calendar = CalendarUtils.getCalenar();
        calendar.set(Calendar.MONTH,month);
        CalendarUtils.setToFirstDay(calendar);
        for(DayView dayView : dayViewList){
            dayView.setDay(calendar.get(Calendar.DAY_OF_MONTH));
            calendar.add(Calendar.DATE,1);
        }
    }

    public int getMonth(){
        return this.month;
    }

    public void setMonth(int month) {
        this.month = month;
    }
}
