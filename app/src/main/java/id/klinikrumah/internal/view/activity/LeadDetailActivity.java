package id.klinikrumah.internal.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import id.klinikrumah.internal.R;
import id.klinikrumah.internal.base.BaseActivity;
import id.klinikrumah.internal.constant.S;
import id.klinikrumah.internal.model.Action;
import id.klinikrumah.internal.model.Client;
import id.klinikrumah.internal.model.Lead;
import id.klinikrumah.internal.model.Project;
import id.klinikrumah.internal.util.CommonFunc;

public class LeadDetailActivity extends BaseActivity {
    private static final String LEAD = "lead";
    // other class

    // from xml
    TextView tvClientName;
    TextView tvProjectLocation;
    TextView tvContact;
    ImageView ivDial;
    ImageView ivWA;
    TextView tvProjectName;
    TextView tvSurvey;
    TextView tvPointToDiscuss;
    TextView tvDraftDesign;
    TextView tvOffering;
    TextView tvDescription;
    TextView tvBudget;
    TextView tvSizeBuilding;
    TextView tvSizeLand;
    TextView tvDate;
    TextView tvTodo;
    FloatingActionButton fabEdit;
    // member var
    private Lead lead = new Lead();

    public static void show(Context context, String lead) {
        Intent intent = new Intent(context, LeadDetailActivity.class);
        Bundle b = new Bundle();
        b.putString(LEAD, lead);
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
        setContentView(R.layout.activity_lead_detail);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Leads Detail");
        }
        tvClientName = findViewById(R.id.tv_client_name);
        tvProjectLocation = findViewById(R.id.tv_project_location);
        tvContact = findViewById(R.id.tv_contact);
        ivDial = findViewById(R.id.iv_dial);
        ivWA = findViewById(R.id.iv_wa);
        tvProjectName = findViewById(R.id.tv_project_name);
        tvSurvey = findViewById(R.id.tv_survey);
        tvPointToDiscuss = findViewById(R.id.tv_point_to_discuss);
        tvDraftDesign = findViewById(R.id.tv_draft_design);
        tvOffering = findViewById(R.id.tv_offering);
        tvDescription = findViewById(R.id.tv_description);
        tvBudget = findViewById(R.id.tv_budget);
        tvSizeBuilding = findViewById(R.id.tv_size_building);
        tvSizeLand = findViewById(R.id.tv_size_land);
        tvDate = findViewById(R.id.tv_date);
        tvTodo = findViewById(R.id.tv_todo);
        fabEdit = findViewById(R.id.fab_edit);
        fabEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        if (getIntent().hasExtra(LEAD)) {
            showHideProgressBar();
            hideError();
            lead = app.getGson().fromJson(getIntent().getStringExtra(LEAD), Lead.class);
            if (lead != null) {
                setData();
            }
        } else {
            setError(getString(R.string.error_general), getString(R.string.error_general_content),
                    getString(R.string.try_again));
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        Intent myIntent = new Intent(getApplicationContext(), LeadListActivity.class);
//        startActivityForResult(myIntent, 0);
        finish();
        return true;
    }

    private void setData() {
        tvDescription.setText(setDefaultIfEmpty(lead.getDescription()));
        tvBudget.setText(setDefaultIfEmpty(lead.getBudget()));
        tvTodo.setText(setDefaultIfEmpty(lead.getToDo()));
        tvDate.setText(setDefaultIfEmpty(lead.getCreateDate()));
        Client client = lead.getClient();
        if (client != null) {
            tvClientName.setText(setDefaultIfEmpty(client.getName()));
            final String contact = client.getContact();
            tvContact.setText(setDefaultIfEmpty(contact));
            ivDial.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CommonFunc.callPhone(view.getContext(), contact);
                }
            });
            ivWA.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CommonFunc.openUrl(view.getContext(), String.format("https://wa.me/%s", contact));
                }
            });
        } else {
            tvClientName.setText(S.DASH);
            tvContact.setText(S.DASH);
            ivDial.setVisibility(View.GONE);
            ivWA.setVisibility(View.GONE);
        }
        Project project = lead.getProject();
        if (project != null) {
            tvProjectLocation.setText(setDefaultIfEmpty(project.getLocation()));
            tvProjectName.setText(setDefaultIfEmpty(project.getName()));
            tvSizeBuilding.setText(setDefaultIfEmpty(project.getSizeBuilding()));
            tvSizeLand.setText(setDefaultIfEmpty(project.getSizeLand()));
        } else {
            tvProjectLocation.setText(S.DASH);
            tvProjectName.setText(S.DASH);
            tvSizeBuilding.setText(S.DASH);
            tvSizeLand.setText(S.DASH);
        }
        Action action = lead.getAction();
        if (action != null) {
            tvSurvey.setText(setDefaultIfEmpty(action.getSurvey()));
            tvPointToDiscuss.setText(setDefaultIfEmpty(action.getPointToDiscuss()));
            tvDraftDesign.setText(setDefaultIfEmpty(action.getDraftDesignLink()));
            tvOffering.setText(setDefaultIfEmpty(action.getOffering()));
        } else {
            tvSurvey.setText(S.DASH);
            tvPointToDiscuss.setText(S.DASH);
            tvDraftDesign.setText(S.DASH);
            tvOffering.setText(S.DASH);
        }
    }
}