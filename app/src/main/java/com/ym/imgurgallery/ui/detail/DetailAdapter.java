package com.ym.imgurgallery.ui.detail;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.ym.imgurgallery.R;
import com.ym.imgurgallery.entity.ImgurComment;

import java.util.ArrayList;
import java.util.List;

public class DetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_IMAGE = 0;
    private final int VIEW_DESCRIPTION = 1;
    private final int VIEW_COMMENT = 2;

    private Fragment fragment;

    private final String imageUrl;
    private final String description;

    public List<ImgurComment> comments = new ArrayList<>();

    public DetailAdapter(Fragment fragment, @NonNull String imageUrl, String description) {
        this.fragment = fragment;
        this.imageUrl = imageUrl;
        this.description = description;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_IMAGE)
            return new ImageHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.template_detail_image, parent, false));
        else if (viewType == VIEW_DESCRIPTION)
            return new DescriptionHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.template_detail_description, parent, false));
        else if (viewType == VIEW_COMMENT)
            return new CommentHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.template_detail_comment, parent, false));

        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (position == 0)
            loadImage(((ImageHolder) holder));
        else if (position == 1)
            loadDescription(((DescriptionHolder) holder).descriptionView);
        else
            loadComment(((CommentHolder) holder), comments.get(position-2));

    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return VIEW_IMAGE;
        else if (position == 1)
            return VIEW_DESCRIPTION;
        else
            return VIEW_COMMENT;
    }

    @Override
    public int getItemCount() {
        return comments.size()+2;
    }

    public void setItems(@NonNull List<ImgurComment> comments) {
        this.comments = comments;
        notifyDataSetChanged();
    }

    private void loadImage(ImageHolder holder) {
        Glide.with(fragment)
            .load(imageUrl)
            .apply(new RequestOptions().skipMemoryCache(true))
            .listener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                    return false;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                    holder.progressView.setVisibility(View.GONE);
                    return false;
                }
            })
            .into(holder.imageView);
    }

    private void loadDescription(TextView textView) {
        textView.setText(description);
    }

    private void loadComment(CommentHolder holder, ImgurComment comment) {
        holder.authorView.setText(comment.getAuthor());
        holder.commentView.setText(comment.getComment());
    }

    public class ImageHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public ProgressBar progressView;

        public ImageHolder(View view) {
            super(view);
            imageView = view.findViewById(R.id.image);
            progressView = view.findViewById(R.id.progress_bar);
        }
    }

    public class DescriptionHolder extends RecyclerView.ViewHolder {
        public TextView descriptionView;

        public DescriptionHolder(View view) {
            super(view);
            descriptionView = view.findViewById(R.id.description);
        }
    }

    public class CommentHolder extends RecyclerView.ViewHolder {
        public TextView authorView;
        public TextView commentView;

        public CommentHolder(View view) {
            super(view);
            authorView = view.findViewById(R.id.author);
            commentView = view.findViewById(R.id.comment);
        }
    }
}
