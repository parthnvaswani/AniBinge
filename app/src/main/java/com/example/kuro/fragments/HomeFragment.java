package com.example.kuro.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.kuro.APIClient;
import com.example.kuro.APIInterface;
import com.example.kuro.AnimePage;
import com.example.kuro.DatabaseHelper;
import com.example.kuro.R;
import com.example.kuro.adaptars.AnimeAdapter;
import com.example.kuro.adaptars.ContinueWatchingAdapter;
import com.example.kuro.dao.ContinueWatchingDao;
import com.example.kuro.entities.ContinueWatching;
import com.example.kuro.pojo.Anime;
import com.example.kuro.pojo.AnimeInfo;
import com.example.kuro.pojo.Animes;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {
    private TextView textView,textView2,textView3,textView4;
    private ImageView imageView;
    private APIInterface apiInterface;
    private RecyclerView recyclerView1,recyclerView2,continueWatching;
    private AnimeAdapter AnimeAdapter1,AnimeAdapter2;
    private ContinueWatchingAdapter continueWatchingAdapter;
    private List<Anime> popularAnimes, trendingAnimes;
    private List<ContinueWatching> continueWatchingList;
    private ProgressBar progressBar;
    private ProgressBar progressBar2;
    private CardView synopsis;
    private static CardView continueCards;
    private String randomAnimeId="";
    private ContinueWatchingDao continueWatchingDao;

    int page1=1,page2=1;
    boolean hasNextPage1=true,hasNextPage2=true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        textView=view.findViewById(R.id.animeId);
        textView2=view.findViewById(R.id.textView2);
        textView3=view.findViewById(R.id.textView);
        textView4=view.findViewById(R.id.textView5);
        imageView=view.findViewById(R.id.imageView);
        progressBar=view.findViewById(R.id.progressBar);
        progressBar2=view.findViewById(R.id.progressBar2);
        synopsis=view.findViewById(R.id.synopsis);
        continueCards=view.findViewById(R.id.continueCards);

        popularAnimes = new ArrayList<>();
        trendingAnimes =new ArrayList<>();
        continueWatchingList = new ArrayList<>();

        continueWatching = view.findViewById(R.id.continueWatching);
        continueWatching.setLayoutManager(new LinearLayoutManager(view.getContext(),LinearLayoutManager.HORIZONTAL,false));
        continueWatchingAdapter = new ContinueWatchingAdapter(continueWatchingList,view.getContext());
        ScaleInAnimationAdapter scaleInAnimationAdapter = new ScaleInAnimationAdapter(continueWatchingAdapter);
        scaleInAnimationAdapter.setDuration(400);
        scaleInAnimationAdapter.setInterpolator(new OvershootInterpolator(1f));
        scaleInAnimationAdapter.setFirstOnly(false);
        continueWatching.setAdapter(scaleInAnimationAdapter);
        continueWatchingAdapter.getItemCount();

        continueWatchingDao = DatabaseHelper.getDB(view.getContext()).continueWatchingDao();

        recyclerView1 = view.findViewById(R.id.popularAnimes);
        recyclerView1.setLayoutManager(new LinearLayoutManager(view.getContext(),LinearLayoutManager.HORIZONTAL,false));
        AnimeAdapter1 = new AnimeAdapter(popularAnimes);
        ScaleInAnimationAdapter scaleInAnimationAdapter1 = new ScaleInAnimationAdapter(AnimeAdapter1);
        scaleInAnimationAdapter1.setDuration(400);
        scaleInAnimationAdapter1.setInterpolator(new OvershootInterpolator(1f));
        scaleInAnimationAdapter1.setFirstOnly(false);
        recyclerView1.setAdapter(scaleInAnimationAdapter1);

        recyclerView2 = view.findViewById(R.id.trendingAnimes);
        recyclerView2.setLayoutManager(new LinearLayoutManager(view.getContext(),LinearLayoutManager.HORIZONTAL,false));
        AnimeAdapter2 = new AnimeAdapter(trendingAnimes);
        ScaleInAnimationAdapter scaleInAnimationAdapter2=new ScaleInAnimationAdapter(AnimeAdapter2);
        scaleInAnimationAdapter2.setDuration(400);
        scaleInAnimationAdapter2.setInterpolator(new OvershootInterpolator(1f));
        scaleInAnimationAdapter2.setFirstOnly(false);
        recyclerView2.setAdapter(scaleInAnimationAdapter2);

        apiInterface = APIClient.getClient(view.getContext()).create(APIInterface.class);

        imageView.setOnClickListener(view1 -> {
            if(randomAnimeId!=null&&!randomAnimeId.equals("")) {
                Intent intent = new Intent(view1.getContext(), AnimePage.class);
                intent.putExtra("id", randomAnimeId);
                startActivity(intent);
            }
        });

        textView2.setOnClickListener(view12 -> {
            if(randomAnimeId!=null&&!randomAnimeId.equals("")) {
                synopsis.setVisibility(View.VISIBLE);
            }
        });

        synopsis.setOnClickListener(view13 -> synopsis.setVisibility(View.GONE));

        recyclerView1.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(!recyclerView1.canScrollHorizontally(1)){
                    if(hasNextPage1) {
                        page1++;
                        getPopularAnime();
                    }
                }
            }
        });

        recyclerView2.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(!recyclerView2.canScrollHorizontally(1)){
                    if(hasNextPage2) {
                        page2++;
                        getTrendingAnime();
                    }
                }
            }
        });

        getRandomAnime();
        getPopularAnime();
        getTrendingAnime();
        getContinueWatching(view.getContext());
    }

    public void getRandomAnime(){
        Call<AnimeInfo> call = apiInterface.randomAnime();
        call.enqueue(new Callback<AnimeInfo>() {
            @Override
            public void onResponse(Call<AnimeInfo> call, Response<AnimeInfo> response) {
                AnimeInfo anime = response.body();
                String title="No Title";
                if(anime!=null) {
                    if (anime.title.romaji != null) title = anime.title.romaji;
                    else if (anime.title.english != null) title = anime.title.english;
                    else if (anime.title.nati != null) title = anime.title.nati;

                    textView.setText(title);
                    textView3.setText(title);
                    if(anime.desc!=null) {
                        textView2.setText(Html.fromHtml(anime.desc, Html.FROM_HTML_MODE_COMPACT));
                        textView4.setText(Html.fromHtml(anime.desc, Html.FROM_HTML_MODE_COMPACT));
                    }

                    Picasso.get().load(anime.image)
                            .placeholder(R.drawable.ic_baseline_broken_image_24)
                            .error(R.drawable.ic_baseline_broken_image_24)
                            .into(imageView);
                    randomAnimeId = anime.id;
                }
                else {
                    getRandomAnime();
                }
            }

            @Override
            public void onFailure(Call<AnimeInfo> call, Throwable t) {
                call.cancel();
            }
        });
    }

    public void getPopularAnime(){
        Call<Animes> call = apiInterface.popularAnime(page1);
        call.enqueue(new Callback<Animes>() {
            @Override
            public void onResponse(Call<Animes> call, Response<Animes> response) {
                Animes resource = response.body();
                hasNextPage1=resource.hasNextPage;
                int size=popularAnimes.size();
                popularAnimes.addAll(resource.results);
                AnimeAdapter1.notifyItemRangeInserted(size,popularAnimes.size()-size);
                progressBar.setVisibility(View.GONE);
                recyclerView1.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(Call<Animes> call, Throwable t) {
                call.cancel();
            }
        });
    }

    public void getTrendingAnime(){
        Call<Animes> call = apiInterface.trendingAnime(page2);
        call.enqueue(new Callback<Animes>() {
            @Override
            public void onResponse(Call<Animes> call, Response<Animes> response) {
                Animes resource = response.body();
                hasNextPage2=resource.hasNextPage;
                int size=trendingAnimes.size();
                trendingAnimes.addAll(resource.results);
                AnimeAdapter2.notifyItemRangeInserted(size,trendingAnimes.size()-size);
                progressBar2.setVisibility(View.GONE);
                recyclerView2.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(Call<Animes> call, Throwable t) {
                call.cancel();
            }
        });
    }

    public void getContinueWatching(Context ctx){
        continueWatchingDao.getAll().observe((LifecycleOwner) ctx, continueWatchings -> {
            continueWatchingList.clear();
            continueWatchingList.addAll(continueWatchings);
            continueWatchingAdapter.notifyDataSetChanged();
            if(continueWatchingList.size()!=0)
                continueCards.setVisibility(View.VISIBLE);
            else
                continueCards.setVisibility(View.GONE);
        });
    }
}