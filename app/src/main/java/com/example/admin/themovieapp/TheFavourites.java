package com.example.admin.themovieapp;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.admin.themovieapp.PostersAdapter.emptying;

public class TheFavourites extends Fragment implements OnItemClickListener,OnLongClickListener
{

    //    This list is the result gotten from the search
    private static List<Movie> movies = new ArrayList<>();
    private List<Movie> favouriteMovies = new ArrayList<>();
    //End of the comment
    private GridLayoutManager gridLayoutManager;
    private PostersAdapter postersAdapter;
    private RecyclerView recyclerView;
    private static final String TAG = "Error";
    private static Retrofit retrofit = null;
    public static final String BASE_URL = "http://api.themoviedb.org/3/";
    //    private Bundle mListState;
    private MovieDatabase mDb;
    public static final String ARG_PAGE = "ARG_PAGE";

    private Movie favourite;
    private TextView notification;
    private SparseBooleanArray positions = new SparseBooleanArray();


    public TheFavourites()
    {
        // Required empty public constructor
    }
    public static TheFavourites newInstance(int page)
    {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        TheFavourites fragment = new TheFavourites();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        setHasOptionsMenu(true);
        View rootView = inflater.inflate(R.layout.activity_the_favourites, container, false);



        mDb = MovieDatabase.getInstance(getActivity().getApplicationContext());

//        The reference to the recyclerView
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView_the_favourites);

//        This method has been called first so that the movie list can be populated
        gettingThePopularMovies();

//        Wiring up the recyclerView
        gridLayoutManager = new GridLayoutManager(getActivity().getApplicationContext(),2);
        recyclerView.setLayoutManager(gridLayoutManager);
        postersAdapter = new PostersAdapter(getActivity().getApplicationContext());
        recyclerView.setAdapter(postersAdapter);
        postersAdapter.setClickListener( this);
        postersAdapter.setLongCLickListener(this);

        if(savedInstanceState != null) {
            Log.v("---------------->", "restored!");

            Parcelable savedRecyclerLayoutState = savedInstanceState.getParcelable("BUNDLE_RECYCLER_LAYOUT");
            recyclerView.getLayoutManager().onRestoreInstanceState(savedRecyclerLayoutState);
            movies = savedInstanceState.getParcelableArrayList("INSTANCE_STATE_ENTRIES");
            postersAdapter.setMovieList(movies);
        }

        return rootView;
    }
    //    This needs to be wrapped in a background thread
    public void gettingThePopularMovies()
    {
        //      It works outside of the main thread so need for the background thread
        ViewModel viewModel = ViewModelProviders.of(this).get(ViewModel.class);
        viewModel.getMovies().observe(this, new Observer<List<Movie>>()
        {
            // This method has the priveledge of accessing the ui
            @Override
            public void onChanged(@Nullable List<Movie> moviesNew)
            {
                movies = moviesNew;
                postersAdapter.setMovieList( movies);
                Log.d("The movie size","The size is " + movies.size());
            }
        });

    }

    @Override
    public void onClick(View view, int position)
    {
        Movie mine = movies.get(position);
        String title = mine.getOriginalTitle();
        Log.d("Tile of Clicked Movie " , title);
        Intent i = new Intent(getActivity().getApplicationContext(),Main2Activity.class);
        i.putExtra("parcel_data", mine);
        startActivity(i);

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.v("---------------->", "saved!");

        outState.putParcelable("BUNDLE_RECYCLER_LAYOUT", recyclerView.getLayoutManager().onSaveInstanceState());
        outState.putParcelableArrayList("INSTANCE_STATE_ENTRIES", (ArrayList<? extends Parcelable>) movies);
        super.onSaveInstanceState(outState);

    }
//    Creating the menu icon
   @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
       inflater.inflate(R.menu.favourites, menu);
       super.onCreateOptionsMenu(menu, inflater);

       final List<Movie> movieSearch = new ArrayList<>();
       MenuItem search_item = menu.findItem(R.id.search);

       final SearchView searchView = (SearchView) search_item.getActionView();
       searchView.setQueryHint("Enter a movie title");


       searchView.setQuery("", false);


       searchView.setIconifiedByDefault(true);

       searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener()
       {
           @Override
           public boolean onQueryTextSubmit(String query)
           {
//                Clearing the search array
               String keyword = query.toLowerCase();
               movieSearch.clear();
               for(int i=0 ; i<movies.size() ; i++)
               {
                   Log.d("Search","Search method called ");
                   Movie movie = movies.get(i);
                   String title = movie.getTitle();
                   String search = title.toLowerCase();
                   if(search.contains(keyword))
                   {
                       movieSearch.add(movie);
                       Log.d("Search","Search found one");
                   }

               }
               postersAdapter.setMovieList(movieSearch);
               return false;
           }

           @Override
           public boolean onQueryTextChange(String newText) {
               return false;
           }
       });

       searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
           @Override
           public void onFocusChange(View view, boolean queryTextFocused) {
               if (!queryTextFocused)
               {
                   postersAdapter.setMovieList(movies);


               }
           }
       });


       MenuItem menuItem = menu.findItem(R.id.favorite_movies);
       View actionView = menuItem.getActionView();
       notification = (TextView) actionView.findViewById(R.id.notification_message);

       displaySelectedCount();

       actionView.setOnClickListener(new View.OnClickListener()
       {
           @Override
           public void onClick(View v)
           {
               addToFavourites();
               notification.setText(" ");
               notification.setVisibility(View.INVISIBLE);
           }
       });



   }

    @Override
    public void onPrepareOptionsMenu(Menu menu)
    {
        super.onPrepareOptionsMenu(menu);
        final MenuItem search_item = menu.findItem(R.id.search);



        search_item.collapseActionView();
        favouriteMovies.clear();
        positions.clear();
        emptying();
        postersAdapter.notifyDataSetChanged();
        Log.d("onprepareOptionsMenu ","Method has been called ");

    }
   public static List<Movie> returnFavouriteMovies()
   {
       return movies;
   }

    @Override
    public void onLongClick(View view, int position)
    {
        Log.d("LongClickMethod","The method has beeen called");
        Log.d("LongClickMethodPosition","This is the position of the clicked item " + position);
//Will populate the array at this point that will be added to the favourites section

        favourite = movies.get(position);
        if(!positions.get(position,false))
        {
            favouriteMovies.add(favourite);
            positions.put(position,true);
        }
        else
        {
            favouriteMovies.remove(favourite);
            positions.put(position,false);
//            This is where one will keep track of what is to be removed from the notification bar
        }

        displaySelectedCount();
    }
    public void displaySelectedCount()
    {
        //  this is where one will keep track of the count to be shown in the notification bar by adding

        if (notification != null)
        {


            if (favouriteMovies.size() == 0)
            {
                notification.setVisibility(View.GONE);

            } else
            {
                int count = favouriteMovies.size();
                notification.setVisibility(View.VISIBLE);
                notification.setText(String.valueOf(count));
            }
        }
    }

    public void addToFavourites()
    {
        if(favouriteMovies.size() == 0)
        {
            Toast.makeText(getActivity().getApplicationContext(),"No movies have been selected to add to favourites",Toast.LENGTH_LONG).show();
        }
        else
        {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run()
                {
                    mDb.movieDao().deleteMovies(favouriteMovies);
                    favouriteMovies.clear();
                }
            });
            thread.start();
            Toast.makeText(getActivity().getApplicationContext(), "Movies have been removed from favourites ", Toast.LENGTH_LONG).show();
        }

        emptying();
        positions.clear();
        postersAdapter.notifyDataSetChanged();

    }

}
