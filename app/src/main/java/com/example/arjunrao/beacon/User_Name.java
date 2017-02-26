package com.example.arjunrao.beacon;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class User_Name extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener {
    private PrefManager2 prefManager;
    private static final String TAG = "SignInActivity";
    SignInButton signInButton;
    String email, name;
    LoginButton loginButton;
    GoogleApiClient mGoogleApiClient;
    private ProgressDialog mProgressDialog;

    private static final int RC_SIGN_IN = 9001;
    SQLiteDatabase db,db1;
    private GoogleSignInOptions googleSignInOptions;

    private Cursor cursor;
    private static final String EMAIL_PATTERN = "^[a-zA-Z0-9#_~!$&'()*+,;=:.\"(),:;<>@\\[\\]\\\\]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*$";
    private Pattern pattern = Pattern.compile(EMAIL_PATTERN);
    private Matcher matcher; //validate email
    String old_name;

    Button normalLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);





        setContentView(R.layout.activity_user__name);

        Beacon_Database dbHelp = new Beacon_Database(this);
        db = dbHelp.getReadableDatabase();

        cursor = db.query("LOGIN", new String[]{"NAME", "EMAIL"}, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            old_name = cursor.getString(0);
            Toast.makeText(User_Name.this, old_name, Toast.LENGTH_LONG).show();
            Intent intent = new Intent(User_Name.this, UserLocation2.class);
            startActivity(intent);
        }


        googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().requestId().build();
        mGoogleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this, this).addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions).build();

        /*SignInButton signInButton = (SignInButton) findViewById(R.id.sign_in_button); //google Integration
        signInButton.setSize(SignInButton.SIZE_WIDE);


        signInButton.setScopes(googleSignInOptions.getScopeArray());*/


       /* signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });*/


        Button mockLogin = (Button) findViewById(R.id.mockLogIn);
        mockLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });
//________________________________________________________________________________________________________________________
        Button normalLogin1 = (Button) findViewById(R.id.btn_login);
        normalLogin1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TextInputLayout nameWrapper = (TextInputLayout) findViewById(R.id.namewrapper);
                TextInputLayout emailWrapper = (TextInputLayout) findViewById(R.id.emailwrap);

                nameWrapper.setHint("Your Name");
                emailWrapper.setHint("Email");

                try {
                    name = nameWrapper.getEditText().getText().toString();
                    email = emailWrapper.getEditText().getText().toString();
                } catch (NullPointerException exc) {
                    AlertDialog.Builder dg = new AlertDialog.Builder(User_Name.this);
                    dg.setTitle("ERROR").setMessage("Please Fill Up all Fields").setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).show();
                }

                if (!validateEmail(email)) {
                    emailWrapper.setError("Not a valid email address!");

                } else {
                    emailWrapper.setErrorEnabled(false);

                    ProgressBar progressBar = (ProgressBar) findViewById(R.id.authProgress);
                    progressBar.setIndeterminate(true);
                    progressBar.setVisibility(View.VISIBLE);

                    Beacon_Database user_info = new Beacon_Database(User_Name.this);
                    db = user_info.getReadableDatabase();


                    ContentValues values1 = new ContentValues();
                    values1.put("NAME", name);
                    values1.put("EMAIL", email);

                    Toast.makeText(User_Name.this, "Welcome "+ name, Toast.LENGTH_LONG).show();


                    db.insert("LOGIN", null, values1);

                    progressBar.setVisibility(View.GONE);


                    Intent intent = new Intent(User_Name.this, UserLocation2.class);
                    startActivity(intent);

                }
            }
        });
    }

//____________________________________________________________________________________________________


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        //for Facebook

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult googleSignInResult = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            GoogleSignInAccount googleSignInAccount = googleSignInResult.getSignInAccount();

            try {
                email = googleSignInAccount.getEmail();
                name = googleSignInAccount.getDisplayName();

                //-------------
                Beacon_Database dbHelper = new Beacon_Database(User_Name.this);
                db = dbHelper.getReadableDatabase();

                //----------------try-----------


                ContentValues values1 = new ContentValues();
                values1.put("NAME", name);
                values1.put("EMAIL", email);


                db.insert("LOGIN", null, values1);

                Toast.makeText(User_Name.this, "Welcome " + name, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(User_Name.this,UserLocation2.class);
                startActivity(intent);

            } catch (NullPointerException exc) {
                Toast.makeText(User_Name.this, "Something Seems To Have Gone Wrong", Toast.LENGTH_LONG).show();
            }

        }
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        AlertDialog.Builder alertdialog = new AlertDialog.Builder(User_Name.this);
        alertdialog.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alertdialog.setTitle("Unexpected Error");
        alertdialog.setMessage("Try Restarting the app");
        alertdialog.show();
    }


    public boolean validateEmail(String email) {
        matcher = pattern.matcher(email);
        return matcher.matches();
    }
}