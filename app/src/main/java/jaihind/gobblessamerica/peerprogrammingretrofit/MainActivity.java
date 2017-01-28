package jaihind.gobblessamerica.peerprogrammingretrofit;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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


public class MainActivity extends AppCompatActivity implements Callback<Nasa> {

    private NetworkManager mNetworkManager;
    private RecyclerView mrecyclerView;
    private RecyclerView.LayoutManager mlayoutManager;
    private NasaAdapter mnasaAdapter;

    public  SharedPreferences.Editor meditor;


    Calendar c=Calendar.getInstance();

    int month=c.get(Calendar.MONTH)+1,year=c.get(Calendar.YEAR),day=c.get(Calendar.DAY_OF_MONTH);

    public  SharedPreferences msharedPreferences;
    public int todays_date=0;
    public int stored_date=0;
    public int min_date=0;
    public boolean isInstanceThere=false,shouldFetch=true;



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

        long time=Calendar.getInstance().getTimeInMillis();
        long time2=Calendar.getInstance().getTimeInMillis();
        long time3=Calendar.getInstance().getTimeInMillis();


        msharedPreferences=getPreferences(Context.MODE_PRIVATE);
           meditor=msharedPreferences.edit();
            meditor.putLong("mindate",System.currentTimeMillis());
           // meditor.commit();
            Log.d("time",msharedPreferences.getLong("mindate",(long)0)+"");
            mNetworkManager=new NetworkManager();

            shouldFetch=msharedPreferences.getBoolean("shouldfetch",true);
            stored_date=   msharedPreferences.getInt("date",0);
            todays_date= day;
            min_date=Math.abs(stored_date-todays_date);

            //min_date=1;
            nasa_images=new ArrayList<>();
            if(savedInstanceState == null) {
                isInstanceThere=true;
            }

            mrecyclerView=(RecyclerView)findViewById(R.id.recycler_view);
            if (mrecyclerView != null) {
                mrecyclerView.setHasFixedSize(true);
            }

        crud();

            if(!((todays_date-stored_date)==0)) {
                //Log.d("Fetch","Inside fetch");
                    fetchData();
            }else {
              if(shouldFetch){
                    fetchData();
                }
                else{

                    getImages();
                }

            }





    }

    private void crud() {
        mrealm.beginTransaction();
        RealmResults<Nasa> lessthan= mrealm.where(Nasa.class).findAllSorted("date", Sort.DESCENDING);

        for (int i=0;i<lessthan.size();i++){
            Nasa obj= lessthan.get(i);
           String title=obj.getTitle();
        }
        mrealm.commitTransaction();
    }

    public void getImages(){
        mnasaAdapter=new NasaAdapter(getdatafromdb(year,month,day),this);
        mrecyclerView.setAdapter(mnasaAdapter);
        mlayoutManager=new LinearLayoutManager(this);
        mrecyclerView.setLayoutManager(mlayoutManager);

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
                teams_list.add(new Nasa(nasa.getExplanation(),nasa.getHdurl(),nasa.getTitle(),nasa.getDate()));}
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



        try {
        for (Nasa nasa : itr) {
        Log.d("inside list for",""+nasa.getTitle());
        teams_list.add(new Nasa(nasa.getExplanation(),nasa.getHdurl(),nasa.getTitle(),nasa.getDate()));
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

                Nasa obj1=new Nasa("asdfffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff",
                        "http://www.jqueryscript.net/images/Simplest-Responsive-jQuery-Image-Lightbox-Plugin-simple-lightbox.jpg",
                        "Dummy data 1","2017-01-25");
                Nasa obj2= new Nasa("io[[n[[bpihhhhhhhhhhhhhhppppppoooooooooooooooooooooooooooooooooooooooooooooooooooohoih",
                        "https://s3-us-west-1.amazonaws.com/powr/defaults/image-slider2.jpg",
                        "Dummy Data 2","2017-01-24");

                Nasa obj3=mrealm.createObject(Nasa.class);
                obj3.setExplanation(obj.getExplanation());obj3.setHdurl(obj.getHdurl());obj3.setTitle(obj.getTitle());obj3.setDate(obj.getDate());
                mrealm.commitTransaction();
                meditor.putBoolean("shouldfetch",false);
                int date= c.get(Calendar.DAY_OF_MONTH);
                meditor.putInt("date",date);
                meditor.commit();
                getImages();




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
        calendar_intent.putExtra("minimum_date",min_date);
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
            mnasaAdapter.notifyDataSetChanged();
            mrecyclerView.setAdapter(mnasaAdapter);
            mlayoutManager=new LinearLayoutManager(getApplicationContext());
            mrecyclerView.setLayoutManager(mlayoutManager);

        }
    }

    private void callListFrag() {
        mnasaAdapter=new NasaAdapter(getlistdatafromdb(),this);
        mrecyclerView.setAdapter(mnasaAdapter);
        mnasaAdapter.notifyDataSetChanged();
        mlayoutManager=new LinearLayoutManager(this);
        mrecyclerView.setLayoutManager(mlayoutManager);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mrealm.close();
    }
}

