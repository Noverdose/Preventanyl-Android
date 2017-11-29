package noverdose.preventanyl;

import android.util.Log;

import java.util.HashMap;

/**
 * Created by yudhvirraj on 2017-11-28.
 */

public class FirebaseOverdose {

    String id;
    String region;
    Double latitude;
    Double longitude;
    String date;
    Double timestamp;


    public FirebaseOverdose() {

    }

    public FirebaseOverdose(String id, String region, Double latitude, Double longitude, String date, Double timestamp) {
        Log.e ("VAL :", region + " "+latitude+ " "+ id + "");
    }
}
