package com.pes.logik;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.pes.logik.Interfaces.Platform;

public class Android implements Platform, BillingProcessor.IBillingHandler {
    private AndroidLauncher launcher;
    public BillingProcessor bp;
    private boolean readyToPurchase = false;

    private static final String PRODUCT_ID = "logik.buy.coffee";
    private static final String LICENSE_KEY = "";
    private static final String MERCHANT_ID= null; //"";

    Android(AndroidLauncher launcher) {
        this.launcher = launcher;

        if(!BillingProcessor.isIabServiceAvailable(launcher)) {
            showToast("In-app billing service is unavailable, please upgrade Android Market/Play to version >= 3.9.16");
        }
        bp = new BillingProcessor(launcher.getApplicationContext(), LICENSE_KEY, MERCHANT_ID,this);
        bp.initialize();
    }

    @Override
    public void onProductPurchased(@NonNull String productId, @Nullable TransactionDetails details) {
//        showToast("product Purchased: " + productId);
        showToast(":-*");
        Boolean consumed = bp.consumePurchase(PRODUCT_ID);
//        if (consumed) showToast("Successfully consumed");
    }
    @Override
    public void onBillingError(int errorCode, @Nullable Throwable error) {
//        showToast("onBillingError: " + Integer.toString(errorCode));
    }
    @Override
    public void onBillingInitialized() {
//        showToast("on Billing Initialized");
        readyToPurchase = true;
    }
    @Override
    public void onPurchaseHistoryRestored() {
//        showToast("onPurchaseHistoryRestored");
/*
				for(String sku : bp.listOwnedProducts())
					Log.d(LOG_TAG, "Owned Managed Product: " + sku);
				for(String sku : bp.listOwnedSubscriptions())
					Log.d(LOG_TAG, "Owned Subscription: " + sku);
*/
    }

    public void showToast(String message) {
        Toast.makeText(launcher, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void buyCoffee() {
        bp.purchase(launcher, PRODUCT_ID);
    }

}
