package com.newren.smartprotecter.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
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
import com.newren.smartprotecter.util.HttpPost;
import com.newren.smartprotecter.util.QueueApplication;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
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

    private static final int PHOTO_CAPTURE = 0x11;
    private static String photoPath = "/sdcard/smartprotecter/";
    private static String photoName = photoPath + "laolisb.jpg";
    Uri imageUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "image.jpg"));//第二个参数是临时文件，在后面将会被修改
    private Button btnTakePhoto;//拍照与下载
    private ImageView img_photo;//显示图片
    private String newName = "laoli.jpg";
    private Button btnPublish2 =null;
    /*
     * 这里的代码应该有问题
     */
    private String uploadFile = "/sdcard/smartprotecter/laolisb.jpg";
    private String actionUrl = "http://121.42.136.4:9000/AccidentApi/Publishment2";// private String actionUrl = "http://192.168.0.104:8080/File/UploadAction";

    private Handler handler=new Handler(){
        public void handleMessage(Message msg){
            Toast toast= Toast.makeText(getActivity(), msg.obj.toString(), Toast.LENGTH_SHORT);
            toast.show();
        }
    };

    private Handler reflresh =new Handler(){
        public void handleMessage(Message msg){
            switch (msg.what) {
                case 1:
                    FragmentAccident accident = new FragmentAccident();
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_content, accident).commit();
            }
        }
    };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        Log.i("hou","actionretext");
        super.onActivityResult(requestCode, resultCode, data);
        String sdStatus = Environment.getExternalStorageState();
        switch (requestCode) {
            case PHOTO_CAPTURE:
                if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) {
                    Log.i("内存卡错误", "请检查您的内存卡");
                } else {
                    BitmapFactory.Options op = new BitmapFactory.Options();
                    // 设置图片的大小
                    Bitmap bitMap = BitmapFactory.decodeFile(photoName);
                    int width = bitMap.getWidth();
                    int height = bitMap.getHeight();
                    // 设置想要的大小
                    int newWidth = 480;
                    int newHeight = 640;
                    // 计算缩放比例
                    float scaleWidth = ((float) newWidth) / width;
                    float scaleHeight = ((float) newHeight) / height;
                    // 取得想要缩放的matrix参数
                    Matrix matrix = new Matrix();
                    matrix.postScale(scaleWidth, scaleHeight);
                    // 得到新的图片
                    bitMap = Bitmap.createBitmap(bitMap, 0, 0, width, height, matrix, true);

                    // canvas.drawBitmap(bitMap, 0, 0, paint)
                    // 防止内存溢出
                    op.inSampleSize = 4; // 这个数字越大,图片大小越小.

                    Bitmap pic = null;
                    pic = BitmapFactory.decodeFile(photoName, op);
                    img_photo.setImageBitmap(pic); // 这个ImageView是拍照完成后显示图片
                    FileOutputStream b = null;
                    ;
                    try {
                        b = new FileOutputStream(photoName);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    if (pic != null) {
                        pic.compress(Bitmap.CompressFormat.JPEG, 50, b);
                    }
                }
                break;
            default:
                return;
        }
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
        btnTakePhoto = (Button) myView.findViewById(R.id.btnTakePicture);
        btnPublish2 = (Button) myView.findViewById(R.id.btnPublish2);
        img_photo = (ImageView) myView.findViewById(R.id.imt_photo);
        btnTakePhoto.setOnClickListener(new Photo());

        btnPublish2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DropDistrict d = (DropDistrict) dropDistrict.getSelectedItem();
                DropBuilding building = (DropBuilding) dropBuilding.getSelectedItem();
                DropFloor floor = (DropFloor) dropFloor.getSelectedItem();
                DropRoom room = (DropRoom) dropRoom.getSelectedItem();
                DropAccidentType type = (DropAccidentType) dropAccidentType.getSelectedItem();
                String description = txtDescription.getText().toString();
                String buildingId = building.getKey();
                String districtId = d.getKey();
                String sfloor = floor.getValue();
                String sroom = room.getValue();
                String stype = type.getValue();
                Integer uid = QueueApplication.getUser().getId();
                // String params = null;
                final Map<String, String> params = new HashMap<String, String>();
                params.put("districtId", districtId);
                params.put("buildingId", buildingId);
                params.put("uid", uid.toString());
                params.put("floor", sfloor);
                params.put("room", sroom);
                params.put("type", stype);
                params.put("description", description);

                final Map<String, File> files = new HashMap<String, File>();

                files.put(System.currentTimeMillis() + ".jpg", new File(uploadFile));

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String str = null;
                        try {
                            str = HttpPost.post(actionUrl, params, files);
                            FlreshThread rThread = new FlreshThread();
                            new Thread(rThread).start();
                        } catch (Exception e) {
                            MsgThread msgThread = new MsgThread("发表失败");
                            new Thread(msgThread).start();
                            Log.i("log1111",e.getMessage());
                        }
                    }
                }).start();

            }
        });



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
                String params = null;
                try {
                    params = "districtId="+districtId+"&buildingId="+buildingId+"&uid="+uid+"&floor="+ URLEncoder.encode(sfloor, "UTF-8")+"&room="+URLEncoder.encode(sroom,"UTF-8")+"&type="+URLEncoder.encode(stype,"UTF-8")+"&description="+URLEncoder.encode(description,"UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                String publishmenturl = "http://121.42.136.4:9000/AccidentApi/Publishment?"+params;

                JsonObjectRequest publishmentrequest = new JsonObjectRequest(publishmenturl,null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    String msg = response.getString("Msg");
                                    String statu = response.getString("Statu");
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

    class Photo implements View.OnClickListener {
        @Override
        public void onClick(View v) {

            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
            //"/sdcard/AnBo/";
            File file = new File(photoPath);
            if (!file.exists()) { // 检查图片存放的文件夹是否存在
                file.mkdir();
            }

            File photo = new File(photoName);
            imageUri = Uri.fromFile(photo);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri); // 这样就将文件的存储方式和uri指定到了Camera应用中
            startActivityForResult(intent, PHOTO_CAPTURE);
        }

    }

    public void uploadPhoto() {
        Map<String, String> params = new HashMap<String, String>();
        Map<String, File> files = new HashMap<String, File>();
        files.put(System.currentTimeMillis()+".jpg", new File(uploadFile));//uploadFile = "/sdcard/AnBo/laolisb.jpg";
        try {
            String str = HttpPost.post(actionUrl, params, files);
            System.out.println("str--->>>" + str);
        } catch (Exception e) {
        }
    }

    class FlreshThread implements Runnable {
        public void run() {
            //执行数据操作，不涉及到UI
            Message msg = new Message();
            msg.what = 1;
            //这三句可以传递数据
            //  Bundle data = new Bundle();
            // data.putInt("COUNT", 100);//COUNT是标签,handleMessage中使用
            // msg.setData(data);

            reflresh.sendMessage(msg); // 向Handler发送消息,更新UI
        }
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
