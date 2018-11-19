package com.agnitioworld.chatbot.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import retrofit2.http.FormUrlEncoded;

public class ChatBotListResult {
    @SerializedName("code")
    @Expose
    String code;

    @SerializedName("message")
    @Expose
    String message;

    @SerializedName("result")
    @Expose
    List<ListResults> resultsList;

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public List<ListResults> getResultsList() {
        return resultsList;
    }
}
