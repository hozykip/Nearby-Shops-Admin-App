package org.nearbyshops.serviceprovider.Preferences;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import org.nearbyshops.serviceprovider.MyApplication;
import org.nearbyshops.serviceprovider.R;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by sumeet on 5/5/16.
 */
public class PrefGeneral {




    //    public static final String DEFAULT_SERVICE_URL = "http://taxireferral.org";
    public static final String DEFAULT_SERVICE_URL = "http://example.com";


    public static final String SERVICE_URL_LOCAL_HOTSPOT = "http://192.168.43.73:5121";
    public static final String SERVICE_URL_NEARBYSHOPS = "http://api.nearbyshops.org";

    private static final String TAG_PREF_CURRENCY = "currency_symbol";





//    public static void saveDistributorID(int distributorID)
//    {
//        Context context = MyApplication.getAppContext();
//        // Get a handle to shared preference
//        SharedPreferences sharedPref;
//        sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_name), context.MODE_PRIVATE);
//
//        // write to the shared preference
//        SharedPreferences.Editor editor = sharedPref.edit();
//        editor.putInt(context.getString(R.string.preference_distributor_id_key),distributorID);
//        editor.commit();
//
//    }
//
//    public static int getDistributorID(Context context) {
//        // Get a handle to shared preference
//        SharedPreferences sharedPref;
//        sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_name), context.MODE_PRIVATE);
//
//        // read from shared preference
//        int distributorID = sharedPref.getInt(context.getString(R.string.preference_distributor_id_key), 0);
//        return distributorID;
//    }



    public static void saveServiceURL(String service_url, Context context)
    {
        context = MyApplication.getAppContext();
        // get a handle to shared Preference
        SharedPreferences sharedPref;

        sharedPref = context.getSharedPreferences(
                context.getString(R.string.preference_file_name),
                context.MODE_PRIVATE);

        // write to the shared preference
        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putString(
                context.getString(R.string.preference_service_url_key),
                service_url);

        editor.apply();
    }








    public static String getServiceURL(Context context) {

        context = MyApplication.getAppContext();
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_name), Context.MODE_PRIVATE);

        //service_url = "http://localareademo-env.ap-southeast-1.elasticbeanstalk.com";

        return sharedPref.getString(context.getString(R.string.preference_service_url_key), SERVICE_URL_NEARBYSHOPS);
    }






    public static void saveServiceURL_GIDB(String service_url)
    {
        Context context = MyApplication.getAppContext();
        // get a handle to shared Preference
        SharedPreferences sharedPref;

        sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_name), Context.MODE_PRIVATE);

        // write to the shared preference
        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putString(context.getString(R.string.preference_service_url_gidb_key), service_url);
        editor.apply();
    }

    public static String getServiceURL_GIDB(Context context) {

        context = MyApplication.getAppContext();

        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_name), Context.MODE_PRIVATE);
        String service_url = sharedPref.getString(context.getString(R.string.preference_service_url_gidb_key), "http://nbsidb.nearbyshops.org");

        //http://192.168.1.35:5200
        //"http://nbsidb.nearbyshops.org"
        //http://192.168.1.35:5100
        //service_url = "http://localareademo-env.ap-southeast-1.elasticbeanstalk.com";

        return service_url;
    }


//    public static boolean isNetworkAvailable(Context context) {
//        ConnectivityManager connectivityManager
//                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
//        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
//    }


    public static String getImageEndpointURL(Context context)
    {
        return PrefGeneral.getServiceURL(context) + "/api/Images";
    }


    public static String getConfigImageEndpointURL(Context context)
    {
        return PrefGeneral.getServiceURL(context) + "/api/ServiceConfigImages";
    }




    public static void saveServiceURL_SDS(String service_url,Context context)
    {
//        Context context = MyApplication.getAppContext();
        // get a handle to shared Preference
        SharedPreferences sharedPref;

        sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_name), Context.MODE_PRIVATE);

        // write to the shared preference
        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putString(context.getString(R.string.preference_service_url_sds_key), service_url);
        editor.apply();
    }





    public static String getServiceURL_SDS(Context context) {

//        context = MyApplication.getAppContext();

        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_name), Context.MODE_PRIVATE);
        return sharedPref.getString(context.getString(R.string.preference_service_url_sds_key), "http://sds.nearbyshops.org");

        //http://192.168.1.35:5050
        //"http://sds.nearbyshops.org"
    }








    public static void saveCurrencySymbol(String symbol, Context context)
    {
        //Creating a shared preference
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_name), MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = sharedPref.edit();
        Gson gson = UtilityFunctions.provideGson();

        String json = gson.toJson(symbol);
        prefsEditor.putString(TAG_PREF_CURRENCY, json);
        prefsEditor.apply();
    }






    public static String getCurrencySymbol(Context context)
    {
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_name), MODE_PRIVATE);
        Gson gson = UtilityFunctions.provideGson();

        return sharedPref.getString(TAG_PREF_CURRENCY, context.getString(R.string.rupee_symbol));
    }




}
