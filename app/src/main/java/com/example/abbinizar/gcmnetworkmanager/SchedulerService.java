package com.example.abbinizar.gcmnetworkmanager;

import android.app.NotificationManager;
import android.app.job.JobParameters;
import android.content.Context;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.GcmTaskService;
import com.google.android.gms.gcm.TaskParams;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.SyncHttpClient;

import org.json.JSONObject;

import java.text.DecimalFormat;

import cz.msebera.android.httpclient.Header;

public class SchedulerService extends GcmTaskService {
    public static final String TAG = "GetWeather";
    private final String APP_ID = "MASUKKAN API KAMU";
    private final String CITY = "MASUKKAN NAMA KOTA KAMU";
    public static String TAG_TASK_WEATHER_LOG = "WeatherTask";

    @Override
    public int onRunTask(TaskParams taskParams) {
        int result = 0;
        if (taskParams.getTag().equals(TAG_TASK_WEATHER_LOG)){
            getCurrentWeather();
            result = GcmNetworkManager.RESULT_SUCCESS;
        }
        return result;
    }
    private void getCurrentWeather() {
        Log.d(TAG, "Running");
        SyncHttpClient client = new SyncHttpClient();
        String url = "http://api.openweathermap.org/data/2.5/weather?q="+CITY+"&appid="+APP_ID;
        Log.e(TAG, "getCurrentWeather: "+url);
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String result = new String(responseBody);
                Log.d(TAG, result);
                try {
                    JSONObject responseObject = new JSONObject(result);
                    String CurrentWheater = responseObject.getJSONArray("Wheater").getJSONObject(0).getString("main");
                    String Description = responseObject.getJSONArray("wheater").getJSONObject(0).getString("Description");
                    double tempInKlvin = responseObject.getJSONArray("main").getDouble(Integer.parseInt("temp"));
                    double tempInCelcius = tempInKlvin-273;
                    String temperature = new DecimalFormat("##.##").format(tempInCelcius);
                    String title = "Current Weather";
                    String message = CurrentWheater + ", "+ Description+"with"+temperature+"celcius";
                    int notifId = 100;
                    showNotification(getApplicationContext(), title, message, notifId);

                } catch (Exception e) {
                    e.printStackTrace();
                }   
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("GetWeather", "Failed");
            }
        });
    }
    public void onInitializeTasks() {
        super.onInitializeTasks();
        ScheduleTask mSchedulerTask = new ScheduleTask(this);
        mSchedulerTask.createPeriodicTask();
    }
    private void showNotification(Context context, String title, String message, int notifId) {
        NotificationManager notificationManagerCompat = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setContentTitle(title)
                .setSmallIcon(R.drawable.ic_replay)
                .setContentText(message)
                .setColor(ContextCompat.getColor(context, android.R.color.black))
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setSound(alarmSound);
        notificationManagerCompat.notify(notifId, builder.build());
    }

}
