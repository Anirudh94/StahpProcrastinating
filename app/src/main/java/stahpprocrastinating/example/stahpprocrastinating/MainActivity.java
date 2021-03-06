package stahpprocrastinating.example.stahpprocrastinating;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.TextView;

import com.braintreepayments.api.Braintree;
import com.braintreepayments.api.dropin.BraintreePaymentActivity;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphRequestAsyncTask;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends FragmentActivity {

    private LoginButton loginBtn;
    private TextView username;
    private CallbackManager callbackManager;

    private static final int PAYMENT_AUTH_REQ = 100;
    private String clientToken;
    private boolean isBraintreeSetup = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AccessTokenTracker accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken newAccessToken) {
                updateWithToken(newAccessToken);
            }
        };
        updateWithToken(AccessToken.getCurrentAccessToken());
        setContentView(R.layout.activity_main);
        loginBtn = (LoginButton) findViewById(R.id.login_button);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onFbLogin();
            }
        });
        getClientToken();
    }

    private void showNextActivity() {
        Intent goNext = new Intent(getBaseContext(), ListGoalsActivity.class);
        startActivity(goNext);
    }

    private void updateWithToken(AccessToken currentAccessToken) {
        if (currentAccessToken != null) {
            showNextActivity();
        }
    }

    private void onFbLogin() {
        callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("email", "user_friends", "public_profile","read_custom_friendlists"));
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {

                        System.out.println("Success");
                        GraphRequest.newMeRequest(
                                loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(final JSONObject json, GraphResponse response) {
                                        if (response.getError() != null) {
                                            // handle error
                                            System.out.println("ERROR");
                                        } else {
                                            System.out.println("Success");
                                            try {


                                                String jsonresult = String.valueOf(json);
                                                System.out.println("JSON Result" + jsonresult);

//                                                String str_email = json.getString("email");
                                                String str_id = json.getString("id");
                                                String str_name = json.getString("name");
                                                GraphRequestAsyncTask async = new GraphRequest(
                                                        AccessToken.getCurrentAccessToken(),
                                                        "/"+str_id+"/friendlists",
                                                        null,
                                                        HttpMethod.GET,
                                                        new GraphRequest.Callback() {
                                                            public void onCompleted(GraphResponse response) {
                                                                JSONObject json_result = response.getJSONObject();
                                                                try {
                                                                    global.facebook_friends = json_result.getJSONArray("data");
                                                                } catch (JSONException e) {
                                                                    e.printStackTrace();
                                                                }
                                                                //getJSONObject("data");

                                                                //        getJSONArray("data");


//                                                                        getJSONObject("paging").getString("next");

            /* handle the result */
                                                            }
                                                        }
                                                ).executeAsync();


                                                // save to mongodb here
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            } finally {
                                                Intent goNext = new Intent(getBaseContext(), ListGoalsActivity.class);
                                                startActivity(goNext);
                                                // TODO: CHECK IF USER IS IN DATABASE ALREADY
                                                onBraintreeSubmit();
                                            }
                                        }
                                    }

                                }).executeAsync();

                    }

                    @Override
                    public void onCancel() {

                    }

                    @Override
                    public void onError(FacebookException error) {

                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PAYMENT_AUTH_REQ && resultCode == BraintreePaymentActivity.RESULT_OK) {
            String nonce = data.getStringExtra(BraintreePaymentActivity.EXTRA_PAYMENT_METHOD_NONCE);
            postNonceToServer(nonce);
            showNextActivity();
        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void getClientToken() {
        AsyncHttpClient client = new AsyncHttpClient();

        TextHttpResponseHandler handler = new TextHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String clientToken) {
                MainActivity.this.clientToken = clientToken;
                Braintree.setup(MainActivity.this, clientToken, new Braintree.BraintreeSetupFinishedListener() {
                    @Override
                    public void onBraintreeSetupFinished(boolean setupSuccessful, Braintree braintree, String errorMessage, Exception exception) {
                        if (setupSuccessful) {
                            isBraintreeSetup = true;
                        }
                    }
                });
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                // Do nothing.
            }
        };

        client.get("http://10.0.2.2:3000/client_token", handler);
    }

    void postNonceToServer(String nonce) {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("payment_method_nonce", nonce);
        client.post("http://10.0.2.2:3000/checkout", params,
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        //TODO: Handle response
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        // Do nothing
                    }
                }
        );
    }

    public void onBraintreeSubmit() {
        //ignore request until braintree is setup
        if (isBraintreeSetup) {
            Intent intent = new Intent(getApplicationContext(), BraintreePaymentActivity.class);
            intent.putExtra(BraintreePaymentActivity.EXTRA_CLIENT_TOKEN, clientToken);
            startActivityForResult(intent, PAYMENT_AUTH_REQ);
        }
    }
}
