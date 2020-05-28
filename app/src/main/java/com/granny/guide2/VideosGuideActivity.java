package com.granny.guide2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.formats.UnifiedNativeAd;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class VideosGuideActivity extends AppCompatActivity {

    private AdLoader adLoader;

    // List of MenuItems and native ads that populate the RecyclerView.
    private List<Object> mRecyclerViewItems = new ArrayList<>();

    // List of native ads that have been successfully loaded.
    private List<UnifiedNativeAd> mNativeAds = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videos_guide);

        if (savedInstanceState == null) {
            Fragment loadingScreenFragment = new LoadingScreenFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.fragment_container, loadingScreenFragment);

            transaction.commitAllowingStateLoss();

            addMenuItemsFromJson();
            loadNativeAds();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        onBackPressed();
        return true;
    }


    public List<Object> getRecyclerViewItems() {
        return mRecyclerViewItems;
    }

    private void insertAdsInMenuItems() {
        if (mNativeAds.size() <= 0) {
            return;
        }

        int offset = (mRecyclerViewItems.size() / mNativeAds.size()) + 1;
        int index = 0;
        for (UnifiedNativeAd ad : mNativeAds) {
            mRecyclerViewItems.add(index, ad);
            index = index + offset;
        }
        loadMenu();
    }

    private void loadNativeAds() {

        AdLoader.Builder builder = new AdLoader.Builder(this, MainActivity.NATIVE_AD_ID);
        adLoader = builder.forUnifiedNativeAd(
                new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
                    @Override
                    public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                        // A native ad loaded successfully, check if the ad loader has finished loading
                        // and if so, insert the ads into the list.
                        mNativeAds.add(unifiedNativeAd);
                        if (!adLoader.isLoading()) {
                            insertAdsInMenuItems();
                        }
                    }
                }).withAdListener(
                new AdListener() {
                    @Override
                    public void onAdFailedToLoad(int errorCode) {
                        // A native ad failed to load, check if the ad loader has finished loading
                        // and if so, insert the ads into the list.
                        Log.e("MainActivity", "The previous native ad failed to load. Attempting to"
                                + " load another.");
                        if (!adLoader.isLoading()) {
                            insertAdsInMenuItems();
                        }
                    }
                }).build();

        // Load the Native ads.
        adLoader.loadAds(new AdRequest.Builder().build(), MainActivity.NUMBER_OF_NATIVE_AD);
    }

    private void loadMenu() {
        // Create new fragment and transaction
        Fragment newFragment = new RecyclerViewFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack
        transaction.replace(R.id.fragment_container, newFragment);
//        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commitAllowingStateLoss();
    }

    /**
     * Adds {@link MenuItem}'s from a JSON file.
     */
    private void addMenuItemsFromJson() {
        try {
            String jsonDataString = readJsonDataFromFile();
            JSONArray menuItemsJsonArray = new JSONArray(jsonDataString);

            for (int i = 0; i < menuItemsJsonArray.length(); ++i) {

                JSONObject menuItemObject = menuItemsJsonArray.getJSONObject(i);

                String menuItemName = menuItemObject.getString("name");
                String menuItemImageName = menuItemObject.getString("photo");
                String video = menuItemObject.getString("video");

                MenuItem menuItem = new MenuItem(menuItemName, menuItemImageName, video);
                mRecyclerViewItems.add(menuItem);
            }
        } catch (IOException | JSONException exception) {
            Log.e(MainActivity.class.getName(), "Unable to parse JSON file.", exception);
        }
    }

    /**
     * Reads the JSON file and converts the JSON data to a {@link String}.
     *
     * @return A {@link String} representation of the JSON data.
     * @throws IOException if unable to read the JSON file.
     */
    private String readJsonDataFromFile() throws IOException {

        InputStream inputStream = null;
        StringBuilder builder = new StringBuilder();

        try {
            String jsonDataString = null;
            inputStream = getResources().openRawResource(R.raw.menu_item);
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(inputStream, "UTF-8"));
            while ((jsonDataString = bufferedReader.readLine()) != null) {
                builder.append(jsonDataString);
            }
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }

        return new String(builder);
    }
}
