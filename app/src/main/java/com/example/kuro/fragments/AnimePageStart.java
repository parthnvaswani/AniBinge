package com.example.kuro.fragments;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kuro.GlobalState;
import com.example.kuro.R;
import com.example.kuro.pojo.AnimeInfo;
import com.squareup.picasso.Picasso;

public class AnimePageStart extends Fragment {
    private AnimeInfo animeInfo;
    private TextView country, popularity, season, type, genres, studios;
    private ImageView thumbnail;
    private ConstraintLayout constraintLayout;

    public AnimePageStart() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_anime_page_start, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        animeInfo = ((GlobalState) view.getContext().getApplicationContext()).getAnimeInfo();

        country = view.findViewById(R.id.aniCountry);
        popularity = view.findViewById(R.id.aniPop);
        season = view.findViewById(R.id.aniSeason);
        type = view.findViewById(R.id.aniType);
        genres = view.findViewById(R.id.aniGenres);
        studios = view.findViewById(R.id.aniStudios);
        thumbnail = view.findViewById(R.id.thumbnail);
        constraintLayout = view.findViewById(R.id.thumbCon);

        country.setText("Country: " + getString(animeInfo.country, "null"));
        popularity.setText("Popularity: " + getString(animeInfo.popularity + "", "null"));
        season.setText("Season: " + getString(animeInfo.season, "null"));
        type.setText("Type: " + getString(animeInfo.type, "null"));

        genres.setText(String.join(", ", animeInfo.genres));
        studios.setText(String.join(", ", animeInfo.studios));

        if (animeInfo.trailer != null)
            Picasso.get().load(animeInfo.trailer.thumbnail).fit().placeholder(R.drawable.ic_baseline_broken_image_24).error(R.drawable.ic_baseline_broken_image_24).into(thumbnail);
        else constraintLayout.setVisibility(View.GONE);

        thumbnail.setOnClickListener(view1 -> watchYoutubeVideo(view.getContext(), animeInfo.trailer.id));
    }

    public String getString(String s, String d) {
        return s != null ? s : d;
    }

    public static void watchYoutubeVideo(Context context, String id) {
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
        Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + id));
        try {
            context.startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            context.startActivity(webIntent);
        }
    }
}