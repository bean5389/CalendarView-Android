package wmlove.sample;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import wmlove.library.CalendarUtils;
import wmlove.library.CalendarView;
import wmlove.library.OnDaySelectListener;
import wmlove.library.OnTimeChangeListener;


public class MainActivity extends Activity {
    private CalendarView calendarview1,calendarview2;
    private TextView dateTextView1,dateTextView2;
    private Date mDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dateTextView1 = (TextView)findViewById(R.id.dateTextView1);
        Date time = Calendar.getInstance().getTime();
        dateTextView1.setText(new SimpleDateFormat("MMMM").format(time));

        calendarview1 = (CalendarView) findViewById(R.id.calendarview1);
        calendarview1.setOnDaySelectListener(new OnDaySelectListener() {
            @Override
            public void OnDaySelect(String time, Date date) {
                String timez = new SimpleDateFormat("MMMMdd").format(date);
                dateTextView1.setText(timez + "日");
            }
        });
        calendarview1.setOnTimeChangeListener(new OnTimeChangeListener() {
            @Override
            public void OnTimeChange(Date currentDate) {
                mDate = currentDate;
                String time = new SimpleDateFormat("MMMM").format(mDate);
                dateTextView1.setText(time);
            }
        });

        calendarview1.setSelectColor(Color.BLUE);


        dateTextView2 = (TextView)findViewById(R.id.dateTextView2);
        Date time2 = Calendar.getInstance().getTime();
        dateTextView2.setText(new SimpleDateFormat("MMMM").format(time2));

        calendarview2 = (CalendarView) findViewById(R.id.calendarview2);
        calendarview2.setOnDaySelectListener(new OnDaySelectListener() {
            @Override
            public void OnDaySelect(String time, Date date) {
                String timez = new SimpleDateFormat("MMMMdd").format(date);
                dateTextView2.setText(timez + "日");
            }
        });
        calendarview2.setOnTimeChangeListener(new OnTimeChangeListener() {
            @Override
            public void OnTimeChange(Date currentDate) {
                mDate = currentDate;
                String time = new SimpleDateFormat("MMMM").format(mDate);
                dateTextView2.setText(time);
            }
        });

        calendarview2.setSelectColor(Color.BLUE);





    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
