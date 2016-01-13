package com.bwellthy.bwellthydictionary.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Meditab on 13/01/16.
 */
public class Dictionary {

    @SerializedName("version")
    private Integer version;

    @SerializedName("words")
    private List<Word> words;

    public Integer getVersion() {
        return version;
    }

    public List<Word> getWords() {
        return words;
    }
}
