package com.example.kuro;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kuro.adaptars.AnimeAdapter;
import com.example.kuro.pojo.AnimeInfo;
import com.squareup.picasso.Picasso;

import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;

public class AnimePageEnd extends Fragment {
    private AnimeInfo animeInfo;
    private RecyclerView recyclerView;
    private AnimeAdapter animeAdapter;

    public AnimePageEnd() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_anime_page_end, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        animeInfo = ((GlobalState)getActivity().getApplicationContext()).getAnimeInfo();

        recyclerView=view.findViewById(R.id.aniRecommend);
        recyclerView.setLayoutManager(new GridLayoutManager(view.getContext(), 2));
        animeAdapter = new AnimeAdapter(animeInfo.recommendations);
        ScaleInAnimationAdapter scaleInAnimationAdapter=new ScaleInAnimationAdapter(animeAdapter);
        scaleInAnimationAdapter.setDuration(400);
        scaleInAnimationAdapter.setInterpolator(new OvershootInterpolator(1f));
        scaleInAnimationAdapter.setFirstOnly(false);
        recyclerView.setAdapter(scaleInAnimationAdapter);
    }

}