package iak.almuwahhid.com.iak1.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import iak.almuwahhid.com.iak1.DetailRepository;
import iak.almuwahhid.com.iak1.R;
import iak.almuwahhid.com.iak1.SavedRepo;
import iak.almuwahhid.com.iak1.kelas.Github;

public class SavedRepoAdapter extends RecyclerView.Adapter<SavedRepoAdapter.Wadah> {

    ArrayList<Github> all_data;
    Context context;

    public SavedRepoAdapter(ArrayList<Github> all_data, Context context) {
        this.all_data = all_data;
        this.context = context;
    }

    @Override
    public SavedRepoAdapter.Wadah onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_saved_repo_adapter, parent, false);
        return new Wadah(v);
    }

    @Override
    public void onBindViewHolder(SavedRepoAdapter.Wadah holder, int position) {
        final Github github = all_data.get(position);
        Picasso.with(context)
                .load(github.getPhoto())
                .placeholder(android.R.drawable.ic_dialog_alert)
                .error(android.R.drawable.ic_dialog_alert)
                .into(holder.img);
        holder.title.setText(github.getName_repo());
        holder.owner.setText(github.getName_owner());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("url", github.getUrl());
                bundle.putString("name_repo", github.getName_repo());
                bundle.putString("desc", github.getDesc());
                bundle.putString("name_owner", github.getName_owner());
                bundle.putString("photo", github.getPhoto());
                bundle.putInt("saved", 1);

                Intent intent = new Intent(context, DetailRepository.class);
                intent.putExtras(bundle);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return all_data.size();
    }

    public class Wadah extends RecyclerView.ViewHolder {
        TextView title, owner;
        ImageView img;
        public Wadah(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.saved_repo_adapter_title);
            owner = (TextView) itemView.findViewById(R.id.saved_repo_adapter_owner);
            img = (ImageView) itemView.findViewById(R.id.saved_repo_adapter_img);
        }
    }
}
