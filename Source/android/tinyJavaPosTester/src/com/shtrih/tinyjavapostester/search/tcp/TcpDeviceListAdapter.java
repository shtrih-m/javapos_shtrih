package com.shtrih.tinyjavapostester.search.tcp;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableArrayList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.shtrih.tinyjavapostester.R;
import com.shtrih.tinyjavapostester.databinding.TcpDeviceItemLayoutBinding;

public class TcpDeviceListAdapter extends BaseAdapter {
    private ObservableArrayList<TcpDevice> list;
    private LayoutInflater inflater;

    public TcpDeviceListAdapter(ObservableArrayList<TcpDevice> l) {
        list = l;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (inflater == null) {
            inflater = (LayoutInflater) parent.getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        TcpDeviceItemLayoutBinding binding = DataBindingUtil.inflate(inflater, R.layout.tcp_device_item_layout, parent, false);
        binding.setVm(list.get(position));

        return binding.getRoot();
    }
}
