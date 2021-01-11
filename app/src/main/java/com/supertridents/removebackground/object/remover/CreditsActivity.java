package com.supertridents.removebackground.object.remover;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAdCallback;

public class CreditsActivity extends AppCompatActivity {

//    BillingClient billingClient;
//    List<String> skulist = new ArrayList<>();
//    String product = "my_test_product_12";

    private AdView mAdView;
    CardView watch,buy;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credits);
       getSupportActionBar().hide();
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.credit));

        watch = findViewById(R.id.watchads);
        buy = findViewById(R.id.buycredits);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        AdView adView = new AdView(this);

        adView.setAdSize(AdSize.BANNER);

        adView.setAdUnitId("ca-app-pub-5324429581828078/8058858771");

        mAdView = findViewById(R.id.adViewcredits);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                super.onAdLoaded();
                //Toast.makeText(CreditsActivity.this, "ad Loded", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdFailedToLoad(LoadAdError adError) {
                // Code to be executed when an ad request fails.
                super.onAdFailedToLoad(adError);
                mAdView.loadAd(adRequest);
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }

            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }
        });


        //Rewarded Ads
        SharedPreferences preferences = getSharedPreferences(MainActivity.creditInfo,MODE_PRIVATE);
        final int[] credits = {preferences.getInt(MainActivity.creditBalance, 0)};
        watch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(Constants.rewardedAd.isLoaded()){
                    Constants.rewardedAd.show(CreditsActivity.this, new RewardedAdCallback() {
                        @Override
                        public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                            credits[0] = credits[0] + 5;
                            SharedPreferences.Editor editor = getSharedPreferences(MainActivity.creditInfo,MODE_PRIVATE).edit();
                            editor.putInt(MainActivity.creditBalance,credits[0]);
                            editor.apply();
                            editor.commit();
                            Toast.makeText(CreditsActivity.this, "5 Credits Added", Toast.LENGTH_SHORT).show();
                            Constants.loadRewardedAd(CreditsActivity.this);
                        }
                    });
                }
                else
                {
                    Toast.makeText(CreditsActivity.this, "Please Wait, Ad is loading", Toast.LENGTH_SHORT).show();
                }
            }
        });

        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CreditsActivity.this, "Available Soon", Toast.LENGTH_SHORT).show();
            }
        });


        //In-app Purchases
//        billingClient = BillingClient.newBuilder(CreditsActivity.this).enablePendingPurchases().setListener(new PurchasesUpdatedListener() {
//            @Override
//            //This method starts when user buys a product
//            public void onPurchasesUpdated(BillingResult billingResult, @Nullable List<Purchase> list) {
//                if(list != null && billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK)
//                {
//                    for(Purchase purchase : list)
//                    {
//                        handlepurchase(purchase);
//                    }
//                }
//                else
//                {
//                    if(billingResult.getResponseCode() == BillingClient.BillingResponseCode.USER_CANCELED)
//                    {
//                        Toast.makeText(CreditsActivity.this, "Try Purchasing Again", Toast.LENGTH_SHORT).show();
//                    }
//                    else
//                    {
//                        if(billingResult.getResponseCode() == BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED)
//                        {
//                            Toast.makeText(CreditsActivity.this, "Already Purchased", Toast.LENGTH_SHORT).show();
//                            //We recover that method by consuming a purchase so that user can buy a product again from same account.
//                        }
//                    }
//                }
//            }
//        }).build();
//        billingClient.startConnection(new BillingClientStateListener() {
//            @Override
//            public void onBillingSetupFinished(BillingResult billingResult) {
//                if(billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK)
//                {
//                    Toast.makeText(CreditsActivity.this, "Successfully connected to Billing client", Toast.LENGTH_SHORT).show();
//                }
//                else
//                {
//                    Toast.makeText(CreditsActivity.this, "Failed to connect", Toast.LENGTH_SHORT).show();
//                }
//            }
//            @Override
//            public void onBillingServiceDisconnected() {
//                Toast.makeText(CreditsActivity.this, "Disconnected from the Client", Toast.LENGTH_SHORT).show();
//            }
//        });
//        skulist.add(product);
//        final SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
//        params.setSkusList(skulist).setType(BillingClient.SkuType.INAPP);  //Skutype.subs for Subscription
//        buy.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View edit.edit) {
//                billingClient.querySkuDetailsAsync(params.build(), new SkuDetailsResponseListener() {
//                    @Override
//                    public void onSkuDetailsResponse(BillingResult billingResult, List<SkuDetails> list) {
//                        if(list != null && billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK)
//                        {
//                            for(final SkuDetails skuDetails : list)
//                            {
//                                String sku = skuDetails.getSku(); // your Product id
//                                String price = skuDetails.getPrice(); // your product price
//                                String description = skuDetails.getDescription(); // product description
//                                //method opens Popup for billing purchase
//                                BillingFlowParams flowParams = BillingFlowParams.newBuilder()
//                                        .setSkuDetails(skuDetails)
//                                        .build();
//                                BillingResult responsecode = billingClient.launchBillingFlow(CreditsActivity.this,flowParams);
//                            }
//                        }
//                    }
//                });
//            }
//        });

    }

//    private void handlepurchase(Purchase purchase) {
//        try {
//            if (purchase.getPurchaseState() == Purchase.PurchaseState.PURCHASED) {
//                if (purchase.getSku().equals(product)) {
//                    ConsumeParams consumeParams = ConsumeParams.newBuilder()
//                            .setPurchaseToken(purchase.getPurchaseToken())
//                            .build();
//                    ConsumeResponseListener consumeResponseListener = new ConsumeResponseListener() {
//                        @Override
//                        public void onConsumeResponse(BillingResult billingResult, String s) {
//                            Toast.makeText(CreditsActivity.this, "Purchase Acknowledged", Toast.LENGTH_SHORT).show();
//                        }
//                    };
//                    billingClient.consumeAsync(consumeParams, consumeResponseListener);
//                    //now you can purchase same product again and again
//                    //Here we give coins to user.
//                    //tv.setText("Purchase Successful");
//                    Toast.makeText(this, "Purchase Successful", Toast.LENGTH_SHORT).show();
//                }
//            }
//        }
//        catch (Exception e)
//        {
//            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
//        }
//    }
//    @Override
//    public void onPurchasesUpdated(BillingResult billingResult, @Nullable List<Purchase> list) {
//        Toast.makeText(this, "onPurchases Updated", Toast.LENGTH_SHORT).show();
//    }
}