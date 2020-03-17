package com.example.newbestgm;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    Context context;
    ArrayList<User> profiles;

    public MyAdapter(Context c , ArrayList<User> p)
    {
        context = c;
        profiles = p;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.card_view,parent,false));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.name.setText(profiles.get(position).getUser_full_name());
        holder.role.setText(profiles.get(position).getUser_company_role());
        Picasso.get().load(profiles.get(position).getImage()).into(holder.profilePic);
    }

    @Override
    public int getItemCount() {
        return profiles.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        View mView;
        TextView name,role;
        ImageView profilePic;
        Button btn;
        final String IS_CLICKED_MSG = " is clicked";
        public MyViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            name = (TextView) itemView.findViewById(R.id.namee);
            role = (TextView) itemView.findViewById(R.id.rolee);
            profilePic = (ImageView) itemView.findViewById(R.id.profilePic);
        }
        public void onClick(final int position)
        {
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, position + IS_CLICKED_MSG , Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
