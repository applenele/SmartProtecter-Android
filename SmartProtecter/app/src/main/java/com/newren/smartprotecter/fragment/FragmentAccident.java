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
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.newren.smartprotecter.Adapter.ListAccidentAdapter;
import com.newren.smartprotecter.R;
import com.newren.smartprotecter.datamodel.DropDistrict;
import com.newren.smartprotecter.model.Accident;
import com.newren.smartprotecter.model.User;
import com.newren.smartprotecter.util.QueueApplication;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by �� on 2015/8/24.
 */
public class FragmentAccident extends Fragment {
    private View myView;
    private ListAccidentAdapter adapter;
    private ListView lv;
    private List<Accident> items = null;
    private  Integer page = 0;
    private boolean lock = false;
    private  User user = QueueApplication.getUser();
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

        myView = inflater.inflate(R.layout.accident, container, false);
        lv = (ListView) myView.findViewById(R.id.lstAccident);
        page =0;
        lock = false;
        String url = "http://121.42.136.4:9000/AccidentApi/GetAccidents?uid="+user.getId()+"&page="+page;
        JsonObjectRequest request = new JsonObjectRequest(url,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            items = new ArrayList<Accident>();
                            String msg = response.getString("Msg");
                            String statu = response.getString("Statu");
                            if(statu.equals("ok")){
                                JSONArray obj = response.getJSONArray("Data");
                                java.text.DateFormat format1 = new java.text.SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                                for(int i=0;i<obj.length();i++){
                                    Accident accident = new Accident();
                                    JSONObject oj = obj.getJSONObject(i);
                                    Integer  id= oj.getInt("ID");
                                    accident.setId(id);
                                    accident.setDescription(oj.getString("Description"));
                                    accident.setTime(oj.get("Time").toString());

                                    accident.setDistrict(oj.getString("District"));
                                    accident.setBuilding(oj.getString("Building"));
                                    accident.setFloor(oj.getString("Floor"));
                                    accident.setRoom(oj.getString("Room"));
                                    accident.setType(oj.getString("AccidentType"));
                                    accident.setStatuAsInt(oj.getInt("Statu"));
                                    items.add(accident);
                                }
                                adapter = new ListAccidentAdapter(getActivity(), items);
                                lv.setAdapter(adapter);
                                page++;
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

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int i=0;
                FragmentAccidentShow accidentShow = new FragmentAccidentShow();
                Bundle bundle = new Bundle();
                bundle.putString("key", "asdasd");
                accidentShow.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_content, accidentShow).commit();
            }
        });

         lv.setOnScrollListener(new AbsListView.OnScrollListener() {
             @Override
             public void onScrollStateChanged(AbsListView view, int scrollState) {
                 //MsgThread thread = new MsgThread("xialaceshi ");
                 //new Thread(thread).start();
                 if(!lock){
                     String url = "http://121.42.136.4:9000/AccidentApi/GetAccidents?uid="+user.getId()+"&page="+page;
                     Log.i("yy1",url);
                     JsonObjectRequest request = new JsonObjectRequest(url,null,
                             new Response.Listener<JSONObject>() {
                                 @Override
                                 public void onResponse(JSONObject response) {
                                     try {
                                         String msg = response.getString("Msg");
                                         String statu = response.getString("Statu");
                                         if(statu.equals("ok")){
                                             JSONArray obj = response.getJSONArray("Data");

                                             List<Accident> list = new ArrayList<Accident>();
                                             if(obj.length()<=0){
                                                 lock = true;
                                                 MsgThread thread = new MsgThread("没有更多数据了");
                                                 new Thread(thread).start();
                                             }else{
                                                 for(int i=0;i<obj.length();i++){
                                                     Accident accident = new Accident();
                                                     JSONObject oj = obj.getJSONObject(i);
                                                     Integer  id= oj.getInt("ID");
                                                     accident.setId(id);
                                                     accident.setDescription(oj.getString("Description"));
                                                     accident.setTime(oj.get("Time").toString());
                                                     accident.setDistrict(oj.getString("District"));
                                                     accident.setBuilding(oj.getString("Building"));
                                                     accident.setFloor(oj.getString("Floor"));
                                                     accident.setRoom(oj.getString("Room"));
                                                     accident.setType(oj.getString("AccidentType"));
                                                     accident.setStatuAsInt(oj.getInt("Statu"));
                                                     list.add(accident);
                                                 }
                                                 items.addAll(list);
                                                 adapter.bindData(items);
                                                 adapter.notifyDataSetChanged();
                                                 page++;
                                             }
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

             }

             @Override
             public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

             }
         });

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
}
