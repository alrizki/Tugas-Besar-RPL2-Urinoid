package urinoid.urinoid;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import urinoid.urinoid.activity.RecognizeConceptsActivity;
import urinoid.urinoid.adapter.CustomAdapter;
import urinoid.urinoid.models.ChatModel;
import urinoid.urinoid.models.BotModel;

public class Chatbot extends AppCompatActivity implements View.OnClickListener,GoogleApiClient.OnConnectionFailedListener {

    ListView listView;
    EditText editText;
    List<ChatModel> list_chat = new ArrayList<>();
    @BindView(R.id.send) TextView _send;
    @BindView(R.id.help) TextView _help;
    @BindView(R.id.camera) TextView _camera;
    @BindView(R.id.btnLogOut) TextView _logout;
    boolean doubleTap = false;
    GoogleApiClient googleApiClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chatbot_content_drawer);
        ButterKnife.bind(this);

        listView = findViewById(R.id.list_of_message);
        editText = findViewById(R.id.user_message);
        _logout = findViewById(R.id.btnLogOut);
        _logout.setOnClickListener(this);
        //Request Email
        GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this,this).addApi(Auth.GOOGLE_SIGN_IN_API,signInOptions).build();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Onclick camera button
        _camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RecognizeConceptsActivity.class);
                startActivity(intent);
                finish();
            }
        });

        //onclick send button
        _send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = editText.getText().toString();
                ChatModel model = new ChatModel(text,true);
                list_chat.add(model);
                //new BotApi().execute(list_chat);

                if (text.toString().equals("!help")){
                    String answare = new String("");
                    ChatModel chatModel = new ChatModel(answare.toString(),false);
                    list_chat.add(chatModel);
                }else if (text.toString().equals("play")){
                    String answare = new String("Khontol");
                    ChatModel chatModel = new ChatModel(answare.toString(),false);
                    list_chat.add(chatModel);
                }else {
                    String answare = new String("Bangsat");
                    ChatModel chatModel = new ChatModel(answare.toString(),false);
                    list_chat.add(chatModel);
                }

                CustomAdapter adapter = new CustomAdapter(list_chat,getApplicationContext());
                listView.setAdapter(adapter);

                //remove user message
                editText.setText("");
            }
        });

        _help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String answare = new String("Darawet" + '\n' + "anyeng" + '\n' + "darawet" + '\n' + "darawet" + '\n' +"anyeng");
                ChatModel chatModel = new ChatModel(answare.toString(),false);
                list_chat.add(chatModel);

                CustomAdapter adapter = new CustomAdapter(list_chat,getApplicationContext());
                listView.setAdapter(adapter);
            }
        });
    }

    //onclick logout button
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnLogOut:
                _logout();
        }
    }

    //method for logout
    private void _logout(){
        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                updateUI(false);
            }
        });
    }

    //just for additional (nothing important in this class)
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

    //on connection failed
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    //on back press default
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
            },1000);
        }
    }
}
