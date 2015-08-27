package com.newren.smartprotecter.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.newren.smartprotecter.R;
import com.newren.smartprotecter.model.Accident;
import com.newren.smartprotecter.util.QueueApplication;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 乐 on 2015/8/27.
 */
public class FragmentAccidentShow extends Fragment {

    private View myView = null;
    private TextView tvTime = null;
    private TextView tvAddress = null;
    private  TextView tvType = null;
    private  TextView tvDescription = null;
    private Handler handler=new Handler(){
        public void handleMessage(Message msg){
            Toast toast= Toast.makeText(getActivity(), msg.obj.toString(), Toast.LENGTH_SHORT);
            toast.show();
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.accident_show, container, false);
        tvTime = (TextView) myView.findViewById(R.id.txtTime);
        tvAddress = (TextView) myView.findViewById(R.id.txtAddress);
        tvType = (TextView) myView.findViewById(R.id.txtType);
        tvDescription = (TextView) myView.findViewById(R.id.txtDescription);
        String i = getArguments().getString("key");
        Log.i("id", i);
        String url = "http://121.42.136.4:9000/AccidentApi/GetAccident?id="+i;
        JsonObjectRequest request = new JsonObjectRequest(url,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String msg = response.getString("Msg");
                            String statu = response.getString("Statu");
                            if(statu.equals("ok")){
                                JSONObject obj = response.getJSONObject("Data");
                                Accident accident = new Accident();
                                accident.setId(obj.getInt("ID"));
                                accident.setDescription(obj.getString("Description"));
                                accident.setTime(obj.get("Time").toString());
                                accident.setDistrict(obj.getString("District"));
                                accident.setBuilding(obj.getString("Building"));
                                accident.setFloor(obj.getString("Floor"));
                                accident.setRoom(obj.getString("Room"));
                                accident.setType(obj.getString("AccidentType"));
                                accident.setStatuAsInt(obj.getInt("Statu"));
                                tvDescription.setText(accident.getDescription());
                                tvTime.setText(accident.getTime().toString());
                                tvAddress.setText(accident.getDistrict() + "-" + accident.getBuilding() + "-" + accident.getFloor() + "-" + accident.getRoom());
                                tvType.setText(accident.getType());
                            }else{
                                MsgThread msgThread = new MsgThread(msg);
                                new Thread(msgThread).start();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        MsgThread msgThread = new MsgThread("出现错误");
                        new Thread(msgThread).start();
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Accept", "application/json");
                headers.put("Content-Type", "application/json; charset=UTF-8");
                return headers;
            }
        };
        QueueApplication.getHttpQueues().add(request);


        return myView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
