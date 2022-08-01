package com.anns.appnghenhacso.presentation.Screen;


import android.view.View;
import android.widget.Button;

import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.android.billingclient.api.*;
import com.anns.appnghenhacso.R;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


public class Purchase extends AppCompatActivity implements PurchasesResponseListener{
    private Button upgrade_vip1,upgrade_vip2,upgrade_vip3,sub1,sub2;
    private PurchasesUpdatedListener purchasesUpdatedListener;
    private BillingClient billingClient;
    ImageView back_from_purchase;
    List<String> skuList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase);

        back_from_purchase = (ImageView) findViewById(R.id.back_from_purchase);
        back_from_purchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        upgrade_vip1 = (Button) findViewById(R.id.upgrade_vip1);
        upgrade_vip2 = (Button) findViewById(R.id.upgrade_vip2);
        upgrade_vip3 = (Button) findViewById(R.id.upgrade_vip3);
        sub1 = (Button) findViewById(R.id.sub1);
        sub2 = (Button) findViewById(R.id.sub2);

        skuList = new ArrayList<>();
        skuList.add("inapp10");
        skuList.add("inapp20");
        skuList.add("inapp30");
        skuList.add("buyapp10");
        skuList.add("buyapp20");

        purchasesUpdatedListener = new PurchasesUpdatedListener() {
            @Override
            public void onPurchasesUpdated(@NonNull @NotNull BillingResult billingResult, List<com.android.billingclient.api.Purchase> list) {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK
                        && list != null) {
                    for (com.android.billingclient.api.Purchase purchase : list) {
                        handlepurchase(purchase);
                    }
                } else if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.USER_CANCELED) {
                    // Handle an error caused by a user cancelling the purchase flow.
                } else {
                    // Handle any other error codes.
                }
            }
        };
        billingClient = BillingClient.newBuilder(Purchase.this)
                .setListener(purchasesUpdatedListener)
                .enablePendingPurchases()
                .build();
        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(BillingResult billingResult) {
                if (billingResult.getResponseCode() ==  BillingClient.BillingResponseCode.OK) {
                    // The BillingClient is ready. You can query purchases here.
                    SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
                    params.setSkusList(skuList).setType(BillingClient.SkuType.INAPP);
                    billingClient.querySkuDetailsAsync(params.build(),
                            new SkuDetailsResponseListener() {
                                @Override
                                public void onSkuDetailsResponse(BillingResult billingResult,List<SkuDetails> skuDetailsList) {
                                    System.out.println(skuDetailsList + "Được");
                                    upgrade_vip1.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            // Process the result.
                                            BillingFlowParams billingFlowParams = BillingFlowParams.newBuilder()
                                                    .setSkuDetails(skuDetailsList.get(0))
                                                    .build();
                                            billingClient.launchBillingFlow(Purchase.this, billingFlowParams);
                                        }
                                    });
                                    upgrade_vip2.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            // Process the result.
                                            BillingFlowParams billingFlowParams = BillingFlowParams.newBuilder()
                                                    .setSkuDetails(skuDetailsList.get(1))
                                                    .build();
                                            billingClient.launchBillingFlow(Purchase.this, billingFlowParams);
                                        }
                                    });
                                    upgrade_vip3.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            BillingFlowParams billingFlowParams = BillingFlowParams.newBuilder()
                                                    .setSkuDetails(skuDetailsList.get(2))
                                                    .build();
                                            billingClient.launchBillingFlow(Purchase.this, billingFlowParams);
                                        }
                                    });

                                }
                            });
                    SkuDetailsParams.Builder params2 = SkuDetailsParams.newBuilder();
                    params2.setSkusList(skuList).setType(BillingClient.SkuType.SUBS);
                    billingClient.querySkuDetailsAsync(params2.build(), new SkuDetailsResponseListener() {
                        @Override
                        public void onSkuDetailsResponse(BillingResult billingResult,List<SkuDetails> list) {
                            sub1.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    BillingFlowParams billingFlowParams = BillingFlowParams.newBuilder()
                                            .setSkuDetails(list.get(0))
                                            .build();
                                    billingClient.launchBillingFlow(Purchase.this, billingFlowParams);
                                }
                            });
                            sub2.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    BillingFlowParams billingFlowParams = BillingFlowParams.newBuilder()
                                            .setSkuDetails(list.get(1))
                                            .build();
                                    billingClient.launchBillingFlow(Purchase.this, billingFlowParams);
                                }
                            });
                        }
                    });
                }
            }
            @Override
            public void onBillingServiceDisconnected() {
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
            }
        });

    }
    private void handlepurchase(com.android.billingclient.api.Purchase purchase) {
        try {
            ConsumeParams consumeParams = ConsumeParams.newBuilder()
                    .setPurchaseToken(purchase.getPurchaseToken())
                    .build();
            ConsumeResponseListener consumeResponseListener = new ConsumeResponseListener() {
                @Override
                public void onConsumeResponse(BillingResult billingResult, String s) {
                    if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                        Toast.makeText(Purchase.this, "Purchase Acknowledged", Toast.LENGTH_SHORT).show();
                    }
                }
            };
            billingClient.consumeAsync(consumeParams, consumeResponseListener);
//                    int quantity = purchase.getQuantity(); // when you create a purchase item in play console then you can set quantity of purchase that how much pack a user can purchase at a time. give coins to user mltiply with that quantity.
//
//                    int value = 600;

            //now you can purchase same product again and again
            //Here we give coins to user.
            //Here we purchase 5 quantity quan: 5 , 600*5 = 3000 coins to user..
//                    tv.setText("Purchase Successful: quan: "+quantity+" , "+value*quantity);

//                    Toast.makeText(this, "Purchase Successful", Toast.LENGTH_SHORT).show();

        }
        catch (Exception e)
        {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onQueryPurchasesResponse(@NonNull @NotNull BillingResult billingResult, @NonNull @NotNull List<com.android.billingclient.api.Purchase> list) {

    }
}