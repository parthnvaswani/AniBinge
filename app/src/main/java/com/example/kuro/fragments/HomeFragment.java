package com.example.kuro.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.kuro.APIClient;
import com.example.kuro.APIInterface;
import com.example.kuro.AnimePage;
import com.example.kuro.R;
import com.example.kuro.adaptars.AnimeAdaptar;
import com.example.kuro.pojo.Anime;
import com.example.kuro.pojo.AnimeInfo;
import com.example.kuro.pojo.Animes;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {
    private TextView textView,textView2;
    private ImageView imageView;
    private APIInterface apiInterface;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView1;
    private RecyclerView recyclerView2;
    private AnimeAdaptar animeAdaptar1;
    private AnimeAdaptar animeAdaptar2;
    private List<Anime> popularAnimes;
    private List<Anime> trendingAnimes;
    private ProgressBar progressBar;
    private ProgressBar progressBar2;
    private String randomAnimeId="";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        textView=getView().findViewById(R.id.animeId);
        textView2=getView().findViewById(R.id.textView2);
        imageView=getView().findViewById(R.id.imageView);
        progressBar=getView().findViewById(R.id.progressBar);
        progressBar2=getView().findViewById(R.id.progressBar2);

        popularAnimes = new ArrayList<>();
        trendingAnimes=new ArrayList<>();

        recyclerView1 = view.findViewById(R.id.popularAnimes);
        recyclerView1.setLayoutManager(new LinearLayoutManager(view.getContext(),LinearLayoutManager.HORIZONTAL,false));
        animeAdaptar1 = new AnimeAdaptar(popularAnimes);
        recyclerView1.setAdapter(animeAdaptar1);

        recyclerView2 = view.findViewById(R.id.trendingAnimes);
        recyclerView2.setLayoutManager(new LinearLayoutManager(view.getContext(),LinearLayoutManager.HORIZONTAL,false));
        animeAdaptar2 = new AnimeAdaptar(trendingAnimes);
        recyclerView2.setAdapter(animeAdaptar2);

        apiInterface = APIClient.getClient().create(APIInterface.class);

        swipeRefreshLayout = getView().findViewById(R.id.pullToRefresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getRandomAnime();
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(randomAnimeId!=null&&randomAnimeId!="") {
                    Intent intent = new Intent(view.getContext(), AnimePage.class);
                    intent.putExtra("id", randomAnimeId);
                    startActivity(intent);
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
                textView.setText(anime.title.romaji);
                textView2.setText(anime.desc);
                Picasso.get().load(anime.image)
                        .placeholder(R.drawable.ic_baseline_broken_image_24)
                        .error(R.drawable.ic_baseline_broken_image_24)
                        .into(imageView);
                randomAnimeId=anime.id;
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<AnimeInfo> call, Throwable t) {
                call.cancel();
            }
        });
    }

    public void getPopularAnime(){
        Call<Animes> call = apiInterface.popularAnime(1);
        call.enqueue(new Callback<Animes>() {
            @Override
            public void onResponse(Call<Animes> call, Response<Animes> response) {
                Animes resource = response.body();
                popularAnimes.clear();
                popularAnimes.addAll(resource.results);
                animeAdaptar1.notifyDataSetChanged();
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
        Call<Animes> call = apiInterface.trendingAnime(1);
        call.enqueue(new Callback<Animes>() {
            @Override
            public void onResponse(Call<Animes> call, Response<Animes> response) {
                Animes resource = response.body();
                trendingAnimes.clear();
                trendingAnimes.addAll(resource.results);
                animeAdaptar2.notifyDataSetChanged();
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