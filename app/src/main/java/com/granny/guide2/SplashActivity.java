package com.granny.guide2;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.granny.guide2.utils.BackUpModel;
import com.granny.guide2.utils.HttpHandler;
import com.ijvpbsdiwc.adx.service.InterstitialAdsManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class SplashActivity extends AppCompatActivity implements Animation.AnimationListener {

    Animation animFadein;
    ImageView imgUnity;

//    private InterstitialAdsManager adsManager;
//
//    private BackUpModel backUpModel;
//    private String TAG = MainActivity.class.getSimpleName();
//
//    public static String NATIVE_AD_ID = "ca-app-pub-3940256099942544/2247696110";
//    public static String INTER_ID = "ca-app-pub-3940256099942544/1033173712";
//    public static String BANNER_ID = "ca-app-pub-3940256099942544/6300978111";
//
//    public static int PERCENT_SHOW_BANNER_AD = 100;
//    public static int PERCENT_SHOW_INTER_AD = 100;
//    public static int PERCENT_SHOW_NATIVE_AD = 100;
//    public static int NUMBER_OF_NATIVE_AD = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_splash);

//        try {
//            Void aVoid = new GetBackUp().execute().get();
//            if(backUpModel != null){
//                if(!backUpModel.isLive){
//                    new AlertDialog.Builder(SplashActivity.this)
//                            .setTitle("Notice from developer")
//                            .setMessage("Please update the new application to continue using it. We are really sorry for this issue.")
//                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int which) {
//                                    showApp(SplashActivity.this, backUpModel.newAppPackage);
//                                }
//                            })
//                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int which) {
//                                    dialog.cancel();
//                                }
//                            })
//                            .setIcon(android.R.drawable.ic_dialog_alert)
//                            .setCancelable(false)
//                            .show();
//                }
//            }
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        adsManager = new InterstitialAdsManager();
//        adsManager.init(true, this, INTER_ID, "#000000", getString(R.string.app_name));

        imgUnity = findViewById(R.id.unity);

        animFadein = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fadein);
        // set animation listener
        animFadein.setAnimationListener(this);

        imgUnity.startAnimation(animFadein);

    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        Intent intentMain = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(intentMain);
        SplashActivity.this.finish();
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

//    private class GetBackUp extends AsyncTask<Void, Void, Void> {
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//
//        }
//
//        @Override
//        protected Void doInBackground(Void... arg0) {
//            HttpHandler sh = new HttpHandler();
//            // Making a request to url and getting response
//            String url = "https://raw.githubusercontent.com/dev4team92/granny_guide/master/backupdata.json";
//            String jsonStr = sh.makeServiceCall(url);
//
//            if (jsonStr != null) {
//                try {
//                    JSONObject jsonObj = new JSONObject(jsonStr);
//                    String appPackage = jsonObj.getString("appPackage");
//                    Boolean isLive = jsonObj.getBoolean("isLive");
//                    String newAppPackage = jsonObj.getString("newAppPackage");
//                    Boolean isAdsShow = jsonObj.getBoolean("isAdsShow");
//                    String inter = jsonObj.getString("inter");
//                    String fb_inter = jsonObj.getString("fb_inter");
//                    Boolean isShowGG = jsonObj.getBoolean("isShowGG");
//                    String banner = jsonObj.getString("banner");
//                    String nativeAd = jsonObj.getString("nativeAd");
//                    String rewarded = jsonObj.getString("rewarded");
//                    int percent_banner = jsonObj.getInt("percent_banner");
//                    int percent_inter = jsonObj.getInt("percent_inter");
//                    int percent_native = jsonObj.getInt("percent_native");
//                    int numberNativeAd = jsonObj.getInt("numberNativeAd");
//
//                    backUpModel = new BackUpModel();
//                    backUpModel.appPackage = appPackage;
//                    backUpModel.isLive = isLive;
//                    backUpModel.newAppPackage = newAppPackage;
//                    backUpModel.isAdsShow = isAdsShow;
//                    backUpModel.inter = inter;
//                    backUpModel.fb_inter = fb_inter;
//                    backUpModel.isShowGG = isShowGG;
//                    backUpModel.banner = banner;
//                    backUpModel.nativeAd = nativeAd;
//                    backUpModel.rewarded = rewarded;
//                    backUpModel.percent_banner = percent_banner;
//                    backUpModel.percent_inter = percent_inter;
//                    backUpModel.percent_native = percent_native;
//                    backUpModel.numberNativeAd = numberNativeAd;
//
//                    INTER_ID = backUpModel.inter;
//                    NATIVE_AD_ID = backUpModel.nativeAd;
//                    BANNER_ID = backUpModel.banner;
//                    PERCENT_SHOW_BANNER_AD = backUpModel.percent_banner;
//                    PERCENT_SHOW_INTER_AD = backUpModel.percent_inter;
//                    PERCENT_SHOW_NATIVE_AD = backUpModel.percent_native;
//                    NUMBER_OF_NATIVE_AD = backUpModel.numberNativeAd;
//
//                } catch (final JSONException e) {
//                    Log.e(TAG, "Json parsing error: " + e.getMessage());
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            Toast.makeText(getApplicationContext(),
//                                    "Json parsing error: " + e.getMessage(),
//                                    Toast.LENGTH_LONG).show();
//                        }
//                    });
//
//                }
//
//            } else {
//                Log.e(TAG, "Couldn't get json from server.");
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Toast.makeText(getApplicationContext(),
//                                "Couldn't get json from server. Check LogCat for possible errors!",
//                                Toast.LENGTH_LONG).show();
//                    }
//                });
//            }
//
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Void result) {
//            super.onPostExecute(result);
//        }
//    }

    public static void showApp(Context context, String pkg) {
        Intent intent = new Intent(new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://play.google.com/store/apps/details?id=" + pkg)));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        if (adsManager != null)
//            adsManager.onResume();
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


}
