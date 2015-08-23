package com.newren.smartprotecter.util;

import android.app.Application;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by ¿÷ on 2015/8/23.
 */
public class QueueApplication extends Application {

    public static RequestQueue queues;

    @Override
    public void onCreate() {
        super.onCreate();
        queues = Volley.newRequestQueue(getApplicationContext());
    }

    public  static RequestQueue getHttpQueues(){
        return queues;
    }

}
