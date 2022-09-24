package com.example.kuro.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.example.kuro.APIClient;
import com.example.kuro.APIInterface;
import com.example.kuro.R;
import com.example.kuro.adaptars.AnimeAdaptar;
import com.example.kuro.pojo.Anime;
import com.example.kuro.pojo.Animes;
import com.google.android.material.textfield.TextInputLayout;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchFragment extends Fragment {
    APIInterface apiInterface;
    RecyclerView recyclerView;
    SearchView searchView;
    AnimeAdaptar animeAdaptar;
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
        animeAdaptar = new AnimeAdaptar(animes);
        recyclerView.setAdapter(animeAdaptar);

        searchView=view.findViewById(R.id.searchView);

        apiInterface = APIClient.getClient().create(APIInterface.class);

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
                animeAdaptar.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<Animes> call, Throwable t) {
                call.cancel();
            }
        });
    }
}