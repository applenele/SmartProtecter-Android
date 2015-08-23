package com.newren.smartprotecter.activity;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.newren.smartprotecter.R;
import com.newren.smartprotecter.util.QueueApplication;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends Activity {

    private Button btnLogin;
    private EditText txtUsername;
    private  EditText txtPassword;

    private Handler handler=new Handler(){
        public void handleMessage(Message msg){
            Toast toast= Toast.makeText(getApplicationContext(), msg.obj.toString(), Toast.LENGTH_SHORT);
            toast.show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnLogin = (Button) findViewById(R.id.signin_button);
        txtUsername = (EditText) findViewById(R.id.username_edit);
        txtPassword = (EditText) findViewById(R.id.password_edit);


        btnLogin.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("username", txtUsername.getText().toString());
                params.put("password", txtPassword.getText().toString());
                JSONObject jsonParams = new JSONObject(params);
                Log.d("TAG1", txtUsername.getText().toString());
                String url = "http://121.42.136.4:9000/UserApi/Login";
                JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonParams,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    String msg = response.getString("Msg");
                                    MsgThread msgThread = new MsgThread(msg);
                                    new Thread(msgThread).start();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                MsgThread msgThread = new MsgThread("���ִ���");
                                new Thread(msgThread).start();
                            }
                        }
                ) {
                    @Override
                    public Map<String, String> getHeaders() {
                        HashMap<String, String> headers = new HashMap<String, String>();
                        headers.put("Accept", "application/json");
                        headers.put("Content-Type", "application/json; charset=UTF-8");
                        return headers;
                    }

                };
                QueueApplication.getHttpQueues().add(request);
            }
        });

    }





    private class MsgThread implements Runnable{
       private  String msg = "";
        public  MsgThread(String msg){
            this.msg = msg;
        }
        public void run() {
            // TODO Auto-generated method stub
            try{
                Thread.sleep(1000);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            Message msgMessage=new Message();
            msgMessage.obj=msg;
            handler.sendMessage(msgMessage);
            Log.e("ThreadName", Thread.currentThread().getName());
        }

    }
}
