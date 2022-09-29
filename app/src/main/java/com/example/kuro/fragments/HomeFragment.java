package com.example.kuro.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Html;
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
import com.example.kuro.R;
import com.example.kuro.adaptars.AnimeAdapter;
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
    private RecyclerView recyclerView1;
    private RecyclerView recyclerView2;
    private AnimeAdapter AnimeAdapter1;
    private AnimeAdapter AnimeAdapter2;
    private List<Anime> popularAnimes;
    private List<Anime> trendingAnimes;
    private ProgressBar progressBar;
    private ProgressBar progressBar2;
    private CardView synopsis;
    private String randomAnimeId="";

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

        popularAnimes = new ArrayList<>();
        trendingAnimes=new ArrayList<>();

        recyclerView1 = view.findViewById(R.id.popularAnimes);
        recyclerView1.setLayoutManager(new LinearLayoutManager(view.getContext(),LinearLayoutManager.HORIZONTAL,false));
        AnimeAdapter1 = new AnimeAdapter(popularAnimes);
        ScaleInAnimationAdapter scaleInAnimationAdapter=new ScaleInAnimationAdapter(AnimeAdapter1);
        scaleInAnimationAdapter.setDuration(400);
        scaleInAnimationAdapter.setInterpolator(new OvershootInterpolator(1f));
        scaleInAnimationAdapter.setFirstOnly(false);
        recyclerView1.setAdapter(scaleInAnimationAdapter);

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
                popularAnimes.addAll(resource.results);
                AnimeAdapter1.notifyDataSetChanged();
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
                trendingAnimes.addAll(resource.results);
                AnimeAdapter2.notifyDataSetChanged();
                progressBar2.setVisibility(View.GONE);
                recyclerView2.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(Call<Animes> call, Throwable t) {
                call.cancel();
            }
        });
    }
}