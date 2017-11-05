package com.example.hp.in_a_click.remote;

import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by hp on 11/5/2017.
 */

public class RetrofitClient {

    private static Retrofit retrofit = null;

    public static Retrofit getClient(String baseUrl) {


        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .build();


        }
        return retrofit;

    }

}
