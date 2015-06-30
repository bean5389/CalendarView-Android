package wmlove.sample;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import wmlove.library.CalendarView;
import wmlove.library.OnDaySelectListener;
import wmlove.library.OnTimeChangeListener;


public class MainActivity extends Activity {
    private CalendarView calendarview;
    private TextView mTextView;
    private Date mDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        calendarview = (CalendarView) findViewById(R.id.calendarview);
        calendarview.setOnDaySelectListener(new OnDaySelectListener() {
            @Override
            public void OnDaySelect(String time, Date date) {
                Log.i("MainActivity", "time=" + time + "date=" + date);
            }
        });
        calendarview.setOnTimeChangeListener(new OnTimeChangeListener() {
            @Override
            public void OnTimeChange(Date currentDate) {
                mDate = currentDate;
                String time = new SimpleDateFormat("dd MMMM").format(mDate);
                mTextView.setText(time);
            }
        });
        calendarview.setSelectColor(Color.BLUE);


        mTextView = (TextView)findViewById(R.id.mTextView);


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
