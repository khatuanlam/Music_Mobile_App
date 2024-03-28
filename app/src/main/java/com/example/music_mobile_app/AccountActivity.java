package com.example.music_mobile_app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.music_mobile_app.ui.AlbumFragment;

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
        setContentView(R.layout.activity_account);

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

        // Bộ lắng nghe sự kiện cho ListView
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                // Gọi phương thức goToAlbumFragment() khi item được chọn
                goToAlbumFragment();
            }
        });
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

    private void goToAlbumFragment() {
        // Tạo đối tượng FragmentManager
        FragmentManager fragmentManager = getSupportFragmentManager();

        // Bắt đầu một giao dịch Fragment
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // Thay thế Fragment hiện tại bằng Fragment đích
        fragmentTransaction.replace(R.id.layout_account, new AlbumFragment());

        // Thêm transaction vào ngăn xếp để có thể quay lại Fragment trước đó (nếu cần)
        fragmentTransaction.addToBackStack(null);

        // Thực hiện giao dịch
        fragmentTransaction.commit();
    }

    private void prepareData() {

        SharedPreferences sharedPreferences = getSharedPreferences("UserData", Context.MODE_PRIVATE);

    }

}
