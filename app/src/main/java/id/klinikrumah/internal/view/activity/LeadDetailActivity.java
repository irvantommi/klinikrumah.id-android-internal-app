package id.klinikrumah.internal.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import id.klinikrumah.internal.R;
import id.klinikrumah.internal.base.BaseActivity;
import id.klinikrumah.internal.constant.S;
import id.klinikrumah.internal.model.Action;
import id.klinikrumah.internal.model.Client;
import id.klinikrumah.internal.model.Lead;
import id.klinikrumah.internal.model.Project;
import id.klinikrumah.internal.util.CommonFunc;
import id.klinikrumah.internal.util.ErrorType;
import id.klinikrumah.internal.view.adapter.ContactDetailAdapter;

public class LeadDetailActivity extends BaseActivity {
    private static final String TITLE = "Leads Detail";
    private static final String LEAD = "lead";
    // other class
    ContactDetailAdapter contactAdapter = new ContactDetailAdapter();
    // from xml
    private TextView tvClientName;
    private LinearLayout llLocation;
    private TextView tvProjectLocation;
    private ImageView ivToMaps;
    private TextView tvProjectName;
    private TextView tvSurvey;
    private TextView tvPointToDiscuss;
    private TextView tvDraftDesign;
    private TextView tvOffering;
    private TextView tvDescription;
    private TextView tvBudget;
    private TextView tvSizeBuilding;
    private TextView tvSizeLand;
    private TextView tvDate;
    private TextView tvTodo;
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lead_detail);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(TITLE);
        }
        tvClientName = findViewById(R.id.tv_client_name);
        llLocation = findViewById(R.id.ll_location);
        tvProjectLocation = findViewById(R.id.tv_project_location);
        ivToMaps = findViewById(R.id.iv_to_maps);
        RecyclerView rvContact = findViewById(R.id.rv_contact);
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
        FloatingActionButton fabEdit = findViewById(R.id.fab_edit);
        fabEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LeadActivity.show(LeadDetailActivity.this, gson.toJson(lead));
            }
        });
        llLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String latLong = lead.getProject().getLatLong();
                if (!TextUtils.isEmpty(latLong)) {
                    String[] split = latLong.split(",");
                    MapsActivity.show(view.getContext(), lead.getProject().getLocation(),
                            "-6.2259813", "106.9988616", split[0], split[1]);
                } else {
                    CommonFunc.openUrl(view.getContext(), String.format(S.GMAP_SEARCH,
                            tvProjectLocation.getText().toString()));
                }
            }
        });
        rvContact.setAdapter(contactAdapter);
        rvContact.setLayoutManager(new LinearLayoutManager(this));
        rvContact.setNestedScrollingEnabled(false);
        if (getIntent().hasExtra(LEAD)) {
//            showHideProgressBar();
            hideError();
            lead = gson.fromJson(getIntent().getStringExtra(LEAD), Lead.class);
            if (lead != null) {
                setData();
            }
        } else {
            setError(ErrorType.GENERAL);
        }
    }

    private void setData() {
        tvDescription.setText(setDefaultIfEmpty(lead.getDescription()));
        tvBudget.setText(setDefaultIfEmpty(lead.getBudget()));
        tvTodo.setText(setDefaultIfEmpty(lead.getToDo()));
        tvDate.setText(setDefaultIfEmpty(lead.getCreateDate()));
        Client client = lead.getClient();
        if (client != null) {
            tvClientName.setText(setDefaultIfEmpty(client.getName()));
            contactAdapter.addAll(client.getContactList());
        } else {
            tvClientName.setText(S.DASH);
            contactAdapter.add();
        }
        Project project = lead.getProject();
        if (project != null) {
            String location = project.getLocation();
            tvProjectLocation.setText(setDefaultIfEmpty(location));
            if (TextUtils.isEmpty(location)) {
                ivToMaps.setVisibility(View.GONE);
                llLocation.setOnClickListener(null);
            }
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