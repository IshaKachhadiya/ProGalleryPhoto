package com.iten.tenoku.utils;

public class Cpp {

    static {
        System.loadLibrary("dogstrorm");
    }

    public static native String baseApi();

    public static final String baseApi = baseApi();

}


