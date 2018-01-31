package rahul.com.androidutility.utility.gmail;

import android.os.Bundle;

/**
 * Created by Rahul Sadhu
 */

public interface GoogleLogInListener {
    void GoogleLogInSuccess(Bundle bundle);

    void GoogleLogInError(String error);
}
