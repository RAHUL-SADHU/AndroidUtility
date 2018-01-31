package rahul.com.androidutility.utility.facebook;

import android.os.Bundle;

/**
 * Created by Rahul Sadhu
 */

public interface FacebookListener {

    void facebookSuccess(Bundle bundle);

    void facebookError(String error);
}
