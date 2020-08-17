package com.example.appwallpaper.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appwallpaper.R;
import com.example.appwallpaper.activity.AlbumsActivity;
import com.example.appwallpaper.model.Album;
import com.example.appwallpaper.model.Photo;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.MyHolder> {
    private List<Album> albumList;
    private Context context;
    private List<Album> listSearch = new ArrayList<>();

    public AlbumAdapter(List<Album> albumList, Context context) {
        this.albumList = albumList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_photo, parent, false);

        MyHolder myHolder = new MyHolder(view);
        return myHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        final Album item = albumList.get(position);
        holder.tvId.setText("Id: " + item.getId() + "");
        holder.tvUserId.setVisibility(View.GONE);
        holder.tvTitle.setVisibility(View.GONE);

        if (item.getId() % 3 == 0) {
            holder.imgStar.setVisibility(View.VISIBLE);
        } else {
            holder.imgStar.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                alertDialog.setView(R.layout.item_photo);
                AlertDialog dialog = alertDialog.show();
                ImageView imgStar;
                TextView tvUserId;
                TextView tvId;
                TextView tvTitle;

                imgStar = (ImageView) dialog.findViewById(R.id.imgStar);
                tvUserId = (TextView) dialog.findViewById(R.id.tvUserId);
                tvId = (TextView) dialog.findViewById(R.id.tvId);
                tvTitle = (TextView) dialog.findViewById(R.id.tvTitle);
                imgStar.setVisibility(View.GONE);
                tvUserId.setText("User id: " + item.getUserId() + "");
                tvId.setText("Id: " + item.getId() + "");
                tvTitle.setText("Title: " + item.getTitle() + "");

                if (item.getId() % 3 == 0) {
                    holder.imgStar.setVisibility(View.VISIBLE);
                } else {
                    holder.imgStar.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return albumList.size();
    }

    public void SearchId(String id) {
        for (int i = 0; i < albumList.size(); i++) {
            if (albumList.get(i).getTitle().contains(id)) {
                listSearch.add(albumList.get(i));
            }
        }
        notifyDataSetChanged();
    }

    public class MyHolder extends RecyclerView.ViewHolder {

        private ConstraintLayout parentConstraint;
        private LinearLayout linearLayout;
        private TextView tvUserId;
        private TextView tvId;
        private TextView tvTitle;
        private ImageView imgStar;

        public MyHolder(@NonNull View itemView) {
            super(itemView);

            parentConstraint = (ConstraintLayout) itemView.findViewById(R.id.parentConstraint);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.linearLayout);
            tvUserId = (TextView) itemView.findViewById(R.id.tvUserId);
            tvId = (TextView) itemView.findViewById(R.id.tvId);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            imgStar = (ImageView) itemView.findViewById(R.id.imgStar);

        }
    }

}
