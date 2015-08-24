package com.newren.smartprotecter.util;

import android.app.Application;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.newren.smartprotecter.model.User;

/**
 * Created by ¿÷ on 2015/8/23.
 */
public class QueueApplication extends Application {

    public static RequestQueue queues;
    public  static User user;
    @Override
    public void onCreate() {
        super.onCreate();
        queues = Volley.newRequestQueue(getApplicationContext());
    }

    public  static RequestQueue getHttpQueues(){
        return queues;
    }

    public static void setUser(User user){
        QueueApplication.user = user;
    }

    public static User getUser(){
        return QueueApplication.user;
    }
}
