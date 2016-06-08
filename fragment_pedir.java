package layout;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.gargui3.faltanchelas.MainActivity;
import com.example.gargui3.faltanchelas.MenuChelero;
import com.example.gargui3.faltanchelas.R;
import com.example.gargui3.faltanchelas.RegisterActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/*
Implementacion de Google maps y LocationListener en fragmento
*/

public class fragment_pedir extends Fragment implements LocationListener {

    private LocationManager locManager;
    private MapView mMapView;
    private GoogleMap mGoogleMap;
    private ViewGroup rootView;
    private double lat = 0.0;
    private double lng = 0.0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_pedir, container, false);
        mMapView = (MapView) rootView.findViewById(R.id.map);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();



        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        mGoogleMap = mMapView.getMap();


        onMapReady(mGoogleMap);

        return rootView;
    }

    public void onMapReady(GoogleMap map) {

        locManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        locManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 1, this);

        Location lc = locManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        onLocationChanged(lc);

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), 10));

        BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.mipmap.marker);

        final LatLng pos = new LatLng(lat, lng);
        Marker m = map.addMarker(new MarkerOptions()
                .position(pos)
                .draggable(true);
    }

    public void seleccionPedido(View view) {
        Intent in = new Intent(getActivity(), MenuChelero.class);
        getActivity().finish();
        startActivity(in);
    }

    @Override
    public void onLocationChanged(Location location) {
        lat = location.getLatitude();
        lng = location.getLongitude();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
