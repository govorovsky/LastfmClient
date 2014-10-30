package com.techpark.lastfmclient.activities;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
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
import com.techpark.lastfmclient.tasks.ApiQueryTask;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by andrew on 11.10.14.
 */
public class LoginActivity extends Activity implements LoaderManager.LoaderCallbacks<String> {

    private AutoCompleteTextView mLoginView;
    private EditText mPassView;
    private Button mLoginButton;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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

        Loader loader = getLoaderManager().getLoader(0);
        if (loader != null) { // check for login task being executed
            showProgressBar(); // ok we are attempting to login now
            Log.d("LOADER", "NOT NULL");
            getLoaderManager().initLoader(0, null, this);
        }
    }

    private void showProgressBar() {
        mProgressBar.setVisibility(View.VISIBLE);
        mLoginButton.setVisibility(View.INVISIBLE);
    }

    private void loginButtonChangeAppearance(Editable s, Editable s1) {
        /* TODO add different states in resources for button */
        if (s.length() > 3 && s1.length() > 3) {
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

        String user = mLoginView.getText().toString();
        String pass = mPassView.getText().toString();
        mLoginView.setError(null);
        mPassView.setError(null);

        showProgressBar();

        Bundle args = new Bundle();
        args.putString("username", user);
        args.putString("password", pass);

        getLoaderManager().restartLoader(0, args, LoginActivity.this);

    }

    @Override
    public Loader<String> onCreateLoader(int id, Bundle args) {
        ApiQuery query = new GetMobileSession(args.getString("username"), args.getString("password"));
        query.prepare();
        Log.d("tttt", "CREATED LOADER");
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
                    mLoginButton.setVisibility(View.VISIBLE);
                    Toast.makeText(this, "Wrong login/pass", Toast.LENGTH_LONG).show();
                } else {
                    Bundle bundle = new Bundle();
                    JSONObject session = object.getJSONObject("session");
                    String name = session.getString("name");
                    String key = session.getString("key");
                    Toast.makeText(this, "OK, +" + key, Toast.LENGTH_LONG).show();

                    saveSession(key);

                }
            } catch (JSONException e) {
            }

        }
        getLoaderManager().destroyLoader(0);

    }

    private void saveSession(String session) {
        SharedPreferences preferences = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("session", session);
        editor.commit();
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {

    }
}
