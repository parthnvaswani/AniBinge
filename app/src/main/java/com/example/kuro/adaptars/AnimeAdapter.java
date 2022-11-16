package com.example.kuro.adaptars;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kuro.AnimePage;
import com.example.kuro.R;
import com.example.kuro.pojo.Anime;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AnimeAdapter extends RecyclerView.Adapter<AnimeAdapter.ViewHolder> {
    List<Anime> animes;

    public AnimeAdapter(List<Anime> animes) {
        this.animes = animes;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.anime_card, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Anime anime = animes.get(position);
        String title;
        if (anime.title.romaji != null) title = anime.title.romaji;
        else if (anime.title.english != null) title = anime.title.english;
        else if (anime.title.nati != null) title = anime.title.nati;
        else title = "No Title";
        holder.textView.setText(title);
        Picasso.get().load(anime.image).placeholder(R.drawable.ic_baseline_broken_image_24).error(R.drawable.ic_baseline_broken_image_24).into(holder.imageView);

        holder.cardView.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), AnimePage.class);
            intent.putExtra("id", anime.id);
            view.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return animes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.animeImage);
            textView = itemView.findViewById(R.id.epNum);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }
}
