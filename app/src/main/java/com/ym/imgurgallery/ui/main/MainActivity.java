package com.ym.imgurgallery.ui.main;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;


import com.ym.imgurgallery.App;
import com.ym.imgurgallery.R;
import com.ym.imgurgallery.api.ImgurApi;
import com.ym.imgurgallery.entity.ImgurContent;
import com.ym.imgurgallery.ui.detail.DetailFragment;
import com.ym.imgurgallery.ui.list.ListFragment;
import com.ym.imgurgallery.utils.java.LayoutUtil;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity implements ListFragment.OnContentClickListener {

    @Inject
    protected LayoutUtil layoutUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        App.getComponent().inject(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        if (savedInstanceState == null)
            initDefaultFragment();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initDefaultFragment() {
        getSupportFragmentManager().beginTransaction()
            .replace(R.id.main_content, ListFragment.newInstance(
                layoutUtil.calculateNoOfColumns()
            )).commit();
    }

    @Override
    public void onContentClick(ImgurContent content) {
        String id = content.getCover() != null ? content.getCover() : content.getId();
        getSupportFragmentManager().beginTransaction()
            .replace(R.id.main_content, DetailFragment.newInstance(
                content.getId(), String.format("%s%s.jpg", ImgurApi.IMG, id), content.getDescription()
            )).addToBackStack(null).commit();
    }
}
