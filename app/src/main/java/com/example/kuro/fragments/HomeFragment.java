package com.example.kuro.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kuro.APIClient;
import com.example.kuro.APIInterface;
import com.example.kuro.R;
import com.example.kuro.pojo.Anime;
import com.example.kuro.pojo.Animes;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {
    private TextView textView;
    private ImageView imageView;
    private APIInterface apiInterface;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        textView=getView().findViewById(R.id.textView);
        imageView=getView().findViewById(R.id.imageView);

        apiInterface = APIClient.getClient().create(APIInterface.class);


        Call<Animes> call = apiInterface.searchAnime("attack",1);
        call.enqueue(new Callback<Animes>() {
            @Override
            public void onResponse(Call<Animes> call, Response<Animes> response) {
                Animes resource = response.body();
                List<Anime> animes = resource.results;
                Anime anime=animes.get(0);
                textView.setText(anime.id+" "+anime.title.romaji);

                Picasso.get().load(anime.image)
                        .placeholder(R.drawable.ic_baseline_broken_image_24)
                        .error(R.drawable.ic_baseline_broken_image_24)
                        .into(imageView);
            }

            @Override
            public void onFailure(Call<Animes> call, Throwable t) {
                call.cancel();
            }
        });
    }
}