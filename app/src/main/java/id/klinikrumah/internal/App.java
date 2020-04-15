package id.klinikrumah.internal;

import androidx.multidex.MultiDexApplication;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Arrays;
import java.util.List;

import de.greenrobot.event.EventBus;
import id.klinikrumah.internal.constant.Pref;
import id.klinikrumah.internal.util.SharedPref;

public class App extends MultiDexApplication {
    private static App instance;

    private EventBus eventBus;
    private Gson gson;

    public static synchronized App getInstance() {
        return instance;
    }

    public App() {
        instance = this;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        eventBus = new EventBus();
        GsonBuilder gsonBuilder = new GsonBuilder();
//        gsonBuilder.registerTypeAdapter(Facebook.class, new FacebookDeserializer());
        gson = gsonBuilder.create();

    }

    public EventBus getEventBus() {
        return eventBus;
    }

    public Gson getGson() {
        return gson;
    }

//    public List<Interest> getInterestList() {
//        String json = SharedPref.getString(Pref.CACHE, this, BaseID.Preferences.CACHE_INTEREST_OBJECT);
//        Interest[] objArray = getGson().fromJson(json, Interest[].class);
//        return Arrays.asList(objArray);
//    }
//
//    public List<Interest> getUserInterestList() {
//        String json = SharedPref.getString(Pref.CACHE, this, BaseID.Preferences.CACHE_USER_INTEREST_OBJECT);
//        Interest[] objArray = getGson().fromJson(json, Interest[].class);
//        return Arrays.asList(objArray);
//    }
}