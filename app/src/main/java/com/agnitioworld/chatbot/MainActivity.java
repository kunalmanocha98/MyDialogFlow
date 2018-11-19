package com.agnitioworld.chatbot;

import android.graphics.Color;
import android.os.AsyncTask;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ai.api.AIDataService;
import ai.api.AIListener;
import ai.api.AIServiceException;
import ai.api.android.AIConfiguration;
import ai.api.android.AIService;
import ai.api.model.AIError;
import ai.api.model.AIRequest;
import ai.api.model.AIResponse;
import ai.api.model.GoogleAssistantResponseMessages;
import ai.api.model.ResponseMessage;
import ai.api.model.Result;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, AIListener, OnSuggestionClickListener {
    ImageView send_btn;
    EditText input_text;
    RecyclerView msg_recyclerView;
    //    RecyclerView suggestion_recycler;
    AutoSpacingViewLayout auto_spacing_view;
    AIService aiService;
    AIConfiguration config;
    List<Chatdata> list = new ArrayList<>();
    List<SuggestionData> suggestionData = new ArrayList<>();
    AIRequest aiRequest = new AIRequest();
    AIDataService aiDataService;
    CustomAdapter adapter;
    CoordinatorLayout coordinatorLayout;
    RelativeLayout send_message_container;
    boolean flag = true;
    private GoogleAssistantResponseMessages.ResponseLinkOutChip linkOutChip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        send_btn = findViewById(R.id.send_btn);
        coordinatorLayout = findViewById(R.id.coordinator);
        input_text = findViewById(R.id.edttxt);
        send_message_container = findViewById(R.id.send_message_wrapper);
//        input_text.addTextChangedListener(new MyTextWatcher(input_text));
        msg_recyclerView = findViewById(R.id.recycler_view);
        auto_spacing_view = findViewById(R.id.autospacing_layout);

        String token = getIntent().getStringExtra(Utils.Intents.TOKEN_ID);
        String name = getIntent().getStringExtra(Utils.Intents.CHATBOT_NAME);


        setTitle(name);
        getSupportActionBar().setSubtitle("Online");

        msg_recyclerView.setLayoutManager(new LinearLayoutManager(this));

        send_btn.setOnClickListener(this);
        config = new AIConfiguration(token,
                AIConfiguration.SupportedLanguages.English,
                AIConfiguration.RecognitionEngine.System);
        aiService = AIService.getService(this, config);
        aiDataService = new AIDataService(config);
        aiService.setListener(this);
        adapter = new CustomAdapter(this, list);
        msg_recyclerView.setAdapter(adapter);
        list.clear();
        suggestionData.clear();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.send_btn: {
                String message = input_text.getText().toString().trim();
                input_text.setText("");
                sendMessage(message);
                break;
            }

        }
    }

    private void sendMessage(final String message) {
        if (Utils.isinternetconnection(this)) {
            // In this method i am sending the message to dialog flow
            messageupdate(message, Utils.MessageTypes.ME);
            aiRequest.setQuery(message);
            new MyAsyncTask().execute(aiRequest);
        } else {
            flag = true;
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, "Oops ! No Internet Connection", Snackbar.LENGTH_LONG);
            View sbView = snackbar.getView();
            TextView textView = sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.YELLOW);
            snackbar.show();
        }

    }

    @Override
    public void onSuggestionClick(String title) {
        sendMessage(title);
    }

    class MyAsyncTask extends AsyncTask<AIRequest, Void, AIResponse> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            messageupdate(Utils.Messages.LOADER, Utils.MessageTypes.LOADER);
            input_text.setEnabled(false);
        }

        @Override
        protected AIResponse doInBackground(AIRequest... aiRequests) {
//            final AIRequest request = aiRequests[0];
            try {
                final AIResponse response = aiDataService.request(aiRequest);
                Thread.sleep(2000);
                return response;
            } catch (AIServiceException e) {

            } catch (InterruptedException e) {
//                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(AIResponse response) {
            removeloader();
            suggestionData.clear();
            if (response != null) {
                Result result = response.getResult();
                String reply = result.getFulfillment().getSpeech();
                List<ResponseMessage> mResponseList = result.getFulfillment().getMessages();
//                Log.e("map",""+mResponseList.size());
                for (int chip = 0; chip < mResponseList.size(); chip++) {
                    ResponseMessage rMessage = mResponseList.get(chip);
//                    Log.e("message",""+rMessage.toString());
                    if (rMessage instanceof GoogleAssistantResponseMessages.ResponseSuggestionChips) {
//                        Log.e("message,1",""+rMessage.toString());
                        GoogleAssistantResponseMessages.ResponseSuggestionChips sgChips
                                = (GoogleAssistantResponseMessages.ResponseSuggestionChips) rMessage;
                        List<GoogleAssistantResponseMessages.ResponseSuggestionChips.Suggestion>
                                mSuggestion = sgChips.getSuggestions();
                        for (int sugg = 0; sugg < mSuggestion.size(); sugg++) {
                            Log.e("suggestions", mSuggestion.get(sugg).getTitle());
                            SuggestionData suggestions = new SuggestionData();
                            suggestions.setSuggestiontext(mSuggestion.get(sugg).getTitle());
                            suggestionData.add(suggestions);
                        }
                    } else if (rMessage instanceof GoogleAssistantResponseMessages.ResponseLinkOutChip) {
                        linkOutChip =
                                (GoogleAssistantResponseMessages.ResponseLinkOutChip) rMessage;
                        messageupdate(linkOutChip.getDestinationName(), Utils.MessageTypes.LINK);

                    }
                }
                messageupdate(reply, Utils.MessageTypes.THEY);
            }
        }
    }

    private void removeloader() {
        list.remove(list.size() - 1);
        adapter.notifyItemRemoved(list.size() - 1);
        input_text.setEnabled(true);
    }

    private void messageupdate(String msg, String type) {
        if (!msg.trim().equals("")) {
            if (suggestionData.size() > 0) {
                auto_spacing_view.removeAllViews();
                flag = true;
                setsuggestions();
                auto_spacing_view.setVisibility(View.VISIBLE);
                send_message_container.setVisibility(View.GONE);
                Utils.hideKeyboard(MainActivity.this);
            } else {
                auto_spacing_view.setVisibility(View.GONE);
                send_message_container.setVisibility(View.VISIBLE);
//            suggestion_recycler.setVisibility(View.GONE);
            }
            Chatdata msgdata = new Chatdata();
            if (type.equals(Utils.MessageTypes.LINK)) {
                msgdata.setLinkurl(linkOutChip.getUrl());
            }
            msgdata.setMessage(msg);
            msgdata.setMessagedata(type);
            list.add(msgdata);
            adapter.notifyDataSetChanged();
            msg_recyclerView.smoothScrollToPosition(list.size() - 1);
        }
    }

    private void setsuggestions() {
        for (int i = 0; i < suggestionData.size(); i++) {
            final TextView textView = new TextView(this);
            textView.setText(suggestionData.get(i).getSuggestiontext());
            textView.setTextSize(16f);
//            textView.setTextColor(Color.parseColor("#000000"));
            textView.setTextColor(Color.parseColor("#ffffff"));
//            textView.setElevation(Utils.convertdipstopixel(this,8f));
            textView.setElevation(4f);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (flag) {
                        flag = false;
                        sendMessage(textView.getText().toString());
                    }
                }
            });
            textView.setPadding((int) Utils.convertdipstopixel(this, 16),
                    (int) Utils.convertdipstopixel(this, 8),
                    (int) Utils.convertdipstopixel(this, 16),
                    (int) Utils.convertdipstopixel(this, 8));
            textView.setBackground(getDrawable(R.drawable.background_button));
            auto_spacing_view.addView(textView);
        }
    }


    @Override
    public void onResult(AIResponse result) {
    }

    @Override
    public void onError(AIError error) {

    }

    @Override
    public void onAudioLevel(float level) {

    }

    @Override
    public void onListeningStarted() {

    }

    @Override
    public void onListeningCanceled() {

    }

    @Override
    public void onListeningFinished() {

    }

    private class MyTextWatcher implements TextWatcher {
        EditText inputtext;

        public MyTextWatcher(EditText input_text) {
            this.inputtext = input_text;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

        @Override
        public void afterTextChanged(Editable editable) {
            if (editable.toString().equals("")) {
                send_btn.setVisibility(View.GONE);
            } else {
                send_btn.setVisibility(View.VISIBLE);
            }
        }
    }
}
