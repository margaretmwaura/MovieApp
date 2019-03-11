package com.example.admin.themovieapp;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
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
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.Context.INPUT_METHOD_SERVICE;
import static com.example.admin.themovieapp.PostersAdapter.emptying;


public class TheMostPopular extends Fragment implements OnItemClickListener,OnLongClickListener {
    //    This list is the result gotten from the search
    private List<Movie> movies = new ArrayList<>();
    private List<Movie> favouriteMovies = new ArrayList<>();
    //End of the comment
    private Menu menu;
    private GridLayoutManager gridLayoutManager;
    private PostersAdapter postersAdapter;
    private RecyclerView recyclerView;
    private Movie favourite;

    private TextView notification, errorMessage;
    private Circle circle;
    private int duration = 2000;
    private Button button;

    private MediaPlayer ring;
    private static final String TAG = "Error";
    private static Retrofit retrofit = null;
    public static final String BASE_URL = "http://api.themoviedb.org/3/";
    //    private Bundle mListState;
    private MovieDatabase mDb;
    public static final String ARG_PAGE = "ARG_PAGE";
    private SparseBooleanArray positions = new SparseBooleanArray();
    private CircleAngleAnimation animationOne;
    private ValueAnimator anim;
    private Animation myanim;
    private View rootView;
    private int flagError = -1;
    private int flagAfterError,flagCount;
    public static NetworkChangeListener networkChangeListener;
    private IntentFilter intentFilter;
    private int flagCurrent = -1;
    private int flagPrevious = -1;
    private static int current = 1 ;

    public static Activity activity;
    public TheMostPopular() {
        // Required empty public constructor
    }

    public static TheMostPopular newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        TheMostPopular fragment = new TheMostPopular();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        Log.d("Method has been called", "Creating the view The Most popular ");
        View rootView = inflater.inflate(R.layout.activity_the_most_popular, container, false);
         this.rootView = rootView;
//         bundle = savedInstanceState;
//        getActivity().getWindow().setSoftInputMode(
//                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        mDb = MovieDatabase.getInstance(getActivity().getApplicationContext());

        activity = getActivity();
        networkChangeListener = new NetworkChangeListener();
        intentFilter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
        getActivity().registerReceiver(networkChangeListener,intentFilter);
        gettingThePopularMovies();




        if (savedInstanceState != null) {
            Log.v("---------------->", "restored The most popular !");
//            int scrollPosition = savedInstanceState.getInt("BUNDLE_RECYCLER_LAYOUT");
//            String log = String.valueOf(scrollPosition);
//            recyclerView.scrollToPosition(scrollPosition);
//            Log.d("The gotten position " , log);
            Parcelable savedRecyclerLayoutState = savedInstanceState.getParcelable("BUNDLE_RECYCLER_LAYOUT");
            recyclerView.getLayoutManager().onRestoreInstanceState(savedRecyclerLayoutState);
            movies = savedInstanceState.getParcelableArrayList("INSTANCE_STATE_ENTRIES");
            flagAfterError = savedInstanceState.getInt("FLAG_ERROR");
            postersAdapter.setMovieList(movies);
        }






        return rootView;
    }

    //    This needs to be wrapped in a background thread
    public int gettingThePopularMovies()
    {
//        Setting up the UI elements
        settingUpTheUI();
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

        }

        theInterface toUse = retrofit.create(theInterface.class);
        Call<AllMovie> call = toUse.getMostPopularMovies(BuildConfig.THE_API_KEY);
