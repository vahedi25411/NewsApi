package com.example.android.newsapp;

import android.content.ClipData;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.newsapp.model.NewsItem;

import java.util.ArrayList;

/**
 * Created by Vahedi on 6/29/17.
 */

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ItemViewHolder>{

    private ArrayList<NewsItem> data;
    private ItemClickListener listener;

    public NewsAdapter(ArrayList<NewsItem> data, ItemClickListener listener) {
        this.data = data;
        this.listener = listener;
    }

    public interface ItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shoudAttachToParentImmediately = false;

        View view = inflater.inflate(R.layout.news_list_item,  parent, shoudAttachToParentImmediately );
        ItemViewHolder itemViewHolder = new ItemViewHolder(view);

        return itemViewHolder;
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        if(data == null) {
            return 0;
        }
        else{
            return data.size();
        }
    }

    public void setData(ArrayList<NewsItem> data){
        this.data = data;
        notifyDataSetChanged();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView title;
        TextView description;
        TextView date;

        ItemViewHolder(View view){
            super(view);

            title = (TextView) view.findViewById(R.id.news_title);
            description = (TextView) view.findViewById(R.id.news_description);
            date = (TextView) view.findViewById(R.id.news_date);

            view.setOnClickListener(this);

        }

        public void bind(int position)
        {
            NewsItem newsItem = data.get(position);
            title.setText(newsItem.getTitle());
            description.setText(newsItem.getDescription());
            date.setText(newsItem.getPublishedAt());
        }

        @Override
        public void onClick(View v) {
            int pos = getAdapterPosition();
            listener.onListItemClick(pos);
        }
    }
}
