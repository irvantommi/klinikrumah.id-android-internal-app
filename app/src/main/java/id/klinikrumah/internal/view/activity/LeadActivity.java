package id.klinikrumah.internal.view.activity;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ShareCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import id.klinikrumah.internal.R;
import id.klinikrumah.internal.base.BaseActivity;
import id.klinikrumah.internal.constant.S;
import id.klinikrumah.internal.model.Action;
import id.klinikrumah.internal.model.Client;
import id.klinikrumah.internal.model.File;
import id.klinikrumah.internal.model.Lead;
import id.klinikrumah.internal.model.Project;
import id.klinikrumah.internal.util.enum_.ErrorType;
import id.klinikrumah.internal.util.static_.CommonFunc;
import id.klinikrumah.internal.util.image.CropImage;
import id.klinikrumah.internal.util.image.InternalContentProvider;
import id.klinikrumah.internal.view.adapter.ContactAdapter;
import id.klinikrumah.internal.view.adapter.ImageAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LeadActivity extends BaseActivity {
    private static final int INTERVAL = 10000;
    private static final int FASTEST_INTERVAL = 5000;
    private static final String TITLE = "%s Peminat";
    private static final String LEAD = "lead";
    private static final String DATE_FORMAT = "dd/MM/yy";
    private final String TAG = this.getClass().getSimpleName();
    // other class
    private ContactAdapter contactAdapter = new ContactAdapter();
    private ImageAdapter imgAdapter = new ImageAdapter();
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
    private String state;
    private java.io.File fileCamera, file;
    private int fileMaxSize = 1000000;
    private String fileId;
    private int filePos;
    private List<File> fileList = new ArrayList<>();

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
        etDate = findViewById(R.id.et_date);
        etTodo = findViewById(R.id.et_todo);
        RecyclerView rvImg = findViewById(R.id.rv_img);

        btnLatLong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermissionLocation();
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

                etDate.setText(new SimpleDateFormat(DATE_FORMAT, Locale.getDefault()).format(calendar.getTime()));
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
        imgAdapter.setTaskListener(new ImageAdapter.TaskListener() {
            @Override
            public void add(String id, int pos) {
                checkPermissionReadWrite();
                createFolder();
                fileId = id;
                filePos = pos;
            }

            @Override
            public void remove(int pos) {
                imgAdapter.remove(pos);
            }
        });
        rvImg.setAdapter(imgAdapter);
        rvImg.setLayoutManager(new GridLayoutManager(this, 4));
        rvImg.setNestedScrollingEnabled(false);
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
                submitData();
            }
        });
        if (getIntent().hasExtra(LEAD)) {
            lead = gson.fromJson(getIntent().getStringExtra(LEAD), Lead.class);
            if (lead != null) {
                setData();
            }
            isUpdate = true;
        } else {
            isUpdate = false;
        }
        tvTitleToolbar.setText(String.format(TITLE, isUpdate ? getString(R.string.update) : getString(R.string.add)));
        etClientName.requestFocus(); // to remove focus contactAdapter, first time loading only
        // setLocationRequest
        locationRequest = new LocationRequest();
        locationRequest.setInterval(INTERVAL);
        locationRequest.setFastestInterval(FASTEST_INTERVAL);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
