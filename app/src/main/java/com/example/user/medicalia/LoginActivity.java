package com.example.user.medicalia;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.medicalia.models.Session;
import com.example.user.medicalia.remote.SessionAPI;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = LoginActivity.class.getSimpleName();
    private static final int REQUEST_SIGNUP = 0;

    private ProgressDialog progressDialog;

    @Bind(R.id.input_email)
    EditText _emailText;
    @Bind(R.id.input_password)
    EditText _passwordText;
    @Bind(R.id.btn_login)
    Button _loginButton;
    @Bind(R.id.link_signup)
    TextView _signupLink;

    String email;
    String password;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        SharedPreferences pref = getSharedPreferences("Medicalia", Context.MODE_PRIVATE);
        if(pref.getBoolean("isLogued", false)){
            Intent intent = new Intent(this, DrawerActivity.class);
            startActivity(intent);
            finish();
        }

        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });

        _signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
            }
        });
    }

    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }

        _loginButton.setEnabled(false);

        progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getString(R.string.authenticating));
        progressDialog.show();

        email = _emailText.getText().toString();
        password = _passwordText.getText().toString();

        // TODO: Implement your own authentication logic here.
        onLoginSuccess();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        // Disable going back to the LoginActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess() {
        _loginButton.setEnabled(true);

        Session session = new Session(email, password);
        SessionAPI.Factory.getInstance().login(session).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                int code = response.code();
                Gson gson = new Gson();

                switch (code){
                    case 201:
                        try {
                            Toast.makeText(LoginActivity.this, response.body().string(), Toast.LENGTH_SHORT).show();
                            Log.e(TAG, response.body().toString());

                            SharedPreferences.Editor editor = getSharedPreferences("Medicalia", Context.MODE_APPEND).edit();
                            editor.putBoolean("isLogued", true);
                            //editor.putString("token", );
                            editor.apply();
                            Intent intent = new Intent(getApplicationContext(), DrawerActivity.class);
                            startActivity(intent);
                            finish();
                        } catch (IOException e) {
                            Log.e(TAG, e.getMessage());
                        }

                        break;
                    case 422:
                        try {
                            JSONObject o =  new JSONObject(response.errorBody().string());
                            String message = (String) o.get("message");
                            Snackbar.make(getCurrentFocus(), message, Snackbar.LENGTH_SHORT).show();
                        } catch (IOException | JSONException e) {
                            Log.e(TAG, e.getMessage());
                        }
                        break;
                }

                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e(TAG, t.getMessage());
            }
        });
    }

    public void onLoginFailed() {
        _loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError(getString(R.string.valid_email));
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 8 || password.length() > 15) {
            _passwordText.setError(getString(R.string.between_8_characters));
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }

}
