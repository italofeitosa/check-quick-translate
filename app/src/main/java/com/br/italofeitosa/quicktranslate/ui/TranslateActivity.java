package com.br.italofeitosa.quicktranslate.ui;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;

import com.br.italofeitosa.quicktranslate.Application;
import com.br.italofeitosa.quicktranslate.R;
import com.br.italofeitosa.quicktranslate.model.Resource;
import com.br.italofeitosa.quicktranslate.ui.adaptee.ResourceAdapter;
import com.br.italofeitosa.quicktranslate.ui.listener.InfiteRecyclerViewScrollListener;

import java.util.List;

/**
 * @author italofeitosa on 14/06/17.
 */
public class TranslateActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ((Application) getApplication()).component().inject(TranslateActivity.this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translate);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        RecyclerView rvItems = (RecyclerView) findViewById(R.id.rvContacts);
        final List<Resource> resourceList = Resource.createResourceList(10, 0);
        final ResourceAdapter adapter = new ResourceAdapter(resourceList);
        rvItems.setAdapter(adapter);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvItems.setLayoutManager(linearLayoutManager);
        InfiteRecyclerViewScrollListener scrollListener = new InfiteRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                final ProgressDialog progress = ProgressDialog.show(TranslateActivity.this, "Aguarde", "Carregando mais dados...", true);
                progress.setCancelable(false);
                List<Resource> moreResource = Resource.createResourceList(10, page);
                final int curSize = adapter.getItemCount();
                resourceList.addAll(moreResource);

                view.post(new Runnable() {
                    @Override
                    public void run() {
                        progress.dismiss();
                        adapter.notifyItemRangeInserted(curSize, resourceList.size() - 1);
                    }
                });
            }
        };

        rvItems.addOnScrollListener(scrollListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_translate, menu);

        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setQueryHint(getString(R.string.search_hint));
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

        return true;
    }
}
