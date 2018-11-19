package com.agnitioworld.chatbot.Network;

import com.agnitioworld.chatbot.Models.ChatBotListResult;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {

    @GET("chatbot_list.php")
    Call<ChatBotListResult> chatbotlist();

}
