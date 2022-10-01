package com.example.kuro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.kuro.dao.ContinueWatchingDao;
import com.example.kuro.dao.EpisodePositionDao;
import com.example.kuro.entities.ContinueWatching;
import com.example.kuro.entities.EpisodePosition;
import com.example.kuro.pojo.AnimeInfo;
import com.example.kuro.pojo.EpisodeLinks;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource;

import java.util.Arrays;
import java.util.List;

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
    private ContinueWatchingDao continueWatchingDao;
    private EpisodePositionDao episodePositionDao;
    private String id;
    private AnimeInfo animeInfo;
    private int pos;
    private boolean isEpFinished=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anime_player);

        playerView=findViewById(R.id.playerView);
        player = new ExoPlayer.Builder(this).build();
        playerView.setPlayer(player);

        globalState = ((GlobalState)getApplicationContext());
        animeInfo = globalState.getAnimeInfo();

        DatabaseHelper databaseHelper=DatabaseHelper.getDB(this);
        continueWatchingDao=databaseHelper.continueWatchingDao();
        episodePositionDao=databaseHelper.episodePositionDao();

        epTitle=playerView.findViewById(R.id.epTitle);
        buffering=findViewById(R.id.buffering);
        next=playerView.findViewById(R.id.next);
        prev=playerView.findViewById(R.id.prev);
        dubButt=playerView.findViewById(R.id.dub);

        Intent intent = getIntent();
        pos = Integer.parseInt(intent.getStringExtra("pos"));
        AnimeInfo.Episode episode=animeInfo.episodes.get(pos);
        id=episode.id;
        int eps =animeInfo.episodes.size()-1;
        List<String> arr= Arrays.asList(id.split("-"));
        int size=arr.size();
        String title;

        if (eps == 0) title = join(arr, 0, size - 2, " ");
        else if(episode.title==null) title = String.join(" ",arr);
        else title=episode.title;
        epTitle.setText(title);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN|View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY|View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        next.setOnClickListener(view -> {
            Intent intent1 = new Intent(view.getContext(), AnimePlayer.class);
            intent1.putExtra("pos", pos+1+"");
            startActivity(intent1);
            finish();
        });

        prev.setOnClickListener(view -> {
            Intent intent12 = new Intent(view.getContext(), AnimePlayer.class);
            intent12.putExtra("pos", pos-1+"");
            startActivity(intent12);
            finish();
            overridePendingTransition(R.anim.exit_to_right, R.anim.enter_from_left);
        });

        dubButt.setOnClickListener(view -> {
            String link;
            if(isSub) {
                dubButt.setText("Dub");
                link=dub;
            }else{
                dubButt.setText("Sub");
                link=sub;
            }
            long ms=player.getCurrentPosition();
            play(link);
            player.seekTo(ms);
            isSub=!isSub;
        });

        if(pos>=eps) next.setVisibility(View.GONE);
        if(pos==0) prev.setVisibility(View.GONE);

        apiInterface = APIClient.getClient(this).create(APIInterface.class);

        getEpisodeLink(id,"sub");
        if(arr.get(size-2).equals("episode"))
            getEpisodeLink(join(arr,0,size-2,"-")+"-dub-"+join(arr,size-2,size,"-"),"dub");
        else
            getEpisodeLink(join(arr,0,size,"-")+"-dub","dub");
    }

    public String join(List<String> a,int f,int l,String d){
        return String.join(d,a.subList(f,l));
    }

    public void getEpisodeLink(String id,String subOrDub){
        Call<EpisodeLinks> call = apiInterface.getEpisodeLinks(id);
        call.enqueue(new Callback<EpisodeLinks>() {
            @Override
            public void onResponse(Call<EpisodeLinks> call, Response<EpisodeLinks> response) {
                EpisodeLinks episodeLinks = response.body();
                if(subOrDub.equals("sub")) {
                    sub=episodeLinks.sources.get(0).url;
                    play(sub);
                    player.seekTo(getEpPos());
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
                    isEpFinished=true;
                    if(next.getVisibility()==View.VISIBLE)
                        next.callOnClick();
                }
                playerView.setKeepScreenOn(state != Player.STATE_IDLE && state != Player.STATE_ENDED);
            }

            @Override
            public void onPlayWhenReadyChanged(boolean playWhenReady, int reason) {
                playerView.setKeepScreenOn(playWhenReady);
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
        update(player.getCurrentPosition());
        player.release();
        super.onDestroy();
    }

    protected void update(long position){
        if(continueWatchingDao.getByAnimeId(animeInfo.id).size()==0)
            continueWatchingDao.add(new ContinueWatching(animeInfo.id,animeInfo.image,pos,position));
        else
            continueWatchingDao.update(animeInfo.id,pos,position);

        if(position!=0){
            if(isEpFinished)
                episodePositionDao.deleteByEpId(id);
            else {
                if (episodePositionDao.getByEpId(id).size() == 0)
                    episodePositionDao.add(new EpisodePosition(id, position));
                else
                    episodePositionDao.update(id, position);
            }
        }
    }

    protected long getEpPos(){
        List<EpisodePosition> episodePositions= episodePositionDao.getByEpId(id);
        if (episodePositions.size()!=0)
            return episodePositions.get(0).getPosition();
        return 0;
    }
}