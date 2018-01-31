package rahul.com.androidutility.utility.gmail;

import android.app.Activity;
import android.content.Context;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

/**
 * Created by Rahul Sadhu On 31/1/18.
 */

public class Utility {

    public static boolean checkPlayServices(Context mContext) {
        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int result = googleAPI.isGooglePlayServicesAvailable(mContext);
        if (result != ConnectionResult.SUCCESS) {
            //Google Play Services app is not available or version is not up to date. Error the
            // error condition here
            if (googleAPI.isUserResolvableError(result)) {
                googleAPI.getErrorDialog((Activity) mContext, result,
                        6).show();
            }
            return false;
        }
        //Google Play Services is available. Return true.
        return true;
    }
}
