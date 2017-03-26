package com.sr.masharef.masharef.services;

import android.app.IntentService;
import android.content.Intent;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.sr.masharef.masharef.MainActivity;
import com.sr.masharef.masharef.common.keychain.KeychainManager;
import com.sr.masharef.masharef.model.AccessToken;
import com.sr.masharef.masharef.utility.Log;
import com.sr.masharef.masharef.utility.MyNotificationManager;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class MyFirebaseMessagingService  extends FirebaseMessagingService {
    private String TAG = "MyFirebaseMessagingService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // ...

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        Log.d(TAG, "From: " + remoteMessage.getMessageId());
        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            Gson gson = new Gson();
            String json1 = gson.toJson(remoteMessage.getData());
            if (remoteMessage.getData().size() > 0) {
                Log.e(TAG, "Data Payload: " + String.valueOf(remoteMessage.getData()));
                try {
                    JSONObject json = new JSONObject(json1);

                    AccessToken accessToken = KeychainManager.storedAccessToken(getApplicationContext());
                    String accTokenValue = accessToken.getToken();
                    if(accTokenValue.equals(json.getString("accesstoken").toString()))
                    sendPushNotification(json);
                } catch (Exception e) {
                    Log.e(TAG, "Exception: " + e.getMessage());
                }
            }
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
      //  sendNotification(remoteMessage);
    }

    @Override
    public void onDeletedMessages() {
        super.onDeletedMessages();
    }
    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param remoteMessage FCM message body received.
     */
    private void sendNotification(RemoteMessage remoteMessage) {
      /*  Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 *//* Request code *//*, intent,
                PendingIntent.FLAG_ONE_SHOT); //FLAG_UPDATE_CURRENT //FLAG_ONE_SHOT

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Masharef Message")
                .setContentText(remoteMessage.getNotification().getBody())
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();

        String[] events = new String[remoteMessage.getData().size()];
        int k= 0;
        for (Map.Entry<String, String > entry : remoteMessage.getData().entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            System.out.println(""+key +" = "+value);
            events[k] = key +" : "+ value;
            inboxStyle.addLine(events[k]);
            k++;
        }
        inboxStyle.setBigContentTitle(remoteMessage.getMessageId());
        notificationBuilder.setStyle(inboxStyle);
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0 *//* ID of notification *//*, notificationBuilder.build());*/
    }

    private void sendPushNotification(JSONObject json) {
        //optionally we can display the json into log
        Log.e(TAG, "Notification JSON " + json.toString());
        try {
            //getting the json data
            JSONObject data = json; //json.getJSONObject("data");

            //parsing json data
            String title = data.getString("title");
            String message = data.getString("message");
        //    String imageUrl = data.getString("image");

            //creating MyNotificationManager object
            MyNotificationManager mNotificationManager = new MyNotificationManager(getApplicationContext());

            //creating an intent for the notification
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);

            //if there is no image
          //  if(imageUrl.equals("null")){
                //displaying small notification
                mNotificationManager.showSmallNotification(title, message, intent);
         /*   }else{
                //if there is an image
                //displaying a big notification
                mNotificationManager.showBigNotification(title, message, imageUrl, intent);
            }*/
        } catch (JSONException e) {
            Log.e(TAG, "Json Exception: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
    }


}
