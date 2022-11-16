package com.example.kuro.adaptars;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kuro.APIClient;
import com.example.kuro.APIInterface;
import com.example.kuro.AnimePage;
import com.example.kuro.AnimePlayer;
import com.example.kuro.GlobalState;
import com.example.kuro.R;
import com.example.kuro.models.ContinueWatching;
import com.example.kuro.pojo.AnimeInfo;
import com.example.kuro.utils.Utils;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContinueWatchingAdapter extends RecyclerView.Adapter<ContinueWatchingAdapter.ViewHolder> {
    List<ContinueWatching> animes;
    APIInterface apiInterface;
    GlobalState globalState;
    Context ctx;
    private final CollectionReference conWatchRef = FirebaseFirestore.getInstance().collection("ContinueWatching");
    private final String Uid = Utils.getUid();

    public ContinueWatchingAdapter(List<ContinueWatching> animes, Context context) {
        this.animes = animes;
        apiInterface = APIClient.getClient(context).create(APIInterface.class);
        globalState = ((GlobalState) context.getApplicationContext());
        ctx = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.continue_watching_card, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ContinueWatching anime = animes.get(position);
        holder.textView.setText("Episode " + (anime.getEpNum() + 1));
        Picasso.get().load(anime.getImg()).placeholder(R.drawable.ic_baseline_broken_image_24).error(R.drawable.ic_baseline_broken_image_24).into(holder.imageView);

        holder.info.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), AnimePage.class);
            intent.putExtra("id", anime.getAnimeId());
            view.getContext().startActivity(intent);
        });

        holder.close.setOnClickListener(view -> conWatchRef.whereEqualTo("animeId", anime.getAnimeId()).whereEqualTo("uid", Uid).get().addOnSuccessListener(queryDocumentSnapshots -> {
            if (!queryDocumentSnapshots.isEmpty())
                conWatchRef.document(queryDocumentSnapshots.getDocuments().get(0).getId()).delete();
        }));

        holder.cardView.setOnClickListener(view -> loadAnimeInfoAndPlay(anime.getAnimeId(), anime.getEpNum()));
    }

    public void loadAnimeInfoAndPlay(String id, int pos) {
        Call<AnimeInfo> call = apiInterface.animeInfo(id);
        call.enqueue(new Callback<AnimeInfo>() {
            @Override
            public void onResponse(Call<AnimeInfo> call, Response<AnimeInfo> response) {
                AnimeInfo resource = response.body();
                globalState.setAnimeInfo(resource);
                Intent intent = new Intent(ctx, AnimePlayer.class);
                intent.putExtra("pos", pos + "");
                ctx.startActivity(intent);
            }

            @Override
            public void onFailure(Call<AnimeInfo> call, Throwable t) {
                call.cancel();
            }
        });
    }

    @Override
    public int getItemCount() {
        return animes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView, close, info;
        TextView textView;
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.animeImage);
            close = itemView.findViewById(R.id.close);
            info = itemView.findViewById(R.id.info);
            textView = itemView.findViewById(R.id.epNum);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }
}
