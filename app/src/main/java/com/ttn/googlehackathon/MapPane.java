package com.ttn.googlehackathon;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * <p/>
 * Project: <b>CodeForIndia 2015</b><br/>
 * Created by: Anand K. Rai on 27/9/15.<br/>
 * Email id: anand.it402@gmail.com<br/>
 * <p/>
 */
public class MapPane extends Activity implements GoogleMap.OnMarkerClickListener, OnMapReadyCallback{

    private Marker perth;
    private Marker melbourne;

    private View mapView;
    private LatLng MELBOURNE;
    private LatLng sydney;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_activity);

        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
         mapView = getFragmentManager().findFragmentById(R.id.map).getView();
    }

    @Override
    public void onMapReady(final GoogleMap map) {



        sydney = new LatLng(28.5413965, 77.3970354);
        MELBOURNE = new LatLng(28.6178864, 77.2793956);

        map.setMyLocationEnabled(true);
        //map.moveCamera(CameraUpdateFactory.newLatLngZoom(camera, 13));

        map.setOnMarkerClickListener(this);
        map.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                // Getting view from the layout file info_window_layout
                View v = getLayoutInflater().inflate(R.layout.windowlayout, null);
                return v;
            }
        });

        perth = map.addMarker(new MarkerOptions()
                .title("Sydney")
                .snippet("The most populous city in Australia.")
                .position(sydney)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));


        melbourne = map.addMarker(new MarkerOptions()
                .position(MELBOURNE)
                .title("Melbourne")
                .snippet("The most populous city in Australia.")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

        if (mapView.getViewTreeObserver().isAlive()) {
            mapView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @SuppressLint("NewApi")
                // We check which build version we are using.
                @Override
                public void onGlobalLayout() {
                    LatLngBounds.Builder bld = new LatLngBounds.Builder();

                    bld.include(sydney);
                    bld.include(MELBOURNE);

                    LatLngBounds bounds = bld.build();
                    map.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 70));
                    mapView.getViewTreeObserver().removeGlobalOnLayoutListener(this);

                }
            });


        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if(marker.equals(perth)){
            marker.showInfoWindow();
        }
        return false;
    }


}

