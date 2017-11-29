package noverdose.preventanyl;

/**
 * Created by yudhvirraj on 2017-11-29.
 */

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;
import android.widget.Toast;

/**
 * This is called whenever app receives notification
 * in background/foreground state so you can
 * apply logic for background task, but still Firebase notification
 * will be shown in notification tray
 */
public class FirebaseDataReceiver extends WakefulBroadcastReceiver {

    private final String TAG = "FirebaseDataReceiver";

    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "I'm in!!!");

        String title = "";
        String body  = "";

        if (intent.getExtras() != null) {
            for (String key : intent.getExtras().keySet()) {
                Object value = intent.getExtras().get(key);
                Log.e("FirebaseDataReceiver", "Key: " + key + " Value: " + value);
            }

            title += intent.getExtras().get("gcm.notification.title");
            body  += intent.getExtras().get("gcm.notification.body");
            if (context != null)
                Toast.makeText(context, title + "\n" + body, Toast.LENGTH_LONG).show();
        }
    }
}
