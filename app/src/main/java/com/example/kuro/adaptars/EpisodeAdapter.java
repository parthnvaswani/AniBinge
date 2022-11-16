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
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kuro.AnimePlayer;
import com.example.kuro.R;
import com.example.kuro.pojo.AnimeInfo;
import com.squareup.picasso.Picasso;

import java.util.List;

public class EpisodeAdapter extends RecyclerView.Adapter<EpisodeAdapter.ViewHolder> {
    List<AnimeInfo.Episode> epiRange;
    List<Integer> pos;

    public EpisodeAdapter(List<AnimeInfo.Episode> epiRange, List<Integer> pos) {
        this.epiRange = epiRange;
        this.pos = pos;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.episode_card, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AnimeInfo.Episode ep = epiRange.get(position);
        int epPos = pos.get(position);
        if (ep.image != null)
            Picasso.get().load(ep.image).fit().placeholder(R.drawable.ic_baseline_broken_image_24).error(R.drawable.ic_baseline_broken_image_24).into(holder.imageView);
        else holder.imageView.setVisibility(View.GONE);

        if (ep.title != null && !ep.title.equals(""))
            holder.textView.setText(ep.num + ". " + ep.title);
        else holder.textView.setText("Episode " + ep.num);

        holder.constraintLayout.setOnClickListener(view -> openPlayer(epPos, holder.cardView.getContext()));
    }

    @Override
    public int getItemCount() {
        return epiRange.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        ImageView imageView;
        CardView cardView;
        ConstraintLayout constraintLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.epName);
            imageView = itemView.findViewById(R.id.epImg);
            cardView = itemView.findViewById(R.id.epImgCard);
            constraintLayout = itemView.findViewById(R.id.episodeCard);
        }
    }

    public static void openPlayer(int pos, Context ctx) {
        Intent intent = new Intent(ctx, AnimePlayer.class);
        intent.putExtra("pos", pos + "");
        ctx.startActivity(intent);
    }
}
