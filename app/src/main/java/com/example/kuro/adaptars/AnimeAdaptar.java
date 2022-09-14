package com.example.kuro.adaptars;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kuro.R;
import com.example.kuro.pojo.Anime;
import com.example.kuro.pojo.Animes;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AnimeAdaptar extends RecyclerView.Adapter<AnimeAdaptar.ViewHolder> {
    List<Anime> animes;

    public AnimeAdaptar(List<Anime> animes) {
        this.animes = animes;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.anime_card,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Anime anime=animes.get(position);
        holder.textView.setText(anime.title.romaji);
        Picasso.get().load(anime.image)
                .placeholder(R.drawable.ic_baseline_broken_image_24)
                .error(R.drawable.ic_baseline_broken_image_24)
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return animes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.animeImage);
            textView=itemView.findViewById(R.id.animeTitle);

        }
    }
}
