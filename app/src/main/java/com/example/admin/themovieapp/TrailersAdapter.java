package com.example.admin.themovieapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class TrailersAdapter extends RecyclerView.Adapter<TrailersAdapter.ViewHolder>
{
      private List<Trailer> trailers;
      private Context context;
      private OnItemClickListener clickListener;

      public TrailersAdapter(Context context)
      {
          this.context = context;
          trailers = new ArrayList<>();
      }

    public void setClickListener(OnItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
}

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;
        View view = inflater.inflate(R.layout.activity_trailerbutton,parent,shouldAttachToParentImmediately);
        TrailersAdapter.ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        Trailer trailer = trailers.get(position);
        String name = trailer.getName();
        holder.title.setText(name);

//        Will deal with the button later on
    }

    @Override
    public int getItemCount() {
        return trailers.size();
    }

    public void setTrailers(List<Trailer> trailersList)
    {
        this.trailers.clear();
        this.trailers.addAll(trailersList);

        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {

      TextView title;


        public ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.youtube_title);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v)
        {

            clickListener.onClick(v,getAdapterPosition());
            Log.d("OnClick","Onclick is set");
        }
    }
}
