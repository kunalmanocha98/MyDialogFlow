package com.agnitioworld.chatbot;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.agnitioworld.chatbot.Models.ListResults;
import com.bumptech.glide.Glide;

import java.util.List;

class ChatbotListAdapter extends RecyclerView.Adapter<ChatbotListAdapter.ViewHolder> {
    Context context;
    List<ListResults> list;
    public ChatbotListAdapter(Context context, List<ListResults> list) {
        this.list=list;
        this.context=context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
       View v=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.chatbot_list_item,viewGroup,false);
       return new  ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {
        final int position=i;
        holder.txt_agent_name.setText(list.get(position).getAgent_name());
        holder.txt_chatbot_name.setText(list.get(position).getChatbot_name());
        Glide.with(context).load(list.get(position).getPic_url()).into(holder.img_chatbot);
        holder.item_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i =new Intent(context,MainActivity.class);
                i.putExtra(Utils.Intents.TOKEN_ID,list.get(position).getToken_id());
                i.putExtra(Utils.Intents.CHATBOT_NAME,list.get(position).getChatbot_name());
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView img_chatbot;
        TextView txt_chatbot_name,txt_agent_name;
        CardView item_container;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            item_container=itemView.findViewById(R.id.chatbot_item_wrapper);
            img_chatbot=itemView.findViewById(R.id.img_chatbot_pic);
            txt_chatbot_name =itemView.findViewById(R.id.txt_chatbot_name);
            txt_agent_name=itemView.findViewById(R.id.agent_name);
        }
    }
}
