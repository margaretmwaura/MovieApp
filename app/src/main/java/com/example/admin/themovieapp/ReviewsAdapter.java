 package com.example.admin.themovieapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ViewHolder>
{
    private List<Reviews> reviews;
    private Context context;

    public ReviewsAdapter(Context context)
    {
        this.context = context;
        reviews = new ArrayList<>();
    }

    @Override
    public ReviewsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;
        View view = inflater.inflate(R.layout.activity_reviews,parent,shouldAttachToParentImmediately);
        ReviewsAdapter.ViewHolder viewHolder = new ReviewsAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        Reviews mine = reviews.get(position);
        String author = mine.getAuthor();
        String details = mine.getDetails();

        holder.details.setText(details);
        holder.author.setText(author);
//        Will deal with the button later on
    }

    @Override
    public int getItemCount()
    {
        return reviews.size();
    }

    public void setReviews(List<Reviews> reviewsList)
    {
        this.reviews.clear();
        this.reviews.addAll(reviewsList);
        // The adapter needs to know that the data has changed. If we don't call this, app will crash.
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder
    {

        TextView author;
        TextView details;


        public ViewHolder(View itemView)
        {
            super(itemView);
            author = itemView.findViewById(R.id.reviews_author);
            details = itemView.findViewById(R.id.reviews_details);

        }


    }
}
