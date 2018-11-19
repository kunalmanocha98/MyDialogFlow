package com.agnitioworld.chatbot.Network;


public class ApiUtils {


    public static ApiService getAPIService() {
        return RetrofitClient.getClient().create(ApiService.class);
    }


}