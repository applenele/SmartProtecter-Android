package com.newren.smartprotecter.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.newren.smartprotecter.R;
import com.newren.smartprotecter.model.Accident;

import java.util.List;

/**
 * Created by 乐 on 2015/8/24.
 */
public class ListAccidentAdapter extends BaseAdapter {

    private List<Accident> items;
    private LayoutInflater inflater;

    public ListAccidentAdapter(Context context, List<Accident> items) {
        this.items = items;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void bindData(List<Accident> items){
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (view == null) {
            view = inflater.inflate(R.layout.list_accident, null);
        }
        TextView description = (TextView) view.findViewById(R.id.txtDescription);
        TextView time = (TextView) view.findViewById(R.id.txtTime);
        description.setText(items.get(position).getDescription());
        time.setText(items.get(position).getTime().toString());
        return view;
    }


}
