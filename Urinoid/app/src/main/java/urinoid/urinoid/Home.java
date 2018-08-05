package urinoid.urinoid;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import urinoid.urinoid.activity.RecognizeConceptsActivity;

/**
 * Created by Saepul Uyun on 6/1/2018.
 */

public class Home extends AppCompatActivity{

    @BindView(R.id.btnChat) TextView _chatBot;
    @BindView(R.id.btnCamera) TextView _Camera;
    TextView textNama;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        textNama = (TextView) findViewById(R.id.textNama);

        final Intent intent = getIntent();

        final String nama = intent.getStringExtra(Login.Nama);
        final String password = intent.getStringExtra(Login.Password);
        final String username = intent.getStringExtra(Login.Username);

        textNama.setText(nama);

        _chatBot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Chatbot.class);
                startActivity(intent);
                finish();
            }
        });

        _Camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RecognizeConceptsActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
}
