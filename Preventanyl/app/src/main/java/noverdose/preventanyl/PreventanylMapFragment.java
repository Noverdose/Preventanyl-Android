package noverdose.preventanyl;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;


/**
 * A simple {@link Fragment} subclass.
 */
public class PreventanylMapFragment extends Fragment {


    private Context mContext;

    private GoogleMap map;
    private SupportMapFragment map_fragment;

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
        map_fragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map_view);

        createMap ();
    }

    @Override
    public void onDetach() {
        super.onDetach();
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

                // check_location_permission method does not work here???
                if (ActivityCompat.checkSelfPermission(mContext.getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(mContext.getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                    return;

                map.moveCamera (CameraUpdateFactory.newLatLngZoom (new LatLng(49.2290040, -123.0412511), 10));

                map.setMyLocationEnabled(true);

                map.getUiSettings().setMapToolbarEnabled(true);

                getActivity ().runOnUiThread (new Runnable () {
                    public void run () {
                        // markerClickListener ();
                    }
                });
            }
        });
        // pullDataFromServer ();
    }

}
