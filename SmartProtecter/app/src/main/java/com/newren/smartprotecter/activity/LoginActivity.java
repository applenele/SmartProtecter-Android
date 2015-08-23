package com.newren.smartprotecter.activity;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.newren.smartprotecter.R;

import java.lang.reflect.Method;

public class LoginActivity extends Activity {

    private Button btnLogin;
    private EditText txtUsername;
    private  EditText txtPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnLogin = (Button) findViewById(R.id.signin_button);
        txtUsername = (EditText) findViewById(R.id.username_edit);
        txtPassword = (EditText) findViewById(R.id.password_edit);

        btnLogin.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v) {
                String url="";
                StringRequest request = new StringRequest(Request.Method.POST,url,)
            }
        });

    }


}
