package com.example.maemm;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class login extends AppCompatActivity {

    Button btnLogin;
    String email, password;
    EditText lblEmail, lblPswd;
    LocalStorage localStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

       localStorage = new  LocalStorage(login.this);

       lblEmail = findViewById(R.id.lblEmail);
        lblPswd = findViewById(R.id.lblPswd);
        btnLogin = findViewById(R.id.next_button);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkLogin();
            }
        });



    }

    private void checkLogin() {
        email = lblEmail.getText().toString();
        password = lblPswd.getText().toString();
        if(email.isEmpty() || password.isEmpty()){
            alertFail("Email and Password is requerid");
        }
        else{
            sendLogin();
        }
    }

    private void sendLogin() {

        JSONObject params = new JSONObject();
        try {
            params.put("email", email);
            params.put("password",password);
        }catch (JSONException e){
            e.printStackTrace();
        }
        String data = params.toString();
        String url = getString(R.string.api_server)+"/auth/login2";

        new Thread(new Runnable() {
            @Override
            public void run() {
                Http http = new Http(login.this,url);
                http.setMethod("post");
                http.setData(data);
                http.send();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                      Integer code= http.getStatusCode();
                      if(code==200){
                          try {
                            JSONObject response = new JSONObject(http.getReponse());
                            String token = response.getString("token");
                            localStorage.setToken(token);
                            Intent intent = new Intent(login.this,alert.class);
                            startActivity(intent);
                            finish();

                          }catch (JSONException e){
                              e.printStackTrace();
                          }
                      }
                      else if(code == 422) {
                          try {
                              JSONObject response = new JSONObject(http.getReponse());
                              String nsg = response.getString("message");
                              alertFail(nsg);

                          }catch (JSONException e){
                              e.printStackTrace();
                          }
                        }
                      else if (code ==481){
                          try{
                              JSONObject response = new JSONObject(http.getReponse());
                              String nsg = response.getString("message");
                              alertFail(nsg);
                          }catch (JSONException e){
                              e.printStackTrace();
                          }
                      }
                      else{
                          Toast.makeText(login.this,"Error"+code, Toast.LENGTH_SHORT).show();
                      }
                    }
                });
            }
        }).start();
        //Intent intent= new Intent(this, login.class);
        //startActivity(intent);
        //finish();

        //Toast.makeText(this, "Send", Toast.LENGTH_SHORT).show();
    }

    private void alertFail(String s) {
        new AlertDialog.Builder(this
        ).setTitle("Failed")
                .setIcon(R.drawable.ic_warning)
                .setMessage(s)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        })
                .show();
    }
}