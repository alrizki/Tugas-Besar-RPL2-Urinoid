package urinoid.urinoid;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import urinoid.urinoid.activity.RecognizeConceptsActivity;
import urinoid.urinoid.adapter.CustomAdapter;
import urinoid.urinoid.models.ChatModel;

public class Chatbot extends AppCompatActivity {

    ListView listView;
    EditText editText;
    List<ChatModel> list_chat = new ArrayList<>();
    @BindView(R.id.send) TextView _send;
    @BindView(R.id.camera) TextView _camera;
    boolean doubleTap = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chatbot_content_drawer);
        ButterKnife.bind(this);

        listView = findViewById(R.id.list_of_message);
        editText = findViewById(R.id.user_message);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        _camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RecognizeConceptsActivity.class);
                startActivity(intent);
                finish();
            }
        });

        _send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = editText.getText().toString();
                ChatModel model = new ChatModel(text,true);
                list_chat.add(model);
                new SimsimiAPI().execute(list_chat);

                //remove user message
                editText.setText("");
            }
        });
    }


    private class SimsimiAPI extends AsyncTask<List<ChatModel>,Void,String> {

        String stream = null;
        List<ChatModel> models;
        String text = editText.getText().toString();

        @Override
        protected String doInBackground(List<ChatModel>... lists) {
            models = lists[0];

            return stream;
        }

        @Override
        protected void onPostExecute(String s) {

            CustomAdapter adapter = new CustomAdapter(models,getApplicationContext());
            listView.setAdapter(adapter);
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
