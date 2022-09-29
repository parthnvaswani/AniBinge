package com.example.kuro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kuro.pojo.AnimeInfo;
import com.example.kuro.pojo.EpisodeLinks;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AnimePlayer extends AppCompatActivity {
    private APIInterface apiInterface;
    private ImageView prev,next;
    private PlayerView playerView;
    private ProgressBar buffering;
    private TextView epTitle,dubButt;
    private ExoPlayer player;
    private String sub="", dub="";
    private boolean isSub=true;
    private GlobalState globalState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anime_player);

        globalState=((GlobalState)getApplicationContext());
        AnimeInfo animeInfo=globalState.getAnimeInfo();


        playerView=findViewById(R.id.playerView);
        epTitle=playerView.findViewById(R.id.epTitle);
        buffering=findViewById(R.id.buffering);
        next=playerView.findViewById(R.id.next);
        prev=playerView.findViewById(R.id.prev);
        dubButt=playerView.findViewById(R.id.dub);

        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        int eps =Integer.parseInt(intent.getStringExtra("eps"));
        String fep =intent.getStringExtra("fep");
        List<String> arr= Arrays.asList(id.split("-"));
        int size=arr.size();
        int ep=1;
        if (isInteger(arr.get(size - 1)))
            ep = Integer.parseInt(arr.get(size - 1));
        else {
            fep = "0";
            ep = 0;
        }
        if(fep==null)fep="1";
        String title;
        if(eps==1)title=join(arr,0,size-2," ");
        else title=arr.stream().collect(Collectors.joining(" "));
        epTitle.setText(title);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN|View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY|View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        int finalEp = ep;
        String finalFep = fep;
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), AnimePlayer.class);
                if(arr.get(size-2).equals("episode"))
                    intent.putExtra("id", join(arr,0,size-1,"-")+"-"+(finalEp +1));
                else
                    intent.putExtra("id", join(arr,0,size,"-")+"-episode-"+(finalEp +1));
                intent.putExtra("eps", eps+"");
                intent.putExtra("fep", finalFep);
                startActivity(intent);
                finish();
            }
        });

        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), AnimePlayer.class);
                if(finalFep.equals("0")&&finalEp==1)
                    intent.putExtra("id", join(arr,0,size-2,"-"));
                else
                    intent.putExtra("id", join(arr,0,size-1,"-")+"-"+(finalEp -1));
                intent.putExtra("eps", eps+"");
                intent.putExtra("fep", finalFep);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.exit_to_right, R.anim.enter_from_left);
            }
        });

        dubButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isSub==true) {
                    dubButt.setText("Dub");
                    long ms=player.getCurrentPosition();
                    player.release();
                    play(dub);
                    player.seekTo(ms);
                }else{
                    player.release();
                    long ms=player.getCurrentPosition();
                    dubButt.setText("Sub");
                    play(sub);
                    player.seekTo(ms);
                }
                isSub=!isSub;
            }
        });

        if(ep>=eps||(fep.equals("0")&&ep>=eps-1)) next.setVisibility(View.GONE);
        if((ep==0&&fep.equals("0"))||(ep==1&&!fep.equals("0"))) prev.setVisibility(View.GONE);

        apiInterface = APIClient.getClient(this).create(APIInterface.class);
        
        getEpisodeLink(id,"sub");
        if(ep!=0)
            getEpisodeLink(join(arr,0,size-2,"-")+"-dub-"+join(arr,size-2,size,"-"),"dub");
        else
            getEpisodeLink(join(arr,0,size,"-")+"-dub","dub");
    }

    public String join(List<String> a,int f,int l,String d){
        return a.subList(f,l).stream().collect(Collectors.joining(d));
    }

    public static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
        } catch(Exception e) {
            return false;
        }
        return true;
    }

    public void getEpisodeLink(String id,String subOrDub){
        Call<EpisodeLinks> call = apiInterface.getEpisodeLinks(id);
        call.enqueue(new Callback<EpisodeLinks>() {
            @Override
            public void onResponse(Call<EpisodeLinks> call, Response<EpisodeLinks> response) {
                EpisodeLinks episodeLinks = response.body();
                if(subOrDub=="sub") {
                    sub=episodeLinks.sources.get(0).url;
                    play(sub);
                }
                else if(episodeLinks!=null){
                    dub=episodeLinks.sources.get(0).url;
                    dubButt.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<EpisodeLinks> call, Throwable t) {
                call.cancel();
            }
        });
    }

    public void play(String link){
        player = new ExoPlayer.Builder(this).build();
        playerView.setPlayer(player);

        DataSource.Factory dataSourceFactory = new DefaultHttpDataSource.Factory().setUserAgent("Mozilla/5.0 (iPhone; CPU iPhone OS 11_0 like Mac OS X) AppleWebKit/604.1.38 (KHTML, like Gecko) Version/11.0 Mobile/15A372 Safari/604.1");
        HlsMediaSource mediaSource = new HlsMediaSource.Factory(dataSourceFactory).
                createMediaSource(MediaItem.fromUri(link));
        player.setMediaSource(mediaSource);
        player.prepare();
        player.setPlayWhenReady(true);
        player.addListener(new Player.Listener() {
            @Override
            public void onPlaybackStateChanged(int state) {
                if (state == Player.STATE_READY) {
                    buffering.setVisibility(View.GONE);
                    player.setPlayWhenReady(true);
                } else if (state == Player.STATE_BUFFERING) {
                    buffering.setVisibility(View.VISIBLE);
                    playerView.setKeepScreenOn(true);
                } else {
                    buffering.setVisibility(View.GONE);
                    player.setPlayWhenReady(true);
                }
                if(state==Player.STATE_ENDED)
                {
                    if(next.getVisibility()==View.VISIBLE)
                        next.callOnClick();
                }
            }

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                if (playbackState == Player.STATE_IDLE || playbackState == Player.STATE_ENDED || !playWhenReady) {
                    playerView.setKeepScreenOn(false);
                } else {
                    playerView.setKeepScreenOn(true);
                }
            }
        });
    }

    @Override
    protected void onPause() {
        player.setPlayWhenReady(false);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        Log.e("hell", player.getCurrentPosition()+"");
        // TODO: Save time to continue watching
        player.release();
        super.onDestroy();
    }
}