package com.zamio.adong.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zamio.adong.R;
import com.zamio.adong.model.Province;

import java.util.List;

public class CustomAdapter extends ArrayAdapter<Province> {

    LayoutInflater flater;

    public CustomAdapter(Activity context, int resouceId, int textviewId, List<Province> list){

        super(context,resouceId,textviewId, list);
//        flater = context.getLayoutInflater();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        return rowview(convertView,position);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return rowview(convertView,position);
    }

    private View rowview(View convertView , int position){

        Province rowItem = getItem(position);

        viewHolder holder ;
        View rowview = convertView;
        if (rowview == null) {

            holder = new viewHolder();
            flater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowview = flater.inflate(R.layout.item_provice_spinner, null, false);

            holder.tvName = (TextView) rowview.findViewById(R.id.tvName);

            rowview.setTag(holder);
        }else{
            holder = (viewHolder) rowview.getTag();
        }

        holder.tvName.setText(rowItem.getName());

        return rowview;
    }

    private class viewHolder{
        TextView tvName;
        ImageView imageView;
    }
}
