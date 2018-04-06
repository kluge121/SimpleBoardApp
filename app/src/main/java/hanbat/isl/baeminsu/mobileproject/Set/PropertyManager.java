package hanbat.isl.baeminsu.mobileproject.Set;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by baeminsu on 2017. 12. 14..
 */

public class PropertyManager {

    private static PropertyManager instance;

    public static PropertyManager getInstance() {
        if (instance == null) {
            instance = new PropertyManager();
        }
        return instance;
    }

    SharedPreferences mSp;
    SharedPreferences.Editor mEditor;

    private PropertyManager() {
        mSp = PreferenceManager.getDefaultSharedPreferences(ApplicationContext.getContext());
        mEditor = mSp.edit();
    }


    private static final String AUTO_ID = "id";
    private static final String AUTO_PASS = "pass";
    private static final String AUTO_BOOLEAN = "bool";

    //f

    public void setAutoLogin(String id, String pass, boolean value) {
        mEditor.putBoolean(AUTO_BOOLEAN, value);
        mEditor.putString(AUTO_ID, id);
        mEditor.putString(AUTO_PASS, pass);
        mEditor.commit();

    }

    public void setAutoLogin(boolean value) {
        mEditor.putBoolean(AUTO_BOOLEAN, value);

        mEditor.commit();

    }

    public boolean getAutoLogin() {
        return mSp.getBoolean(AUTO_BOOLEAN, false);

    }


    public String getId() {
        return mSp.getString(AUTO_ID, "");
    }

    public String getPass() {
        return mSp.getString(AUTO_PASS, "");
    }

}
