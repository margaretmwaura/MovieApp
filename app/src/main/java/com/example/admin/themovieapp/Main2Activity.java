package com.example.admin.themovieapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.admin.themovieapp.TheFavourites.returnFavouriteMovies;

public class Main2Activity extends AppCompatActivity implements OnItemClickListener
{

    public static final String BASE_URL = "http://api.themoviedb.org/3/";
    private ImageView mine;
    private CheckBox favourite;
    private TextView title,synopsis,voteAverage,releaseDate;
    private RecyclerView recyclerView,recyclerView1;
    private TrailersAdapter adapter;
    private ReviewsAdapter adapter1;
    private List<Trailer> trailers = new ArrayList<>();
    private List<Reviews> reviews = new ArrayList<>();
    private int id;
    private static Retrofit retrofit = null;
    private Movie movie;
    private Boolean savedState;
    public static final String IMAGE_URL_BASE_PATH = " http://image.tmdb.org/t/p/w154";
    private MovieDatabase mdb;
    private List<Movie> favouriteMovies = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        //This gets us the passed object
        movie = (Movie) getIntent().getParcelableExtra("parcel_data");
        mdb = MovieDatabase.getInstance(getApplicationContext());


         gettingFavouriteMovies();

//        This is for the poster path
       String url = IMAGE_URL_BASE_PATH + movie.getPosterPath();
        String image = url.trim();

        id = movie.getId();
        String titleText = movie.getTitle();
       String sysnopsisTetx = movie.getOverview();
       Double voteAverageDouble = movie.getVoteAverage();
       String releaseDateTetx = movie.getReleaseDate();


        getSupportActionBar().setTitle(titleText);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);




        mine = (ImageView) findViewById(R.id.imageView);
        title = (TextView) findViewById(R.id.textView6);
        synopsis = (TextView) findViewById(R.id.textView2);
        voteAverage = (TextView) findViewById(R.id.textView5);
        releaseDate = (TextView) findViewById(R.id.textView8);
       favourite = (CheckBox) findViewById(R.id.favorite);


       checkingWhetherItHasBeenAddedToFavourites();

        title.setText(titleText);
        synopsis.setText(sysnopsisTetx);
        releaseDate.setText(releaseDateTetx);
        voteAverage.setText(String.valueOf(voteAverageDouble));

        Picasso.get()
                .load(image)
                .fit()
                .into(mine);


//        This is the code for starting the trailer in you tube and reviews
        getTrailers();
        getReviews();

//        Initializing the recyclerView This is for the trailers
        //        This is important so that our recyclerView can be horizontally scrolling
        recyclerView = findViewById(R.id.recylerview2);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new TrailersAdapter(this);
        recyclerView.setAdapter(adapter);
        adapter.setClickListener(this);

//        Recycler View for the trailers
        recyclerView1 = findViewById(R.id.recylerview3);
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView1.setLayoutManager(linearLayoutManager1);
        adapter1 = new ReviewsAdapter(this);
        recyclerView1.setAdapter(adapter1);

//        Maintaining the state of the checkbox
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        savedState = preferences.getBoolean("State",false);
        int movieId = preferences.getInt("MovieID",0);
        if(id == movieId)
        {
            favourite.setChecked(savedState);
        }

    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    private void getTrailers()
    {
        id = movie.getId();

        if(retrofit == null)
        {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

        }

        theInterface toUse = retrofit.create(theInterface.class);
        Call<TrailerResponse> call = toUse.getMoviesTrailer(id,BuildConfig.THE_API_KEY);
//        Already running in the backgeound
        call.enqueue(new Callback<TrailerResponse>() {
            @Override
            public void onResponse(Call<TrailerResponse> call, Response<TrailerResponse> response) {
                trailers = response.body().getResults();
                adapter.setTrailers(trailers);
                if (trailers.isEmpty())
                {
                    Log.d("DetailActivity error", " This was a huge fail");

                }
                else
                {
                    Log.d("DetailActivitySuccess", "This was a huge success");
                }
            }

            @Override
            public void onFailure(Call<TrailerResponse> call, Throwable t)
            {
                Log.d("DetailActivity error", " This was a huge fail" + t.getMessage());
            }
        });

    }

    @Override
    public void onClick(View v,int position)
    {
        Log.d("Trailer","Trailer has been clicked");
      Trailer trailer = trailers.get(position);
      String key = trailer.getKey();
      Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse("http://www.youtube.com/watch?v="+key));
            intent.putExtra("Video id", key);
            startActivity(intent);
        Log.d("This is the video link ", "http://www.youtube.com/watch?v="+key);

    }
    public void getReviews()
    {

        id = movie.getId();

        if(retrofit == null)
        {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

        }

        theInterface toUse = retrofit.create(theInterface.class);
        Call<AllReviews> call = toUse.getMoviesReviews(id,BuildConfig.THE_API_KEY);
//        This is already running in the background no need to wrap it in a loader
        call.enqueue(new Callback<AllReviews>() {
            @Override
            public void onResponse(Call<AllReviews> call, Response<AllReviews> response)
            {
                  reviews = response.body().getReviews();
                  adapter1.setReviews(reviews);
            }

            @Override
            public void onFailure(Call<AllReviews> call, Throwable t) {

            }
        });

    }
    public void addToFavourites()
    {
           mdb.movieDao().insertMovie(movie);
    }
    public void itemClicked(View v)
    {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        final SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("State",favourite.isChecked());
        editor.putInt("MovieID",movie.getId());
        editor.apply();


        //code to check if this checkbox is checked!
        Thread thread = new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                if(favourite.isChecked())
                {
                    addToFavourites();
                    Log.d("Adeed","aAdded to favourites");
                }
            }
        });
        thread.start();


//        This is for the case the checkbox changes
        favourite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                editor.putBoolean("State", isChecked);
                editor.putInt("MovieID",movie.getId());
                editor.commit();
                Log.d("State has changed to ", " ");

                if(!isChecked)
                {
                    removeFromFavourites();
                }
            }
        });

//        Dealing with the state of the checkbox after one returns to the activity
        Boolean check = favourite.isChecked();
        if(savedState!=check)
        {
            if(!check)
            {

                editor.putBoolean("State",check);
                editor.putInt("MovieID",movie.getId());
                editor.commit();
                removeFromFavourites();
            }
        }



    }
    public void removeFromFavourites()
    {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run()
            {
                mdb.movieDao().deleteMovie(movie);
                Log.d("Delete","Item deleted");
            }
        });
        thread.start();

    }

    public void checkingWhetherItHasBeenAddedToFavourites()
    {

        Log.d("favouriteSize","This is the size of the favourite movies " + favouriteMovies.size());
        if(favouriteMovies.size() != 0)
        {
           int i=0;
           while (i<favouriteMovies.size())
           {
               Log.d("FavouriteCount","This is the " + i +" movie");
               Movie compare = favouriteMovies.get(i);
               int compareId = compare.getId();
               if(id == compareId)
               {
                   favourite.setChecked(true);
               }
               i++;
           }
        }
    }
    public void gettingFavouriteMovies()
    {
        Log.d("Checking","The method for checking the favourite list has been called");

        favouriteMovies = returnFavouriteMovies();

    }
}
