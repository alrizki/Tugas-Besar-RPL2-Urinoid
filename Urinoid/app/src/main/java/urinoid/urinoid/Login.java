package urinoid.urinoid;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatCheckBox;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import urinoid.urinoid.database.Users;

public class Login extends AppCompatActivity implements View.OnClickListener,GoogleApiClient.OnConnectionFailedListener{

    public static final String Nama = "nama";;
    public static final String Username= "username";
    public static final String Password = "password";

    private EditText username;
    private EditText password;
    private AppCompatCheckBox checkbox;
    private SignInButton googleSignIn;
    private GoogleApiClient googleApiClient;
    private static final int REQ_CODE = 9001;

    boolean doubleTap = false;

    Button btnsignin;

    @BindView(R.id.registrationLink) TextView _registrationLink;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        checkbox = findViewById(R.id.checkbox);
        username = findViewById(R.id.Username);
        password = findViewById(R.id.password);
        btnsignin = findViewById(R.id.btnSignin);
        googleSignIn = findViewById(R.id.googleSignIn);
        googleSignIn.setOnClickListener(this);
        GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this,this).addApi(Auth.GOOGLE_SIGN_IN_API,signInOptions).build();

        _registrationLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Registrasi.class);
                startActivity(intent);
                finish();
            }
        });

        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (!isChecked) {
                    // show password
                    password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                } else {
                    // hide password
                    password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
            }
        });

        //init firebase
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference Users = database.getReference("User");


        btnsignin.setOnClickListener(new  View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!validateUsername() | !validatePassword()) {
                    return;
                }

                final ProgressDialog mDialog = new ProgressDialog(Login.this);
                mDialog.setMessage("Mohon menunggu..");
                mDialog.show();

                Users.addListenerForSingleValueEvent(new ValueEventListener() {


                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //cek jika user tidak terdaftar didatabase
                        if (dataSnapshot.child(username.getText().toString()).exists()) {


                            //ambil data user
                            mDialog.dismiss();
                            final Users users = dataSnapshot.child(username.getText().toString()).getValue(Users.class);
                            if (users.getPassword().equals(password.getText().toString())) {
                                Toast.makeText(Login.this, "Login Berhasil", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), Chatbot.class);

                                intent.putExtra(Nama, users.getNama());
                                intent.putExtra(Username, users.getUsername());
                                intent.putExtra(Password, users.getPassword());

                                startActivity(intent);
                            } else {
                                Toast.makeText(Login.this, "Usename/Password Salah!", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            mDialog.dismiss();
                            Toast.makeText(Login.this, "User belum terdaftar di database", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
            }
        });
    }

    private boolean validateUsername(){
        String usernameInput =  username.getText().toString().trim();

        if (usernameInput.isEmpty()) {
            username.setError("Username masih kosong");
            return false;
        } else {
            username.setError(null);
            return true;
        }
    }

    private boolean validatePassword(){
        String passwordInput =  password.getText().toString().trim();

        if (passwordInput.isEmpty()) {
            password.setError("Password masih kosong");
            return false;
        } else {
            password.setError(null);
            return true;
        }
    }

    @Override
    public void onBackPressed() {
        if (doubleTap) {
            super.onBackPressed();
        } else {
            Toast.makeText(this, "Tekan Lagi Untuk Keluar Aplikasi", Toast.LENGTH_SHORT).show();
            doubleTap = true;
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    doubleTap = false;
                }
            },500);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.googleSignIn:
                signIn();
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void signIn(){
        Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(intent,REQ_CODE);
    }

    private void handleResult(GoogleSignInResult result){
        if (result.isSuccess()){
            GoogleSignInAccount account = result.getSignInAccount();
            String _email = account.getEmail();
            updateUI(true);
        } else {
            updateUI(false);
        }
    }

    private void updateUI(boolean isLogin){
        if (isLogin){
            Intent intent = new Intent(getApplicationContext(), Chatbot.class);
            startActivity(intent);
            finish();
        } else {
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==REQ_CODE){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleResult(result);
        }
    }
}
