package id.klinikrumah.internal.view.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

import id.klinikrumah.internal.R;
import id.klinikrumah.internal.base.BaseActivity;
import id.klinikrumah.internal.model.Action;
import id.klinikrumah.internal.model.Client;
import id.klinikrumah.internal.model.Lead;
import id.klinikrumah.internal.model.Project;

public class LeadActivity extends BaseActivity {
    private static final String LEAD = "lead";
    // other class

    // from xml
    private TextInputEditText etClientName;
    private TextInputEditText etProjectLocation;
    private TextInputEditText etContact;
    private TextInputEditText etProjectName;
    private TextInputEditText etSurvey;
    private TextInputEditText etPointToDiscuss;
    private TextInputEditText etDraftDesign;
    private TextInputEditText etOffering;
    private TextInputEditText etDescription;
    private TextInputEditText etBudget;
    private TextInputEditText etSizeBuilding;
    private TextInputEditText etSizeLand;
    private TextInputEditText etDate;
    private TextInputEditText etTodo;
    // member var
    private Lead lead = new Lead();
    private Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
    private boolean isUpdate = false;

    public static void show(Context context, String lead) {
        Intent intent = new Intent(context, LeadActivity.class);
        Bundle b = new Bundle();
        b.putString(LEAD, lead);
        intent.putExtras(b);
        context.startActivity(intent);
        ((Activity) context).overridePendingTransition(0, 0);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lead);

        etClientName = findViewById(R.id.et_client_name);
        etProjectLocation = findViewById(R.id.et_project_location);
        etContact = findViewById(R.id.et_contact);
        etProjectName = findViewById(R.id.et_project_name);
        etSurvey = findViewById(R.id.et_survey);
        etPointToDiscuss = findViewById(R.id.et_point_to_discuss);
        etDraftDesign = findViewById(R.id.et_draft_design);
        etOffering = findViewById(R.id.et_offering);
        etDescription = findViewById(R.id.et_description);
        etBudget = findViewById(R.id.et_budget);
        etSizeBuilding = findViewById(R.id.et_size_building);
        etSizeLand = findViewById(R.id.et_size_land);
        etTodo = findViewById(R.id.et_todo);
        etDate = findViewById(R.id.et_date);

        final DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                etDate.setText(new SimpleDateFormat("dd/MM/yy", Locale.getDefault()).
                        format(calendar.getTime()));
            }
        };
        final DatePickerDialog datePickerDialog = new DatePickerDialog(LeadActivity.this,
                dateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        etSizeLand.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    datePickerDialog.show();
                    etTodo.requestFocus();
                    return true;
                }
                return false;
            }
        });
        etDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog.show();
            }
        });
        Button btnCancel = findViewById(R.id.btn_cancel);
        Button btnSubmit = findViewById(R.id.btn_submit);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                submitData(isUpdate);
                LeadDetailActivity.show(LeadActivity.this, app.getGson().toJson(lead));
            }
        });
        if (getIntent().hasExtra(LEAD)) {
            showHideProgressBar();
            hideError();
            lead = app.getGson().fromJson(getIntent().getStringExtra(LEAD), Lead.class);
            if (lead != null) {
                setData();
            }
            isUpdate = true;
        } else {
            setError(getString(R.string.error_general), getString(R.string.error_general_content),
                    getString(R.string.try_again));
        }
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(String.format("%s Leads", isUpdate ? "Perbarui" : "Buat"));
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
        etDescription.setText(lead.getDescription());
        etBudget.setText(lead.getBudget());
        etTodo.setText(lead.getToDo());
        etDate.setText(lead.getCreateDate());
        Client client = lead.getClient();
        if (client != null) {
            etClientName.setText(client.getName());
            etContact.setText(client.getContact());
        }
        Project project = lead.getProject();
        if (project != null) {
            etProjectLocation.setText(project.getLocation());
            etProjectName.setText(project.getName());
            etSizeBuilding.setText(project.getSizeBuilding());
            etSizeLand.setText(project.getSizeLand());
        }
        Action action = lead.getAction();
        if (action != null) {
            etSurvey.setText(action.getSurvey());
            etPointToDiscuss.setText(action.getPointToDiscuss());
            etDraftDesign.setText(action.getDraftDesignLink());
            etOffering.setText(action.getOffering());
        }
    }

    private void submitData(boolean isUpdate) {
        lead.setDescription(setString(etDescription.getText()));
        lead.setBudget(setString(etBudget.getText()));
        lead.setToDo(setString(etTodo.getText()));
        lead.setCreateDate(setString(etDate.getText()));

        Client client = isUpdate ? lead.getClient() : new Client();
        if (client != null) {
            client.setName(setString(etClientName.getText()));
            client.setContact(setString(etContact.getText()));
        }
        lead.setClient(client);

        Project project = isUpdate ? lead.getProject() : new Project();
        if (project != null) {
            project.setLocation(setString(etProjectLocation.getText()));
            project.setName(setString(etProjectName.getText()));
            project.setSizeBuilding(setString(etSizeBuilding.getText()));
            project.setSizeLand(setString(etSizeLand.getText()));
        }
        lead.setProject(project);

        Action action = isUpdate ? lead.getAction() : new Action();
        if (action != null) {
            action.setSurvey(setString(etSurvey.getText()));
            action.setPointToDiscuss(setString(etPointToDiscuss.getText()));
            action.setDraftDesignLink(setString(etDraftDesign.getText()));
            action.setOffering(setString(etOffering.getText()));
        }
        lead.setAction(action);
    }
}