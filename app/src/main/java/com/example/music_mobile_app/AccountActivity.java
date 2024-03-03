package com.example.music_mobile_app;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.Arrays;

import de.hdodenhof.circleimageview.CircleImageView;

public class AccountActivity extends AppCompatActivity {

    CircleImageView imageAvt;
    ListView listView;
    ArrayList<String> arrItem;
    ArrayAdapter adapter;

    Button buttonEditAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        // Hide action bar
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        getSupportActionBar().hide();
//
        setContentView(R.layout.activity_account);
//
//        Toolbar toolbar = findViewById(R.id.app_bar);
////        setSupportActionBar(toolbar);
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                onBackPressed();
//            }
//        });
        // Căn giữa tiêu đề
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, R.color.purple_50)));

        //View Logo
        imageAvt = (CircleImageView) findViewById(R.id.imageAvt);

        //Inner List Item
        listView = (ListView) findViewById(R.id.listViewPlaylist);
        arrItem = new ArrayList<>(Arrays.asList("Danh sách phát 1", "Danh sách phát 2", "Danh sách phát 3"));
        adapter = new ArrayAdapter(this, R.layout.custom_list_playlist, R.id.textViewName, arrItem);
        listView.setAdapter(adapter);

        buttonEditAccount = (Button) findViewById(R.id.buttonEditAccount);
        //Onclick RegisterFree
        buttonEditAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AccountActivity.this, EditAccountActivity.class);
                startActivity(intent);
            }
        });

    }
    // Xử lý sự kiện khi nút quay lại được nhấn
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
