package com.example.banksnearmev1;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.Switch;
import androidx.core.app.ActivityCompat;
import com.example.banksnearmev1.asynctask.BanksListAsyncTask;
import com.example.banksnearmev1.models.Bank;
import com.example.banksnearmev1.models.BanksList;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.logging.Logger;

public class BackgroundService extends Service implements LocationListener {
    // This is the object that receives interactions from clients.
    private final IBinder mBinder = new LocalBinder();
    private LocationManager locationManager;
    private Location location;
    private Logger logger = Logger.getLogger("BackgroundService");
    BanksListAsyncTask asyncTask;
    private Marker marker_in_skopje;
    GoogleMap mMap;




    public class LocalBinder extends Binder {
        BackgroundService getService() {
            return BackgroundService.this;
        }
    }

    @Override
    public void onCreate() {

        System.out.println("SERVICE CREATED");
        locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        logger.info("START_SERVICE");
        return START_REDELIVER_INTENT;
    }

    @Override
    public void onDestroy() {

        logger.info("DESTROYED");
    }

    @Override
    public IBinder onBind(Intent intent) {
        System.out.println("SERVICE BOUNDED");
        listenLocationUpdates();

        intent.getParcelableExtra("marker");
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);

    }

    void stopService(){
        locationManager.removeUpdates(this);
    }

    void getLocation(GoogleMap mMap) {
        // Add a marker in Macedonia and move the camera

        this.mMap = mMap;
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    Activity#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            return;
        }
        this.location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        LatLng skopje = new LatLng(location.getLatitude(), location.getLongitude());
        marker_in_skopje = mMap.addMarker(new MarkerOptions().position(skopje).title("Marker in Skopje"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(skopje));
        mMap.moveCamera(CameraUpdateFactory.zoomTo(12f));

    }

    @Override
    public void onLocationChanged(Location location) {
        logger.info("onLocationChanged");
        this.location = location;
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        marker_in_skopje.setPosition(latLng);
        marker_in_skopje.setTitle("Marker title");
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(marker_in_skopje.getPosition(), 12f));

        try {
            callApi();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private void callApi() throws InterruptedException {

        asyncTask = new BanksListAsyncTask(mMap);
        asyncTask.execute(location);




    }


    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        logger.info("onStatusChanged");

    }

    @Override
    public void onProviderEnabled(String provider) {
        logger.info("onProviderEnabled");

    }

    @Override
    public void onProviderDisabled(String provider) {
        logger.info("onProviderDisabled");

    }


    private void listenLocationUpdates() {
        logger.info("onlistenLocationUpdates");

        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    Activity#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 0, this);

        //getGeocode();

    }
}
