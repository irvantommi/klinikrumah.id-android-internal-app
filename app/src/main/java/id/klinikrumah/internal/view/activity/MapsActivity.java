package id.klinikrumah.internal.view.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;

import androidx.core.app.ActivityCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.DirectionsApi;
import com.google.maps.GeoApiContext;
import com.google.maps.android.PolyUtil;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DirectionsLeg;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.TravelMode;

import org.jetbrains.annotations.Nullable;
import org.joda.time.DateTime;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import id.klinikrumah.internal.R;
import id.klinikrumah.internal.base.BaseActivity;
import id.klinikrumah.internal.constant.S;
import id.klinikrumah.internal.util.CommonFunc;

public class MapsActivity extends BaseActivity implements OnMapReadyCallback {
    private static final int overview = 0;
    private static final String TITLE = "TITLE";
    private static final String ORG_LAT = "ORG_LAT";
    private static final String ORG_LNG = "ORG_LNG";
    private static final String DEST_LAT = "DEST_LAT";
    private static final String DEST_LNG = "DEST_LNG";
    // other class

    // from xml

    // member var
    private GoogleMap gMap;
    private String title;
    private String orgLat;
    private String orgLng;
    private String destLat;
    private String destLng;

    public static void show(Context context, String title, String orgLat, String orgLng,
                            String destLat, String destLng) {
        Intent intent = new Intent(context, MapsActivity.class);
        Bundle b = new Bundle();
        b.putString(TITLE, title);
        b.putString(ORG_LAT, orgLat);
        b.putString(ORG_LNG, orgLng);
        b.putString(DEST_LAT, destLat);
        b.putString(DEST_LNG, destLng);
        intent.putExtras(b);

        context.startActivity(intent);
        ((Activity) context).overridePendingTransition(0, 0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
        if (getIntent() != null) {
            title = getIntent().getStringExtra(TITLE);
            orgLat = getIntent().getStringExtra(ORG_LAT);
            orgLng = getIntent().getStringExtra(ORG_LNG);
            destLat = getIntent().getStringExtra(DEST_LAT);
            destLng = getIntent().getStringExtra(DEST_LNG);
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;
//        if (Build.VERSION.SDK_INT >= 18) {
//            CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(
//                    Double.parseDouble(destLat), Double.parseDouble(destLng))).zoom(15).build();
//            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//                // TODO: Consider calling ActivityCompat#requestPermissions
//                // here to request the missing permissions, and then overriding
//                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                //                                          int[] grantResults)
//                // to handle the case where the user grants the permission. See the documentation
//                // for ActivityCompat#requestPermissions for more details.
//                gMap.setMyLocationEnabled(true);
//            }
//            gMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
//            UiSettings mUiSettings = gMap.getUiSettings();
//            mUiSettings.setZoomControlsEnabled(true);
//            mUiSettings.setCompassEnabled(true);
//            mUiSettings.setScrollGesturesEnabled(true);
//            mUiSettings.setZoomGesturesEnabled(true);
//            mUiSettings.setTiltGesturesEnabled(true);
//            mUiSettings.setRotateGesturesEnabled(true);
//            /*gMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
//                @Override
//                public void onMapLoaded() {
//                    DirectionsResult results = getDirectionsDetails(orgLat + "," + orgLng,
//                            destLat + "," + destLng);
//                    if (results != null) {
//                        // add Polyline
//                        DirectionsRoute route = results.routes[overview];
//                        List<LatLng> decodedPath = PolyUtil.decode(route.overviewPolyline.getEncodedPath());
//                        gMap.addPolyline(new PolylineOptions()
//                                .width(7)
//                                .color(getResources().getColor(R.color.colorPrimary))
//                                .addAll(decodedPath));
//                        // positionCamera
//                        DirectionsLeg leg = route.legs[overview];
//                        com.google.maps.model.LatLng org = leg.startLocation;
//                        com.google.maps.model.LatLng dest = leg.startLocation;
//                        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(org.lat, org.lng), 13));
//                        // addMarkersToMap
//                        gMap.addMarker(new MarkerOptions()
//                                .position(new LatLng(org.lat, org.lng))
////                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map))
//                                .title(leg.startAddress));
//
//                        gMap.addMarker(new MarkerOptions()
//                                .position(new LatLng(dest.lat, dest.lng))
//                                .title(leg.endAddress)
////                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_hospital))
//                                .snippet("Jarak : " + leg.distance.humanReadable +
//                                        "\nWaktu : " + leg.durationInTraffic.humanReadable));
//                    }
//                }
//            });*/
//        } else {
            LatLng destination = new LatLng(Double.parseDouble(destLat), Double.parseDouble(destLng));
//            gMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        UiSettings uiSettings = gMap.getUiSettings();
        uiSettings.setAllGesturesEnabled(true);
        uiSettings.setCompassEnabled(true);
        uiSettings.setMapToolbarEnabled(true);
        uiSettings.setMyLocationButtonEnabled(true);
        uiSettings.setZoomControlsEnabled(true);
            float zoomLevel = (float) 13.0; // Max 21
            gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(destination, zoomLevel));
            Marker marker = gMap.addMarker(new MarkerOptions().position(destination).title(title));
            marker.showInfoWindow();
            gMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    CommonFunc.openUrl(MapsActivity.this, String.format(S.GMAP_LINK,
                            Double.parseDouble(destLat), Double.parseDouble(destLng)));
                    return false;
                }
            });
//        }
    }

    @Nullable
    private DirectionsResult getDirectionsDetails(String origin, String destination) {
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());
        try {
            return DirectionsApi.newRequest(new GeoApiContext()
                    .setQueryRateLimit(3)
                    .setApiKey(getString(R.string.google_maps_key))
                    .setConnectTimeout(1, TimeUnit.SECONDS)
                    .setReadTimeout(1, TimeUnit.SECONDS)
                    .setWriteTimeout(1, TimeUnit.SECONDS)
            )
                    .mode(TravelMode.DRIVING)
                    .origin(origin)
                    .destination(destination)
                    .departureTime(new DateTime())
                    .await();
        } catch (ApiException e) {
            e.printStackTrace();
            return null;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}