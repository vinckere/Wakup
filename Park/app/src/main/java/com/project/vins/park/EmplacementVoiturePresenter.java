package com.project.vins.park;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.location.Location;

import java.util.Date;

/**
 * Created by Vins on 26/04/2016.
 */
public class EmplacementVoiturePresenter implements EmplacementVoitureMVP.Presenter, EmplacementVoitureMVP.Model.OnLocalisationFinishedListener {

    private EmplacementVoitureMVP.View emplacementVoitureView;
    private EmplacementVoitureMVP.Model emplacementVoitureModel;
    private String jour,mois,annee,heure,minute;
    private Location lastLocation;
    private long timeLastLocation;
    boolean demandeEnCours = false;
    private boolean dejaEmplacement = false;
    private Resources res;

    public EmplacementVoiturePresenter(EmplacementVoitureMVP.View emplacementVoitureView) {
        this.emplacementVoitureView = emplacementVoitureView;
        this.emplacementVoitureModel = new EmplacementVoitureModel(this, emplacementVoitureView.getContext());
        res = emplacementVoitureView.getContext().getResources();
        emplacementVoitureModel.chargerDernierEmplacement();
    }

    public void clickJeMeGare(){
        if(dejaEmplacement){
            emplacementVoitureView.showConfirmation(jour,mois,annee,heure,minute);
        }
        else{
            emplacementVoitureView.showProgress();
            demandeEnCours = true;
        }
    }

    public void cancelProgress(){
        emplacementVoitureModel.cancelProgress();
        demandeEnCours = false;
//        emplacementVoitureView.showCustomToast("KAKAKAKA","Arret des actualisations");
    }

    @Override
    public void enregistrerLocalisation() {

        if(lastLocation==null){
//            emplacementVoitureView.showCustomToast("On a pas encore de location","xxx");
            demandeEnCours = true;
            emplacementVoitureView.showProgress();
        }
        else{
            long diff = (new Date().getTime()) - timeLastLocation;
            if(diff <= 10000){
//                emplacementVoitureView.showCustomToast("On est dans le bon truc","xxx");
                //On enregistre la localisation ds SharedPreference et on met à jour l'IHM (setDetailDate) appelé dans enregistrerLocalisation()
                emplacementVoitureModel.enregistrerLocation(lastLocation);
                emplacementVoitureView.succes();
            }else{
//                emplacementVoitureView.showCustomToast("diff > 15 secondes","xxx");
                emplacementVoitureModel.reActiverBoucleLocalisation();
                demandeEnCours = true;
                emplacementVoitureView.showProgress();
            }
        }
    }

    @Override
    public void ouvrirLocalisations() {

        SharedPreferences sharedPref = emplacementVoitureView.getContext().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        String date = sharedPref.getString("Date", "");
        float latitude = sharedPref.getFloat("Latitude", 0);
        float longitude = sharedPref.getFloat("Longitude", 0);
        if(longitude != 0){
            emplacementVoitureView.lancerNavigation(latitude, longitude);
        }else{
            emplacementVoitureView.showCustomToast(res.getString(R.string.attention),res.getString(R.string.aucun_emplacement));
        }
    }

    @Override
    public void afficher(String titre, String msg) {
        emplacementVoitureView.showCustomToast(titre,msg);
    }

    @Override
    public void setDetailDate(String jour,String mois,String annee,String heure,String minute){
        this.jour = jour;
        this.mois = mois;
        this.annee = annee;
        this.heure = heure;
        this.minute = minute;
        emplacementVoitureView.changerTexteDernierEmplacement(jour,mois,annee,heure,minute);
        dejaEmplacement = true;
    }

    @Override
    public void setLastLocation(Location loc, long time) {
        this.lastLocation = loc;
        this.timeLastLocation = time;

        if(demandeEnCours){
            emplacementVoitureModel.enregistrerLocation(lastLocation);
            emplacementVoitureView.closeProgress();
            emplacementVoitureView.succes();
            demandeEnCours = false;
            dejaEmplacement = true;
        }
    }
}
