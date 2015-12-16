package com.third.analytics;

import android.content.Context;
import android.os.Build;

import com.flurry.android.FlurryAgent;

/**
 * Created by haoxiqiang on 15/12/10.
 */
public final class Analytics {

    public static void init(Context context, String appKey) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            initFlurry(context, appKey);
        }
    }

    private static void initFlurry(Context context, String appKey) {
        // configure Flurry
        FlurryAgent.setLogEnabled(true);
        // init Flurry
        FlurryAgent.init(context, appKey);
    }
}
