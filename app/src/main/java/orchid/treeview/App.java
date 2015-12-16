package orchid.treeview;

import android.app.Application;

import com.third.analytics.Analytics;

/**
 * Created by haoxiqiang on 15/12/10.
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Analytics.init(this, "8STJVH4D87BDDG4VXT9M");
    }
}
