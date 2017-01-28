package jaihind.gobblessamerica.peerprogrammingretrofit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CalendarView;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CalendarActivity extends AppCompatActivity {
CalendarView mcalendarView;
    String minimum_date="",maximum_date="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        mcalendarView=(CalendarView)findViewById(R.id.calendarView);
        minimum_date=getIntent().getStringExtra("minimum_date");
        maximum_date=getIntent().getStringExtra("maximum_date");
        SimpleDateFormat format= new SimpleDateFormat("yyyy-mm-dd");
        long milli=0,milli1=0;
        try {
            Date dt=  format.parse(minimum_date);
            Date dt1= format.parse(maximum_date);
            milli= dt.getTime();
            milli1= dt1.getTime();
            //System.out.println(milli1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //long decrease_time= (java.util.Calendar.getInstance().getTimeInMillis()-((min_date)*86400000));
       final Intent date_intent=new Intent(this,MainActivity.class);
        mcalendarView.setMinDate(milli);
        mcalendarView.setMaxDate(milli1);

        mcalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                //Bundle args=new Bundle();
                //Calendar c=new Calendar(year,month,dayOfMonth);
                date_intent.putExtra("clicked_year",  year);
                date_intent.putExtra("clicked_month",  month+1);
                date_intent.putExtra("clicked_day",  dayOfMonth);
                //date_intent.putExtra("selected_date",args);
                setResult(2,date_intent);
                finish();
            }
        });

    }
    public class Calendar implements Serializable{
        int year;
        int month;
        int dayofMonth;

        public Calendar(int year, int month, int dayofMonth) {
            this.year = year;
            this.month = month;
            this.dayofMonth = dayofMonth;
        }
    }
}
