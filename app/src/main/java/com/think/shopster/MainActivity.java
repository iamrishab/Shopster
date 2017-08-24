package com.think.shopster;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.plus.Plus;


public class MainActivity extends FragmentActivity implements
        ConnectionCallbacks, OnConnectionFailedListener,
        View.OnClickListener {
    private static final int REQUEST_CODE_ASK_PERMISSIONS = 1;
    private String TAG="Shopster";
    private static final int SIGNED_IN = 0;
    private static final int STATE_SIGNING_IN = 1;
    private static final int STATE_IN_PROGRESS = 2;
    private static final int RC_SIGN_IN = 0;
//    public static int REQUEST_CODE_ASK_PERMISSIONS;
    private GoogleApiClient mGoogleApiClient;
    private int mSignInProgress;
    private PendingIntent mSignInIntent;

    private SignInButton mSignInButton;
    private TextView mSignInTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSignInButton = (SignInButton) findViewById(R.id.sign_in_button);
        mSignInTextView = (TextView) findViewById(R.id.sign_in_textview);
        mSignInTextView.setEnabled(false);
        // Add click listeners for the buttons
        mSignInButton.setOnClickListener(this);

        // Build a GoogleApiClient
        mGoogleApiClient = buildGoogleApiClient();

    }
    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }


    @Override
    public void onConnected(Bundle bundle) {


        try {
            if(android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                int hasWriteContactsPermission = checkSelfPermission(Manifest.permission.GET_ACCOUNTS);
                if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[] {Manifest.permission.GET_ACCOUNTS},
                            REQUEST_CODE_ASK_PERMISSIONS);
                    return;
                }
            }

        }
        catch(Exception ex){
            String exception = ex.getLocalizedMessage();
            String exceptionString = ex.toString();
            Log.d(TAG,exception);
            // Note that you should log these errors in a 'real' app to aid in debugging
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    mSignInButton.setEnabled(false);
                    // Indicate that the sign in process is complete.
                    mSignInProgress = SIGNED_IN;
                    String emailAddress = Plus.AccountApi.getAccountName(mGoogleApiClient);
                    Toast.makeText(this,"Signed In with Email: "+emailAddress,Toast.LENGTH_SHORT);
                    startActivity(new Intent(getBaseContext(),SearchActivity.class));
                } else {
                    // Permission Denied
                    Toast.makeText(this,"GET_ACCOUNTS Denied",Toast.LENGTH_LONG);
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (mSignInProgress != STATE_IN_PROGRESS) {
            mSignInIntent = connectionResult.getResolution();
            if (mSignInProgress == STATE_SIGNING_IN) {
                resolveSignInError();
            }
        }
        onSignedOut();
    }

    private void onSignedOut() {
        // Update the UI to reflect that the user is signed out.
        mSignInButton.setEnabled(true);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case RC_SIGN_IN:
                if (resultCode == RESULT_OK) {
                    mSignInProgress = STATE_SIGNING_IN;
                } else {
                    mSignInProgress = SIGNED_IN;
                }

                if (!mGoogleApiClient.isConnecting()) {
                    mGoogleApiClient.connect();
                }
                break;
        }
    }

    private void resolveSignInError() {
        if (mSignInIntent != null) {
            try {
                mSignInProgress = STATE_IN_PROGRESS;
                startIntentSenderForResult(mSignInIntent.getIntentSender(),
                        RC_SIGN_IN, null, 0, 0, 0);
                Toast.makeText(this,"Progress",Toast.LENGTH_LONG).show();
            } catch (IntentSender.SendIntentException e) {
                Toast.makeText(this,"Exception",Toast.LENGTH_LONG).show();
                mSignInProgress = STATE_SIGNING_IN;
                mGoogleApiClient.connect();
            }
        } else {
            Toast.makeText(this,"Play Services Error",Toast.LENGTH_LONG).show();
        }
    }

    private GoogleApiClient buildGoogleApiClient() {
        return new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API, Plus.PlusOptions.builder().build())
                .addScope(new Scope("email"))
                .build();
    }

    @Override
    public void onClick(View v) {
        if (!mGoogleApiClient.isConnecting()) {
            switch (v.getId()) {
                case R.id.sign_in_button:
                    resolveSignInError();
                    break;

            }
        }
    }
}
