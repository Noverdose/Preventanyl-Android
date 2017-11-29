package noverdose.preventanyl;

import android.util.Log;

import java.util.HashMap;

/**
 * Created by yudhvirraj on 2017-11-28.
 */

public class FirebaseOverdose {
    HashMap<String, String> address;
    String comments;
    String phone;
    String displayName;
    String userId;
    HashMap<String, Double> coordinates;
    String id;

    public FirebaseOverdose() {

    }

    public FirebaseOverdose(HashMap<String, String> address, String comments, String phone, String displayName, HashMap<String, Double> coordinates, String id, String userId) {
        Log.e ("VAL :", comments);
    }
}
