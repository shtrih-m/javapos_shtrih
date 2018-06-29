package com.shtrih.tinyjavapostester;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.shtrih.tinyjavapostester.databinding.ActivityTcpDeviceSearchBinding;


public class TcpDeviceSearchActivity extends AppCompatActivity {

    public static final int REQUEST_SEARCH_TCP_DEVICE = 666;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_tcp_device_search);

        ActivityTcpDeviceSearchBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_tcp_device_search);

        TcpDeviceSearchViewModel vm = ViewModelProviders.of(this).get(TcpDeviceSearchViewModel.class);

        binding.setVm(vm);
        binding.setActivity(this);
    }
}
