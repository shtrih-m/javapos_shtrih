package com.shtrih.util;


/**
 * Created by amednyy on 12/15/16.
 */

import com.shtrih.util.ServiceVersion;

public class ServiceVersionUtil {
    public static int getVersionInt() {
        int result;
        String parts[] = ServiceVersion.VERSION.split("-");
        try {
            result = Integer.parseInt(parts[0]);
        }catch (NumberFormatException e){
            e.printStackTrace();
            result = 0;
        }
        return result;
    }
}
