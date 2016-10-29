package overall.brandon.messageapp;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

/**
 * Created by Brandon on 10/29/2016.
 */

public class SharedPrefManager {
    SharedPreferences prefManager;
    Context context;

    public SharedPrefManager(Context context) {
        this.context = context;
        this.prefManager = context.getSharedPreferences("prefs",Context.MODE_PRIVATE);
    }

    public void saveObject(String name, Object object) {
        SharedPreferences.Editor prefsEditor = prefManager.edit();
        Gson gson = new Gson();
        prefsEditor.putString(name, gson.toJson(object));
        prefsEditor.commit();
    }

    public User retrieveUser() {
        Gson gson = new Gson();
        return gson.fromJson(prefManager.getString("user", ""),User.class);
    }
}
