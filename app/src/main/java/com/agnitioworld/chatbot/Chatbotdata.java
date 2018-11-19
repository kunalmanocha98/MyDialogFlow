package com.agnitioworld.chatbot;

import android.graphics.Bitmap;
import android.support.annotation.Nullable;

class Chatbotdata {
    private String token_key;
    private Bitmap agent_bmp;
    private String chatbot_name;
    private String agent_name;

    public String getAgentName() {
        return agent_name;
    }

    public Chatbotdata(String token, Bitmap bmp, String chatbot_name, String agent_name) {
        this.token_key = token;
        this.agent_bmp = bmp;
        this.chatbot_name = chatbot_name;
        this.agent_name=agent_name;
    }

    public Bitmap getAgent_bmp() {
        return agent_bmp;
    }

    public String getChatbot_name() {
        return chatbot_name;
    }

    public String getToken_key() {
        return token_key;
    }

}
