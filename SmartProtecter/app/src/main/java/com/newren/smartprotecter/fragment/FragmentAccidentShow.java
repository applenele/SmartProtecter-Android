package com.newren.smartprotecter.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.newren.smartprotecter.R;

/**
 * Created by 乐 on 2015/8/27.
 */
public class FragmentAccidentShow extends Fragment {

    private View myView = null;
    private TextView text = null;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.accident_show, container, false);
        text = (TextView) myView.findViewById(R.id.textView2);
        String i = getArguments().getString("key");
        Log.i("id", i);
        text.setText(i);
        return myView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
