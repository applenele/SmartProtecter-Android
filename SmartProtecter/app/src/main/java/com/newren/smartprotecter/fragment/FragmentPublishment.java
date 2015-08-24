package com.newren.smartprotecter.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.newren.smartprotecter.R;
import com.newren.smartprotecter.activity.MainActivity;
import com.newren.smartprotecter.datamodel.DropDistrict;
import com.newren.smartprotecter.model.User;
import com.newren.smartprotecter.util.QueueApplication;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 乐 on 2015/8/24.
 */
public class FragmentPublishment extends Fragment {
    private View myView = null;
    private Spinner dropDistrict = null;
    private List<DropDistrict> DistrictArry = null;


    private ArrayAdapter<DropDistrict> districtAdapter;

    private Handler handler=new Handler(){
        public void handleMessage(Message msg){
            Toast toast= Toast.makeText(getActivity(), msg.obj.toString(), Toast.LENGTH_SHORT);
            toast.show();
        }
    };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // TODO Auto-generated method stub

        myView = inflater.inflate(R.layout.publishment, container, false);
        dropDistrict = (Spinner) myView.findViewById(R.id.dropDistrict);

        String url = "http://121.42.136.4:9000/AccidentApi/GetDistricts";
        JsonObjectRequest request = new JsonObjectRequest(url,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String msg = response.getString("Msg");
                            String statu = response.getString("Statu");
                            if(statu.equals("ok")){
                                JSONArray obj = response.getJSONArray("Data");
                                DistrictArry = new ArrayList<DropDistrict>();
                                for(int i=0;i<obj.length();i++){
                                    DropDistrict district = new DropDistrict();
                                    JSONObject oj = obj.getJSONObject(i);

                                    Integer  id= oj.getInt("ID");
                                    district.setKey(id.toString());
                                    district.setValue(oj.getString("SchoolDistrictName"));
                                    DistrictArry.add(district);
                                }
                                Log.d("TAG1", DistrictArry.toArray().toString());

                                districtAdapter = new ArrayAdapter<DropDistrict>(getActivity().getBaseContext(),android.R.layout.simple_spinner_item,DistrictArry);
                                districtAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                dropDistrict.setAdapter(districtAdapter);
                                dropDistrict.setOnItemSelectedListener(dropDistrictListener);
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
                        MsgThread msgThread = new MsgThread("���ִ���");
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

    AdapterView.OnItemSelectedListener dropDistrictListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            Spinner spinner = (Spinner)parent;

            DropDistrict d =(DropDistrict) spinner.getSelectedItem() ;
            String value= d.getKey();
            String url = "http://121.42.136.4:9000/AccidentApi/GetDistricts";
            JsonObjectRequest request = new JsonObjectRequest(url,null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                String msg = response.getString("Msg");
                                String statu = response.getString("Statu");
                                if(statu.equals("ok")){
                                    JSONArray obj = response.getJSONArray("Data");
                                    DistrictArry = new ArrayList<DropDistrict>();
                                    for(int i=0;i<obj.length();i++){
                                        DropDistrict district = new DropDistrict();
                                        JSONObject oj = obj.getJSONObject(i);

                                        Integer  id= oj.getInt("ID");
                                        district.setKey(id.toString());
                                        district.setValue(oj.getString("SchoolDistrictName"));
                                        DistrictArry.add(district);
                                    }
                                    Log.d("TAG1", DistrictArry.toArray().toString());

                                    districtAdapter = new ArrayAdapter<DropDistrict>(getActivity().getBaseContext(),android.R.layout.simple_spinner_item,DistrictArry);
                                    districtAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    dropDistrict.setAdapter(districtAdapter);
                                    dropDistrict.setOnItemSelectedListener(dropDistrictListener);
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
                            MsgThread msgThread = new MsgThread("���ִ���");
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
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

}
