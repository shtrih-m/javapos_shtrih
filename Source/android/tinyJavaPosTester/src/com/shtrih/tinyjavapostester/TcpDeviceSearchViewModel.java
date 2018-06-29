package com.shtrih.tinyjavapostester;

import android.app.Activity;
import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.content.Intent;
import android.support.annotation.NonNull;

public class TcpDeviceSearchViewModel extends AndroidViewModel {
    public TcpDeviceSearchViewModel(@NonNull Application application) {
        super(application);
    }

    public void onClick(Activity activity) {

        Intent intent = new Intent();
        intent.putExtra("Address", "192.168.1.45:7778");

        activity.setResult(Activity.RESULT_OK, intent);
        activity.finish();
    }
}
