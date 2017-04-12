package com.artgram.artgram;

/**
 * Created by sonal on 08-06-2016.
 */

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;


/**
 * Created by hp1 on 21-01-2015.
 */
public class TaboneFragment extends Fragment {

    private RecyclerView post_list;
private DatabaseReference mdatabase;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        mdatabase= FirebaseDatabase.getInstance().getReference().child("Posts");
        View v =inflater.inflate(R.layout.tabone,container,false);
        post_list=(RecyclerView) v.findViewById(R.id.recycle_view);
        post_list.setHasFixedSize(true);
        post_list.setLayoutManager(new LinearLayoutManager(getContext()));


        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<Post,PostViewHolder> firebaseRecyclerViewAdapter = new FirebaseRecyclerAdapter<Post, PostViewHolder>(
                Post.class,
                R.layout.single_post,
                PostViewHolder.class,
                mdatabase
        ) {
            @Override
            protected void populateViewHolder(PostViewHolder viewHolder, Post model, int position) {
            viewHolder.setCaption(model.getCaption());
                viewHolder.setImage(getContext(), model.getImage());
            }
        };
        post_list.setAdapter(firebaseRecyclerViewAdapter);
    }
    public static class PostViewHolder extends RecyclerView.ViewHolder
    {
        View mview;

        public PostViewHolder(View itemView) {
            super(itemView);
            mview=itemView;
        }
        public void setCaption(String caption)
        {
            TextView post_caption=(TextView) mview.findViewById(R.id.post_caption);
            post_caption.setText(caption);
        }
        public void setImage(Context context,String image)
        {
            ImageView post_image=(ImageView)mview.findViewById(R.id.post_image);
            Picasso.with(context).load(image).into(post_image);

        }
    }
}