//            deleteLocalFile(fileId); ??? kenapa  diapus ya tiap result_OK
            if (requestCode == S.RequestCode.SETTING_RESULT) {
                checkPermissionLocation();
            } else if (requestCode == S.RequestCode.OPEN_GALLERY) {
                if (data != null) {
                    file = updateFileLocation(data.getData());
                    fileCamera = updateFileLocation(data.getData());
                }
                copyStream(data.getData());
                if (getFileSize(new java.io.File(file.getPath())) <= fileMaxSize) {
                    startCropImage(file);
                } else {
                    try {
                        CommonFunc.showDialog(this, getString(R.string.alert_max_file) +
                                " " + fileMaxSize + " MB", "OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                checkPermissionReadWrite();
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else if (requestCode == S.RequestCode.TAKE_PICTURE) {
                if (getFileSize(new java.io.File(fileCamera.getPath())) <= fileMaxSize) {
                    startCropImage(fileCamera);
                } else {
                    CommonFunc.showDialog(this, getString(R.string.alert_max_file) + " " +
                            fileMaxSize + " MB", "OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            checkPermissionReadWrite();
                        }
                    });
                }
            } else if (requestCode == S.RequestCode.TAKE_PICTURE_WITH_CROP) {
                addFile(data.getStringExtra(CropImage.IMAGE_PATH), "image");
                setDataUpload(file.getName());
                setDataUpload(fileCamera.getName());
                imgAdapter.addAll(fileList);
//                upload();
            } else if (requestCode == S.RequestCode.DOC) {
                file = updateFileLocation(data.getData());
                copyStream(data.getData());
                String path = file.getPath();
                if (getFileSize(new java.io.File(path)) <= fileMaxSize) {
                    addFile(path, "pdf");
                    setDataUpload(file.getName());
                } else {
                    CommonFunc.showDialog(this, getString(R.string.alert_max_file) + " " +
                            fileMaxSize + " MB", "OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            checkPermissionReadWrite();
                        }
                    });
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean isGranted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
//        boolean isDenied = grantResults[0] == PackageManager.PERMISSION_DENIED;
        if (permissions.length > 0 && isGranted) {
            String permission = permissions[0];
            switch (requestCode) {
                case S.RequestCode.LOCATION:
                    if (permission.equalsIgnoreCase(Manifest.permission.ACCESS_FINE_LOCATION)) {
                        getLastLocation();
                    }
                    break;
                case S.RequestCode.EXTERNAL_STORAGE:
                    if (permission.equalsIgnoreCase(Manifest.permission.READ_EXTERNAL_STORAGE) ||
                            permission.equalsIgnoreCase(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        chooseFile();
                    }
                    break;
                case S.RequestCode.CAMERA:
                    if (permission.equalsIgnoreCase(Manifest.permission.CAMERA)) {
                        selectImageCamera();
                    }
                    break;
            }
        }
    }

    // https://developer.android.com/guide/topics/ui/dialogs
    private void showPopUp() {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.allow_location)
                .setPositiveButton(R.string.allow, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS),
                                S.RequestCode.SETTING_RESULT);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        builder.create().show();
    }

    private void checkPermissionLocation() {
        if (Build.VERSION.SDK_INT > 22) {
            String accessFineLocation = Manifest.permission.ACCESS_FINE_LOCATION;
            List<String> permissionList = new ArrayList<>();
            if (CommonFunc.isGranted(this, accessFineLocation)) {
                getLastLocation();
            } else {
                permissionList.add(accessFineLocation);
                String[] params = permissionList.toArray(new String[permissionList.size()]);
                requestPermissions(params, S.RequestCode.LOCATION);
            }
        } else {
            getLastLocation();
        }
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

    private void checkPermissionReadWrite() {
        if (Build.VERSION.SDK_INT > 22) {
            String readExternalStorage = android.Manifest.permission.READ_EXTERNAL_STORAGE;
            String writeExternalStorage = android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
            List<String> permissionList = new ArrayList<>();
            if (CommonFunc.isGranted(this, readExternalStorage) &&
                    CommonFunc.isGranted(this, writeExternalStorage)) {
                chooseFile();
            } else {
                permissionList.add(readExternalStorage);
                permissionList.add(writeExternalStorage);
                String[] params = permissionList.toArray(new String[permissionList.size()]);
                requestPermissions(params, S.RequestCode.EXTERNAL_STORAGE);
            }
        } else {
            chooseFile();
        }
    }

    private void createFolder() {
        state = Environment.getExternalStorageState();
        String path = String.format("%s/AloTemp", this.getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath());
        file = new java.io.File(path);
        fileCamera = new java.io.File(path);
        if (!file.exists()) {
            file.mkdir();
        }
    }

    private void chooseFile() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder
                .setTitle(R.string.choose_from)
                .setItems(R.array.file_upload_array, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        switch (i) {
                            case 0:
                                selectImageGallery();
                                break;
                            case 1:
                                checkPermissionCamera();
                                break;
                            case 2:
                                browseDocuments();
                                break;
                        }
                        dialog.dismiss();
                    }
                });
        dialogBuilder.create().show();
    }

    private void selectImageGallery() {
        Intent photoPickerIntent;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            photoPickerIntent = new Intent(Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        } else {
            photoPickerIntent = new Intent(Intent.ACTION_PICK);
        }
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, S.RequestCode.OPEN_GALLERY);
    }

    private void checkPermissionCamera() {
        if (Build.VERSION.SDK_INT > 22) {
            String camera = Manifest.permission.CAMERA;
            List<String> permissionList = new ArrayList<>();
            if (CommonFunc.isGranted(this, camera)) {
                selectImageCamera();
            } else {
                permissionList.add(camera);
                String[] params = permissionList.toArray(new String[permissionList.size()]);
                requestPermissions(params, S.RequestCode.CAMERA);
            }
        } else {
            selectImageCamera();
        }
    }

    private void selectImageCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && intent.resolveActivity(getPackageManager()) != null) {
            try {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(this,
                        "id.klinikrumah.internal.fileprovider", createImageFile()));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } else {
            try {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Environment.MEDIA_MOUNTED.equals(state) ?
                        Uri.fromFile(fileCamera) : InternalContentProvider.CONTENT_URI);
                intent.putExtra("return-data", true);
            } catch (ActivityNotFoundException e) {
                e.printStackTrace();
            }
        }
        startActivityForResult(intent, S.RequestCode.TAKE_PICTURE);
    }

    @NotNull
    private java.io.File createImageFile() throws IOException {
        java.io.File file = java.io.File.createTempFile("JPEG" + "_", ".jpg",
                getExternalFilesDir(Environment.DIRECTORY_PICTURES));
        fileCamera = new java.io.File(file.getAbsolutePath());
        return file;
    }

    private void browseDocuments() {
        // if you wanna add more extension to be able for import add this array the mimetypes
        String[] mimeTypes = {S.MIME_TYPE_PDF};
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            intent.setType(mimeTypes[0]);
            intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        } else {
            StringBuilder mimeTypesStr = new StringBuilder();
            for (String mimeType : mimeTypes) {
                mimeTypesStr.append(mimeType).append("|");
            }
            intent.setType(mimeTypesStr.substring(0, mimeTypesStr.length() - 1));
        }
        startActivityForResult(Intent.createChooser(intent, "ChooseFile"), S.RequestCode.DOC);
    }

    private void deleteLocalFile(String fieldId) {
        for (int i = 0; i < fileList.size(); i++) {
            if (fileList.get(i).getId().equals(fieldId)) {
                new java.io.File(fileList.get(i).getPath()).delete();
                fileList.remove(i);
            }
        }
//        createClaimTriageAdapter = new CreateClaimTriageAdapter(this, dataFormClaimModels, claimMenuModels, claimCause, this);
//        formClaimRv.setAdapter(createClaimTriageAdapter);
    }

    private java.io.File updateFileLocation(Uri data) {
        boolean isMounted = Environment.MEDIA_MOUNTED.equals(state);
        return new java.io.File(String.format("%s/KR_Temp", isMounted ?
                this.getExternalFilesDir(Environment.DIRECTORY_PICTURES) : getFilesDir()),
                String.format(getFileName(data), ""));
    }

    public String getFileName(@NotNull Uri uri) {
        String result = null;
        if (uri.getScheme() != null && uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
        if (result == null) {
            result = uri.getPath();
            if (result != null) {
                int cut = result.lastIndexOf('/');
                if (cut != -1) {
                    result = result.substring(cut + 1);
                }
            }
        }
        return result;
    }

    private void copyStream(Uri uri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            if (inputStream != null) {
                CommonFunc.copyStream(inputStream, fileOutputStream);
                fileOutputStream.close();
                inputStream.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int getFileSize(@NotNull java.io.File file) {
        if (file.exists()) {
            String size = new DecimalFormat("0").format(file.length() / 1024 / 1024);
            Log.e("File", "File!" + " " + size);
            return Integer.parseInt(size);
        } else {
            Log.e("File", "File does not exists!");
            return 0;
        }
    }

    private void startCropImage(@NonNull java.io.File file) {
        Intent intent = new Intent(this, CropImage.class);
        intent.putExtra(CropImage.IMAGE_PATH, file.getPath());
        intent.putExtra(CropImage.SCALE, true);
        intent.putExtra(CropImage.ASPECT_X, 3);
        intent.putExtra(CropImage.ASPECT_Y, 3);
        intent.putExtra(CropImage.OUTPUT_X, 300);
        intent.putExtra(CropImage.OUTPUT_Y, 300);
        startActivityForResult(intent, S.RequestCode.TAKE_PICTURE_WITH_CROP);
    }

    private void addFile(String path, String type) {
        File file = new File();
        file.setId(fileId);
        file.setPath(path);
        file.setType(type);
        fileList.add(file);
    }

    private void setDataUpload(String fileName) {
        int document = 0;
//        for (int i = 0; i < dataFormClaimModels.size(); i++) {
//            if (dataFormClaimModels.get(i).getSection().equals("document")) {
//                document = i;
//            }
//        }
//        dataFormClaimModels.get(document).getFormFieldModels().get(positionUpload).setValue(fileName);
//        createClaimTriageAdapter.notifyItemChanged(document);
    }

    // TODO: 29/04/20 https://futurestud.io/tutorials/retrofit-2-how-to-upload-files-to-server 
    private void upload() {
        File file = fileList.get(0);
        String path = file.getPath();
        java.io.File ioFile = new java.io.File(path);
        Intent shareIntent = ShareCompat.IntentBuilder.from(this)
                .setText("Unggah Gambar/PDF")
                .setType(S.MIME_TYPE_PDF)
                .setType(S.MIME_TYPE_JPG)
                .setStream(Uri.fromFile(ioFile) )
                .getIntent()
                .setPackage(S.PKG_DRIVE);
        startActivityForResult(shareIntent, S.RequestCode.G_DRIVE);
        /*
        // create RequestBody instance from ioFile
        String type = getContentResolver().getType(Uri.fromFile(ioFile));
        if (type == null) {
            type = image.getType();
        }
        RequestBody requestFile = RequestBody.create(MediaType.parse(type + "/jpeg"), ioFile);
        // MultipartBody.Part is used to send also the actual ioFile name
        String fileName = ioFile.getName();
        MultipartBody.Part body = MultipartBody.Part.createFormData("picture", fileName, requestFile);
        // add another part within the multipart request
        RequestBody rgFileId = RequestBody.create(okhttp3.MultipartBody.FORM, image.getId());
        RequestBody rbFileName = RequestBody.create(okhttp3.MultipartBody.FORM, fileName);

        Call<ResponseBody> upload = apiGoogle.upload(fileName.length(),
                image.getType() + "/jpeg", (int) ioFile.length(), rgFileId, rbFileName, body);
        upload.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                response.body();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e(TAG, "", t);
            }
        });*/
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

    private void submitData() {
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

        showHideProgressBar();
        api.saveLead(gson.toJson(lead)).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NotNull Call<JsonObject> call, @NotNull Response<JsonObject> response) {
                JsonObject data = processResponse(response);
                if (data != null) {
                    finish();
                    lead = gson.fromJson(data.toString(), Lead.class);
                    LeadDetailActivity.show(LeadActivity.this, lead.getId());
//                    LeadDetailActivity.show(LeadActivity.this, data.toString());
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
    }
}