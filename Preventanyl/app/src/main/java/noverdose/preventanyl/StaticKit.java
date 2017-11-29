package noverdose.preventanyl;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

/**
 * Created by yudhvirraj on 2017-11-28.
 */

public class StaticKit implements ClusterItem {
    private String id;
    private String comments;
    private String displayName;
    private String phone;
    private String userId;
    private LatLng coordinates;
    private Address address;

    public StaticKit (String id, String comments, String displayName, String phone, String userId, double lat, double lng, Address address) {
        this.id = id;
        this.comments = comments;
        this.displayName = displayName;
        this.phone = phone;
        this.userId = userId;
        this.coordinates = new LatLng(lat, lng);
        this.address = address;
    }

    @Override
    public LatLng getPosition() {
        return coordinates;
    }

    @Override
    public String getTitle() {
        return displayName;
    }

    @Override
    public String getSnippet() {
        return comments;
    }
}
