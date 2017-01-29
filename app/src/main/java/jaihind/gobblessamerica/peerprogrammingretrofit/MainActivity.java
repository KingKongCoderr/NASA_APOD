package jaihind.gobblessamerica.peerprogrammingretrofit;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.nfc.tech.NfcA;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.io.Serializable;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import io.realm.Sort;
import jaihind.gobblessamerica.peerprogrammingretrofit.Adapter.NasaAdapter;
import jaihind.gobblessamerica.peerprogrammingretrofit.Database.CupboardNasaSQLiteHelper;
import jaihind.gobblessamerica.peerprogrammingretrofit.Model.Nasa;
import jaihind.gobblessamerica.peerprogrammingretrofit.Network.NetworkManager;
import jaihind.gobblessamerica.peerprogrammingretrofit.Network.services.NasaService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static java.security.AccessController.getContext;


public class MainActivity extends AppCompatActivity implements Callback<Nasa> {

    private NetworkManager mNetworkManager;
    private RecyclerView mrecyclerView;
    private RecyclerView.LayoutManager mlayoutManager;
    private NasaAdapter mnasaAdapter;

    public  SharedPreferences.Editor meditor;

    String minimum_date="",maximum_date="";

    Calendar c=Calendar.getInstance();

    int month=c.get(Calendar.MONTH)+1,year=c.get(Calendar.YEAR),day=c.get(Calendar.DAY_OF_MONTH);

    public  SharedPreferences msharedPreferences;
    public int todays_date=0;
    public int stored_date=0;
    public int min_date=0;
    public boolean isInstanceThere=false,shouldFetch=true;
    //shouldDummy=true;



    //database
    /*public CupboardNasaSQLiteHelper dbhelper;
    public SQLiteDatabase db;*/
    private Realm mrealm;


    public List<Nasa> nasa_images;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            mrealm.init(getApplicationContext());
            mrealm= Realm.getDefaultInstance();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd");

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

            mrecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
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
            mrealm.commitTransaction();
        }
        return teams_list;
    }


    public List<Nasa> getlistdatafromdb(){
        List<Nasa> teams_list = new ArrayList<Nasa>();
        mrealm.beginTransaction();
        //RealmResults<Nasa> itr=mrealm.where(Nasa.class).findAll();
        RealmResults<Nasa> itr= mrealm.where(Nasa.class).findAllSorted("date", Sort.DESCENDING);


        maximum_date=itr.get(0).getDate();
        minimum_date=itr.get(itr.size()-1).getDate();
        int size=itr.size();

        try {
        for (Nasa nasa : itr) {
        Log.d("inside list for",""+nasa.getTitle());
        teams_list.add(new Nasa(nasa.getExplanation(),nasa.getHdurl(),nasa.getTitle(),nasa.getDate(),nasa.getCopyright()));
        }
        } catch (Exception e) {
        e.printStackTrace();
        } finally {

        mrealm.commitTransaction();

        }




        return teams_list;

    }
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




            }
            else{

                Log.e("Response Failure",response.code()+"");
            }
        }

        @Override
        public void onFailure(Call<Nasa> call, Throwable t) {

            if(t instanceof UnknownHostException){

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
        }
        return true;
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
        mnasaAdapter=new NasaAdapter(getlistdatafromdb(),this);
        mrecyclerView.setAdapter(mnasaAdapter);
        mnasaAdapter.notifyDataSetChanged();
       // mrecyclerView.setLayoutManager(mlayoutManager);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mrealm.close();
    }
}

