package com.newren.smartprotecter.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.newren.smartprotecter.R;
import com.newren.smartprotecter.model.Accident;
import com.newren.smartprotecter.model.Reply;
import com.newren.smartprotecter.util.BitmapCache;
import com.newren.smartprotecter.util.QueueApplication;

import java.util.List;

/**
 * Created by 乐 on 2015/8/30.
 */
public class ListReplyAdapter extends BaseAdapter {

    private List<Reply> items;
    private LayoutInflater inflater;

    public ListReplyAdapter(Context context, List<Reply> items) {
        this.items = items;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void bindData(List<Reply> items){
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
            view = inflater.inflate(R.layout.list_reply, null);
        }
        Reply item = items.get(position);
        ImageView userPhotot = (ImageView) view.findViewById(R.id.ivUserPhoto);
        TextView tvTime = (TextView) view.findViewById(R.id.txtReplyTime);
        TextView tvDescription = (TextView) view.findViewById(R.id.txtReply);
        TextView tvUsername =  (TextView) view.findViewById(R.id.txtUsername);
        tvTime.setText(item.getTime());
        tvDescription.setText(item.getDescription());
        tvUsername.setText(item.getUserName());
        ImageLoader imageLoader = new ImageLoader(QueueApplication.getHttpQueues(), new BitmapCache());

        ImageLoader.ImageListener listener = ImageLoader.getImageListener(userPhotot,R.drawable.userphoto, R.drawable.userphoto);
        imageLoader.get("http://121.42.136.4:9000/UserApi/ShowPicture/" + item.getUserId(), listener);
        return view;
    }
}
