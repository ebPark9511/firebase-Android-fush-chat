package com.example.park.ststemparkbi.Chat;


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.park.ststemparkbi.AppManager;
import com.example.park.ststemparkbi.DBHelper;
import com.example.park.ststemparkbi.R;
import com.example.park.ststemparkbi.DisplayActivity.loding_Activity;
import com.google.firebase.messaging.RemoteMessage;


public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {
    private static final String TAG = "FirebaseMsgService";

    // [START receive_message]
    SharedPreferences setting;
    SharedPreferences.Editor editor;
    DBHelper dbHelper;
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        dbHelper = new DBHelper(getApplicationContext(), "SystemParkbi.db", null, 1);
        //추가한것
        sendNotification(remoteMessage.getData().get("message"), remoteMessage.getData().get("nick"));

        if(remoteMessage.getNotification() ==null){

            Log.e("getNotification()", "  " + remoteMessage.getData().get("nick"));
            Log.e("getNotification()", "  " + remoteMessage.getData().get("message"));
        }else{
            Log.e("else", "  " + remoteMessage.getNotification().getTitle());

        }

    }
    NotificationCompat.Builder notificationBuilder;
    private void sendNotification(String message , String nick) {
        Intent intent = new Intent(this, loding_Activity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);


        //채팅방에서는 푸쉬가 안뜨게함.

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        String msg = "";
        //안녕하세요반갑습니다저는박은비라고합니다.
        //21
        int j = 0;
        for(int i = 0; i< message.length() ; i++){
            msg += String.valueOf(message.charAt(i));
            j++;
            if(j == 15) {
                msg += "\n";
                j=0;
            }
        }

        dbHelper.insert(nick, msg, 0);
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if(Chat_Activity.Chat_Activity != null) {
            ((Chat_Activity) Chat_Activity.Chat_Activity).AdapterreSet();
        }

        if(AppManager.getInstance().getChat_Actvity()){

        }else {
            notificationBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.ic_five_img)
                    .setContentTitle(nick + "님의 메세지")
                    .setContentText(message)
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri);
            notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
        }




    }

}