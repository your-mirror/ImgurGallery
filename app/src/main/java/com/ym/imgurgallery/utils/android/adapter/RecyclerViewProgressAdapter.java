package com.ym.imgurgallery.utils.android.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.ym.imgurgallery.R;

import java.util.ArrayList;
import java.util.List;


abstract public class RecyclerViewProgressAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int VISIBLE_THRESHOLD = 1;
    public static final int VIEW_TYPE_ITEM = 0;
    public static final int VIEW_TYPE_PROGRESS = 10;

    protected List<T> dataSet = new ArrayList<>();
    protected int firstVisibleItem, visibleItemCount, totalItemCount = 0;
    protected boolean isMoreLoading = false;
    protected OnLoadMoreListener onLoadMoreListener;

    public RecyclerViewProgressAdapter(RecyclerView recyclerView, final OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
        addScrollListener(recyclerView);
    }

    protected void addScrollListener(RecyclerView recyclerView) throws UnsupportedOperationException {
        if (!(recyclerView.getLayoutManager() instanceof LinearLayoutManager)) {
            throw new UnsupportedOperationException(
                System.out.format("%s class is not supported", recyclerView.getLayoutManager().getClass()).toString()
            );
        }

        if (recyclerView.getLayoutManager() instanceof GridLayoutManager) {
            final GridLayoutManager layoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
            recyclerView.addOnScrollListener(new CustomScrollListener(layoutManager));
            layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    switch(getItemViewType(position)){
                        case VIEW_TYPE_ITEM:
                            return 1;
                        case VIEW_TYPE_PROGRESS:
                            return layoutManager.getSpanCount();
                        default:
                            return -1;
                    }
                }
            });
        }
        else {
            LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            recyclerView.addOnScrollListener(new CustomScrollListener(layoutManager));
        }
    }

    public class CustomScrollListener extends RecyclerView.OnScrollListener {
        LinearLayoutManager layoutManager;

        public CustomScrollListener(LinearLayoutManager layoutManager) {
            this.layoutManager = layoutManager;
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            totalItemCount = layoutManager.getItemCount();
            visibleItemCount = layoutManager.getChildCount();
            firstVisibleItem = layoutManager.findFirstVisibleItemPosition();

            if (!isMoreLoading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + VISIBLE_THRESHOLD)) {
                if (onLoadMoreListener != null) {
                    onLoadMoreListener.onLoadMore();
                }
            }
        }
    }

    public interface OnLoadMoreListener{
        void onLoadMore();
    }

    public abstract RecyclerView.ViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType);

    public abstract void onBindItemView(RecyclerView.ViewHolder genericHolder, int position);

    public void showProgressBar() {
        addItem(null);
        isMoreLoading = true;
    }

    public void hideProgressBar() {
        removeItem(null);
        isMoreLoading = false;
    }

    public void addItems(@NonNull List<T> newDataSetItems) {
        dataSet.addAll(newDataSetItems);
        notifyItemRangeChanged(0, dataSet.size());
    }

    public void addItem(T item) {
        if (!dataSet.contains(item)) {
            dataSet.add(item);
            notifyItemInserted(dataSet.size() - 1);
        }
    }

    public void removeItem(T item) {
        int indexOfItem = dataSet.indexOf(item);
        if (indexOfItem != -1) {
            this.dataSet.remove(indexOfItem);
            notifyItemRemoved(indexOfItem);
        }
    }

    public T getItem(int index) {
        if (dataSet != null && dataSet.get(index) != null) {
            return dataSet.get(index);
        } else {
            throw new IllegalArgumentException("Item with index " + index + " doesn't exist, dataSet is " + dataSet);
        }
    }

    public List<T> getDataSet() {
        return dataSet;
    }

    @Override
    public int getItemViewType(int position) {
        return dataSet.get(position) != null ? VIEW_TYPE_ITEM : VIEW_TYPE_PROGRESS;
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            return onCreateItemViewHolder(parent, viewType);
        } else if (viewType == VIEW_TYPE_PROGRESS) {
            return onCreateFooterViewHolder(parent, viewType);
        } else {
            throw new IllegalStateException("Invalid type, this type ot items " + viewType + " can't be handled");
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == VIEW_TYPE_ITEM) {
            onBindItemView(holder, position);
        } else {
            onBindFooterView(holder, position);
        }
    }

    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public ProgressViewHolder(View v) {
            super(v);
            //progressBar = v.findViewById(R.id.spinner_progress_bar);
        }
    }

    public RecyclerView.ViewHolder onCreateFooterViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_progress_bar, parent, false);
        return new ProgressViewHolder(v);
    }

    public void onBindFooterView(RecyclerView.ViewHolder genericHolder, int position) {
        ((ProgressViewHolder) genericHolder).progressBar.setVisibility(View.VISIBLE);
    }
}
