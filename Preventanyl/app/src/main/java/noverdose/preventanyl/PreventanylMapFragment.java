package noverdose.preventanyl;


import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class PreventanylMapFragment extends Fragment {


    private Context mContext;
    private Activity activity;

    private GoogleMap map;
    private SupportMapFragment map_fragment;
    private PolylineOptions lineOptions;
    private Polyline route;
    private PreventanylMapFragment.ParserTask parserTask;

    protected static ClusterManager<StaticKit> mClusterManager;

    public PreventanylMapFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FruitTreeMapFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PreventanylMapFragment newInstance(String param1, String param2) {
        PreventanylMapFragment fragment = new PreventanylMapFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_preventanyl_map, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onActivityCreated (Bundle args) {
        super.onActivityCreated (args);
        activity = getActivity ();
        map_fragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map_view);

        createMap ();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void zoom_current_position (int zoom_lvl) {
        if (MainActivity.mCurrentLocation != null)
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng (MainActivity.mCurrentLocation.getLatitude(), MainActivity.mCurrentLocation.getLongitude()), zoom_lvl));
    }

    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String,String>>>> {

        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try{
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                // Starts parsing data
                routes = parser.parse(jObject);
            }catch(Exception e){
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points;

            for(int i=0;i<result.size();i++){
                points = new ArrayList<>();
                lineOptions = new PolylineOptions();

                List<HashMap<String, String>> path = result.get(i);

                for(int j=0; j < path.size();++j) {
                    HashMap<String,String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat")), lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                lineOptions.addAll(points);
                lineOptions.width(4);
                lineOptions.color(Color.RED); // MAYBE CHANGE TO BLUE BEFORE RELEASE????
            }

            if (route != null)
                route.remove ();

            if (lineOptions != null) {
                route = map.addPolyline(lineOptions);

                map.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {

                    @Override
                    public void onCameraChange(CameraPosition cameraPosition) {
                        route.setWidth(cameraPosition.zoom < 13 ? 10 : 4);
                    }

                });
            }
        }
    }

    // Calculates the closest bin relative to cur user position/location
    private int get_closest_bin () {
        int index = -1;
        float minDistance = Float.MAX_VALUE;
        final Location location = MainActivity.mCurrentLocation;

        if (location == null || MainActivity.staticKits.isEmpty())
            return index;

        if (0 < MainActivity.staticKits.size ()) {
            Location target = new Location ("target");
            for (int i = 0; i < MainActivity.staticKits.size(); ++i) {
                LatLng temp = MainActivity.staticKits.get (i).getPosition ();
                target.setLatitude  (temp.latitude);
                target.setLongitude (temp.longitude);
                if (location.distanceTo (target) < minDistance) {
                    minDistance = location.distanceTo(target);
                    index = i;
                }
            }
        }
        return index;
    }

    // Creates and returns the directions url
    private String get_directions_url (LatLng origin, LatLng dest) {

        String str_origin = "origin=" + origin.latitude + "," + origin.longitude, str_dest = "destination=" + dest.latitude + "," + dest.longitude, sensor = "sensor=false", parameters = str_origin + "&" + str_dest + "&" + sensor;

        return "https://maps.googleapis.com/maps/api/directions/json?" + parameters;

    }

    private synchronized void draw_route (final Location location) {
        if (location == null)
            return;

        Log.e ("LOCATION", "INSIDE ROUTE");

        activity.runOnUiThread (new Runnable () {
            public synchronized void run () {
                int index = get_closest_bin();
                if (index != -1) {
                    LatLng pos = MainActivity.staticKits.get(index).getPosition();
                    LatLng cur = new LatLng (location.getLatitude(), location.getLongitude()), latLng = pos;
                    String url = get_directions_url(cur, latLng);

                    if (checkNetworkConnection ())
                        Ion.with(mContext).
                                load(url).

                                asString()
                                .setCallback(
                                        new FutureCallback<String>() {
                                            @Override
                                            public void onCompleted(Exception e, String result) {
                                                if (e != null) {
                                                    Log.e ("Runnable", "Exception");
                                                    Toast.makeText(mContext, e.toString(), Toast.LENGTH_LONG).show ();
                                                    // error handling goes here
                                                } else {
                                                    Log.e ("Runnable", "Start Drawing Route");
                                                    parserTask = new PreventanylMapFragment.ParserTask();
                                                    parserTask.execute(result);
                                                    Log.e ("Runnable", "Finish Drawing Route");
                                                }
                                            }
                                        }
                                );
                }
            }
        });
        Log.e ("Runnable", "FINISH DRAW ROUTE");
    }

    private boolean checkNetworkConnection () {
        ConnectivityManager cm =
                (ConnectivityManager)mContext.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    // Uses an anonymous inner class to implement the map
    // Creates the map,
    // Has a listener which constantly updates the cur location private variable at the top
    // Executes the background task that fetches the data and then calculates the closest marker
    private void createMap () {
        map_fragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                map = googleMap;

                mClusterManager  = new ClusterManager<> (mContext, map);


                Button helpMeButton = activity.findViewById(R.id.helpMeButton);

                helpMeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        Overdose overdose = new Overdose(null, new Date(), new LatLng(MainActivity.mCurrentLocation.getLatitude(), MainActivity.mCurrentLocation.getLongitude()));

                        String url = "https://preventanyl.com/regionfinder.php?id="+overdose.getId()+"&lat="+overdose.getCoordinates().latitude+"&long="+overdose.getCoordinates().longitude;

                        // TODO post this URL


                    }
                });

                //android:id="@+id/directionBtn"


                map.setOnCameraIdleListener(mClusterManager);
                map.setOnMarkerClickListener(mClusterManager);
                map.setOnInfoWindowClickListener(mClusterManager);

                mClusterManager.setOnClusterClickListener(new ClusterManager.OnClusterClickListener<StaticKit>() {

                    @Override
                    public boolean onClusterClick(Cluster<StaticKit> cluster) {
                        return false;
                    }

                });

                // check_location_permission method does not work here???
                if (ActivityCompat.checkSelfPermission(mContext.getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(mContext.getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                    return;

                map.moveCamera (CameraUpdateFactory.newLatLngZoom (new LatLng(49.2290040, -123.0412511), 10));

                map.setMyLocationEnabled(true);

                map.getUiSettings().setMapToolbarEnabled(true);

                loadMarkers();

            }
        });
        // pullDataFromServer ();
    }


    public void loadMarkers() {
        getActivity ().runOnUiThread (new Runnable () {
            public void run () {
                if(mClusterManager != null) {
                    mClusterManager.clearItems();
                    mClusterManager.addItems(MainActivity.staticKits);
                    mClusterManager.cluster();
                    // draw_route (MainActivity.mCurrentLocation);
                    zoom_current_position(13);
                    // markerClickListener ();
                }

            }
        });
    }

}
