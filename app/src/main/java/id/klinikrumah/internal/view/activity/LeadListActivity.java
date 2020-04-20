package id.klinikrumah.internal.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import id.klinikrumah.internal.R;
import id.klinikrumah.internal.base.BaseActivity;
import id.klinikrumah.internal.model.Lead;
import id.klinikrumah.internal.view.adapter.LeadListAdapter;

public class LeadListActivity extends BaseActivity {
    private static final String LEAD_LIST = "lead_list";
    // other class
    private LeadListAdapter adapter;
    // from xml

    // member var
    private List<Lead> leadList = new ArrayList<>();

    public static void show(Context context, String leadList) {
        Intent intent = new Intent(context, LeadListActivity.class);
        Bundle b = new Bundle();
        b.putString(LEAD_LIST, leadList);
        intent.putExtras(b);
        context.startActivity(intent);
        ((Activity) context).overridePendingTransition(0, 0);
    }

    /*@Override
    protected void onResume() {
        super.onResume();
        eventBus.register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
//        controller.cancelPendingRequests();
        eventBus.unregister(this);
    }*/

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lead_list);

        RecyclerView rvLeadList = findViewById(R.id.rv_lead_list);
        if (getIntent().hasExtra(LEAD_LIST)) {
            showHideProgressBar();
            hideError();
            leadList = Arrays.asList(app.getGson().fromJson(getIntent().getStringExtra(LEAD_LIST), Lead[].class));
        } else {
             setError(getString(R.string.data_not_found), getString(R.string.data_not_found_content),
                     getString(R.string.create_new));
        }
        adapter = new LeadListAdapter(new ArrayList<Lead>());
        adapter.setTaskListener(new LeadListAdapter.TaskListener() {
            @Override
            public void selectLead(Lead lead) {
                LeadDetailActivity.show(LeadListActivity.this, app.getGson().toJson(lead));
//                Intent returnIntent = new Intent();
//                returnIntent.putExtra("lead", app.getGson().toJson(lead));
//                setResult(Activity.RESULT_OK, returnIntent);
//                finish();
            }
        });
        rvLeadList.setAdapter(adapter);
        rvLeadList.setLayoutManager(new LinearLayoutManager(this));

        adapter.addAll(leadList);
    }
}