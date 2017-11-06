package com.example.hp.in_a_click.remote;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

/**
 * Created by hp on 11/5/2017.
 */

public interface IGoogleAPI {

    @GET
    Call<String> getPath(@Url String url);

}
