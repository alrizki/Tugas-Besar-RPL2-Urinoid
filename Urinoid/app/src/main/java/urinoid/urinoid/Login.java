package urinoid.urinoid;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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

    private EditText username;
    private EditText password;
    private AppCompatCheckBox checkbox;

    boolean doubleTap = false;

    Button btnsignin;

    @BindView(R.id.registrationLink) TextView _registrationLink;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        checkbox = findViewById(R.id.checkbox);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        btnsignin = findViewById(R.id.btnSignin);

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

    private boolean validateUsername(){
        String emailInput =  username.getText().toString().trim();

        if (emailInput.isEmpty()) {
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
}
