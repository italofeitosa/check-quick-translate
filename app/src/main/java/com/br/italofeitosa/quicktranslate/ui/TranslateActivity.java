package com.br.italofeitosa.quicktranslate.ui;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.br.italofeitosa.quicktranslate.Application;
import com.br.italofeitosa.quicktranslate.R;
import com.br.italofeitosa.quicktranslate.model.Resource;
import com.br.italofeitosa.quicktranslate.retrofit.ResourceService;
import com.br.italofeitosa.quicktranslate.retrofit.ResourceTO;
import com.br.italofeitosa.quicktranslate.ui.adaptee.ResourceAdapter;
import com.br.italofeitosa.quicktranslate.ui.listener.InfiniteRecyclerViewScrollListener;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmQuery;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author italofeitosa on 14/06/17.
 */
public class TranslateActivity extends AppCompatActivity {

    //private static final int RESULT_LANGUAGE_MODULE = 58;

    @Inject
    ResourceService mResourceService;

    @Inject
    Realm realm;

    @Inject
    Gson gson;

    @BindView(R.id.fab)
    FloatingActionButton fab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ((Application) getApplication()).component().inject(TranslateActivity.this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translate);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestJsonResources();
            }
        });


        RecyclerView rvItems = (RecyclerView) findViewById(R.id.rvContacts);
        final List<Resource> resourceList = Resource.createResourceList(10, 0);
        final ResourceAdapter adapter = new ResourceAdapter(resourceList);
        rvItems.setAdapter(adapter);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvItems.setLayoutManager(linearLayoutManager);
        InfiniteRecyclerViewScrollListener scrollListener = new InfiniteRecyclerViewScrollListener(linearLayoutManager) {
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

    private void requestJsonResources (){
        final ProgressDialog progress = ProgressDialog.show(TranslateActivity.this, "Aguarde", "Carregando os dados...", true);
        progress.setCancelable(false);

        mResourceService.getResourcesSince().enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if (response.isSuccessful()) {
                    progress.setMessage("Salvandos os dados no dispositivo...");
                    List<ResourceTO> resourceTOList = new ArrayList<>();
                    for(int i=0; i < response.body().size(); i++) {
                        /*JsonElement jsonElement = response.body().get(i);
                        String jsonValue = jsonElement.getAsJsonObject().get("resource").getAsString();
                        ResourceTO resourceTO = gson.fromJson(jsonValue, ResourceTO.class);
                        resourceTOList.add(resourceTO);*/
                        ResourceTO resourceTO = gson.fromJson(response.body().get(i).getAsJsonObject().get("resource"), ResourceTO.class);
                        resourceTOList.add(resourceTO);
                    }

                    saveResourceLocal(resourceTOList, progress);


                } else {
                    progress.dismiss();

                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                progress.dismiss();
                t.printStackTrace();
                Log.e("LoginError", t.getMessage() != null ? t.getMessage(): "Timeout");
                Toast.makeText(TranslateActivity.this, "Não foi possivel carregar os dados", Toast.LENGTH_LONG).show();
            }
        });
    }


    void saveResourceLocal(final List<ResourceTO> resourceTOList, final ProgressDialog progress) {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {


                for(int i=0; i < resourceTOList.size(); i++) {
                    Resource resource = new Resource(resourceTOList.get(i), (long) i + 1);
                    bgRealm.copyToRealm(resource);
                }
            }

        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                progress.dismiss();
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                progress.dismiss();
                Log.e("exception", error.getMessage(), error);
                Toast.makeText(TranslateActivity.this, "Não foi possível salvar os dados no dispositivo", Toast.LENGTH_LONG).show();
            }
        });
    }


    void getResourcesList(final ProgressDialog progress) {
        RealmQuery<Resource> resourceRealmQuery = realm.where(Resource.class);
        List<Resource> resourceList = resourceRealmQuery.findAll();
        progress.dismiss();
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_language_module) {
            /*Intent intent = new Intent();
            intent.setClass(TranslateActivity.this, FilterLanguageModuleActivity.class);
            startActivity(intent);*/
            final ProgressDialog progress = ProgressDialog.show(TranslateActivity.this, "Aguarde", "Carregando os dados...", true);
            progress.setCancelable(false);
            getResourcesList(progress);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
