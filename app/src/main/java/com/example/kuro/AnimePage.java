package com.example.kuro;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kuro.adaptars.EpisodeExpandAdapter;
import com.example.kuro.fragments.AnimePageEnd;
import com.example.kuro.fragments.AnimePageStart;
import com.example.kuro.pojo.AnimeInfo;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AnimePage extends AppCompatActivity {
    private APIInterface apiInterface;
    private GlobalState globalState;
    private AnimeInfo animeInfo;
    private TextView title,status,episodes,year,duration,description,rating,synTitle,synDescription;
    private RatingBar ratingBar;
    private ImageView imageView;
    private CardView synopsis,progress;
    private RecyclerView recyclerView;
    private EpisodeExpandAdapter episodeExpandAdapter;
    private List<String> epiRange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anime_page);

        globalState=((GlobalState)getApplicationContext());

        Intent intent = getIntent();
        String id = intent.getStringExtra("id");

        title=findViewById(R.id.aniTitle);
        synTitle=findViewById(R.id.aniSynTitle);
        status=findViewById(R.id.aniStatus);
        episodes=findViewById(R.id.aniEp);
        year=findViewById(R.id.aniYear);
        duration=findViewById(R.id.aniDur);
        description=findViewById(R.id.aniDesc);
        synDescription=findViewById(R.id.aniSynDesc);
        rating=findViewById(R.id.aniRate);
        ratingBar=findViewById(R.id.ratingBar);
        imageView=findViewById(R.id.animImg);
        synopsis=findViewById(R.id.synopsis);
        progress=findViewById(R.id.aniProg);

        epiRange=new ArrayList<>();
        recyclerView=findViewById(R.id.aniEps);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        episodeExpandAdapter = new EpisodeExpandAdapter(epiRange);
        recyclerView.setAdapter(episodeExpandAdapter);

        description.setOnClickListener(view12 -> {
            if(animeInfo!=null)
                synopsis.setVisibility(View.VISIBLE);
        });

        synopsis.setOnClickListener(view13 -> synopsis.setVisibility(View.GONE));

        apiInterface = APIClient.getClient(this).create(APIInterface.class);

        loadAnimeInfo(id);
    }

    public void loadAnimeInfo(String id){
        Call<AnimeInfo> call = apiInterface.animeInfo(id);
        call.enqueue(new Callback<AnimeInfo>() {
            @Override
            public void onResponse(Call<AnimeInfo> call, Response<AnimeInfo> response) {
                AnimeInfo resource = response.body();
                globalState.setAnimeInfo(resource);
                animeInfo=resource;
                progress.setVisibility(View.GONE);
                if(animeInfo==null) {
                    finish();
                    Toast.makeText(AnimePage.this, "Anime info not found!!!", Toast.LENGTH_SHORT).show();
                }
                else
                    setInfo();
            }

            @Override
            public void onFailure(Call<AnimeInfo> call, Throwable t) {
                call.cancel();
            }
        });
    }

    public void setInfo(){
        String titl="No Title";
        if (animeInfo.title.romaji != null) titl = animeInfo.title.romaji;
        else if (animeInfo.title.english != null) titl = animeInfo.title.english;
        else if (animeInfo.title.nati != null) titl = animeInfo.title.nati;

        title.setText(titl);
        synTitle.setText(titl);

        description.setText(Html.fromHtml(getString(animeInfo.desc,"description"), Html.FROM_HTML_MODE_COMPACT));
        synDescription.setText(Html.fromHtml(getString(animeInfo.desc,"description"), Html.FROM_HTML_MODE_COMPACT));

        Picasso.get().load(animeInfo.image)
                .placeholder(R.drawable.ic_baseline_broken_image_24)
                .error(R.drawable.ic_baseline_broken_image_24)
                .into(imageView);

        status.setText("Status: "+animeInfo.status);
        year.setText("Year: "+animeInfo.year);
        episodes.setText("Episodes: "+getString(animeInfo.totalEpisodes,"0"));
        duration.setText("Duration: "+getString(animeInfo.duration,"0"));
        if(animeInfo.rating!=null) {
            float rate = animeInfo.rating / 10.0f;
            rating.setText(rate + "");
            ratingBar.setRating(rate / 2);
        }
        else {
            rating.setVisibility(View.GONE);
            ratingBar.setVisibility(View.GONE);
        }

        int size=animeInfo.episodes.size(),pages,extra,pageSize=20;
        if(size!=0) {
            pages= (int) Math.floor(size/pageSize);
            extra= size%pageSize;
            for (int i = 0; i < pages; i++) {
                int start=pageSize*i;
                epiRange.add(start+1+"-"+(start+pageSize));
            }
            epiRange.add(pageSize*pages+1+"-"+(pageSize*pages+extra));
        }

        FragmentManager fm = getSupportFragmentManager();
        if (!fm.isDestroyed())
            fm.beginTransaction().replace(R.id.start_panel, new AnimePageStart()).replace(R.id.end_panel, new AnimePageEnd()).commit();
    }

    public String getString(String s,String d){
        return s!=null?s:d;
    }
}