package com.techpark.lastfmclient.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.techpark.lastfmclient.R;
import com.techpark.lastfmclient.api.auth.GetMobileSession;
import com.techpark.lastfmclient.api.ApiQuery;
import com.techpark.lastfmclient.api.user.UserHelpers;
import com.techpark.lastfmclient.tasks.ApiQueryTask;

import org.json.JSONException;
import org.json.JSONObject;


public class LoginActivity extends FragmentActivity implements LoaderManager.LoaderCallbacks<String> {

    private static final String LOG_TAG = LoginActivity.class.getName();

    private static final String USERNAME_BUNDLE = "username";
    private static final String PASSWORD_BUNDLE = "password";

    private AutoCompleteTextView mLoginView;
    private EditText mPassView;
    private Button mLoginButton;
    private ProgressBar mProgressBar;

    private static final int MIN_LEN = 3;

    private static final boolean DEBUG = true;

    private static final String NET_ERROR = "Network error. Try again.";
    private static final String BAD_CREDENTIALS = "Wrong login/pass.";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("ON CREATE", "START");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mLoginView = (AutoCompleteTextView) findViewById(R.id.login_input);
        mPassView = (EditText) findViewById(R.id.pass_input);
        mPassView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == R.id.login_form || actionId == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });


        mLoginButton = (Button) findViewById(R.id.comein);
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptLogin();
            }
        });
//        mLoginButton.setActivated(false);

        loginButtonChangeAppearance(mLoginView.getText(), mPassView.getText());


        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);

        mLoginView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                loginButtonChangeAppearance(s, mPassView.getText());
            }
        });

        mPassView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                loginButtonChangeAppearance(s, mLoginView.getText());
            }
        });

        Loader loader = getSupportLoaderManager().getLoader(0);
        if (loader != null) { // check for login task being executed
            showProgressBar(); // ok we are attempting to login now
            getSupportLoaderManager().initLoader(0, null, this);
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (DEBUG) {
            mLoginView.setText("SiCrash");
            mPassView.setText("112358132134");
            mLoginButton.callOnClick();
        }
    }

    private void showProgressBar() {
        mProgressBar.setVisibility(View.VISIBLE);
        mLoginButton.setVisibility(View.INVISIBLE);
    }

    private void loginButtonChangeAppearance(Editable s, Editable s1) {
        /* TODO add different states in resources for button */
        if (s.length() > MIN_LEN && s1.length() > MIN_LEN) {
            mLoginButton.setBackgroundColor(Color.parseColor("#D51007"));
            mLoginButton.setTextColor(Color.parseColor("#ffffff"));
            mLoginButton.setEnabled(true);
        } else {
            mLoginButton.setBackgroundColor(Color.parseColor("#E1E1E1"));
            mLoginButton.setTextColor(Color.parseColor("#ACACAC"));
            mLoginButton.setEnabled(false);
        }
    }

    private void attemptLogin() {

        //if (DEBUG) {
        //    launchMainActivity();
        //    return;
        //}

        String user = mLoginView.getText().toString();
        String pass = mPassView.getText().toString();

        if (user.length() < MIN_LEN || pass.length() < MIN_LEN) return;

        mLoginView.setError(null);
        mPassView.setError(null);

        showProgressBar();

        Bundle args = new Bundle();
        args.putString(USERNAME_BUNDLE, user);
        args.putString(PASSWORD_BUNDLE, pass);

        getSupportLoaderManager().restartLoader(0, args, LoginActivity.this);

    }

    @Override
    public Loader<String> onCreateLoader(int id, Bundle args) {
        ApiQuery query = new GetMobileSession(args.getString(USERNAME_BUNDLE), args.getString(PASSWORD_BUNDLE));
        query.prepare();
        Log.d(LOG_TAG, "CREATED LOADER");
        return new ApiQueryTask(LoginActivity.this, query);
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {

        mProgressBar.setVisibility(View.INVISIBLE);
        if (data != null) {
            try {
                JSONObject object = new JSONObject(data);
                if (!object.isNull("error")) {
                    // error occurs..
                    showError(BAD_CREDENTIALS, true);
                } else {

                    Bundle bundle = new Bundle(); /* TODO pass user data through activities in bundle */
                    JSONObject session = object.getJSONObject("session");
                    String name = session.getString("name");
                    String key = session.getString("key");

                    UserHelpers.saveUserSession(getApplicationContext(), key, name);

                    launchMainActivity();

                }
            } catch (JSONException e) {
            }

        } else {
            showError(NET_ERROR, true);
        }
        getSupportLoaderManager().destroyLoader(0); //
    }

    private void showError(String text, boolean reattempt) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
        if (reattempt) {
            mLoginButton.setVisibility(View.VISIBLE);
        }
    }


    private void launchMainActivity() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }


    @Override
    public void onLoaderReset(Loader<String> loader) {

    }
}
