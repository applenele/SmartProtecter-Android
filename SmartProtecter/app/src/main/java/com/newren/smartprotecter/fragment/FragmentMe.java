package com.newren.smartprotecter.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.newren.smartprotecter.R;
import com.newren.smartprotecter.model.Common;
import com.newren.smartprotecter.model.User;
import com.newren.smartprotecter.util.BitmapCache;
import com.newren.smartprotecter.util.QueueApplication;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by ¿÷ on 2015/8/24.
 */
public class FragmentMe extends Fragment {
    private TextView txtName = null;
    private TextView txtNumber = null;
    private ImageView imgPhoto = null;
    private TextView txtSex = null;
    private TextView txtRole = null;
    private User user =null;
    private View myView = null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        user = QueueApplication.getUser();
        myView = inflater.inflate(R.layout.me, container, false);
        txtName = (TextView) myView.findViewById(R.id.txtName);
        txtNumber = (TextView) myView.findViewById(R.id.txtNumber);
        imgPhoto = (ImageView) myView.findViewById(R.id.imgPhoto);
        txtSex = (TextView) myView.findViewById(R.id.txtSex);
        txtRole = (TextView) myView.findViewById(R.id.txtRole);
        txtName.setText(user.getName());
        txtNumber.setText(user.getNumber());
        txtSex.setText(Common.sexDisply[user.getSexAsInt()]);
        txtRole.setText(Common.roleDisply[user.getRoleAsInt()]);


        ImageLoader imageLoader = new ImageLoader(QueueApplication.getHttpQueues(), new BitmapCache());

        ImageLoader.ImageListener listener = ImageLoader.getImageListener(imgPhoto,R.drawable.userphoto, R.drawable.userphoto);
        imageLoader.get("http://121.42.136.4:9000/UserApi/ShowPicture/"+user.getId(), listener);
        return  myView;
    }



}
