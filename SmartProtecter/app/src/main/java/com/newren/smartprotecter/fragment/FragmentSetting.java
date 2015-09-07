package com.newren.smartprotecter.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.newren.smartprotecter.R;
import com.newren.smartprotecter.activity.LoginActivity;
import com.newren.smartprotecter.util.QueueApplication;

/**
 * Created by �� on 2015/8/24.
 */
public class FragmentSetting extends Fragment {

    private View myView =null;
    private Button btnMyPublish = null;
    private Button btnLogout = null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
    }


    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        myView = inflater.inflate(R.layout.setting, container, false);
        btnMyPublish = (Button) myView.findViewById(R.id.btnMyPublish);
        btnLogout = (Button) myView.findViewById(R.id.btnLogout);

        btnMyPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentMyPublishment accidents = new FragmentMyPublishment();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_content, accidents).commit();
            }
        });
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QueueApplication.setUser(null);
                Intent intent = new Intent();
                intent.setClass(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });

        return myView;
    }
}
