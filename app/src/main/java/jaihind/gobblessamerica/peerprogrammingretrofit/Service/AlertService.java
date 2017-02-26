package jaihind.gobblessamerica.peerprogrammingretrofit.Service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Calendar;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;
import jaihind.gobblessamerica.peerprogrammingretrofit.MainActivity;
import jaihind.gobblessamerica.peerprogrammingretrofit.Model.Nasa;
import jaihind.gobblessamerica.peerprogrammingretrofit.Network.NetworkManager;
import jaihind.gobblessamerica.peerprogrammingretrofit.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by nande on 2/21/2017.
 */

public class AlertService extends BroadcastReceiver implements Callback<Nasa>{

    private NetworkManager networkManager;
    Realm mrealm;
    String todays_title="";
    Context mcontext;
    @Override
    public void onReceive(Context context, Intent intent) {

        networkManager=new NetworkManager();
        mcontext=context;
        mrealm.init(context);
        mrealm= Realm.getDefaultInstance();
        fetchData();


    }
    private void fetchData() {
        Call<Nasa> apiCall= networkManager.getNasaService().gettodayPicture("IhPSRcqHVZ0WDgWwGAUrUOTGSBTz2fCiN8Pd3dwc");
        apiCall.enqueue(this);

    }

    @Override
    public void onResponse(Call<Nasa> call, Response<Nasa> response) {
        if (response.isSuccessful()) {
            Log.d("Inside", "inside notification response");
            mrealm.beginTransaction();
            Nasa alert_obj = response.body();
            todays_title=alert_obj.getTitle();
            mrealm.commitTransaction();

            Intent notification_intent=new Intent(mcontext, MainActivity.class);

            Uri notification_sound= Uri.parse("android.resource://" + mcontext.getPackageName() + "/"+R.raw.rocketlaunch);


            PendingIntent pendingIntent2=PendingIntent.getActivity(mcontext,1,notification_intent,PendingIntent.FLAG_UPDATE_CURRENT);

            NotificationCompat.Builder notification_builder= (NotificationCompat.Builder) new NotificationCompat.Builder(mcontext).
                    setContentTitle("New NASA image available").setContentText(todays_title)
                    .setSmallIcon(R.drawable.rocket)
                    .setSound(notification_sound)
                    .setLights(0,191,255)
                    .setDefaults(NotificationCompat.DEFAULT_VIBRATE)
                    .setContentIntent(pendingIntent2)
                    .setAutoCancel(true);

            NotificationManager noti_manager=(NotificationManager)mcontext.getSystemService(mcontext.NOTIFICATION_SERVICE);
            noti_manager.notify(1,notification_builder.build());
        } else {
            Log.e("Response Failure",response.code()+"");
        }
    }

    @Override
    public void onFailure(Call<Nasa> call, Throwable t) {

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
}
