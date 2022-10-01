package com.example.kuro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.kuro.pojo.AnimeInfo;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AnimePage extends AppCompatActivity {
    private APIInterface apiInterface;
    private GlobalState globalState;
    private TextView textView;
    private AnimeInfo animeInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anime_page);

        globalState=((GlobalState)getApplicationContext());

        Intent intent = getIntent();
        String id = intent.getStringExtra("id");

        textView=findViewById(R.id.animeId);
        apiInterface = APIClient.getClient(this).create(APIInterface.class);

        textView.setOnClickListener(view -> {
            if(animeInfo!=null)
                openPlayer(0);
        });

        loadAnimeInfo(id,false);
    }

    public void loadAnimeInfo(String id,boolean isDub){
        Call<AnimeInfo> call = apiInterface.animeInfo(id);
        call.enqueue(new Callback<AnimeInfo>() {
            @Override
            public void onResponse(Call<AnimeInfo> call, Response<AnimeInfo> response) {
                AnimeInfo resource = response.body();
                globalState.setAnimeInfo(resource);
                animeInfo=resource;
            }

            @Override
            public void onFailure(Call<AnimeInfo> call, Throwable t) {
                call.cancel();
            }
        });
    }

    public void openPlayer(int pos){
        Intent intent = new Intent(this, AnimePlayer.class);
        intent.putExtra("pos", pos+"");
        startActivity(intent);
    }
}