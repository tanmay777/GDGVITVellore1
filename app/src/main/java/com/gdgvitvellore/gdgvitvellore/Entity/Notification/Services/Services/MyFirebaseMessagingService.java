package com.gdgvitvellore.gdgvitvellore.Entity.Notification.Services.Services;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;

import com.gdgvitvellore.gdgvitvellore.Entity.Notification.Services.app.Config;
import com.gdgvitvellore.gdgvitvellore.Entity.Notification.Services.utils.NotificationUtils;
import com.gdgvitvellore.gdgvitvellore.Entity.Navigation.HomeActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by tanmay jha on 31-12-2016.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG=MyFirebaseMessagingService.class.getSimpleName();
    private NotificationUtils notificationUtils;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.e(TAG,"FROM: "+remoteMessage.getFrom());
        //check if message contains a notification payload
        if(remoteMessage.getNotification()!=null){
            Log.e(TAG,"Notification body:"+remoteMessage.getNotification().getBody());
            handleNotification(remoteMessage.getNotification().getBody(),remoteMessage.getNotification().getTitle(),remoteMessage.getData().get("image"));
            Log.v("MessagingService",remoteMessage.getNotification().getTitle());
        }

        if(remoteMessage.getData().size()>0){
            Log.v("Messaging Service","Check1");
            handleFirebaseDataMessage(remoteMessage);
        }
        //check if message contains a data payload
        /*if(remoteMessage.getData().size()>0){
            Log.e(TAG,"data Payload:"+remoteMessage.getData().toString());
            try{
                JSONObject json= new JSONObject(remoteMessage.getData().toString());
                handleDataMessage(json,remoteMessage);
            }catch (Exception e)
            {
                Log.e(TAG,"Exception: "+e.getMessage());
            }
        }*/

        /*Above logic will be used if backend would have been integerated*/
    }

    private void handleFirebaseDataMessage(RemoteMessage remoteMessage) {

        String imageUrl=remoteMessage.getData().get("image");
        Boolean isBackground=Boolean.valueOf(remoteMessage.getData().get("is_background"));
        String timestamp="";
        String title=remoteMessage.getData().get("title");
        String message=remoteMessage.getData().get("message");
        String payload=remoteMessage.getData().get("payload");
//
//        if(!NotificationUtils.isAppIsInBackground(getApplicationContext())){
//            //app is in foreground, broadcast the push message
//            Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
//            pushNotification.putExtra("message",message);
//            pushNotification.putExtra("title",title);
//            pushNotification.putExtra("imageUrl",imageUrl);
//            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);
//
//            //play notification sound
//            NotificationUtils notificationUtils =new NotificationUtils(getApplicationContext());
//            notificationUtils.playNotificationSound();
//        }else{
            Log.v("FirebaseMessageService","Check two");
            //app is in background, show the notification in notification tray
            Intent resultIntent=new Intent(getApplicationContext(),HomeActivity.class);
            resultIntent.putExtra("message",message);

            //check for image attachment
            if(TextUtils.isEmpty(imageUrl))
            {
                Log.v("FirebaseService","Check 3");
                showNotificationMessage(getApplicationContext(),title,message,timestamp,resultIntent);

            }
            else {
                Log.v("Firebase Service","Check 4");
                // image is present, show notification with image
                showNotificationMessageWithBigImage(getApplicationContext(), title, message, timestamp, resultIntent, imageUrl);
            }
        }

//    }

    private void handleNotification(String message,String title,String imageUrl) {
        if(!NotificationUtils.isAppIsInBackground(getApplicationContext())){
            // app is in foreground,broadcast the push message
            Intent pushNotifaction= new Intent(Config.PUSH_NOTIFICATION);
            pushNotifaction.putExtra("message",message);
            pushNotifaction.putExtra("title",title);
            pushNotifaction.putExtra("imageUrl",imageUrl);
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotifaction);

//            play notification sound
//            NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
//            notificationUtils.playNotificationSound();
        }else{
            //Intent intent=new Intent(this, HomeActivity.class);
            //startActivity(intent);
            Log.v("Messaging service","Check");
            //if app is in background, then firebase itself handles the notification
        }
    }

   /* private void handleDataMessage(JSONObject json,RemoteMessage remoteMessage){
        Log.e(TAG,"push json: "+json.toString());

        try{
            JSONObject data=json.getJSONObject("data");
            String title=data.getString("title");
            String message=data.getString("message");
            boolean isBackground=data.getBoolean("is_background");
            String imageUrl=data.getString("image");
            String timestamp=data.getString("timestamp");
            JSONObject payload=data.getJSONObject("payload");
            Log.e(TAG, "title: " + title);
            Log.e(TAG, "message: " + message);
            Log.e(TAG, "isBackground: " + isBackground);
            Log.e(TAG, "payload: " + payload.toString());
            Log.e(TAG, "imageUrl: " + imageUrl);
            Log.e(TAG, "timestamp: " + timestamp);

            if(!NotificationUtils.isAppIsInBackground(getApplicationContext())){
                //app is in foreground, broadcast the push message
                Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
                pushNotification.putExtra("message",message);
                LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

                //play notification sound
                NotificationUtils notificationUtils =new NotificationUtils(getApplicationContext());
                notificationUtils.playNotificationSound();
            }else{
                //app is in background, show the notification in notification tray
                Intent resultIntent=new Intent(getApplicationContext(),HomeActivity.class);
                resultIntent.putExtra("message",message);

                //check for image attachment
                if(TextUtils.isEmpty(imageUrl))
                {
                    showNotificationMessage(getApplicationContext(),title,message,timestamp,resultIntent);
                }
                else {
                    // image is present, show notification with image
                    showNotificationMessageWithBigImage(getApplicationContext(), title, message, timestamp, resultIntent, imageUrl);
                }
            }
        }catch(JSONException e){
            Log.e(TAG,"Json Exception: "+e.getMessage());
        }catch (Exception e){
            Log.e(TAG,"Exception:"+e.getMessage());

        }

    }
    */
    /**
     * Showing notification with text only
     */

    private void showNotificationMessage(Context applicationContext, String title, String message, String timestamp, Intent resultIntent) {
        notificationUtils=new NotificationUtils(applicationContext);
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title,message,timestamp,resultIntent);
    }

    /**
     * Showing notification with text and image
     */

    private void showNotificationMessageWithBigImage(Context context,String title,String message,String timeStamp, Intent intent, String imageUrl ){
        notificationUtils= new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title,message,timeStamp,intent,imageUrl);
    }
}
