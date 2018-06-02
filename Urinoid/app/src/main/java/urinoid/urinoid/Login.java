package urinoid.urinoid;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import butterknife.ButterKnife;

import urinoid.urinoid.database.Users;

public class Login extends AppCompatActivity {

    public static final String Nama = "nama";;
    public static final String Email= "email";
    public static final String Username = "username";
    public static final String Password = "password";

    private TextInputLayout textInputUsername;
    private TextInputLayout textInputPassword;

    boolean doubleTap = false;

    Button btnsignin;

    @BindView(R.id.signupLink) TextView _signupLink;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        textInputUsername = findViewById(R.id.text_input_username);
        textInputPassword = findViewById(R.id.text_input_password);
        btnsignin = findViewById(R.id.btnSignin);

        _signupLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Registrasi.class);
                startActivity(intent);
                finish();
            }
        });

        //init firebase
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference Users = database.getReference("User");


        btnsignin.setOnClickListener(new  View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validateEmail() | !validatePassword()) {
                    return;
                }

                final ProgressDialog mDialog = new ProgressDialog(Login.this);
                mDialog.setMessage("Mohon menunggu..");
                mDialog.show();

                Users.addListenerForSingleValueEvent(new ValueEventListener() {


                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //cek jika user tidak terdaftar didatabase
                        if (dataSnapshot.child(textInputUsername.getEditText().getText().toString()).exists()) {


                            //ambil data user
                            mDialog.dismiss();
                            final Users users = dataSnapshot.child(textInputUsername.getEditText().getText().toString()).getValue(Users.class);
                            if (users.getPassword().equals(textInputPassword.getEditText().getText().toString())) {
                                Toast.makeText(Login.this, "Login Berhasil", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), Chatbot.class);

                                intent.putExtra(Nama, users.getNama());
                                intent.putExtra(Email, users.getEmail());
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

    private boolean validateEmail(){
        String emailInput =  textInputUsername.getEditText().getText().toString().trim();

        if (emailInput.isEmpty()) {
            textInputUsername.setError("Email masih kosong");
            return false;
        } else {
            textInputUsername.setError(null);
            return true;
        }
    }

    private boolean validatePassword(){
        String passwordInput =  textInputPassword.getEditText().getText().toString().trim();

        if (passwordInput.isEmpty()) {
            textInputPassword.setError("Password masih kosong");
            return false;
        } else {
            textInputPassword.setError(null);
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
}
