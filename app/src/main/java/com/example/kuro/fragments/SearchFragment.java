package com.example.kuro.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.SearchView;

import com.example.kuro.APIClient;
import com.example.kuro.APIInterface;
import com.example.kuro.R;
import com.example.kuro.adaptars.AnimeAdapter;
import com.example.kuro.pojo.Anime;
import com.example.kuro.pojo.Animes;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchFragment extends Fragment {
    APIInterface apiInterface;
    RecyclerView recyclerView;
    SearchView searchView;
    AnimeAdapter animeAdapter;
    List<Anime> animes;
    String query="";
    int page=1;
    boolean hasNextPage=true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        animes = new ArrayList<>();

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(view.getContext(), 2));
        animeAdapter = new AnimeAdapter(animes);
        ScaleInAnimationAdapter scaleInAnimationAdapter=new ScaleInAnimationAdapter(animeAdapter);
        scaleInAnimationAdapter.setDuration(400);
        scaleInAnimationAdapter.setInterpolator(new OvershootInterpolator(1f));
        scaleInAnimationAdapter.setFirstOnly(false);
        recyclerView.setAdapter(scaleInAnimationAdapter);

        searchView=view.findViewById(R.id.searchView);

        apiInterface = APIClient.getClient(view.getContext()).create(APIInterface.class);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                query=s;
                page=1;
                searchAnime(s,true);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(!recyclerView.canScrollVertically(1)){
                    if(hasNextPage) {
                        page++;
                        searchAnime(query,false);
                    }
                }
            }
        });
    }

    public void searchAnime(String s,boolean clearAll){
        Call<Animes> call = apiInterface.searchAnime(s,page);
        call.enqueue(new Callback<Animes>() {
            @Override
            public void onResponse(Call<Animes> call, Response<Animes> response) {
                Animes resource = response.body();
                hasNextPage=resource.hasNextPage;
                if(clearAll)animes.clear();
                animes.addAll(resource.results);
                animeAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<Animes> call, Throwable t) {
                call.cancel();
            }
        });
    }
}