package urinoid.urinoid;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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


public class Registrasi extends AppCompatActivity {

    private EditText username;
    private EditText displayName;
    private EditText password;
    private EditText confpassword;
    private AppCompatCheckBox checkbox;

    Button btnRegistrtation;

    @BindView(R.id.loginLink) TextView _loginLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrasi);
        ButterKnife.bind(this);

        checkbox = findViewById(R.id.checkbox);
        username = findViewById(R.id.Username);
        displayName = findViewById(R.id.displayName);
        password = findViewById(R.id.password);
        confpassword = findViewById(R.id.confPassword);
        btnRegistrtation = findViewById(R.id.btnRegistration);

        _loginLink.setOnClickListener(new  View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (getApplicationContext(), Login.class);
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


        btnRegistrtation.setOnClickListener(new  View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!validateUsername() | !validateDisplay() | !validatePassword()) {
                    return;
                }

                final ProgressDialog mDialog = new ProgressDialog(Registrasi.this);
                mDialog.setMessage("Mohon Tunggu Sebentar");
                mDialog.show();

                Users.addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        Users users = new Users(username.getText().toString(), password.getText().toString(), confpassword.getText().toString(), displayName.getText().toString());
                        Users.child(username.getText().toString()).setValue(users);
                        Toast.makeText(Registrasi.this, "Berhasil Registrasi", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(getApplicationContext(), Login.class);;
                        startActivity(intent);
                        finish();
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

    private boolean validateDisplay(){
        String displayInput = displayName.getText().toString().trim();

        if (displayInput.isEmpty()){
            displayName.setError("Nama masih kosong");
            return false;
        } else if (displayInput.length() > 15){
            displayName.setError("Nama terlalu panjang");
            return false;
        } else {
            displayName.setError(null);
            return true;
        }
    }

    private boolean validatePassword(){
        String passwordInput =  password.getText().toString().trim();
//        String confPasswordInput =  confpassword.getText().toString().trim();


        if (passwordInput.isEmpty()) {
            password.setError("Password masih kosong");
        }
        if (passwordInput.length()<8){
            password.setError("Password Minimal 8 karakter");
            return false;
        }
//        if (confPasswordInput.isEmpty()) {
//            confpassword.setError("Konfirmasi Password");
//        }
//        if (passwordInput.equals(confPasswordInput)) {
//            confpassword.setError("Password Tidak Sama");
//            return false;
//        }
        else {
            password.setError(null);
            return true;
        }
    }

    @Override
    public void onBackPressed() {
        Intent i;

        super.onBackPressed();
        i = new Intent(Registrasi.this, Login.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        finish();
    }
}
