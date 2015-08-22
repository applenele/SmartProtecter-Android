package com.newren.smartprotecter.activity;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.newren.smartprotecter.R;

public class LoginActivity extends Activity {

   private Button btnLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnLogin = (Button) findViewById(R.id.signin_button);

        btnLogin.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v) {
                String strTmp = "µã»÷Button01";
            }
        });

    }


}