//        Running in the background
        call.enqueue(new Callback<AllMovie>() {
            @Override
            public void onResponse(Call<AllMovie> call, Response<AllMovie> response)
            {
                flagError = 0;

                flagPrevious = flagCurrent ;
                flagCurrent = flagError ;
                if(flagPrevious == 1)
                {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            getActivity().unregisterReceiver(networkChangeListener);

                            Toast.makeText(getActivity().getApplicationContext(),"The receiver has been unregistered " , Toast.LENGTH_LONG).show();
                        }
                    });
                }
                savingFlag();
                seetingFlagErrorValue(flagError);
                recyclerView.setVisibility(View.VISIBLE);
                movies = response.body().getResults();
                postersAdapter.setMovieList(movies);
                Log.d("Sucess", "This was a success");
                button.setVisibility(View.INVISIBLE);
                errorMessage.setVisibility(View.INVISIBLE);
                circle.setVisibility(View.INVISIBLE);


            }

            @Override
            public void onFailure(Call<AllMovie> call, Throwable t)
            {
                flagError = 1;
                flagPrevious = flagCurrent;
                flagCurrent = flagError;
                seetingFlagErrorValue(flagError);
                savingFlag();
                settingUpAnimation();

            }
        });

      return flagError;
    }

    @Override
    public void onClick(View view, int position) {
        Movie mine = movies.get(position);
        String title = mine.getOriginalTitle();
        Log.d("Tile of Clicked Movie ", title);
        Intent i = new Intent(getActivity().getApplicationContext(), Main2Activity.class);
        i.putExtra("parcel_data", mine);
        startActivity(i);

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.v("---------------->", "saved The Most Popular!");
//        int position = gridLayoutManager.findFirstVisibleItemPosition();
//        outState.putInt("BUNDLE_RECYCLER_LAYOUT", gridLayoutManager.findFirstVisibleItemPosition());
//        String log = String.valueOf(position);
//        Log.d("The position is " ,log);
        outState.putParcelable("BUNDLE_RECYCLER_LAYOUT", recyclerView.getLayoutManager().onSaveInstanceState());
        outState.putParcelableArrayList("INSTANCE_STATE_ENTRIES", (ArrayList<? extends Parcelable>) movies);
        if(flagError != -1)
        {
            outState.putInt("FLAG_ERROR", flagError);
        }
        super.onSaveInstanceState(outState);

    }


    //    Creating the menu
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        this.menu = menu;
        inflater.inflate(R.menu.search, menu);
        super.onCreateOptionsMenu(menu, inflater);
        Log.d("onCreateOptionsMenu", "Method called The Most Popular ");


        final List<Movie> movieSearch = new ArrayList<>();
        MenuItem search_item = menu.findItem(R.id.action_search);

        final SearchView searchView = (SearchView) search_item.getActionView();
        searchView.setQueryHint("Enter a movie title");


        searchView.setQuery("", false);
//        searchView.setIconified(true);
//        searchView.setIconified(true);

        searchView.setIconifiedByDefault(true);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
//                Clearing the search array
                String keyword = query.toLowerCase();
                movieSearch.clear();
                for (int i = 0; i < movies.size(); i++) {
                    Log.d("Search", "Search method called ");
                    Movie movie = movies.get(i);
                    String title = movie.getTitle();
                    String search = title.toLowerCase();
                    if (search.contains(keyword)) {
                        movieSearch.add(movie);
                        Log.d("Search", "Search found one");
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
                if (!queryTextFocused) {
                    postersAdapter.setMovieList(movies);


                }
            }
        });


        MenuItem menuItem = menu.findItem(R.id.favorite_movies);
        View actionView = menuItem.getActionView();
        notification = (TextView) actionView.findViewById(R.id.notification_message);

        displaySelectedCount();

        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToFavourites();
                notification.setText(" ");
                notification.setVisibility(View.INVISIBLE);
            }
        });

    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        final MenuItem search_item = menu.findItem(R.id.action_search);

        final SearchView searchView = (SearchView) search_item.getActionView();

