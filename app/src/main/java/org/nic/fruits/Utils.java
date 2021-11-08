package org.nic.fruits;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.widget.EditText;

import java.util.Locale;


//Programming by Harsha  for version 1.0 release
public final class Utils {
    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return (activeNetworkInfo != null && activeNetworkInfo.isConnected());
    }

    public static boolean isEmpty(EditText etText) {
        if (etText.getText().toString().trim().length() > 0)
            return false;

        return true;
    }

    public static Locale getCurrentLocale(Context context){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){

            System.out.println("calling aa locale is  " +context.getResources().getConfiguration().getLocales().get(0));
            return context.getResources().getConfiguration().getLocales().get(0);
        } else{
            //noinspection deprecation
            System.out.println("calling aa locale is  " +context.getResources().getConfiguration().locale);
            return context.getResources().getConfiguration().locale;
        }
    }
}