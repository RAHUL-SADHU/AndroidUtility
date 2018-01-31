package rahul.com.androidutility.utility.facebook;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookAuthorizationException;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Rahul Sadhu
 */

public class FacebookUtils {

    public static final String TAG = FacebookUtils.class.getSimpleName();
    private CallbackManager callbackManager;
    private Context context;
    private FacebookListener listener;

    public FacebookUtils(Context context, FacebookListener facebookListener) {
        this.context = context;
        this.listener = facebookListener;
    }

    public void loginWithFacebook() {
        callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().logOut();
        LoginManager.getInstance().logInWithReadPermissions((Activity) context, Arrays.asList("email", "public_profile", "user_birthday", "user_hometown", "user_birthday"));


        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {

                                Bundle bFacebookData = getFacebookData(object);

                                if (bFacebookData != null)
                                    listener.facebookSuccess(bFacebookData);
                                else
                                    listener.facebookSuccess(null);
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,birthday,email,gender,picture.width(200).height(200),last_name,first_name,hometown,link,cover");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                listener.facebookError("Cancel");
            }

            @Override
            public void onError(FacebookException error) {

                if (error instanceof FacebookAuthorizationException) {
                    if (AccessToken.getCurrentAccessToken() != null) {
                        LoginManager.getInstance().logOut();
                    }
                }

                listener.facebookError(error.getMessage());
            }
        });

    }

    private Bundle getFacebookData(JSONObject object) {
        ArrayList<String> mPictureUrl = new ArrayList<>();

        Bundle bundle = new Bundle();
        String id = object.optString("id");
        JSONObject pictureJson = object.optJSONObject("picture");

        if (pictureJson != null) {
            JSONObject pictureData = pictureJson.optJSONObject("data");
            if (pictureData != null)
                bundle.putString(GlobalKeys.KEY_FB_PROFILE_IMAGE, pictureData.optString("url"));
            else
                bundle.putString(GlobalKeys.KEY_FB_PROFILE_IMAGE, "");
        } else
            bundle.putString(GlobalKeys.KEY_FB_PROFILE_IMAGE, "");

        JSONObject JOSource = object.optJSONObject("cover");
        if (JOSource != null) {
            bundle.putString((GlobalKeys.KEY_FB_COVER_PHOTO), JOSource.optString("source"));
        }


        bundle.putString(GlobalKeys.KEY_FB_ID, id);
        bundle.putString(GlobalKeys.KEY_FB_NAME, object.optString("name"));
        bundle.putString(GlobalKeys.KEY_FB_EMAIL, object.optString("email"));
        bundle.putString(GlobalKeys.KEY_FB_GENDER, object.optString("gender"));
        bundle.putString(GlobalKeys.KEY_FB_FIRSTNAME, object.optString("first_name"));
        bundle.putString(GlobalKeys.KEY_FB_LASTNAME, object.optString("last_name"));
        bundle.putString(GlobalKeys.KEY_FB_LINK, object.optString("link"));
        bundle.putString(GlobalKeys.KEY_FB_DOB, object.optString("birthday"));
        bundle.putStringArrayList(GlobalKeys.KEY_FB_PICTURE_LIST, mPictureUrl);
        return bundle;
    }

    public CallbackManager getCallback() {
        return callbackManager;
    }

    public void fbLogut() {
        LoginManager.getInstance().logOut();
    }

}
