package www.justme.co.in.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import www.justme.co.in.model.User;

import com.google.gson.Gson;

public class SessionManager {

    private final SharedPreferences mPrefs;
    SharedPreferences.Editor mEditor;
    public static String intro = "intro";
    public static String login = "login";
    public static String wallet = "wallet";
    public static String user = "users";
    public static String dcharge = "dcharge";
    public static String address = "address";

    public static String userDetails = "userDetails";


    public static String currency = "currency";
    public static String pincode = "pincode";
    public static String pincoded = "pincoded";
    public static String coupon = "coupon";
    public static String couponid = "couponid";
    public static String storename = "storename";
    public static String storeid = "storeid";


    public static String terms = "terms";
    public static String contact = "contact";
    public static String about = "about";
    public static String policy = "policy";

    public SessionManager(Context context) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        mEditor = mPrefs.edit();
    }

    public void setStringData(String key, String val) {
        mEditor.putString(key, val);
        mEditor.commit();
    }

    public String getStringData(String key) {
        return mPrefs.getString(key, "");
    }

    public void setFloatData(String key, float val) {
        mEditor.putFloat(key, val);
        mEditor.commit();
    }

    public float getFloatData(String key) {
        return mPrefs.getFloat(key, 0);
    }

    public void setBooleanData(String key, Boolean val) {
        mEditor.putBoolean(key, val);
        mEditor.commit();
    }

    public boolean getBooleanData(String key) {
        return mPrefs.getBoolean(key, false);
    }

    public void setIntData(String key, int val) {
        mEditor.putInt(key, val);
        mEditor.commit();
    }

    public int getIntData(String key) {
        return mPrefs.getInt(key, 0);
    }

    public void setUserDetails(String key, User val) {
        putObject("userDetails",val);
        /*mEditor.putString(key, new Gson().toJson(val));
        mEditor.commit();*/
    }

    public User getUserDetails(String key) {
       return getObject("userDetails",User.class);
    }

    public void putObject(String key, Object obj) {
        checkForNullKey(key);
        Gson gson = new Gson();
        putString(key, gson.toJson(obj));
    }

    public <T> T getObject(String key, Class<T> classOfT) {

        String json = getString(key);
        Object value = new Gson().fromJson(json, classOfT);
        if (value == null)
            throw new NullPointerException();
        return (T) value;
    }

    public String getString(String key) {
        return mPrefs.getString(key, "");
    }


    public void putString(String key, String value) {
        checkForNullKey(key);
        checkForNullValue(value);
        mPrefs.edit().putString(key, value).apply();
    }

    private void checkForNullKey(String key) {
        if (key == null) {
            throw new NullPointerException();
        }
    }

    private void checkForNullValue(String value) {
        if (value == null) {
            throw new NullPointerException();
        }
    }

    public void logoutUser() {
        mEditor.clear();
        mEditor.commit();
    }
}
