package com.example.hp.in_a_click.common;

import com.example.hp.in_a_click.remote.IGoogleAPI;
import com.example.hp.in_a_click.remote.RetrofitClient;

/**
 * Created by hp on 11/5/2017.
 */

public class Common {

    public static final String baseUrl = "https://maps.googleapis.com";

    public static IGoogleAPI getGoogleApi() {
        return RetrofitClient.getClient(baseUrl).create(IGoogleAPI.class);
    }


}
