package id.klinikrumah.internal;

import androidx.multidex.MultiDexApplication;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import de.greenrobot.event.EventBus;
import id.klinikrumah.internal.constant.Pref;
import id.klinikrumah.internal.model.GoogleUserData;
import id.klinikrumah.internal.util.static_.SharedPref;

public class App extends MultiDexApplication {
    private static App instance;

    private EventBus eventBus;
    private Gson gson;

    public App() {
        instance = this;
    }

    public static synchronized App getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        eventBus = new EventBus();
        GsonBuilder gsonBuilder = new GsonBuilder();
//        gsonBuilder.registerTypeAdapter(Facebook.class, new FacebookDeserializer());
        gsonBuilder.setLenient();
        gson = gsonBuilder.create();

    }

    public EventBus getEventBus() {
        return eventBus;
    }

    public Gson getGson() {
        return gson;
    }

    public boolean isLogin() {
        return SharedPref.getBoolean(Pref.CACHE_LOGIN);
    }

    public void setLogin(boolean isLogin) {
        SharedPref.putBoolean(Pref.CACHE_LOGIN, isLogin);
    }

    public GoogleUserData getAccountGoogle() {
        return gson.fromJson(SharedPref.getString(Pref.CACHE_ACCOUNT_GOOGLE), GoogleUserData.class);
    }

    public void setAccountGoogle(GoogleUserData userData) {
        SharedPref.putString(Pref.CACHE_ACCOUNT_GOOGLE, gson.toJson(userData));
    }

    public String getAuthToken() {
        return SharedPref.getString(Pref.CACHE_AUTH_TOKEN);
    }

    public void setAuthToken(String authToken) {
        SharedPref.putString(Pref.CACHE_AUTH_TOKEN, authToken);
    }
//    public List<Interest> getUserInterestList() {
//        String json = SharedPref.getString(Pref.CACHE, this, BaseID.Preferences.CACHE_USER_INTEREST_OBJECT);
//        Interest[] objArray = getGson().fromJson(json, Interest[].class);
//        return Arrays.asList(objArray);
//    }
}