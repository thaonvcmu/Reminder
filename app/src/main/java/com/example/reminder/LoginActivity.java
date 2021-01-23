package com.example.reminder;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    EditText edtUserName;
    EditText edtPassword;
    UserDbHelper userDbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtUserName = findViewById(R.id.edtUserName);
        edtPassword = findViewById(R.id.edtPassword);

        userDbHelper = new UserDbHelper(this);
        userDbHelper.open();

        if (savedInstanceState == null){
            //Clear user
            userDbHelper.deleteAllUser();
            //Add user
            userDbHelper.addUser("Nguyen Van Thao", "thaonvcmu74@gmail.com","123");
        }

        Button button = findViewById(R.id.btnLogin);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tenDangNhap = edtUserName.getText().toString();
                String matKhau = edtPassword.getText().toString();
                //Toast.makeText(LoginActivity.this, "Xin chao",Toast.LENGTH_LONG).show();
                if (userDbHelper.checkUser(tenDangNhap,matKhau )){
                    //Toast.makeText(LoginActivity.this, "Dang nhap thanh cong",Toast.LENGTH_LONG).show();
                    Intent intent = new Intent();
                    intent.setClass(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(LoginActivity.this, "Dang nhap khong thanh cong",Toast.LENGTH_LONG).show();
                }
                //userDbHelper.close();
            }
        });
    }
}
