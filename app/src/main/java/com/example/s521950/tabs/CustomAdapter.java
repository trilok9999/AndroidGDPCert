package com.example.s521950.tabs;

/**
 * Created by S521950 on 10/26/2015.
 */
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Map;

public class CustomAdapter extends BaseAdapter {
    private final ArrayList mData;

    public CustomAdapter(Map<String, String> map) {
        mData = new ArrayList();
        mData.addAll(map.entrySet());
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Map.Entry<String, String> getItem(int position) {
        return (Map.Entry) mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO implement you own logic with ID
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final View result;

        if (convertView == null) {
            result = LayoutInflater.from(parent.getContext()).inflate(R.layout.messageslayout, parent, false);
        } else {
            result = convertView;
        }

        Map.Entry<String, String> item = getItem(position);


        TextView teamName=(TextView)result.findViewById(R.id.sender);
        teamName.setText(item.getKey().substring(0,item.getKey().length()-2));
        TextView message=(TextView)result.findViewById(R.id.message);
        message.setText(item.getValue());


        return result;
    }
}