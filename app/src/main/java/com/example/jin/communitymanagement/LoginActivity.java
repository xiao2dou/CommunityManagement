package com.example.jin.communitymanagement;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    private MyDBHelper dbHelper;
    private EditText username;
    private EditText userpassword;
    private Button btn_login;
    private TextView tv_register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);
        username = (EditText) findViewById(R.id.editText2);
        userpassword = (EditText) findViewById(R.id.editText);

        btn_login = (Button) findViewById(R.id.btn_login);
        tv_register = (TextView) findViewById(R.id.tv_register);

        username.requestFocus();
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginClicked(view);
            }
        });
        tv_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register(view);

            }
        });

        dbHelper = new MyDBHelper(this, "UserStore.db", null,BaseActivity.DATABASE_VERSION);



    }

    //点击注册按钮进入注册页面
    public void register(View view) {
        Intent intent = new Intent(LoginActivity.this, SignInActivity.class);
        startActivity(intent);
    }


    //点击登录按钮
    public void loginClicked(View view) {
        String userName = username.getText().toString();
        String passWord = userpassword.getText().toString();
        if (login(userName, passWord)) {
            Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    //验证登录
    public boolean login(String username, String password) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String sql = null;
        if(!exits("user"))
            db.execSQL("create table user(id integer primary key ,"
                    + "name varchar(255),"
                    + "password varchar(255))");
        try {
            sql = "select * from user where name=? and password=?";
            Cursor cursor = db.rawQuery(sql, new String[]{username, password});
            if (cursor.moveToFirst()) {
                cursor.close();
                return true;
            }
        } catch (SQLiteException e) {
            e.printStackTrace();
        }
        Toast.makeText(this,"账号密码不匹配", Toast.LENGTH_SHORT).show();
        return false;
    }


    public boolean exits(String table) {
        boolean exits = false;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String sql = "select * from sqlite_master where name=" + "'" + table + "'";
        Cursor cursor = db.rawQuery(sql, null);

        if (cursor.getCount() != 0) {
            exits = true;
        }
        cursor.close();
        return exits;
    }
}
