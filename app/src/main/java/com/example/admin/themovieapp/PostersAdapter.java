package com.example.admin.themovieapp;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView.OnItemLongClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class PostersAdapter  extends RecyclerView.Adapter<PostersAdapter.PosterViewHolder>
{

    private static SparseBooleanArray recyclerArrayState = new SparseBooleanArray();
    private List<Movie> mMovieList,addFavourite;
    private Context mContext;
    public static final String IMAGE_URL_BASE_PATH = " http://image.tmdb.org/t/p/w342";
    private OnItemClickListener cLickListener;
    private OnLongClickListener longCLickListener;


    public PostersAdapter(Context context)
    {
        this.mContext = context;
        this.mMovieList = new ArrayList<>();

    }
    public void setClickListener(OnItemClickListener itemClickListener)
    {
        this.cLickListener = itemClickListener;
    }
    public void setLongCLickListener(OnLongClickListener itemClickListener)
    {
        this.longCLickListener = itemClickListener;
    }


    @Override
    public PosterViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;
        View view = inflater.inflate(R.layout.activity_images,parent,shouldAttachToParentImmediately);
        PosterViewHolder posterViewHolder = new PosterViewHolder(view);
        return posterViewHolder;
    }

    @Override
    public void onBindViewHolder(PosterViewHolder holder, int position)
    {

        Movie movie = mMovieList.get(position);
        String image_url = IMAGE_URL_BASE_PATH + movie.getPosterPath();
        String toLoad = image_url.trim();
        Log.d("The url",image_url);
        // This is how we use Picasso to load images from the internet.
//        Picasso.with(mContext)
//                .load(image_url)
//                .into(holder.posterImageView, new com.squareup.picasso.Callback()
//                {
//                    @Override
//                    public void onSuccess()
//                    {
//                        Toast.makeText(mContext, " Huge success dear one", Toast.LENGTH_LONG).show();
//                    }
//
//                    @Override
//                    public void onError()
//                    {
//                        Toast.makeText(mContext,"Fail",Toast.LENGTH_LONG).show();
//                    }
//                });


        Picasso.Builder builder = new Picasso.Builder(mContext);
        builder.listener(new Picasso.Listener()
        {
            @Override
            public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception)
            {
                exception.printStackTrace();
                Log.d("Picasso Error","The error " + exception);
            }
        });
        builder.build().load(toLoad).fit().into(holder.posterImageView);

//This is to be called during the longClicks if the position changes

        holder.bind(position);

    }

    @Override
    public int getItemCount() {
        return mMovieList.size();
    }

    public void setMovieList(List<Movie> movieList)
    {
        this.mMovieList.clear();
        this.mMovieList.addAll(movieList);
        // The adapter needs to know that the data has changed. If we don't call this, app will crash.
        notifyDataSetChanged();
    }

    public static void emptying()
    {
        recyclerArrayState.clear();
    }

    class PosterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener
    {
        ImageView posterImageView;

        public PosterViewHolder(View itemView) {
            super(itemView);
            posterImageView = (ImageView) itemView.findViewById(R.id.posters);

            itemView.setOnLongClickListener(this);
            itemView.setOnClickListener(this);
       }

        @Override
        public void onClick(View v)
        {
            cLickListener.onClick(v, getAdapterPosition());
        }


        @Override
        public boolean onLongClick(View v)
        {

//            int previousSelect = selectedPosition;
//            selectedPosition=getAdapterPosition();
//
////          This notify methods are enabling the bindViewHolderMethods to be called once the values change
//            notifyItemChanged(previousSelect);
//            notifyItemChanged(selectedPosition);
//            Checking whether the adapter position exists in the sparsebooleanArray

            longCLickListener.onLongClick(v,getAdapterPosition());
            if(!recyclerArrayState.get(getAdapterPosition(),false))
            {
//                itemView.setBackgroundColor(Color.RED);
                ImageView posterImageView = (ImageView) itemView.findViewById(R.id.posters);
                posterImageView.setColorFilter(Color.parseColor("#80FAEBD7"));
                recyclerArrayState.put(getAdapterPosition(),true);
                Log.d("ViewHolderLongClick","The viewholder has been longClicked");


            }
            else
            {
                itemView.setBackgroundColor(Color.WHITE);
                ImageView posterImageView = (ImageView) itemView.findViewById(R.id.posters);
                posterImageView.setColorFilter(Color.TRANSPARENT);
                recyclerArrayState.put(getAdapterPosition(),false);

            }

//This method is causing a reload hence something ugly
//            notifyDataSetChanged();

//            Set to true to prevent the onClickLIstener from being fired
            return true;
        }

        public void bind(int position)
        {
            if(!recyclerArrayState.get(position,false))
            {
                itemView.setBackgroundColor(Color.WHITE);
                ImageView posterImageView = (ImageView) itemView.findViewById(R.id.posters);
                posterImageView.setColorFilter(Color.TRANSPARENT);

            }
            else
            {
//                itemView.setBackgroundColor(Color.RED);
                ImageView posterImageView = (ImageView) itemView.findViewById(R.id.posters);
                posterImageView.setColorFilter(Color.parseColor("#80FAEBD7"));

            }
        }



    }

    }
