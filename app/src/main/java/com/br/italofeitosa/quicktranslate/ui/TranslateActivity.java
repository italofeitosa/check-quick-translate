package com.br.italofeitosa.quicktranslate.ui;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.br.italofeitosa.quicktranslate.Application;
import com.br.italofeitosa.quicktranslate.R;
import com.br.italofeitosa.quicktranslate.model.Resource;
import com.br.italofeitosa.quicktranslate.model.ResourceTO;
import com.br.italofeitosa.quicktranslate.repository.ResourceRepository;
import com.br.italofeitosa.quicktranslate.retrofit.ResourceService;
import com.br.italofeitosa.quicktranslate.ui.adaptee.ResourceAdapter;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author italofeitosa on 14/06/17.
 */
public class TranslateActivity extends AppCompatActivity {

    private static final String LANGUAGE_PREFERENCE = "language";
    private static final String MODULE_PREFERENCE = "module";

    @Inject
    ResourceService mResourceService;

    @Inject
    ResourceRepository resourceRepository;

    @Inject
    Realm realm;

    @Inject
    Gson gson;

    @Inject
    SharedPreferences preferences;

    @BindView(R.id.fab)
    FloatingActionButton fab;

    @BindView(R.id.rvContacts)
    RecyclerView recyclerView;

    private SearchView searchView;

    protected List<Resource> resourceList;

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

        ProgressDialog progress = ProgressDialog.show(TranslateActivity.this, getString(R.string.wait_request), getString(R.string.request_message), true);
        progress.setCancelable(false);
        resourceList = queryResources(progress);

        infiteScrollList(resourceList);
    }

    private void infiteScrollList(List<Resource> resources){
        ResourceAdapter adapter = new ResourceAdapter(resources);
        recyclerView.setAdapter(adapter);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    private void requestJsonResources (){
        final ProgressDialog progress = ProgressDialog.show(TranslateActivity.this, getString(R.string.wait_request), getString(R.string.request_message), true);
        progress.setCancelable(false);

        mResourceService.getResourcesSince().enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if (response.isSuccessful()) {
                    progress.setMessage(getString(R.string.save_message));
                    List<ResourceTO> resourceTOList = new ArrayList<>();
                    for(int i=0; i < response.body().size(); i++) {
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
                Log.e("RequestResourceError", t.getMessage() != null ? t.getMessage(): "Timeout");
                Toast.makeText(TranslateActivity.this, getString(R.string.save_message_excption), Toast.LENGTH_LONG).show();
            }
        });
    }

    void saveResourceLocal(final List<ResourceTO> resourceTOList, final ProgressDialog progress) {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {
                bgRealm.delete(Resource.class);

                for(int i=0; i < resourceTOList.size(); i++) {
                    Resource resource = new Resource(resourceTOList.get(i), (long) i + 1);
                    bgRealm.copyToRealm(resource);
                }
            }

        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                List<Resource> resourceList = queryResources(progress);
                    infiteScrollList(resourceList);
                progress.dismiss();
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                progress.dismiss();
                Log.e("exception", error.getMessage(), error);
                Toast.makeText(TranslateActivity.this, getString(R.string.save_message_excption), Toast.LENGTH_LONG).show();
            }
        });
    }

    private List<Resource> queryResources(ProgressDialog progress){
        String language = preferences.getString(LANGUAGE_PREFERENCE, "");
        String module = preferences.getString(MODULE_PREFERENCE, "");
        if(!Objects.equals(language, "") && !Objects.equals(module, "")){
            return resourceRepository.getFromFilter(progress, language, module);
        } else {
            return resourceRepository.getResourcesList(progress);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_translate, menu);
        MenuItem searchMenuItem = menu.findItem(R.id.search);

        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) searchMenuItem.getActionView();
        searchView.setQueryHint(getString(R.string.search_hint));
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

        setupSearchView(searchMenuItem);

        return true;
    }

    private void setupSearchView(MenuItem searchMenuItem){
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                ProgressDialog progress = ProgressDialog.show(TranslateActivity.this, getString(R.string.wait_request), getString(R.string.request_message), true);
                progress.setCancelable(false);
                String language = preferences.getString(LANGUAGE_PREFERENCE, "");
                String module = preferences.getString(MODULE_PREFERENCE, "");
                List<Resource> resourceList = resourceRepository.searchValue(query, language, module, progress);
                infiteScrollList(resourceList);

                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) { return false; }
        });

        MenuItemCompat.setOnActionExpandListener(searchMenuItem, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) { return true; }
            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                ProgressDialog progress = ProgressDialog.show(TranslateActivity.this, getString(R.string.wait_request), getString(R.string.request_message), true);
                progress.setCancelable(false);
                infiteScrollList(queryResources(progress));
                return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_language_module) {
            List<String> languages = resourceRepository.getLanguages();
            List<String> modules = resourceRepository.getModule();
            filterLanguageAndModule(languages, modules);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void filterLanguageAndModule(final List<String> languages, final List<String> modules){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = layoutInflater.inflate(R.layout.activity_filter_language_module, null);

        builder.setTitle(R.string.filter_title)
                .setMessage(R.string.filter_message)
                .setIcon(R.drawable.filter)
                .setView(dialogView)
                .setCancelable(false)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        final ProgressDialog progress = ProgressDialog.show(TranslateActivity.this, getString(R.string.wait_request), getString(R.string.request_message), true);
                        progress.setCancelable(false);
                        String language = preferences.getString(LANGUAGE_PREFERENCE, "");
                        String module = preferences.getString(MODULE_PREFERENCE, "");
                        List<Resource>resourceList = resourceRepository.getFromFilter(progress, language, module);
                        infiteScrollList(resourceList);
                        dialog.dismiss();
                    }
                });

        Spinner languageSpinner = (Spinner) dialogView.findViewById(R.id.spinerLanguage);
        ArrayAdapter<String> languageAdapter = new ArrayAdapter<>(getApplication(), R.layout.spinner_item, languages);
        languageAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        languageSpinner.setAdapter(languageAdapter);
        int positionLanguage = languageAdapter.getPosition(preferences.getString(LANGUAGE_PREFERENCE, ""));
        languageSpinner.setSelection(positionLanguage);
        languageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                preferences.edit().putString(LANGUAGE_PREFERENCE, languages.get(position) != null ? languages.get(position): "").apply();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        Spinner moduleSpinner = (Spinner) dialogView.findViewById(R.id.spinerModule);
        ArrayAdapter<String> moduleAdapter = new ArrayAdapter<>(getApplication(), R.layout.spinner_item, modules);
        moduleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        moduleSpinner.setAdapter(moduleAdapter);
        int positionModule = moduleAdapter.getPosition(preferences.getString(MODULE_PREFERENCE, ""));
        moduleSpinner.setSelection(positionModule);

        moduleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                preferences.edit().putString(MODULE_PREFERENCE, modules.get(position) != null ? modules.get(position): "").apply();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        builder.show();
    }
}