package com.wzq.jz_app.utils;


import com.google.gson.Gson;

public class GsonUtil {
    private static class GsonHolder{
        private static final Gson INSTANCE = new Gson();
    }

    public static Gson getInstance()
    {
        return GsonHolder.INSTANCE;
    }


}
