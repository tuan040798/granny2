package com.granny.guide2;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.granny.guide2.utils.BackUpModel;
import com.granny.guide2.utils.HttpHandler;
import com.ijvpbsdiwc.adx.service.InterstitialAdsManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.ExecutionException;

import vn.aib.ratedialog.RatingDialog;

public class MainActivity extends AppCompatActivity {

    private InterstitialAdsManager adsManager;

    private BackUpModel backUpModel;
    private String TAG = MainActivity.class.getSimpleName();

    public static String NATIVE_AD_ID = "ca-app-pub-3940256099942544/2247696110";
    public static String INTER_ID = "ca-app-pub-3940256099942544/1033173712";
    public static String BANNER_ID = "ca-app-pub-3940256099942544/6300978111";

    public static int PERCENT_SHOW_BANNER_AD = 100;
    public static int PERCENT_SHOW_INTER_AD = 100;
    public static int PERCENT_SHOW_NATIVE_AD = 100;
    public static int NUMBER_OF_NATIVE_AD = 1;

    private ImageView btnIntro, btnFindItem, btnVideo, btnKillSpider, btnQuit;
    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            Void aVoid = new GetBackUp().execute().get();
            if(backUpModel != null){
                if(!backUpModel.isLive){
                    new AlertDialog.Builder(MainActivity.this)
                            .setTitle("Notice from developer")
                            .setMessage("Please update the new application to continue using it. We are really sorry for this issue.")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    showApp(MainActivity.this, backUpModel.newAppPackage);
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setCancelable(false)
                            .show();
                }
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        adsManager = new InterstitialAdsManager();
        adsManager.init(true, this, INTER_ID, "#000000", getString(R.string.app_name));

        initView();
        setOclickListenerView();

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(MainActivity.INTER_ID);
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                loadAds();
            }

            @Override
            public void onAdFailedToLoad(int i) {

            }

        });

        SharedPreferences prefs = getSharedPreferences("rate_dialog", MODE_PRIVATE);

        Boolean rated = prefs.getBoolean("rate", false);
        if(!rated){
            showRateDialog();
        }
    }

    private class GetBackUp extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            // Making a request to url and getting response
            String url = "https://raw.githubusercontent.com/guidestore2/granny/master/backupdata.json";
            String jsonStr = sh.makeServiceCall(url);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    String appPackage = jsonObj.getString("appPackage");
                    Boolean isLive = jsonObj.getBoolean("isLive");
                    String newAppPackage = jsonObj.getString("newAppPackage");
                    Boolean isAdsShow = jsonObj.getBoolean("isAdsShow");
                    String inter = jsonObj.getString("inter");
                    String fb_inter = jsonObj.getString("fb_inter");
                    Boolean isShowGG = jsonObj.getBoolean("isShowGG");
                    String banner = jsonObj.getString("banner");
                    String nativeAd = jsonObj.getString("nativeAd");
                    String rewarded = jsonObj.getString("rewarded");
                    int percent_banner = jsonObj.getInt("percent_banner");
                    int percent_inter = jsonObj.getInt("percent_inter");
                    int percent_native = jsonObj.getInt("percent_native");
                    int numberNativeAd = jsonObj.getInt("numberNativeAd");

                    backUpModel = new BackUpModel();
                    backUpModel.appPackage = appPackage;
                    backUpModel.isLive = isLive;
                    backUpModel.newAppPackage = newAppPackage;
                    backUpModel.isAdsShow = isAdsShow;
                    backUpModel.inter = inter;
                    backUpModel.fb_inter = fb_inter;
                    backUpModel.isShowGG = isShowGG;
                    backUpModel.banner = banner;
                    backUpModel.nativeAd = nativeAd;
                    backUpModel.rewarded = rewarded;
                    backUpModel.percent_banner = percent_banner;
                    backUpModel.percent_inter = percent_inter;
                    backUpModel.percent_native = percent_native;
                    backUpModel.numberNativeAd = numberNativeAd;

                    INTER_ID = backUpModel.inter;
                    NATIVE_AD_ID = backUpModel.nativeAd;
                    BANNER_ID = backUpModel.banner;
                    PERCENT_SHOW_BANNER_AD = backUpModel.percent_banner;
                    PERCENT_SHOW_INTER_AD = backUpModel.percent_inter;
                    PERCENT_SHOW_NATIVE_AD = backUpModel.percent_native;
                    NUMBER_OF_NATIVE_AD = backUpModel.numberNativeAd;

                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });

                }

            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG).show();
                    }
                });
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
        }
    }

    public static void showApp(Context context, String pkg) {
        Intent intent = new Intent(new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://play.google.com/store/apps/details?id=" + pkg)));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }


    private void loadAds() {
        if (!mInterstitialAd.isLoading() && !mInterstitialAd.isLoaded()) {
            AdRequest adRequest = new AdRequest.Builder().build();
            mInterstitialAd.loadAd(adRequest);
        }

    }

    private void showInterstitial() {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            if(isOnline()){
                loadAds();
            } else {
                Toast.makeText(MainActivity.this, "Please check network connection!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public boolean isOnline() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int     exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        }
        catch (IOException e)          { e.printStackTrace(); }
        catch (InterruptedException e) { e.printStackTrace(); }

        return false;
    }

    private void initView(){
        btnIntro = findViewById(R.id.btn_intro);
        btnFindItem = findViewById(R.id.btn_find_item);
        btnVideo = findViewById(R.id.btn_video);
        btnKillSpider = findViewById(R.id.btn_kill_spider);
        btnQuit = findViewById(R.id.btn_quit);
    }

    private void setOclickListenerView(){
        btnIntro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, IntroActivity.class));
                Random r = new Random();
                int ads = r.nextInt(100);

                if (ads < MainActivity.PERCENT_SHOW_INTER_AD){
                    showInterstitial();
                }
            }
        });
        btnFindItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, FindItemActivity.class));
                Random r = new Random();
                int ads = r.nextInt(100);

                if (ads < MainActivity.PERCENT_SHOW_INTER_AD){
                    showInterstitial();
                }
            }
        });
        btnVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, VideosGuideActivity.class));
                Random r = new Random();
                int ads = r.nextInt(100);

                if (ads < MainActivity.PERCENT_SHOW_INTER_AD){
                    showInterstitial();
                }
            }
        });
        btnKillSpider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent videoIntent = new Intent(MainActivity.this, VideoActivity.class);
                videoIntent.putExtra("videoUrl", "http://data.aib.babylover.me/guide/granny/grannykill.mp4");
                startActivity(videoIntent);
                Random r = new Random();
                int ads = r.nextInt(100);

                if (ads < MainActivity.PERCENT_SHOW_INTER_AD){
                    showInterstitial();
                }
            }
        });
        btnQuit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                System.exit(0);
            }
        });
    }

    private void showRateDialog() {
        RatingDialog ratingDialog = new RatingDialog(this);
        ratingDialog.setRatingDialogListener(new RatingDialog.RatingDialogInterFace() {
            @Override
            public void onDismiss() {
            }

            @Override
            public void onSubmit(float rating) {
                rateApp(MainActivity.this);
                SharedPreferences.Editor editor = getSharedPreferences("rate_dialog", MODE_PRIVATE).edit();
                editor.putBoolean("rate", true);
                editor.commit();
            }

            @Override
            public void onRatingChanged(float rating) {
            }
        });
        ratingDialog.showDialog();
    }

    public static void rateApp(Context context) {
        Intent intent = new Intent(new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://play.google.com/store/apps/details?id=" + context.getPackageName())));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        System.exit(0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (adsManager != null)
            adsManager.onResume();
    }

}
