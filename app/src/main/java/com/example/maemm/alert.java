package com.example.maemm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class alert extends AppCompatActivity {
    TextView txname;
    Button logout_button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert);

        txname = findViewById(R.id.txname);
        logout_button = findViewById(R.id.logout_button);
        getUser();

        logout_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

    }



    private void getUser() {
        txname.setText("¡HI!, ");

    }

    private void logout() {
        Intent intent = new Intent(alert.this, login.class);
        startActivity(intent);
        finish();
    }
}