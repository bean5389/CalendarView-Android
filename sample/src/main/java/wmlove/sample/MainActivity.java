package wmlove.sample;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;

import wmlove.library.CalendarView;
import wmlove.library.OnDaySelectListener;
import wmlove.library.OnTimeChangeListener;


public class MainActivity extends Activity {
    private CalendarView calendarview;
    private TextView mTextView;
    private Date mDate;
    final double[] t = {0,0};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        calendarview = (CalendarView) findViewById(R.id.calendarview);
        initView();

        mTextView = (TextView)findViewById(R.id.mTextView);

//        final ProgressDialog dialog = ProgressDialog.show(MainActivity.this, "请稍后", "加载中...");
//        new Thread(new Runnable() {
//
//            @Override
//            public void run() {
//                try {
//                    Thread.sleep(3000);
//                    initView();
//                    t[0] = Calendar.MILLISECOND;
//
//                }catch (Exception e){
//                    e.printStackTrace();
//                }finally {
//                    t[1] = Calendar.MILLISECOND;
//                    dialog.cancel();
//                }
//            }
//        }).start();
//
//        Log.i("Time",t[0]-t[1]+"");
    }

    private void initView() {

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
