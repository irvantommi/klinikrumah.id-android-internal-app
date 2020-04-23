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

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import id.klinikrumah.internal.R;
import id.klinikrumah.internal.base.BaseActivity;
import id.klinikrumah.internal.model.Lead;
import id.klinikrumah.internal.view.adapter.LeadAdapter;

public class LeadListActivity extends BaseActivity {
    private static final String LEAD_LIST = "lead_list";
    // other class
    private LeadAdapter adapter = new LeadAdapter();
    // from xml

    // member var
    private List<Lead> leadList = new ArrayList<>();

    public static void show(Context context, String leadList) {
        Intent intent = new Intent(context, LeadListActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        Bundle b = new Bundle();
        b.putString(LEAD_LIST, leadList);
        intent.putExtras(b);
        context.startActivity(intent);
        ((Activity) context).overridePendingTransition(0, 0);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lead_list);

        handleIntent(getIntent());
        if (getIntent().hasExtra(LEAD_LIST)) {
//            showHideProgressBar();
            hideError();
            leadList = Arrays.asList(gson.fromJson(getIntent().getStringExtra(LEAD_LIST),
                    Lead[].class));
        } else {
            setError(getString(R.string.data_not_found), getString(R.string.data_not_found_content),
                    getString(R.string.create_new));
        }
        FloatingActionButton fabEdit = findViewById(R.id.fab_new);
        fabEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LeadListActivity.this, LeadActivity.class));
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

        adapter.addAll(leadList);
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

    private void handleIntent(@NotNull Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            //use the query to search your data somehow
        }
    }
}