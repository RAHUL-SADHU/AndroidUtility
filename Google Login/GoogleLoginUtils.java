package rahul.com.androidutility.utility.gmail;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import rahul.com.androidutility.utility.gmail.GlobalKeys;

/**
 * Created by Rahul Sadhu
 */

public class GoogleLoginUtils {
    private Context mContext;
    private GoogleSignInClient mGoogleSignInClient;
    private GoogleLogInListener mListener;

    public GoogleLoginUtils(Context mContext, GoogleLogInListener listener) {
        this.mContext = mContext;
        this.mListener = listener;
    }

    public void initClient() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(mContext, gso);

    }

    public void loginWithGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        ((Activity) mContext).startActivityForResult(signInIntent,GlobalKeys.GOOGLE_SIGN_IN);
    }

    public void setResult(Intent data) {
        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
        try {
            GoogleSignInAccount account = task.getResult(ApiException.class);
            ((BaseAppCompactActivity) mContext).hideProgressDialog();
            if (account != null) {
                Bundle bundle = new Bundle();
                bundle.putString(GlobalKeys.KEY_GOOGLE_ID, account.getId());
                bundle.putString(GlobalKeys.KEY_GOOGLE_NAME, account.getDisplayName());
                bundle.putString(GlobalKeys.KEY_GOOGLE_EMAIL, account.getEmail());
                if (account.getPhotoUrl() != null) {
                    bundle.putString(GlobalKeys.KEY_GOOGLE_PROFILE_IMAGE, account.getPhotoUrl().toString());
                }
                mListener.GoogleLogInSuccess(bundle);
            } else {

                mListener.GoogleLogInError("Account data is null.");
            }


        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
          /*  Log.w(GlobalKeys.LOG_TAG, "signInResult:failed code=" + e.getStatusCode());*/
            mListener.GoogleLogInError(e.getMessage());

        }
    }


    public void signOut() {
        mGoogleSignInClient.signOut();
    }


}
