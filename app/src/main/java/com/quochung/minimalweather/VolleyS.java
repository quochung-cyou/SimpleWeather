package com.quochung.minimalweather;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class VolleyS {

    private RequestQueue requestQueue;
    private static VolleyS mInstance;

    private VolleyS(Context context){
        requestQueue = Volley.newRequestQueue(context.getApplicationContext());
    }

    public static synchronized VolleyS getmInstance(Context context){

        if (mInstance == null){
            mInstance = new VolleyS(context);
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue(){return requestQueue;}
}