package com.ym.imgurgallery.ui.list;

import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.ym.imgurgallery.R;
import com.ym.imgurgallery.api.ImgurApi;
import com.ym.imgurgallery.entity.ImgurContent;
import com.ym.imgurgallery.ui.common.LoadingMoreAdapter;
import com.ym.imgurgallery.ui.list.ListFragment.OnContentClickListener;

public class ContentAdapter extends LoadingMoreAdapter<ImgurContent, ContentAdapter.ViewHolder> {

    private Fragment fragment;
    private final RequestManager glide;
    private final OnContentClickListener listener;

    private final String URL = String.format("%s%%sl.jpg", ImgurApi.IMG);
    private final String URL_THUMB = String.format("%s%%ss.jpg", ImgurApi.IMG);

    public ContentAdapter(Fragment fragment, OnContentClickListener listener) {
        this.fragment = fragment;
        this.glide = Glide.with(fragment);
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.fragment_list_item, parent, false);

        ViewHolder holder = new ViewHolder(view);

        holder.mView.setOnClickListener(v -> {
            if (null != listener) {
                listener.onContentClick((ImgurContent) holder.mView.getTag());
            }
        });
        return holder;
    }

    @Override
    public void onBindItemView(ViewHolder holder, int position) {
        holder.mView.setTag(getItem(position));
        holder.textView.setText(getItem(position).getTitle());

        // todo need to fix bug when it can be caught in scrolling up
        glide
            .load(String.format(URL,
                getItem(position).getCover() != null ? getItem(position).getCover() : getItem(position).getId()
            ))
            .thumbnail(Glide.with(fragment).load(String.format(URL_THUMB,
                getItem(position).getCover() != null ? getItem(position).getCover() : getItem(position).getId()
            )))
            .into(holder.imageView);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final ImageView imageView;
        public final TextView textView;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            imageView =  view.findViewById(R.id.content);
            textView =  view.findViewById(R.id.title);
        }
    }
}
