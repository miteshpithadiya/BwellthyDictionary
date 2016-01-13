package com.bwellthy.bwellthydictionary.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

/**
 * Created by miteshp on 13/01/2016.
 */
public class RestClient {

    private static final String BASE_URL = "http://appsculture.com";


    public static RestAdapter getRestAdapter() {

        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy'-'MM'-'dd'T'HH':'mm':'ss'.'SSS'Z'")
                .create();


        return new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.NONE)
                .setEndpoint(BASE_URL)
                .setConverter(new GsonConverter(gson))
                .setErrorHandler(new RetrofitErrorHandler())
                .build();
    }

}