//        searchView.setIconified(true);
//        searchView.setIconified(true);

        search_item.collapseActionView();
        favouriteMovies.clear();
        positions.clear();
        emptying();
        postersAdapter.notifyDataSetChanged();
        Log.d("onprepareOptionsMenu ", "Method has been called The Most Popular ");

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.refresh) {
            gettingThePopularMovies();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onLongClick(View view, int position) {
        Log.d("LongClickMethod", "The method has beeen called");
        Log.d("LongClickMethodPosition", "This is the position of the clicked item " + position);
//Will populate the array at this point that will be added to the favourites section

        favourite = movies.get(position);
        if (!positions.get(position, false)) {
            favouriteMovies.add(favourite);
            positions.put(position, true);
        } else {
            favouriteMovies.remove(favourite);
            positions.put(position, false);
//            This is where one will keep track of what is to be removed from the notification bar
        }

        displaySelectedCount();
    }

    public void displaySelectedCount() {
        //  this is where one will keep track of the count to be shown in the notification bar by adding

        if (notification != null) {


            if (favouriteMovies.size() == 0) {
                notification.setVisibility(View.GONE);

            } else {
                int count = favouriteMovies.size();
                notification.setVisibility(View.VISIBLE);
                notification.setText(String.valueOf(count));
            }
        }
    }

    public void addToFavourites() {
        if (favouriteMovies.size() == 0) {
            Toast.makeText(getActivity().getApplicationContext(), "No movies have been selected to add to favourites", Toast.LENGTH_LONG).show();
        } else {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    mDb.movieDao().insertMovies(favouriteMovies);

                    favouriteMovies.clear();

                }
            });
            thread.start();
            Toast.makeText(getActivity().getApplicationContext(), "Movies have been added to favourites ", Toast.LENGTH_LONG).show();
        }


        emptying();
        positions.clear();
        postersAdapter.notifyDataSetChanged();

    }

    public void clearingAnimations()
    {
        Log.d("PageUnselected", "The page the Most Popular has been unselected so the animation will stop");
//        Toast.makeText(getActivity().getApplicationContext(),"The page has been unselected",Toast.LENGTH_LONG).show();

        if(ring!=null)
        {
            ring.release();
            ring=null;
        }
        else
        {
            ring=null;
            Log.d("Media Player ", " Media Player is equal to null");
        }

        current = 0;
//
//        if(activity != null)
//        {
//            activity.unregisterReceiver(networkChangeListener);
//            Log.d("UnregisteringReceiver","Broadcast receiver unregistered ");
//        }

    }

    public void settingUpTheUI()
    {
        circle = (Circle) rootView.findViewById(R.id.error);
        button = (Button) rootView.findViewById(R.id.retry);
        button.setVisibility(View.INVISIBLE);
        errorMessage = rootView.findViewById(R.id.errorMessage);
        errorMessage.setVisibility(View.INVISIBLE);



//        The reference to the recyclerView
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView_most_popular);

//        This method has been called first so that the movie list can be populated



