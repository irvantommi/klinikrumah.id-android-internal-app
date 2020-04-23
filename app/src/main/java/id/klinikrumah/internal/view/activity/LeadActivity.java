package id.klinikrumah.internal.view.activity;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import id.klinikrumah.internal.R;
import id.klinikrumah.internal.base.BaseActivity;
import id.klinikrumah.internal.model.Action;
import id.klinikrumah.internal.model.Client;
import id.klinikrumah.internal.model.Lead;
import id.klinikrumah.internal.model.Project;
import id.klinikrumah.internal.util.CommonFunc;
import id.klinikrumah.internal.view.adapter.ContactAdapter;

public class LeadActivity extends BaseActivity implements GoogleApiClient.ConnectionCallbacks {
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private static final int REQUEST_CHECK_SETTINGS = 2;
    private static final int REQUEST_SETTING_RESULT = 3;
    private static final int INTERVAL = 10000;
    private static final int FASTEST_INTERVAL = 5000;
    private static final String LEAD = "lead";
    // other class
    private ContactAdapter contactAdapter = new ContactAdapter();
    // from xml
    private TextInputEditText etClientName;
    private TextInputEditText etProjectLocation;
    private TextView tvLatLong;
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
    private boolean isUpdate = false;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private Calendar calendar = Calendar.getInstance(TimeZone.getDefault());

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
        tvLatLong = findViewById(R.id.tv_lat_long);
        Button btnLatLong = findViewById(R.id.btn_lat_long);
        RecyclerView rvContact = findViewById(R.id.rv_contact);
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

        btnLatLong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermission();
            }
        });
        rvContact.setAdapter(contactAdapter);
        rvContact.setLayoutManager(new LinearLayoutManager(this));
        rvContact.setNestedScrollingEnabled(false);
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
                LeadDetailActivity.show(LeadActivity.this, gson.toJson(lead));
            }
        });
        if (getIntent().hasExtra(LEAD)) {
//            showHideProgressBar();
            hideError();
            lead = gson.fromJson(getIntent().getStringExtra(LEAD), Lead.class);
            if (lead != null) {
                setData();
            }
            isUpdate = true;
        } else {
            isUpdate = false;
//            setError(getString(R.string.error_general), getString(R.string.error_general_content),
//                    getString(R.string.try_again));
        }
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(String.format("%s Leads", isUpdate ? "Perbarui" : "Buat"));
        }
        // setLocationRequest
        locationRequest = new LocationRequest();
        locationRequest.setInterval(INTERVAL);
        locationRequest.setFastestInterval(FASTEST_INTERVAL);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {
            if (permissions.length > 0 && permissions[0].equalsIgnoreCase(Manifest.permission.ACCESS_FINE_LOCATION)) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    showLocationAccessDialog();
                } else if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    showPopUp();
                }
            }
        }
    }

    private void showPopUp() {
        startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), REQUEST_SETTING_RESULT);
    }

    private void checkPermission() {
        if (Build.VERSION.SDK_INT > 22) {
            String accessFineLocation = Manifest.permission.ACCESS_FINE_LOCATION;
            List<String> permissionList = new ArrayList<>();
            if (CommonFunc.isGranted(this, accessFineLocation)) {
                showLocationAccessDialog();
            } else {
                permissionList.add(accessFineLocation);
                String[] params = permissionList.toArray(new String[permissionList.size()]);
                requestPermissions(params, MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            }
        } else {
            showLocationAccessDialog();
        }
    }

    private void showLocationAccessDialog() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .build();
        googleApiClient.connect();

        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(
                googleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(@NotNull LocationSettingsResult locationSettingsResult) {
                final Status status = locationSettingsResult.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        getLastLocation();
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        try {
                            status.startResolutionForResult(LeadActivity.this, REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            e.printStackTrace();
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        getLastLocation();
                        break;
                }
            }
        });
    }

    private void getLastLocation() {
        final FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NotNull LocationResult locationResult) {
                for (Location location : locationResult.getLocations()) {
                    setLatLong(location);
                    fusedLocationClient.removeLocationUpdates(locationCallback);
                }
            }
        };
        fusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location == null) {
                    fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
                } else {
                    setLatLong(location);
                }
            }
        });
    }

    private void setLatLong(@NotNull Location location) {
        tvLatLong.setText(String.format("%s, %s", location.getLatitude(), location.getLongitude()));
    }

    private void setData() {
        etDescription.setText(lead.getDescription());
        etBudget.setText(lead.getBudget());
        etTodo.setText(lead.getToDo());
        etDate.setText(lead.getCreateDate());
        Client client = lead.getClient();
        if (client != null) {
            etClientName.setText(client.getName());
            contactAdapter.addAll(client.getContactList());
        }
        Project project = lead.getProject();
        if (project != null) {
            etProjectLocation.setText(project.getLocation());
            tvLatLong.setText(project.getLatLong());
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
            client.setContactList(contactAdapter.getContactList());
        }
        lead.setClient(client);

        Project project = isUpdate ? lead.getProject() : new Project();
        if (project != null) {
            project.setLocation(setString(etProjectLocation.getText()));
            project.setLatLong(setString(tvLatLong.getText()));
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