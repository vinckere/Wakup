package com.project.vins.park;

import android.app.ActivityManager;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class EmplacementVoitureView extends AppCompatActivity implements EmplacementVoitureMVP.View{

    private EmplacementVoiturePresenter presenter;
    private Dialog dProgress, dConfirm;
    private TextView txtDernierEmplacement;
    private RelativeLayout rl;
    private Button cancelProgress, jeMeGare, jeRecupere, share;
    private ImageView imgLoadingBarre, progressCancelProgress, loading;
    Resources res;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.statusBar));

            Bitmap img = BitmapFactory.decodeResource(getResources(), R.mipmap.logoo);
            setTaskDescription(new ActivityManager.TaskDescription("Voituroutai", img, Color.GREEN));
        }else{
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }

        res = getResources();

        setContentView(R.layout.activity_main);

        //////////////////////////////////////////////////////
        //           On identifie les elements              //
        //////////////////////////////////////////////////////


        share = (Button) findViewById(R.id.share);
        jeMeGare = (Button) findViewById(R.id.jeMeGare);
        jeRecupere = (Button) findViewById(R.id.jeRecupere);
        txtDernierEmplacement = (TextView) findViewById(R.id.txtDernierEmplacement);
        rl = (RelativeLayout) findViewById(R.id.layoutProgress);
        cancelProgress = (Button) findViewById(R.id.cancelProgress);
        imgLoadingBarre = (ImageView) findViewById(R.id.imgLoadingBarre);



        //////////////////////////////////////////////////////
        //          On met en place les animations          //
        //////////////////////////////////////////////////////

        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bouton);
        jeMeGare.startAnimation(animation);
        jeRecupere.startAnimation(animation);
//        jeMeGare.clearAnimation();
//        jeRecupere.clearAnimation();



        //////////////////////////////////////////////////////
        //          On initialise le presenter              //
        //////////////////////////////////////////////////////

        if (savedInstanceState == null) {
            presenter = new EmplacementVoiturePresenter(this);
        } else {
            presenter = new EmplacementVoiturePresenter(this);
            //presenter = PresenterManager.getInstance().restorePresenter(savedInstanceState);
        }

        //////////////////////////////////////////////////////
        //         On met en place les callbacks            //
        //////////////////////////////////////////////////////

        if(jeMeGare!=null) {
            jeMeGare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    presenter.clickJeMeGare();
                }
            });
        }

        if(jeRecupere!=null) {
            jeRecupere.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    presenter.ouvrirLocalisations();
                }
            });
        }

        if(cancelProgress!=null){
            cancelProgress.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    presenter.cancelProgress();
                    rl.setVisibility(View.INVISIBLE);
                    share.setEnabled(true);
                    share.setVisibility(View.VISIBLE);
                    jeMeGare.setEnabled(true);
                }
            });
        }
    }

    public EmplacementVoiturePresenter getPresenter(){
        return presenter;
    }

    @Override
    public Context getContext() {
        return getApplicationContext();
    }

    public void lancerNavigation(float latitude, float longitude) {

        Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                Uri.parse("http://maps.google.com/maps?daddr=" + latitude + "," + longitude));
        startActivity(intent);
    }

    @Override
    public void showConfirmation(String jour,String mois,String annee,String heure,String minute) {


        dConfirm = new Dialog(EmplacementVoitureView.this);
        dConfirm.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dConfirm.setContentView(R.layout.confirmation);
        dConfirm.closeOptionsMenu();
        dConfirm.setCancelable(false);
        dConfirm.show();


        TextView txtHautConfirmation = (TextView) dConfirm.findViewById(R.id.txtHautConfirmation);
        txtHautConfirmation.setText(getString(R.string.dernier_emplacement) + heure + "h" + minute + getString(R.string.le) + jour + " " + mois + " " + annee);

        ImageButton validerConfirmation = (ImageButton)dConfirm.findViewById(R.id.validerConfirmation);
        validerConfirmation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.enregistrerLocalisation();
                dConfirm.dismiss();
            }
        });

        ImageButton annulerConfirmation = (ImageButton)dConfirm.findViewById(R.id.annulerConfirmation);
        annulerConfirmation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dConfirm.cancel();
            }
        });
    }

    @Override
    public void showProgress(){
        jeMeGare.setEnabled(false);
        dProgress = new Dialog(EmplacementVoitureView.this);
        dProgress.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dProgress.setContentView(R.layout.progress);
        dProgress.show();

        progressCancelProgress = (ImageView) dProgress.findViewById(R.id.progressCancelProgress);
        loading = (ImageView) dProgress.findViewById(R.id.imageLocalisation);


        if(progressCancelProgress!=null){
            progressCancelProgress.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    presenter.cancelProgress();
                    dProgress.dismiss();
                    jeMeGare.setEnabled(true);
                }
            });
        }

        dProgress.setOnCancelListener(new Dialog.OnCancelListener(){
            @Override
            public void onCancel(DialogInterface dialog) {
                loading.clearAnimation();
                share.setEnabled(false);
                share.setVisibility(View.INVISIBLE);
                rl.setVisibility(View.VISIBLE);
                Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.loading);
                imgLoadingBarre.startAnimation(animation);
            }
        });


        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.loading);
        loading.startAnimation(animation);
    }

    @Override
    public void closeProgress() {
        if (dProgress.isShowing()){
            dProgress.dismiss();
        }
        else{
            rl.setVisibility(View.INVISIBLE);
            imgLoadingBarre.clearAnimation();
            share.setEnabled(true);
        }
    }

    @Override
    public void changerTexteDernierEmplacement(String jour, String mois, String annee, String heure, String minute) {

        txtDernierEmplacement.setText(getString(R.string.dernier_emplacement) + heure + "h" + minute + getString(R.string.le) + jour + " " + mois + " " + annee);
    }

    @Override
    public void succes() {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.localisation_reussie,
                (ViewGroup) findViewById(R.id.toast_reussi));

        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.BOTTOM, 0, 100);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();

        jeMeGare.setEnabled(true);
    }

    public void showCustomToast(String titre, String contenu){
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.toast_layout,
                (ViewGroup) findViewById(R.id.toast_layout_root));

        TextView tvTitre = (TextView) layout.findViewById(R.id.titreToast);
        TextView tvContenu = (TextView) layout.findViewById(R.id.contenuToast);
        tvTitre.setText(titre);
        tvContenu.setText(contenu);

        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.BOTTOM, 0, 100);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }


    /*          LIEN GOOGLE PLAY
    public void launchGooglePlay(){
        final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }
    */

    public boolean isRunning(){
        if(dProgress!=null){
            return dProgress.isShowing()|| (rl.getVisibility() == View.VISIBLE);
        }
        else{
            return (rl.getVisibility() == View.VISIBLE);
        }
    }


    public void share(View view){
        try
        { Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_SUBJECT, "Voituroutai");
            String sAux = "\nJe te recommande cette application !\n\n";
            sAux = sAux + "https://play.google.com/store/apps/details?id=com.project.vins.park \n\n";
            i.putExtra(Intent.EXTRA_TEXT, sAux);
            startActivity(Intent.createChooser(i, "Choisi comment la partager !"));
        }
        catch(Exception e)
        { //e.toString();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        jeMeGare.clearAnimation();
        jeRecupere.clearAnimation();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bouton);
        jeMeGare.startAnimation(animation);
        jeRecupere.startAnimation(animation);
    }
}
