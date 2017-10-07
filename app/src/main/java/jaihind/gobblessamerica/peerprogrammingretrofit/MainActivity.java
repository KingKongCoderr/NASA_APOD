package jaihind.gobblessamerica.peerprogrammingretrofit;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;
import jaihind.gobblessamerica.peerprogrammingretrofit.Adapter.NasaAdapter;
import jaihind.gobblessamerica.peerprogrammingretrofit.Model.Nasa;
import jaihind.gobblessamerica.peerprogrammingretrofit.Network.NetworkManager;

import jaihind.gobblessamerica.peerprogrammingretrofit.BroadcastService.AlertReceiver;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity implements Callback<Nasa> {

    private NetworkManager mNetworkManager;
    private RecyclerView mrecyclerView;
    private RecyclerView.LayoutManager mlayoutManager;
    private NasaAdapter mnasaAdapter;
    //ZoomControls mzoom_bt;
   // HorizontalScrollView mhorizontal_sv;


    public  SharedPreferences.Editor meditor;

    String minimum_date="",maximum_date="";

    Calendar c=Calendar.getInstance();

    int month=c.get(Calendar.MONTH)+1,year=c.get(Calendar.YEAR),day=c.get(Calendar.DAY_OF_MONTH);

    public  SharedPreferences msharedPreferences;
    public int todays_date=0;
    public int stored_date=0;
    public int min_date=0;
    public boolean isInstanceThere=false,shouldFetch=true,isNotificationset=false;
    //shouldDummy=true;





    //database
    /*public CupboardNasaSQLiteHelper dbhelper;
    public SQLiteDatabase db;*/
    private Realm mrealm;
    public List<Nasa> nasa_images;
/************************PINCH IN ZOOM VARIABLES **************************************/
    /*View mainView = null;
    // Remember some things for zooming
    PointF start = new PointF();
    PointF mid = new PointF();

    float oldDist = 1f;
    PointF oldDistPoint = new PointF();

    public static String TAG = "ZOOM";

    static final int NONE = 0;
    static final int DRAG = 1;
    static final int ZOOM = 2;
    int mode = NONE;
*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            mrealm.init(getApplicationContext());
        //mainView=(LinearLayout)findViewById(R.id.mainfragment_container);
            mrealm= Realm.getDefaultInstance();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd");
           //mhorizontal_sv=(HorizontalScrollView)findViewById(R.id.horizontal_sv);



        mrecyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        final float org_x=mrecyclerView.getScaleX(),org_y=mrecyclerView.getScaleY();



       /* mzoom_bt=(ZoomControls)findViewById(R.id.zoom_bt);
        mzoom_bt.show();
            mzoom_bt.setIsZoomOutEnabled(false);
            final float zoomFactor = 1.5f;
            mzoom_bt.setOnZoomInClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    float x = mrecyclerView.getScaleX();
                    float y = mrecyclerView.getScaleY();
                    if(x>(org_x+5)){
                        mzoom_bt.setIsZoomInEnabled(false);
                    }
                    else {
                        mrecyclerView.setPivotX(0);
                        mrecyclerView.setPivotY(0);
                        mrecyclerView.setScaleX((float) (x + 1));
                        mrecyclerView.setScaleY((float) (y + 1));
                        mzoom_bt.setIsZoomOutEnabled(true);
                    }

                    //zoom(2f, 2f, new PointF(0, 0));
                }
            });
            mzoom_bt.setOnZoomOutClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    float x = mrecyclerView.getScaleX();
                    float y = mrecyclerView.getScaleY();
                    if(x>org_x) {
                        mrecyclerView.setPivotX(0);
                        mrecyclerView.setPivotY(0);
                        mrecyclerView.setScaleX((float) (x - 1));
                        mrecyclerView.setScaleY((float) (y - 1));
                        mzoom_bt.setIsZoomInEnabled(true);
                    }else{
                        mzoom_bt.setIsZoomOutEnabled(false);
                    }


                   // zoom(0.5f, 0.5f, new PointF(0, 0));
                }
            });*/



       /* View v = findViewById(R.id.mainfragment_container); // get reference to root activity view
        v.setOnClickListener(new View.OnClickListener() {
            float zoomFactor = 1.5f;
            boolean zoomedOut = false;

            @Override
            public void onClick(View v) {
                if(zoomedOut) {
                    // now zoom in
                    v.setScaleX(1);
                    v.setScaleY(1);
                    zoomedOut = false;
                }
                else {
                    v.setScaleX(zoomFactor);
                    v.setScaleY(zoomFactor);
                    zoomedOut = true;
                }
            }
        });*/

            long milli, milli1;
            try {
                Date dt = format.parse("2017-01-27");
                milli = dt.getTime();
                milli1 = Calendar.getInstance().getTimeInMillis();
                System.out.println(milli1);
            } catch (ParseException e) {
                e.printStackTrace();
            }



            msharedPreferences = getPreferences(Context.MODE_PRIVATE);
            meditor = msharedPreferences.edit();
            meditor.putLong("mindate", System.currentTimeMillis());
         isNotificationset= msharedPreferences.getBoolean("isnotificationset",false);
            // meditor.commit();
            Log.d("time", msharedPreferences.getLong("mindate", (long) 0) + "");
            mNetworkManager = new NetworkManager();

            shouldFetch = msharedPreferences.getBoolean("shouldfetch", true);
            stored_date = msharedPreferences.getInt("date", day);
            //shouldDummy = msharedPreferences.getBoolean("dummy", true);
            todays_date = day;


            //min_date=1;
            nasa_images = new ArrayList<>();
            if (savedInstanceState == null) {
                isInstanceThere = true;
            }

            if (mrecyclerView != null) {
                mrecyclerView.setHasFixedSize(true);
            }
            mlayoutManager = new LinearLayoutManager(this);
            mrecyclerView.setLayoutManager(mlayoutManager);


           /* if (shouldDummy) {
                crud();
            }*/


            if (!((todays_date - stored_date) == 0)) {
                //Log.d("Fetch","Inside fetch");
                if (isOnline()) {
                    fetchData();
                }else {
                    Toast.makeText(this, "No network", Toast.LENGTH_SHORT).show();
                }

            } else {
                if (shouldFetch) {
                    if (isOnline()) {
                        fetchData();
                    }
                    else {
                        Toast.makeText(this, "No network", Toast.LENGTH_SHORT).show();
                    }
                } else {

                    // getImages();
                    // getlistdatafromdb();
                    callListFrag();
                    //callListFrag();
                }

            }






    }


 /*   *//**
     * zooming is done from here
     *//*
    public void zoom(Float scaleX, Float scaleY, PointF pivot) {
        mainView.setPivotX(pivot.x);
        mainView.setPivotY(pivot.y);
        mainView.setScaleX(scaleX);
        mainView.setScaleY(scaleY);
    }

    *//**
     * space between the first two fingers
     *//*
    private float spacing(MotionEvent event) {
        // ...
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float)Math.sqrt(x * x + y * y);
    }

    private PointF spacingPoint(MotionEvent event) {
        PointF f = new PointF();
        f.x = event.getX(0) - event.getX(1);
        f.y = event.getY(0) - event.getY(1);
        return f;
    }

    *//**
     * the mid point of the first two fingers
     *//*
    private void midPoint(PointF point, MotionEvent event) {
        // ...
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x / 2, y / 2);
    }*/

    /*private void crud() {
        mrealm.beginTransaction();
        //RealmResults<Nasa> lessthan= mrealm.where(Nasa.class).findAllSorted("date", Sort.DESCENDING);

        Nasa obj1[]=new Nasa[]{new Nasa("asdfffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff",
                "http://www.jqueryscript.net/images/Simplest-Responsive-jQuery-Image-Lightbox-Plugin-simple-lightbox.jpg",
                "Dummy data 1","2017-01-25"),new Nasa("io[[n[[bpihhhhhhhhhhhhhhppppppoooooooooooooooooooooooooooooooooooooooooooooooooooohoih",
                "https://s3-us-west-1.amazonaws.com/powr/defaults/image-slider2.jpg",
                "Dummy Data 2","2017-01-24"), new Nasa("io[[n[[bpihhhhhhhhhhhhhhppppppoooooooooooooooooooooooooooooooooooooooooooooooooooohoih",
                "http://www.falconflooringct.com/product_images/5840f3ba7c84a.jpg",
                "Dummy Data 3","2017-01-21")};


        for (int i=0;i<obj1.length;i++){
            Nasa obj= mrealm.createObject(Nasa.class);
            obj.setExplanation(obj1[i].getExplanation());
            obj.setHdurl(obj1[i].getHdurl());
            obj.setTitle(obj1[i].getTitle());
            obj.setDate(obj1[i].getDate());
          // String title=obj.getTitle();
        }
        mrealm.commitTransaction();
        meditor.putBoolean("dummy",false);
        meditor.commit();
    }*/

    public void getImages(){
        mnasaAdapter=new NasaAdapter(getdatafromdb(year,month,day),this);
       // mnasaAdapter.notifyDataSetChanged();
        mrecyclerView.setAdapter(mnasaAdapter);
        //mlayoutManager=new LinearLayoutManager(this);
        //mrecyclerView.setLayoutManager(mlayoutManager);

    }

    public List<Nasa> getdatafromdb(int lyear,int lmonth,int lday){

        List<Nasa> teams_list = new ArrayList<Nasa>();
        mrealm.beginTransaction();
        RealmResults<Nasa> itr=mrealm.where(Nasa.class).findAll();
        try {
            for (Nasa nasa : itr) {
                String dates[]=nasa.getDate().split("-");
                if(Integer.parseInt(dates[0])==lyear && Integer.parseInt(dates[1])==(lmonth)
                        && Integer.parseInt(dates[2])==lday){
                    Log.d("yahoo","inside if");
                teams_list.add(new Nasa(nasa.getExplanation(),nasa.getHdurl(),nasa.getTitle(),nasa.getDate(),nasa.getCopyright()));}
                else{

                }
        }

            if (teams_list.size()==0){
                Toast.makeText(this, "No data Available", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
           /* if(teams_list.size()>0){
                for(int i=teams_list.size()-1;i>0;i--){
                 teams_list.remove(i);}
            }*/
            mrealm.commitTransaction();
        }
        return teams_list;
    }


    public List<Nasa> getlistdatafromdb(){
        List<Nasa> teams_list = new ArrayList<Nasa>();
        mrealm.beginTransaction();
        //RealmResults<Nasa> itr=mrealm.where(Nasa.class).findAll();
        RealmResults<Nasa> itr= mrealm.where(Nasa.class).findAllSorted("date", Sort.DESCENDING);//.distinct("date");

        maximum_date=itr.get(0).getDate();
        minimum_date=itr.get(itr.size()-1).getDate();
        int size=itr.size();

        try {
        for (Nasa nasa : itr) {
            String date= nasa.getDate();
        Log.d("inside list for",""+nasa.getTitle());
            Nasa add_object=new Nasa(nasa.getExplanation(),nasa.getHdurl(),nasa.getTitle(),nasa.getDate(),nasa.getCopyright());
                teams_list.add(add_object);
        }
        } catch (Exception e) {
        e.printStackTrace();
        } finally {
        mrealm.commitTransaction();
        }
        return teams_list;
    }
/*
    public List<Nasa> getdatedatafromdb(int lyear,int lmonth,int lday){
        List<Nasa> single_list= new ArrayList<Nasa>();

    }*/

  private void fetchData() {
     Call<Nasa> apiCall= mNetworkManager.getNasaService().gettodayPicture("IhPSRcqHVZ0WDgWwGAUrUOTGSBTz2fCiN8Pd3dwc");
        apiCall.enqueue(this);
    }


    @Override
    public void onResponse(Call<Nasa> call, Response<Nasa> response) {


            if(response.isSuccessful()){
    Log.d("Inside","inside response");
                mrealm.beginTransaction();
                Nasa obj=response.body();
                Nasa obj3=mrealm.createObject(Nasa.class);

                obj3.setExplanation(obj.getExplanation());
                obj3.setHdurl(obj.getHdurl());
                obj3.setTitle(obj.getTitle());
                obj3.setDate(obj.getDate());
                obj3.setCopyright(obj.getCopyright());
                String copyri=obj.getCopyright();
                mrealm.commitTransaction();
                meditor.putBoolean("shouldfetch",false);
                int date= c.get(Calendar.DAY_OF_MONTH);
                meditor.putInt("date",date);
                meditor.commit();
               // getImages();
                callListFrag();

                if(!(isNotificationset)) {
                    setNotification(obj.getTitle());
                }



            }
            else{

                Log.e("Response Failure",response.code()+"");
            }
        }

        @Override
        public void onFailure(Call<Nasa> call, Throwable t)
        {

            if(t instanceof UnknownHostException)
            {
                Log.e("On Failure","No network"+t.getMessage());
            }else{
                if(t instanceof SocketTimeoutException){
                    Log.e("on Failure","Timeout"+t.getMessage());
                }
                else{
                    Log.e("on Failure","error"+t.getMessage());
                }

            }

        }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }
    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) this.getSystemService(this.CONNECTIVITY_SERVICE);

        return (cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnectedOrConnecting());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        switch (id){
            case R.id.date_selection:callDateFrag();break;
            case R.id.list_selection:callListFrag();break;
            case R.id.submit_apod:callPhotoActivity();break;
            case R.id.about:showaboutdialog();break;
        }
        return true;
    }

    private void setNotification(String message){
        Calendar calendar= Calendar.getInstance();
        // long trigger_time=calendar.getTimeInMillis()+(24*60)*60000;
        long trigger_time=calendar.getTimeInMillis()+15*1000;
        Intent notifictrigger_intent=new Intent(this, AlertReceiver.class);
        notifictrigger_intent.putExtra("message",message);
        PendingIntent pendingIntent= PendingIntent.getBroadcast(this,1,notifictrigger_intent,PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager=(AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,trigger_time,AlarmManager.INTERVAL_DAY,pendingIntent);
       /* meditor.putBoolean("isnotificationset",true);
        meditor.commit();*/
    }
    private void callPhotoActivity(){
        Intent photo_intent=new Intent(this,PhotoActivity.class);
        startActivity(photo_intent);

    }
    private void showaboutdialog() {

        FragmentManager frag_manager=getSupportFragmentManager();
        AboutDialog dialog_frag=new AboutDialog();
        dialog_frag.show(frag_manager,"About dialog");
    }

    private void callDateFrag() {
        Intent calendar_intent=new Intent(this,CalendarActivity.class);
        calendar_intent.putExtra("minimum_date",minimum_date);
        calendar_intent.putExtra("maximum_date",maximum_date);

        startActivityForResult(calendar_intent,1);

        /*mdatepicker.init(year, month-1, day, new DatePicker.OnDateChangedListener() {
                    @Override
                    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        mnasaAdapter=new NasaAdapter(getdatafromdb(year,monthOfYear+1,dayOfMonth),getApplicationContext());
                        mnasaAdapter.notifyDataSetChanged();
                        mrecyclerView.setAdapter(mnasaAdapter);
                        mlayoutManager=new LinearLayoutManager(getApplicationContext());
                        mrecyclerView.setLayoutManager(mlayoutManager);
                        Log.d("click",year+"");Log.d("click",monthOfYear+"");Log.d("click",dayOfMonth+"");
                        Log.d("click","todays"+todays_date);
                        mdatepicker.setVisibility(View.INVISIBLE);
                    }
                });*/


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode==2){
           // Bundle args=data.getBundleExtra("selected_date");
          //  CalendarActivity.Calendar c= (CalendarActivity.Calendar) args.getSerializable("calendar");
           // Log.d("intent_data","year"+c.year+"month"+c.month+"day"+c.dayofMonth);
            int clicked_year=data.getIntExtra("clicked_year",2017),clicked_month=data.getIntExtra("clicked_month",1),
                    clicked_day=data.getIntExtra("clicked_day",1);
            mnasaAdapter=new NasaAdapter(getdatafromdb(clicked_year,clicked_month,clicked_day),getApplicationContext());
            //mnasaAdapter.notifyDataSetChanged();
            mrecyclerView.setAdapter(mnasaAdapter);
            //mlayoutManager=new LinearLayoutManager(getApplicationContext());
          //  mrecyclerView.setLayoutManager(mlayoutManager);

        }
    }

    private void callListFrag() {
        if(isDataEmpty()==false) {
            mnasaAdapter = new NasaAdapter(getlistdatafromdb(), this);
            mrecyclerView.setAdapter(mnasaAdapter);
            mnasaAdapter.notifyDataSetChanged();
            // mrecyclerView.setLayoutManager(mlayoutManager);
        }
        else{
            Toast.makeText(this, "No Saved Data", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mrealm.close();
    }

    public boolean isDataEmpty(){
        if(mrealm.isEmpty()){
            return true;
        }
        else
            return false;
    }


}

