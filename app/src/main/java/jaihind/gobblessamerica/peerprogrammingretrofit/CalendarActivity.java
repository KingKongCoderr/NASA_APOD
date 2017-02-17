package jaihind.gobblessamerica.peerprogrammingretrofit;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;
import android.widget.TextView;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CalendarActivity extends AppCompatActivity {
CalendarView mcalendarView;
    String minimum_date="",maximum_date="";
    TextView mtv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        mtv=(TextView)findViewById(R.id.calendarView_tv);
        mcalendarView=(CalendarView)findViewById(R.id.calendarView);
        if (isOnline()) {
            minimum_date=getIntent().getStringExtra("minimum_date");
            maximum_date=getIntent().getStringExtra("maximum_date");
           // minimum_date=getMonthformated(minimum_date);
           // maximum_date=getMonthformated(maximum_date);
            SimpleDateFormat format= new SimpleDateFormat("yyyy-MM-dd");
            long milli=0,milli1=0;
            try {
                Date dt=  format.parse(minimum_date);
                Date dt1= format.parse(maximum_date);
                milli= dt.getTime();
                milli1= dt1.getTime();
                System.out.println(milli1);
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
        else {
            mcalendarView.setVisibility(View.INVISIBLE);
            mtv.setText("Unable to load information; check your internet to load todays image or click the list button in the main menu to retreive any stored information");
        }


    }

    private String getMonthformated(String pdate) {
        String date="";
        String split[]=pdate.split("-");
        date+=split[0]+"-";
        int month=Integer.parseInt(split[1]);
        date+=month+"-";
        date+=split[2];
        return date;
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

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) this.getSystemService(this.CONNECTIVITY_SERVICE);

        return (cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnectedOrConnecting());
    }
}
