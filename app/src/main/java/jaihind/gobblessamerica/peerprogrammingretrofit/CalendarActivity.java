package jaihind.gobblessamerica.peerprogrammingretrofit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CalendarView;

import java.io.Serializable;
import java.util.Calendar;

public class CalendarActivity extends AppCompatActivity {
CalendarView mcalendarView;
    int min_date=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        mcalendarView=(CalendarView)findViewById(R.id.calendarView);
        min_date=getIntent().getIntExtra("minimum_date",0);
        long decrease_time= (java.util.Calendar.getInstance().getTimeInMillis()-((min_date+1)*86400000));
       final Intent date_intent=new Intent(this,MainActivity.class);
        mcalendarView.setMinDate(decrease_time);
        mcalendarView.setMaxDate(java.util.Calendar.getInstance().getTimeInMillis());

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
