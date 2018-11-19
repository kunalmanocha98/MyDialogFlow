package com.agnitioworld.chatbot;

import android.content.res.ColorStateList;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.agnitioworld.chatbot.Models.ChatBotListResult;
import com.agnitioworld.chatbot.Models.ListResults;
import com.agnitioworld.chatbot.Network.ApiUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OptionsPage extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    RecyclerView chatbot_recycler;
    List<Chatbotdata> list = new ArrayList<>();
    Toolbar toolbar;
    CollapsingToolbarLayout ctl;
    SwipeRefreshLayout swipetorefresh;
    CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options_page);
        ctl = findViewById(R.id.toolbar_layout);
        swipetorefresh=findViewById(R.id.swipe_refresh);
coordinatorLayout=findViewById(R.id.coordinator_layout);
//        ctl.setExpandedTitleTextColor();
        ctl.setCollapsedTitleTextAppearance(R.style.collapsed_appearance);
        ctl.setExpandedTitleTextAppearance(R.style.expanded_appearance);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = true;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    setTitle("Agnitio bot");
                    ctl.setTitle("Agnitio bot");
                    isShow = true;
                } else if(isShow) {
                    setTitle("");
                    ctl.setTitle("");
                    isShow = false;
                }
            }
        });

        chatbot_recycler = findViewById(R.id.chatbot_recycler);
        chatbot_recycler.setLayoutManager(new LinearLayoutManager(this));
        swipetorefresh.setOnRefreshListener(this);
        swipetorefresh.setRefreshing(true);
        getdata();

    }

    private void getdata() {

        if (Utils.isinternetconnection(this)) {
            ApiUtils.getAPIService().chatbotlist().enqueue(new Callback<ChatBotListResult>() {
                @Override
                public void onResponse(Call<ChatBotListResult> call, Response<ChatBotListResult> response) {
                    if (response.isSuccessful()){
                        if (response.body().getCode().equals("101")){
                            setlistofchatbots(response.body().getResultsList());
                            swipetorefresh.setRefreshing(false);
                        }
                    }
                }

                @Override
                public void onFailure(Call<ChatBotListResult> call, Throwable t) {
                }
            });

        } else {
            swipetorefresh.setRefreshing(false);
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, "Oops ! No Internet Connection", Snackbar.LENGTH_LONG);
            View sbView = snackbar.getView();
            TextView textView = sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.YELLOW);
            snackbar.show();
        }



    }

    private void setlistofchatbots(List<ListResults> resultsList) {
        ChatbotListAdapter adapter=new ChatbotListAdapter(this,resultsList);
        chatbot_recycler.setAdapter(adapter);
    }

    @Override
    public void onRefresh() {
        getdata();
    }
}
