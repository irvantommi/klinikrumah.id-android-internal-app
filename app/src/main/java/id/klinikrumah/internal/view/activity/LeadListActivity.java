package id.klinikrumah.internal.view.activity;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.SearchView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import id.klinikrumah.internal.R;
import id.klinikrumah.internal.base.BaseActivity;
import id.klinikrumah.internal.model.Lead;
import id.klinikrumah.internal.util.enum_.ErrorType;
import id.klinikrumah.internal.view.adapter.LeadAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LeadListActivity extends BaseActivity {
    private static final String LEAD_LIST = "lead_list";
    // from xml
    public RecyclerView rvLeadList;
    private FloatingActionButton fabNew;
    // other class
    private LeadAdapter adapter = new LeadAdapter(this);
    // member var
    private List<Lead> leadList = new ArrayList<>();

    public static void show(Context context) {
        Intent intent = new Intent(context, LeadListActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
        ((Activity) context).overridePendingTransition(0, 0);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData(false);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lead_list);
        ibBack.setVisibility(View.GONE);
        tvTitleToolbar.setText(getString(R.string.title_activity_lead_list));
        ivSearch.setVisibility(View.VISIBLE);

        handleIntent(getIntent());
        setSearchListener(new SearchListener() {
            @Override
            public void onSearchSubmit(String s) {
                keyboardHide();
                getData(true);
            }

            @Override
            public void onSearchChange(String s) {
                getData(true);
            }

            @Override
            public void onSearchClear() {
                getData(false);
                adapter.showAll();
            }
        });
        adapter.setTaskListener(new LeadAdapter.TaskListener() {
            @Override
            public void selectLead(String leadId) {
                LeadDetailActivity.show(LeadListActivity.this, leadId);
            }
        });
        rvLeadList = findViewById(R.id.rv_lead);
        rvLeadList.setAdapter(adapter);
        rvLeadList.setLayoutManager(new LinearLayoutManager(this));

        fabNew = findViewById(R.id.fab_new);
        fabNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createLead();
            }
        });
        setBtnBaseClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (errorType) {
                    case GENERAL:
                        getData(false);
                        break;
                    case NOT_FOUND:
                        createLead();
                        break;
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu, menu);
        // Associate searchable configuration with the SearchView
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        if (searchManager != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if (svBase.getVisibility() == View.GONE) {
            super.onBackPressed();
        } else {
            setSearchIconified(true);
            getData(false);
            adapter.showAll();
        }
    }

    private void getData(boolean isFilter) {
        if (adapter.getFilteredList().size() == 0) {
            hideError();
            setContentVisibility(View.GONE);
            fabNew.setVisibility(View.GONE);
            showHideProgressBar();
            api.getLeadList().enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(@NotNull Call<JsonObject> call, @NotNull
                        Response<JsonObject> response) {
                    JsonObject data = processResponse(response);
                    if (data != null && data.has(LEAD_LIST)) {
                        leadList = Arrays.asList(gson.fromJson(data.getAsJsonArray(LEAD_LIST).toString(),
                                Lead[].class));
                        adapter.clear();
                        adapter.addAll(leadList);
                        setContentVisibility(View.VISIBLE);
                        fabNew.setVisibility(View.VISIBLE);
                    } else {
                        showError(ErrorType.NOT_FOUND);
                    }
                    showHideProgressBar();
                }

                @Override
                public void onFailure(@NotNull Call<JsonObject> call, @NotNull Throwable t) {
                    onRetrofitFailure(t.toString());
                }
            });
        } else if (isFilter) {
            filter();
        }
    }

    private void filter() {
        String query = svBase.getQuery().toString().trim();
        if (query.isEmpty()) {
            adapter.showAll();
            hideError();
        } else {
            adapter.getFilter().filter(query);
        }
    }

    private void createLead() {
        startActivity(new Intent(this, LeadActivity.class));
    }

    private void handleIntent(@NotNull Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            //use the query to search your data somehow
        }
    }
}