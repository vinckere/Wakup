package com.project.vins.park;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

public class EmplacementVoitureModel implements EmplacementVoitureMVP.Model, LocationListener {

    private int counter = 1;
    private long timeLastLocation;
    protected LocationManager locationManager;
    private OnLocalisationFinishedListener onLocalisationFinishedListener;
    private Context context;
    private int intervalle = 5000;

    private String jour,mois,annee,heure,minute;

    SimpleDateFormat sdf;


    public EmplacementVoitureModel(OnLocalisationFinishedListener onLocalisationFinishedListener, Context context){

        sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        this.onLocalisationFinishedListener = onLocalisationFinishedListener;
        this.context = context;

        ////////////////////////////////////////////////////////////////////
        //      A la création on démarre les requetes à un rythme soutenu //
        ////////////////////////////////////////////////////////////////////
        boucleLocalisation(intervalle);

    }

    public void boucleLocalisation(int intervalle){
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        if(context.checkCallingOrSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, intervalle, 0, this);
        }
    }

    @Override
    public void reActiverBoucleLocalisation() {
        if(context.checkCallingOrSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED) {
            locationManager.removeUpdates(this);
            intervalle = 5000;
            boucleLocalisation(intervalle);
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////
    //                                Méthode du LISTENER                             //
    ////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void onLocationChanged(Location location) {

        // On passe chaque nouvelle location au presenter
        timeLastLocation = new Date().getTime();
        onLocalisationFinishedListener.setLastLocation(location,timeLastLocation);

        // Toutes les 4 localisations on enleve la requete et on en recréé une avec 2 fois plus de temps d'intervalle jusqu'à 50 secondes d'intervalles
        if(counter>=2){
            if(context.checkCallingOrSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED)
            {
                locationManager.removeUpdates(this);
                counter=0;
                if(intervalle < 10000){
                    intervalle*=2;
                    boucleLocalisation(intervalle);
                }
            }
        }
        counter++;
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d("Latitude","disable");
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d("Latitude","enable");
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        if(status == LocationProvider.OUT_OF_SERVICE){
//            onLocalisationFinishedListener.afficher("Out of Service","");
        }else if(status == LocationProvider.TEMPORARILY_UNAVAILABLE){
//            onLocalisationFinishedListener.afficher("Temporarily unavailable","");
        }else if(status == LocationProvider.AVAILABLE) {
//            onLocalisationFinishedListener.afficher("Available","");
        }

    }

    ////////////////////////////////////////////////////////////////////////////////////

    public void getLocation(final OnLocalisationFinishedListener onLocalisationFinishedListener, Context context) {

        this.onLocalisationFinishedListener = onLocalisationFinishedListener;
        this.context = context;
    }

    @Override
    public void enregistrerLocation(Location location) {

        double latitude, longitude;
        long timeNow;

        latitude = location.getLatitude();
        longitude = location.getLongitude();

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMMM-yyyy-HH-mm");
        Date dateNow = new Date();
        String currentDateAndTime = sdf.format(dateNow);
        timeNow = dateNow.getTime();
        String[] array = null;

        array = currentDateAndTime.split("-");

        jour = array[0];
        mois = array[1];
        annee = array[2];
        heure = array[3];
        minute = array[4];

        SharedPreferences sharedPref = context.getSharedPreferences("userInfo", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("Date","On met la daaaaaaaate ici");
        editor.putFloat("Latitude", (float) latitude);
        editor.putFloat("Longitude",(float) longitude);
        editor.putString("jour", jour);
        editor.putString("mois", mois);
        editor.putString("annee", annee);
        editor.putString("heure", heure);
        editor.putString("minute", minute);
        editor.putLong("timeLoc", timeNow);
        editor.apply();
        onLocalisationFinishedListener.setDetailDate(jour, mois, annee, heure, minute);
    }

    @Override
    public void chargerDernierEmplacement() {
        SharedPreferences sharedPref = context.getSharedPreferences("userInfo",Context.MODE_PRIVATE);
        jour = sharedPref.getString("jour", "xx");
        if(!jour.equalsIgnoreCase("xx")) {
            mois = sharedPref.getString("mois", "");
            annee = sharedPref.getString("annee", "");
            heure = sharedPref.getString("heure", "");
            minute = sharedPref.getString("minute", "");
            onLocalisationFinishedListener.setDetailDate(jour, mois, annee, heure, minute);
        }
        else{
        }
    }

    @Override
    public void cancelProgress() {

        if(context.checkCallingOrSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED) {
            locationManager.removeUpdates(this);
        }
    }

}
