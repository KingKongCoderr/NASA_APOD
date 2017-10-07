package jaihind.gobblessamerica.peerprogrammingretrofit.BroadcastService;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Calendar;

/**
 * Created by nande on 2/26/2017.
 */

public class BootService extends BroadcastReceiver {


    public Context mcontext;
    @Override
    public void onReceive(Context context, Intent intent) {
       /* SharedPreferences sharedPreferences=context.getSharedPreferences("name",context.MODE_PRIVATE);
        SharedPreferences.Editor meditor= sharedPreferences.edit() ;
        boolean checking=sharedPreferences.getBoolean("isnotificationset",false);
        meditor.putBoolean("isnotificationset",false);
        meditor.commit();*/
        Log.d("mwsu","inside bootservice");

       /* NotificationCompat.Builder noti= (NotificationCompat.Builder) new NotificationCompat.Builder(context).setSmallIcon(R.drawable.camera)
                .setDefaults(NotificationCompat.DEFAULT_VIBRATE).setContentTitle("hello booted")
                .setContentText("yeah!!!!!!!!!!!!!!");
        NotificationManager notificationManager= (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        notificationManager.notify(2,noti.build());*/
        mcontext=context;
        setNotification("rebooted");



        }
    private void setNotification(String message){
        Calendar calendar= Calendar.getInstance();
        // long trigger_time=calendar.getTimeInMillis()+(24*60)*60000;
        long trigger_time=calendar.getTimeInMillis()+15*1000;
        Intent notifictrigger_intent=new Intent(mcontext, AlertReceiver.class);
        notifictrigger_intent.putExtra("message",message);
        PendingIntent pendingIntent= PendingIntent.getBroadcast(mcontext,1,notifictrigger_intent,PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager=(AlarmManager)mcontext.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,trigger_time,AlarmManager.INTERVAL_DAY,pendingIntent);
       /* meditor.putBoolean("isnotificationset",true);
        meditor.commit();*/
    }
}
