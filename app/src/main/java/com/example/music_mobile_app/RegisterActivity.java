package com.example.music_mobile_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {
    Button buttonRegisterFree;
    Button buttonRegisterPhone;
    Button buttonLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Hide action bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_register);

        //button RegisterFree
        buttonRegisterFree = (Button) findViewById(R.id.buttonRegisterFree);
        //button RegisterPhone
        buttonRegisterPhone = (Button) findViewById(R.id.buttonRegisterPhone);
        //button Login
        buttonLogin = (Button) findViewById(R.id.buttonLogin);

        //Onclick RegisterFree
        buttonRegisterFree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, RegisterFreeActivity.class);
                startActivity(intent);
            }
        });
        //Onclick RegisterPhone
        buttonRegisterPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, RegisterPhoneActivity.class);
                startActivity(intent);
            }
        });
        //Onclick Login
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

    }
}
