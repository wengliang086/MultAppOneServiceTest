package com.hoolai.mylibrary;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Process;
import android.util.Log;

import com.ibm.mqtt.IMqttClient;
import com.ibm.mqtt.MqttClient;
import com.ibm.mqtt.MqttException;
import com.ibm.mqtt.MqttSimpleCallback;

/**
 * Created by Administrator on 2017/3/31.
 */

public class HoolaiPushService extends Service {

    public static final String TAG = "HoolaiPushService";

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate: " + Process.myPid());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "onStartCommand() called with: intent = [" + intent + "], flags = [" + flags + "], startId = [" + startId + "]");
        // Establish an MQTT connection
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    new MQTTConnection();
                } catch (MqttException e) {
                    Log.e(TAG, "onStartCommand: ", e);
                }
            }
        }).start();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG, "onBind: ");
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy: ");
    }

    // This inner class is a wrapper on top of MQTT client.
    private class MQTTConnection implements MqttSimpleCallback {
        IMqttClient mqttClient = null;

        // Creates a new connection given the broker address and initial topic
        public MQTTConnection() throws MqttException {
            // Create connection spec
            String mqttConnSpec = "tcp://" + "192.168.150.82" + "@" + "1883";
            // Create the client and connect
            mqttClient = MqttClient.createMqttClient(mqttConnSpec, null);
            String clientID = "ABCD";
            mqttClient.connect(clientID, true, (short) (60 * 15));
            // register this client app has being able to receive messages
            mqttClient.registerSimpleHandler(this);
            // Subscribe to an initial topic, which is combination of client ID and device ID.
            subscribeToTopic("topic/" + clientID);
        }

        private int[] MQTT_QUALITIES_OF_SERVICE = {0};

        /*
         * Send a request to the message broker to be sent messages published with
         *  the specified topic name. Wildcards are allowed.
         */
        private void subscribeToTopic(String topicName) throws MqttException {
            if ((mqttClient == null) || !mqttClient.isConnected()) {
                // quick sanity check - don't try and subscribe if we don't have
                Log.i(TAG, "subscribeToTopic: " + "Connection error" + "No connection");
            } else {
                String[] topics = {topicName};
                mqttClient.subscribe(topics, MQTT_QUALITIES_OF_SERVICE);
            }
        }

        /*
         * Called if the application loses it's connection to the message broker.
         */
        public void connectionLost() throws Exception {
            Log.i(TAG, "connectionLost: ");
        }

        /*
         * Called when we receive a message from the message broker.
         */
        public void publishArrived(String topicName, byte[] payload, int qos, boolean retained) {
            // Show a notification
            String s = new String(payload);
//            showNotification(s);
            Intent intent = new Intent();
            intent.setAction("android.intent.action.MyBroadcastReceiver");
            intent.putExtra("msg", s);
            sendBroadcast(intent);
        }

    }

    // Display the topbar notification
    private void showNotification(String text) {
        PendingIntent pi = PendingIntent.getActivity(this, 0, new Intent(this, NotificationActivity.class), 0);

        Notification.Builder builder = new Notification.Builder(this);
        builder.setAutoCancel(true);
        builder.setContentIntent(pi);
        builder.setWhen(System.currentTimeMillis());
        builder.setDefaults(Notification.DEFAULT_ALL);
        builder.setSmallIcon(R.drawable.icon);
        builder.setContentTitle("NOTIF_TITLE");
        builder.setContentText(text);

        Notification n = builder.build();
        n.flags |= Notification.FLAG_SHOW_LIGHTS;
        n.flags |= Notification.FLAG_AUTO_CANCEL;
        ((NotificationManager) getSystemService(NOTIFICATION_SERVICE)).notify(0, n);
    }
}
