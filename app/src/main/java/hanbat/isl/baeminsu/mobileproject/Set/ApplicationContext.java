package hanbat.isl.baeminsu.mobileproject.Set;

import android.app.Application;
import android.content.Context;

/**
 * Created by baeminsu on 2017. 12. 14..
 */

public class ApplicationContext extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
    }

    public static Context getContext() {
        return context;
    }
}
