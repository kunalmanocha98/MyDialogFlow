package com.agnitioworld.chatbot.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ListResults {
    @SerializedName("agent_name")
    @Expose
    String agent_name;

    @SerializedName("chatbot_name")
    @Expose
    String chatbot_name;

    @SerializedName("token_id")
    @Expose
    String token_id;

    @SerializedName("agent_pic_url")
    @Expose
    String pic_url;

    @SerializedName("availability_status")
    @Expose
    String availability_status;


    public String getAgent_name() {
        return agent_name;
    }

    public String getChatbot_name() {
        return chatbot_name;
    }

    public String getToken_id() {
        return token_id;
    }

    public String getAvailability_status() {
        return availability_status;
    }

    public String getPic_url() {
        return pic_url;
    }
}
