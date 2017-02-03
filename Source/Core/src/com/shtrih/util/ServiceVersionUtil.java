package com.shtrih.util;


/**
 * Created by amednyy on 12/15/16.
 */

public class ServiceVersionUtil {
    public static int getVersionInt() {
        int result;
        String parts[] = ServiceVersion.VERSION.split("-");
        try {
            if(parts.length == 0)
                return 0;

            if(parts[0].equals(""))
                return 0;

            result = Integer.parseInt(parts[0]);
        }catch (NumberFormatException e){
            e.printStackTrace();
            result = 0;
        }
        return result;
    }
}
