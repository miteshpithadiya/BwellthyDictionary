package com.bwellthy.bwellthydictionary.rest;

import com.bwellthy.bwellthydictionary.models.Dictionary;
import com.bwellthy.bwellthydictionary.models.Word;
import com.bwellthy.bwellthydictionary.network.RestCallback;

import java.util.List;

import retrofit.http.GET;

/**
 * Created by miteshp on 13/01/2016.
 */
public interface DictionaryService {

    @GET("/vocab/words.json")
    void getActivities(final RestCallback<Dictionary> callback);

}
