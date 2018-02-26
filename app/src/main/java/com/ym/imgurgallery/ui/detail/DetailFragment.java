package com.ym.imgurgallery.ui.detail;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ym.imgurgallery.R;


public class DetailFragment extends Fragment {
    private static final String ARG_ID = "ID";
    private static final String ARG_LINK = "LINK";
    private static final String ARG_DESCRIPTION = "DESCRIPTION";

    private RecyclerView detailView;

    private String id;
    private String link;
    private String description;

    public DetailFragment() {
        // Required empty public constructor
    }

    public static DetailFragment newInstance(String id, String link, String description) {
        DetailFragment fragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_ID, id);
        args.putString(ARG_LINK, link);
        args.putString(ARG_DESCRIPTION, description);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            id = getArguments().getString(ARG_ID);
            link = getArguments().getString(ARG_LINK);
            description = getArguments().getString(ARG_DESCRIPTION);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        View view = inflater.inflate(R.layout.fragment_detail, container, false);

        initViews(view);
        initPresenter();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void initViews(View view) {
        detailView = view.findViewById(R.id.recycler_detail);
    }

    private void initPresenter() {
        DetailViewModel detailViewModel = ViewModelProviders.of(this).get(DetailViewModel.class);

        DetailAdapter detailAdapter = new DetailAdapter(this, link, description);
        detailView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        detailView.setAdapter(detailAdapter);

        detailViewModel.commentsResponse.observe(this, response -> {
            if (response == null || !response.isSuccess())
                return;
            detailAdapter.setItems(response.getData());
        });
        detailViewModel.requestComments(id);
    }
}
