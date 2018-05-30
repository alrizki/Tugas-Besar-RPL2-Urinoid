package com.example.adlif.urinoid;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adlif.urinoid.Adapter.CustomAdapter;
import com.example.adlif.urinoid.Helper.HttpDataHelper;
import com.example.adlif.urinoid.Models.ChatModel;
import com.example.adlif.urinoid.Models.SimsimiModel;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    boolean doubleTap = false;

    ListView listView;
    EditText editText;
    List<ChatModel> list_chat = new ArrayList<>();
    @BindView(R.id.fab) TextView _fab;
    @BindView(R.id.add) TextView _add;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        listView = findViewById(R.id.list_of_message);
        editText = findViewById(R.id.user_message);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Urinoid");
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));

        _add.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
            }
        });

        _fab.setOnClickListener(new View.OnClickListener() {
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
            String url = String.format("http://sandbox.api.simsimi.com/request.p?key=%s&lc=id&ft=1.0&text=%s",getString(R.string.simsimi_api),text);
            models = lists[0];
            HttpDataHelper httpDataHelper = new HttpDataHelper();
            stream = httpDataHelper.GetHttpData(url);
            return stream;
        }

        @Override
        protected void onPostExecute(String s) {
            Gson gson = new Gson();

            SimsimiModel response = gson.fromJson(s, SimsimiModel.class);

            ChatModel chatModel = new ChatModel(response.getResponse(),false); // Ngambil Respon Dari Simsimi
            models.add(chatModel);
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