//        Wiring up the recyclerView
        gridLayoutManager = new GridLayoutManager(getActivity().getApplicationContext(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        postersAdapter = new PostersAdapter(getActivity().getApplicationContext());
        recyclerView.setAdapter(postersAdapter);
        postersAdapter.setClickListener(this);
        postersAdapter.setLongCLickListener(this);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gettingThePopularMovies();
                flagCount = 1;
            }
        });
    }
    public void checkIfError()
    {
        current = 1;
        SharedPreferences preferences = getActivity().getApplicationContext().getSharedPreferences("FLAG_ERROR",Context.MODE_PRIVATE);
        flagAfterError = preferences.getInt("ERROR",0);

//        Toast.makeText(getActivity().getApplicationContext(),"Retrieving flag value " + flagAfterError,Toast.LENGTH_LONG).show();
//         Log.d("CheckingError","This is the value of the flag " + flagAfterError);
//         Toast.makeText(fragmentContext,"This is the saved value " + flagAfterError,Toast.LENGTH_LONG).show();

//            Write the code to check for the internet connection and until that internet connection is
//            positive call the m

        if(flagAfterError == 1)
        {

            networkChangeListener = new NetworkChangeListener();
            intentFilter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
            getActivity().registerReceiver(networkChangeListener,intentFilter);
        }
    }

    public void savingFlag()
    {

        SharedPreferences.Editor editor = getActivity().getApplicationContext().getSharedPreferences("FLAG_ERROR", Context.MODE_PRIVATE).edit();
        editor.putInt("ERROR",flagError);
        editor.commit();
        Toast.makeText(getActivity().getApplicationContext(),"Saving flag value  " + flagError,Toast.LENGTH_LONG).show();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser)
    {
        super.setUserVisibleHint(isVisibleToUser);

        activity = getActivity();

        if(isVisibleToUser && isResumed())
        {
//            checkIfError();
//            Toast.makeText(getActivity().getApplicationContext(),"The visibility method has been called ",Toast.LENGTH_LONG).show();
            checkIfError();
        }
    }
    public void settingUpAnimation()
    {

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run()
            {
                Log.d(TAG, " This was a huge fail The Most Popular Java Class ");
                recyclerView.setVisibility(View.INVISIBLE);
                circle.setVisibility(View.VISIBLE);
                animationOne = new CircleAngleAnimation(circle, 320);
                animationOne.setDuration(duration);
                Animation.AnimationListener animationListener = new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {

//                        duration = 1;


                        anim = ValueAnimator.ofFloat(30.0f, 10.0f);
                        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(ValueAnimator animation) {
                                circle.getPaint().setStrokeWidth((Float) anim.getAnimatedValue());
//                                Log.d("Animation","Method has been called  " + (Float)anim.getAnimatedValue() );
                                circle.invalidate();
                                circle.requestLayout();
                            }
                        });

                        anim.addListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);

                                myanim = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.rotate);
                                myanim.setAnimationListener(new Animation.AnimationListener() {
                                    @Override
                                    public void onAnimationStart(Animation animation)
                                    {

                                        if(flagCount == 0)
                                        {
                                            button.setVisibility(View.VISIBLE);
                                            errorMessage.setVisibility(View.VISIBLE);
                                            Log.d("Myanim animation ", "Method has been called The Most Popular");

//                                Code for adding the error sound
                                            ring = MediaPlayer.create(getActivity().getApplicationContext(), R.raw.ring);
                                            ring.start();
                                            Log.d("Media Player animation ", "Method has been called The Most Popular");
                                        }
                                    }

                                    @Override
                                    public void onAnimationEnd(Animation animation)
                                    {
                                        if(flagCount == 1 && current == 1 )
                                        {
                                            duration = 1;
                                            circle.startAnimation(animationOne);
                                            Log.d("AnimationVisiMostPopul","The animation is visible ");
                                        }

                                    }

                                    @Override
                                    public void onAnimationRepeat(Animation animation) {

                                    }
                                });

                                circle.startAnimation(myanim);

                            }
                        });
                        anim.setDuration(1000);
                        anim.start();
                    }



                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                };
                animationOne.setAnimationListener(animationListener);
                circle.startAnimation(animationOne);



                Toast.makeText(getActivity().getApplicationContext(),"The value of the flag is " + flagError , Toast.LENGTH_LONG).show();
            }
        });

            }

    private int seetingFlagErrorValue(int flag)
    {
        this.flagError = flag;
        return flagError;
    }

    private boolean haveNetworkConnection()
    {
        boolean haveConnection = false;

        ConnectivityManager cm = (ConnectivityManager)getActivity().getSystemService(getActivity().getApplicationContext().CONNECTIVITY_SERVICE);
        NetworkInfo[] networkInfo = cm.getAllNetworkInfo();

        for(NetworkInfo ni : networkInfo)
        {
            if(ni.getTypeName().equalsIgnoreCase("WIFI"))
            {
                if(ni.isConnected())
                {
                    haveConnection = true;
                }
            }
            if(ni.getTypeName().equalsIgnoreCase("MOBILE"))
            {
                if(ni.isConnected())
                {
                    haveConnection = true;
                }
            }
        }
        return haveConnection;
    }

    private class NetworkChangeListener extends BroadcastReceiver
    {

        @Override
        public void onReceive(Context context, Intent intent)
        {
            Toast.makeText(getActivity().getApplicationContext(),"The onReceive method has been called",Toast.LENGTH_LONG).show();
            boolean status = haveNetworkConnection();
            if ("android.net.conn.CONNECTIVITY_CHANGE".equals(intent.getAction())) {
                if (status == true)
                {
                    gettingThePopularMovies();
                }
                if (status == false)
                {
//                        Toast.makeText(getActivity().getApplicationContext(), "There is not network connectivity yet \n currently setting up the animation ", Toast.LENGTH_LONG).show();
                    settingUpAnimation();
                }

            }
        }
    }



}

