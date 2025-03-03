package ru.shtrih_m.kktnetd;

public class Api{
    static{
        System.loadLibrary("kktnetd");
    }
    //the function is defined in a c-file
    public static native int start(final String name);
    public static native void stop();
    // api v2
    public static native long api_init(final String instance_config);
    public static native long api_run(long ctx);
    public static native void api_stop(long ctx);
    public static native void api_deinit(long ctx);
    public static native String api_status(long ctx);
}
