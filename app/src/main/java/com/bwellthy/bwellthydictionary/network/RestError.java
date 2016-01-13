package com.bwellthy.bwellthydictionary.network;

import com.google.gson.annotations.SerializedName;

/**
 * Created by miteshp on 13/01/2016.
 */
public class RestError {
    public RestError() {
    }

    @SerializedName("code")

    private Integer code;

    @SerializedName("error_message")
    private String strMessage;

    public RestError(String strMessage) {
        this.strMessage = strMessage;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getStrMessage() {
        return strMessage;
    }

    public void setStrMessage(String strMessage) {
        this.strMessage = strMessage;
    }
}
