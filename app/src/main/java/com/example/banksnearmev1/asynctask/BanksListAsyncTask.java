package com.example.banksnearmev1.asynctask;

import android.app.backup.BackupAgent;
import android.location.Location;
import android.os.AsyncTask;

import com.example.banksnearmev1.client.GoogleApi;
import com.example.banksnearmev1.models.Bank;
import com.example.banksnearmev1.models.BanksList;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import retrofit2.Call;

import java.io.IOException;

public class BanksListAsyncTask extends AsyncTask <Location,Integer,BanksList>{

    BanksList banksList;
    GoogleMap  mMap;
    public BanksListAsyncTask( GoogleMap mMap){
        this.mMap =mMap;
    }

    @Override
    protected BanksList doInBackground(Location[] locations) {
        if(locations[0] != null ){
            System.out.println("LOKACIJA NE E NULL");
        }
        StringBuilder sb = new StringBuilder();
        sb.append(locations[0].getLatitude());
        sb.append(",");
        sb.append(locations[0].getLongitude());
        System.out.println(sb.toString());
        final Call<BanksList> call = GoogleApi.getService().getBanks(sb.toString(),"AIzaSyBZTCoVDoeJMmHSC5EIYxWbbC5bUwybZQM","5000","bank");
        try{

            return call.execute().body();
        }catch (IOException e) {
            System.out.println("MARIOOOOOOOOOOOOOOOOOOOOOO");
            e.printStackTrace();
        }
        System.out.println("NEMA");

        return null;
    }

    @Override
    protected void onPostExecute(BanksList banksList) {
        super.onPostExecute(banksList);
        System.out.println("VLEZE");
        this.banksList = banksList;
        System.out.println(this.banksList.getResults().size());


        loadBanks();

    }

    private void loadBanks() {
        System.out.println("MARKERI");

        if (this.banksList.getResults() != null) {
            System.out.println("MARKERI2");

            for (Bank r : banksList.getResults()) {
                System.out.println("MARKERI3");

                MarkerOptions markerOptions = new MarkerOptions();
                LatLng tempLatLang = new LatLng(r.getGeometry().getLocation().getLat(), r.getGeometry().getLocation().getLng());
                markerOptions.position(tempLatLang);
                markerOptions.title(r.getName());
                mMap.addMarker(markerOptions);

            }
        }
    }

}

