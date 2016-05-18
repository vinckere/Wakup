package com.project.vins.park;

import android.content.Context;
import android.location.Location;

/**
 * Created by Vins on 09/05/2016.
 */
public interface EmplacementVoitureMVP {


    interface Model {
        interface OnLocalisationFinishedListener {
            void afficher(String titre,String msg);
            void setLastLocation(Location loc, long time);
            void setDetailDate(String jour,String mois,String annee,String heure,String minute);
        }

        void reActiverBoucleLocalisation();
        void enregistrerLocation( Location loc);
        void chargerDernierEmplacement();
        void getLocation(OnLocalisationFinishedListener onLocalisationFinishedListener, Context context);
        void cancelProgress();

    }

    interface View {
        void showCustomToast(String titre, String contenu);
        Context getContext();
        void changerTexteDernierEmplacement(String jour, String mois, String annee, String heure, String minute);
        void succes();
        void showConfirmation(String jour,String mois,String annee,String heure,String minute);
        void showProgress();
        void closeProgress();
        void lancerNavigation(float latitude, float longitude);
    }

    interface Presenter {
        void enregistrerLocalisation();
        void ouvrirLocalisations();
    }
}
