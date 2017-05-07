package iak.almuwahhid.com.iak1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import iak.almuwahhid.com.iak1.Controller.EventHandler;
import iak.almuwahhid.com.iak1.Helper.DatabaseHandler;

public class Login extends AppCompatActivity {

    Button btn;
    EditText edt;
    EventHandler handler;
    DatabaseHandler dbhandler;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        handler = new EventHandler();
        dbhandler = new DatabaseHandler(this);

        btn = (Button) findViewById(R.id.login_btn);
        edt = (EditText) findViewById(R.id.login_edt);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifikasi(edt.getText().toString());
            }
        });
    }
    void verifikasi(String username){
        if(dbhandler.check_user(username) != ""){
            sp = getApplicationContext().getSharedPreferences("user", 0);
            handler.setSESSION(sp, username);
            startActivity(new Intent(Login.this, MainActivity.class));
        }else{
            Toast.makeText(this, "Maaf username tidak terdaftar", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        sp = getApplicationContext().getSharedPreferences("user", 0);
        if(handler.auth(sp)){
            startActivity(new Intent(Login.this, MainActivity.class));
        }
    }
}
