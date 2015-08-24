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
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.newren.smartprotecter.R;
import com.newren.smartprotecter.activity.MainActivity;
import com.newren.smartprotecter.datamodel.DropAccidentType;
import com.newren.smartprotecter.datamodel.DropBuilding;
import com.newren.smartprotecter.datamodel.DropDistrict;
import com.newren.smartprotecter.datamodel.DropFloor;
import com.newren.smartprotecter.datamodel.DropRoom;
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
    private Button btnPublish =null;
    private TextView txtDescription = null;
    private Spinner dropDistrict = null;
    private Spinner dropBuilding = null;
    private Spinner dropFloor  =null;
    private Spinner dropRoom = null;
    private Spinner dropAccidentType = null;
    private List<DropDistrict> DistrictArry = null;
    private List<DropFloor> FloorArry = null;
    private List<DropAccidentType> AccidentTypeArry = null;
    private ArrayAdapter<DropDistrict> districtAdapter;

    private ArrayAdapter<DropFloor> floorAdapter;
    private ArrayAdapter<DropAccidentType> accidentTypeAdapter;

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
        btnPublish = (Button) myView.findViewById(R.id.btnPublish);
        txtDescription = (TextView) myView.findViewById(R.id.txtDescription);
        dropDistrict = (Spinner) myView.findViewById(R.id.dropDistrict);
        dropBuilding = (Spinner) myView.findViewById(R.id.dropBuilding);
        dropFloor  = (Spinner) myView.findViewById(R.id.dropFloor);
        dropRoom = (Spinner) myView.findViewById(R.id.dropRoom);
        dropAccidentType = (Spinner) myView.findViewById(R.id.dropAccidentType);

        btnPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DropDistrict d =(DropDistrict) dropDistrict.getSelectedItem() ;
                DropBuilding building = (DropBuilding) dropBuilding.getSelectedItem() ;
                DropFloor floor = (DropFloor) dropFloor.getSelectedItem();
                DropRoom room = (DropRoom) dropRoom.getSelectedItem();
                DropAccidentType type = (DropAccidentType) dropAccidentType.getSelectedItem();
                String description = txtDescription.getText().toString();
                String buildingId = building.getKey();
                String districtId = d.getKey();
                String sfloor = floor.getValue();
                String sroom  = room.getValue();
                String stype = type.getValue();
                Integer uid = QueueApplication.getUser().getId();
                String publishmenturl = "http://121.42.136.4:9000/AccidentApi/Publishment?districtId="+districtId+"&buildingId="+buildingId+"&uid="+uid+"&floor="+sfloor+"&room="+sroom+"&type="+stype+"&description="+description;
                JsonObjectRequest publishmentrequest = new JsonObjectRequest(publishmenturl,null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    String msg = response.getString("Msg");
                                    String statu = response.getString("Statu");
                                    Log.d("TAG1", response.toString());
                                    if(statu.equals("ok")){
                                        FragmentAccident accident = new FragmentAccident();
                                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_content, accident).commit();
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
                QueueApplication.getHttpQueues().add(publishmentrequest);
            }
        });

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

        String floorurl = "http://121.42.136.4:9000/AccidentApi/GetFloors";
        JsonObjectRequest floorrequest = new JsonObjectRequest(floorurl,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String msg = response.getString("Msg");
                            String statu = response.getString("Statu");
                            if(statu.equals("ok")){
                                JSONArray obj = response.getJSONArray("Data");
                                FloorArry = new ArrayList<DropFloor>();
                                for(int i=0;i<obj.length();i++){
                                    DropFloor floor = new DropFloor();
                                    JSONObject oj = obj.getJSONObject(i);

                                    Integer  id= oj.getInt("Id");
                                    floor.setKey(id.toString());
                                    floor.setValue(oj.getString("FloorName"));
                                    FloorArry.add(floor);
                                }
                                Log.d("TAG1", DistrictArry.toArray().toString());

                                floorAdapter = new ArrayAdapter<DropFloor>(getActivity().getBaseContext(),android.R.layout.simple_spinner_item,FloorArry);
                                floorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                dropFloor.setAdapter(floorAdapter);
                                dropFloor.setOnItemSelectedListener(dropFloorListener);
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
        QueueApplication.getHttpQueues().add(floorrequest);

        String typeurl = "http://121.42.136.4:9000/AccidentApi/GetAccidentTypes";
        JsonObjectRequest typeequest = new JsonObjectRequest(typeurl,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String msg = response.getString("Msg");
                            String statu = response.getString("Statu");
                            if(statu.equals("ok")){
                                JSONArray obj = response.getJSONArray("Data");
                                AccidentTypeArry = new ArrayList<DropAccidentType>();
                                for(int i=0;i<obj.length();i++){
                                    DropAccidentType type = new DropAccidentType();
                                    JSONObject oj = obj.getJSONObject(i);

                                    Integer  id= oj.getInt("ID");
                                    type.setKey(id.toString());
                                    type.setValue(oj.getString("AccidentName"));
                                    AccidentTypeArry.add(type);
                                }
                                accidentTypeAdapter = new ArrayAdapter<DropAccidentType>(getActivity().getBaseContext(),android.R.layout.simple_spinner_item,AccidentTypeArry);
                                accidentTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                dropAccidentType.setAdapter(accidentTypeAdapter);
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
        QueueApplication.getHttpQueues().add(typeequest);

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

    AdapterView.OnItemSelectedListener dropFloorListener = new AdapterView.OnItemSelectedListener() {
        private List<DropRoom> roomArr = null;
        private ArrayAdapter<DropRoom> roomAdapter;
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            Spinner spinner = (Spinner)parent;

            DropFloor d =(DropFloor) spinner.getSelectedItem() ;
            String key= d.getKey();
            String url = "http://121.42.136.4:9000/AccidentApi/GetRooms/"+key;
            JsonObjectRequest request = new JsonObjectRequest(url,null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                String msg = response.getString("Msg");
                                String statu = response.getString("Statu");
                                if(statu.equals("ok")){
                                    JSONArray obj = response.getJSONArray("Data");
                                    roomArr = new ArrayList<DropRoom>();
                                    for(int i=0;i<obj.length();i++){
                                        DropRoom room = new DropRoom();
                                        JSONObject oj = obj.getJSONObject(i);

                                        Integer  id= oj.getInt("Id");
                                        room.setKey(id.toString());
                                        room.setValue(oj.getString("RoomNumber"));
                                        roomArr.add(room);
                                    }
                                    roomAdapter = new ArrayAdapter<DropRoom>(getActivity().getBaseContext(),android.R.layout.simple_spinner_item,roomArr);
                                    roomAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    dropRoom.setAdapter(roomAdapter);
                                  //  dropBuilding.setOnItemSelectedListener(dropDistrictListener);
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
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };


    AdapterView.OnItemSelectedListener dropDistrictListener = new AdapterView.OnItemSelectedListener() {
        private List<DropBuilding> buildingArr = null;
        private ArrayAdapter<DropBuilding> buildingAdapter;
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            Spinner spinner = (Spinner)parent;

            DropDistrict d =(DropDistrict) spinner.getSelectedItem() ;
            String key= d.getKey();
            String url = "http://121.42.136.4:9000/AccidentApi/GetBuildings/"+key;
            JsonObjectRequest request = new JsonObjectRequest(url,null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                String msg = response.getString("Msg");
                                String statu = response.getString("Statu");
                                if(statu.equals("ok")){
                                    JSONArray obj = response.getJSONArray("Data");
                                    buildingArr = new ArrayList<DropBuilding>();
                                    for(int i=0;i<obj.length();i++){
                                        DropBuilding building = new DropBuilding();
                                        JSONObject oj = obj.getJSONObject(i);

                                        Integer  id= oj.getInt("Id");
                                        building.setKey(id.toString());
                                        building.setValue(oj.getString("BuildName"));
                                        buildingArr.add(building);
                                    }
                                    Log.d("TAG1", buildingArr.toArray().toString());

                                    buildingAdapter = new ArrayAdapter<DropBuilding>(getActivity().getBaseContext(),android.R.layout.simple_spinner_item,buildingArr);
                                    buildingAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    dropBuilding.setAdapter(buildingAdapter);
                                    //  dropBuilding.setOnItemSelectedListener(dropDistrictListener);
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
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };


}
