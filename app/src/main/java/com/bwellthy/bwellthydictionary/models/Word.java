package com.bwellthy.bwellthydictionary.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by miteshp on 13/01/16.
 */
public class Word {

    @SerializedName("id")
    private Integer id;

    @SerializedName("word")
    private String word;

    @SerializedName("variant")
    private Integer variant;

    @SerializedName("meaning")
    private String meaning;

    @SerializedName("ratio")
    private double ratio;

    public Integer getId() {
        return id;
    }

    public String getWord() {
        return word;
    }

    public Integer getVariant() {
        return variant;
    }

    public String getMeaning() {
        return meaning;
    }

    public double getRatio() {
        return ratio;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public void setVariant(Integer variant) {
        this.variant = variant;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }

    public void setRatio(double ratio) {
        this.ratio = ratio;
    }
}
