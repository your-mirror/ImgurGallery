package com.ym.imgurgallery.ui.list;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ym.imgurgallery.App;
import com.ym.imgurgallery.R;
import com.ym.imgurgallery.entity.ImgurContent;
import com.ym.imgurgallery.ui.common.LoadingMoreAdapter;
import com.ym.imgurgallery.utils.java.NetworkUtil;

import javax.inject.Inject;


public class ListFragment extends Fragment implements LoadingMoreAdapter.OnLoadMoreListener {

    private static final String ARG_COLUMN_COUNT = "ARG_COLUMN_COUNT";
    private int mColumnCount = 2;

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeLayout;

    private ListViewModel listViewModel;
    private ContentAdapter galleryAdapter;

    private OnContentClickListener mListener;

    @Inject
    protected NetworkUtil networkUtil;

    public ListFragment() {}

    @SuppressWarnings("unused")
    public static ListFragment newInstance(int columnCount) {
        ListFragment fragment = new ListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null)
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        App.getComponent().inject(this);

        initViews(view);
        initPresenter();

        return view;
    }

    private void initViews(View view) {
        recyclerView = view.findViewById(R.id.recycler_gallery);
        swipeLayout = view.findViewById(R.id.layout_swipe);
    }

    private void initPresenter() {
        listViewModel = ViewModelProviders.of(this).get(ListViewModel.class);

        /* init recycler and adapter */
        galleryAdapter = new ContentAdapter(this, mListener);
        galleryAdapter.setLoadMoreListener(this);
        recyclerView.setLayoutManager(
                new StaggeredGridLayoutManager(mColumnCount, StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setAdapter(galleryAdapter);

        /* init listeners */
        listViewModel.contentsResponse.observe(this, response -> {
            if (response == null) {
                return;
            } else if (!response.isSuccess()) {
                showInternetError(1);
                galleryAdapter.hideProgressBar();
                swipeLayout.setRefreshing(false);
                return;
            }

            galleryAdapter.addItems(response.getData());
            galleryAdapter.hideProgressBar();
            swipeLayout.setRefreshing(false);
        });
        swipeLayout.setOnRefreshListener(() -> {
            galleryAdapter.removeItems();
            listViewModel.requestGallery(1);
        });

        initRequest();
    }

    private void initRequest() {
        if (networkUtil.isNetworkConnected()) {
            galleryAdapter.showProgressBar();
            listViewModel.requestGallery(1);
        } else {
            showInternetError(1);
        }
    }

    private void showInternetError(final int pageToLoad) {
        if (isDetached())
            return;
        Snackbar
            .make(getActivity().findViewById(R.id.main_activity), R.string.common_error_check_internet_connection, Snackbar.LENGTH_LONG)
            .setAction(R.string.common_repeat, v -> onLoadMore(pageToLoad))
            .show();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnContentClickListener) {
            mListener = (OnContentClickListener) context;
        } else {
            throw new RuntimeException(context.toString()
                + " must implement OnContentClickListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnContentClickListener {
        void onContentClick(ImgurContent content);
    }

    @Override
    public void onLoadMore(int page) {
        galleryAdapter.showProgressBar();
        listViewModel.requestGallery(page);
    }
}
