package com.example.musicplayerr;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    private List<post> postList; // Matches small 'p' post class

    // Constructor
    public PostAdapter(List<post> postList) {
        this.postList = postList;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Uses a standard Android simple list item layout
        View view = LayoutInflater.from(parent.getContext())
                .inflate(android.R.layout.simple_list_item_2, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        post currentPost = postList.get(position);
        holder.textTitle.setText(currentPost.getTitle());
        holder.textBody.setText(currentPost.getBody());
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public static class PostViewHolder extends RecyclerView.ViewHolder {
        TextView textTitle, textBody;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            textTitle = itemView.findViewById(android.R.id.text1);
            textBody = itemView.findViewById(android.R.id.text2);
        }
    }
}