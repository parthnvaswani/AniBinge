package com.example.kuro.adaptars;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kuro.GlobalState;
import com.example.kuro.R;
import com.example.kuro.pojo.AnimeInfo;

import java.util.ArrayList;
import java.util.List;

public class EpisodeExpandAdapter extends RecyclerView.Adapter<EpisodeExpandAdapter.ViewHolder> {
    List<String> epiRange;
    AnimeInfo animeInfo;

    public EpisodeExpandAdapter(List<String> epiRange) {
        this.epiRange = epiRange;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.episode_dropdown, parent, false);
        animeInfo = ((GlobalState) parent.getContext().getApplicationContext()).getAnimeInfo();
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String ep = epiRange.get(position);
        holder.textView.setText("Episode " + ep);
        List<AnimeInfo.Episode> epPos = new ArrayList<>();
        List<Integer> pos = new ArrayList<>();
        String[] eps = ep.split("-");
        int start = Integer.parseInt(eps[0]), end = Integer.parseInt(eps[1]);
        for (int i = start; i < end + 1; i++) {
            epPos.add(animeInfo.episodes.get(i - 1));
            pos.add(i - 1);
        }
        holder.recyclerView.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext(), LinearLayoutManager.VERTICAL, false));
        holder.recyclerView.setAdapter(new EpisodeAdapter(epPos, pos));
        holder.constraintLayout.setOnClickListener(view -> {
            if (holder.cardView.getVisibility() == View.GONE) {
                holder.imageView.animate().rotationBy(90);
                holder.cardView.setVisibility(View.VISIBLE);
            } else {
                holder.imageView.animate().rotationBy(-90);
                holder.cardView.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public int getItemCount() {
        return epiRange.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        ImageView imageView;
        ConstraintLayout constraintLayout;
        RecyclerView recyclerView;
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.epExpandEp);
            imageView = itemView.findViewById(R.id.epExp);
            constraintLayout = itemView.findViewById(R.id.toggleEps);
            recyclerView = itemView.findViewById(R.id.epsList);
            cardView = itemView.findViewById(R.id.epsListCon);
            cardView.setVisibility(View.GONE);
        }
    }
}
