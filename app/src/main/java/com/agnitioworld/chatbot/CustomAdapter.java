package com.agnitioworld.chatbot;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

class CustomAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    List<Chatdata> list;

    public CustomAdapter(Context context, List<Chatdata> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (i == 1) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.left_bubble, viewGroup, false);
            LeftViewHolder vh = new LeftViewHolder(v);
            return vh;
        } else if (i == 0) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.right_bubble, viewGroup, false);
            RightViewHolder vh = new RightViewHolder(v);
            return vh;
        } else if (i == 2) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.left_bubble_gif, viewGroup, false);
            GifViewHolder vh = new GifViewHolder(v);
            return vh;
        }else if (i == 3) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.left_bubble_link, viewGroup, false);
            LinkViewHolder vh = new LinkViewHolder(v);
            return vh;
        } else {
            return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        final int position=i;
        if (viewHolder instanceof LeftViewHolder) {
            ((LeftViewHolder) viewHolder).txt_msg.setText(list.get(i).message);
        }else if (viewHolder instanceof RightViewHolder){
            ((RightViewHolder) viewHolder).txt_msg.setText(list.get(i).message);
        }else if (viewHolder instanceof GifViewHolder){
            Glide.with(context).asGif().load(R.drawable.loader).into(((GifViewHolder) viewHolder).img_gif);
        }else if (viewHolder instanceof LinkViewHolder){
            ((LinkViewHolder) viewHolder).txt_link.setText(list.get(i).getMessage());
            ((LinkViewHolder) viewHolder).txt_link.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i=new Intent(context,MyWebView.class);
                    i.putExtra(Utils.Intents.LINKURL,list.get(position).getLinkurl());
                    context.startActivity(i);
                }
            });
        }
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (list.get(position).messagedata.equals(Utils.MessageTypes.ME)) {
            return 0;
        } else if (list.get(position).getMessagedata().equals(Utils.MessageTypes.THEY)) {
            return 1;
        } else if (list.get(position).getMessagedata().equals(Utils.MessageTypes.LOADER)) {
            return 2;
        } else if (list.get(position).getMessagedata().equals(Utils.MessageTypes.LINK)){
            return 3;
        }else {
            return 4;
        }
    }

    public class LeftViewHolder extends RecyclerView.ViewHolder {
        TextView txt_msg;

        public LeftViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_msg = itemView.findViewById(R.id.txt_msg);
        }
    }

    public class RightViewHolder extends RecyclerView.ViewHolder {
        TextView txt_msg;

        public RightViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_msg = itemView.findViewById(R.id.txt_msg);
        }
    }
    private class GifViewHolder extends RecyclerView.ViewHolder {
        ImageView img_gif;

        public GifViewHolder(@NonNull View itemView) {
            super(itemView);
            img_gif = itemView.findViewById(R.id.giffy_view);
        }
    }

    private class LinkViewHolder extends RecyclerView.ViewHolder {
        TextView txt_link;

        public LinkViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_link=itemView.findViewById(R.id.txt_link);
        }
    }
}
