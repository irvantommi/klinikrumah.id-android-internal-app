package id.klinikrumah.internal.view.activity;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import id.klinikrumah.internal.util.ErrorType;
import id.klinikrumah.internal.view.adapter.LeadAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LeadListActivity extends BaseActivity {
    private static final String LEAD_LIST = "lead_list";
    // other class
    private LeadAdapter adapter = new LeadAdapter();
    // from xml
    FloatingActionButton fabNew;
    // member var
    private List<Lead> leadList = new ArrayList<>();
    private ErrorType errorType;

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
        getData();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lead_list);

        handleIntent(getIntent());
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
                        getData();
                        break;
                    case NOT_FOUND:
                        createLead();
                        break;
                }
            }
        });
        adapter.setTaskListener(new LeadAdapter.TaskListener() {
            @Override
            public void selectLead(Lead lead) {
                LeadDetailActivity.show(LeadListActivity.this, gson.toJson(lead));
            }
        });
        RecyclerView rvLeadList = findViewById(R.id.rv_lead);
        rvLeadList.setAdapter(adapter);
        rvLeadList.setLayoutManager(new LinearLayoutManager(this));
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

    private void getData() {
        hideError();
        showHideProgressBar();
        fabNew.setVisibility(View.GONE);
        Call<List<JsonObject>> leadListCall = api.getLeadList();
        leadListCall.enqueue(new Callback<List<JsonObject>>() {
            @Override
            public void onResponse(Call<List<JsonObject>> call, Response<List<JsonObject>> response) {
                JsonObject data = processResponse(response);
                if (data != null && data.has(LEAD_LIST)) {
                    leadList = Arrays.asList(gson.fromJson(data.getAsJsonArray(LEAD_LIST).toString(),
                            Lead[].class));
                    adapter.addAll(leadList);
                    fabNew.setVisibility(View.VISIBLE);
                } else {
                    errorType = ErrorType.NOT_FOUND;
                    setError(errorType);
                }
                showHideProgressBar();
            }

            @Override
            public void onFailure(Call<List<JsonObject>> call, Throwable t) {
                Log.e("Retrofit Get", t.toString());
                errorType = ErrorType.GENERAL;
                setError(errorType);
                showHideProgressBar();
            }
        });
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