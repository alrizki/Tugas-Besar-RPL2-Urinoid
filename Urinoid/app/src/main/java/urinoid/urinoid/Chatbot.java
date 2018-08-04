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

                if (text.toString().equals("keyword")){
                    String answare = new String("Silahkan Masukan Keyword di bawah ini :" + '\n' + "1. keyword -> Menampilkan semua keyword." + '\n' + "2. keterangan -> Menerangkan kenapa urin menjadi sangat penting." + '\n' +"3. penyakit -> Menampilkan penyakit-penyakit akibat kekurangan cairan."+ '\n' +"4. gejala -> Menampilkan gejala-gejala dehidrasi."+ '\n' +"5. solusi -> Menampilkan solusi untuk mengatasi kekurangan cairan."+ '\n' +"6. tips -> Menampilkan tips yang membantu menghindari dehidrasi."+ '\n' +"7. cek -> Aplikasi akan mengecek urin yang Anda upload." + '\n' + "" + '\n' + "Silakan ketik “Keyword” (tanpa tanda “ ”) untuk melihat semua kata kunci perintah.");
                    ChatModel chatModel = new ChatModel(answare.toString(),false);
                    list_chat.add(chatModel);
                }else if (text.toString().equals("keterangan")){
                    String answare = new String("Dari urine, kita bisa melihat seberapa baik kesehatan anda, contohnya jika berwarna bening maka kadar cairan tubuh terpenuhi, sebaliknya jika kuning pekat maka anda harus banyak minum air putih, anda mengalami dehidrasi.");
                    ChatModel chatModel = new ChatModel(answare.toString(),false);
                    list_chat.add(chatModel);
                }else if (text.toString().equals("penyakit")){
                    String answare = new String("Berikut adalah macam-macam akibat dari kurang minum :" + '\n' + "1. Dehidrasi" + '\n' + "2. Mengganggu kekuatan otot, sendi, dan temperature tubuh" + '\n' + "3. Mengganggu sistem kekebalan tubuh" + '\n' + "4. Sembelit" + '\n' + "5. Tekanan darah tinggi" + '\n' + "6. Gangguan ginjal" + '\n' + "7. Stress");
                    ChatModel chatModel = new ChatModel(answare.toString(),false);
                    list_chat.add(chatModel);
                }else if (text.toString().equals("gejala")){
                    String answare = new String("Berikut ini adalah gejala-gejala Anda mengalami dehidrasi :" + '\n' + "1. Rasa haus" + '\n' + "2. Jumlah dan frekuensi pembuangan urin menurun dan warnanya menjadi lebih pekat" + '\n' + "3. Mulut kering" + '\n' + "4. Mudah mengantuk" + '\n' + "5. Pusing & Sakit kepala");
                    ChatModel chatModel = new ChatModel(answare.toString(),false);
                    list_chat.add(chatModel);
                }else if (text.toString().equals("solusi")){
                    String answare = new String("Untuk mengatasi dehidrasi solusi paling utama yaitu minum air putih yang cukup. Selain minum air putih berikut adalah makanan dan minuman yang mengandung cukup air untuk mengatasi dehidrasi :" + '\n' + "1. Jus buah murni" + '\n' + "2. Minuman bebas kafein" + '\n' + "3. Minuman herbal" + '\n' + "4. Sayuran & buah-buahan");
                    ChatModel chatModel = new ChatModel(answare.toString(),false);
                    list_chat.add(chatModel);
                }else if (text.toString().equals("tips")){
                    String answare = new String("Berikut ini tips-tips yang dapat membantu menghindari dehidrasi :" + '\n' + "1. Tempelkan kain basah pada leher, wajah, punggung, dada, ataupun perut." + '\n' + "2. Selalu sedia botol air putih atau minuman lain dengan jarak yang dekat supaya selalu teringat untuk minum.");
                    ChatModel chatModel = new ChatModel(answare.toString(),false);
                    list_chat.add(chatModel);
                }else if (text.toString().equals("cek")){
                    String answare = new String("Cek Urine Berdasarkan warna :" + '\n' + "1. Kuning pekat : “Anda sangat kekurangan cairan dan membutuhkan banyak cairan, segeralah perbanyak minum air putih!”." + '\n' + "2. Kuning muda : “Anda kekurangan cairan, segera minum air putih.”." + '\n' + "3. Kuning bening : “Anda sudah cukup cairan, dianjurkan untuk tetap minum air putih.”." + '\n' + "4. Bening : “Kondisi tubuh Anda sudah baik.”.");
                    ChatModel chatModel = new ChatModel(answare.toString(),false);
                    list_chat.add(chatModel);
                }else {
                    String answare = new String("Keyword salah. Silahkan tekan tombol Bantuan");
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
                String answare = new String("Silahkan Masukan Keyword di bawah ini :" + '\n' + "1. keyword -> Menampilkan semua keyword." + '\n' + "2. keterangan -> Menerangkan kenapa urin menjadi sangat penting." + '\n' +"3. penyakit -> Menampilkan penyakit-penyakit akibat kekurangan cairan."+ '\n' +"4. gejala -> Menampilkan gejala-gejala dehidrasi."+ '\n' +"5. solusi -> Menampilkan solusi untuk mengatasi kekurangan cairan."+ '\n' +"6. tips -> Menampilkan tips yang membantu menghindari dehidrasi."+ '\n' +"7. cek -> Aplikasi akan mengecek urin yang Anda upload." + '\n' + "" + '\n' + "Silakan ketik “Keyword” (tanpa tanda “ ”) untuk melihat semua kata kunci perintah.");
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
