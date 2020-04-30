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
import java.util.List;

import id.klinikrumah.internal.R;
import id.klinikrumah.internal.base.BaseActivity;
import id.klinikrumah.internal.model.Action;
import id.klinikrumah.internal.model.Client;
import id.klinikrumah.internal.model.Lead;
import id.klinikrumah.internal.model.Project;
import id.klinikrumah.internal.util.enum_.ErrorType;
import id.klinikrumah.internal.view.adapter.LeadAdapter;

public class LeadListActivity extends BaseActivity {
    private static final String LEAD_LIST = "lead_list";
    // from xml
    public RecyclerView rvLeadList;
    private FloatingActionButton fabNew;
    // other class
    private LeadAdapter adapter = new LeadAdapter(this);
    // member var
    private List<Lead> leadList = new ArrayList<>();
    private boolean isFilter = false;
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
            public void selectLead(Lead lead) {
                LeadDetailActivity.show(LeadListActivity.this, gson.toJson(lead));
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
        this.isFilter = isFilter;
        if (adapter.getOriList().size() == 0) {
            hideError();
            setContentVisibility(View.GONE);
            fabNew.setVisibility(View.GONE);
            showHideProgressBar();
            // temp
            leadList = getDummy();
            adapter.addAll(leadList);
            setContentVisibility(View.VISIBLE);
            fabNew.setVisibility(View.VISIBLE);
            showHideProgressBar();
            /*Call<List<JsonObject>> leadListCall = api.getLeadList();
            leadListCall.enqueue(new Callback<List<JsonObject>>() {
                @Override
                public void onResponse(Call<List<JsonObject>> call, Response<List<JsonObject>> response) {
                    JsonObject data = processResponse(response);
                    if (data != null && data.has(LEAD_LIST)) {
                        leadList = Arrays.asList(gson.fromJson(data.getAsJsonArray(LEAD_LIST).toString(),
                                Lead[].class));
                        adapter.addAll(leadList);
                        setContentVisibility(View.VISIBLE);
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
//                errorType = ErrorType.GENERAL;
//                setError(errorType);
                    showHideProgressBar();
                }
            });*/
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

    @NotNull
    private Lead setDummy(String clientName, String location, String projectName, String survey,
                          String pointToDiscuss, String draftDesign, String offering, String desc,
                          String budget, String sizeBuilding, String sizeLand, String leadDate,
                          String todo, String contact1, String contact2) {
        Client client = new Client();
        client.setName(clientName);
        List<String> contactList = new ArrayList<>();
        contactList.add(contact1);
        contactList.add(contact2);
        client.setContactList(contactList);

        Project project = new Project();
        project.setName(projectName);
        project.setLocation(location);
        project.setSizeBuilding(sizeBuilding);
        project.setSizeLand(sizeLand);

        Action action = new Action();
        action.setSurvey(survey);
        action.setPointToDiscuss(pointToDiscuss);
        action.setDraftDesignLink(draftDesign);
        action.setOffering(offering);

        Lead lead = new Lead();
        lead.setClient(client);
        lead.setProject(project);
        lead.setAction(action);
        lead.setDescription(desc);
        lead.setBudget(budget);
        lead.setCreateDate(leadDate);
        lead.setToDo(todo);

        return lead;
    }

    @NotNull
    private List<Lead> getDummy() {
        List<Lead> leadList = new ArrayList<>();
        leadList.add(setDummy("Leads 1", "", "", "", "", "", "", "", "", "", "", "", "", "", ""));
        leadList.add(setDummy("Leads 2", "", "", "", "", "", "", "", "", "", "", "", "", "08114564382", ""));
        leadList.add(setDummy("Pak Herman", "Desa Belimbing, Kosambi, Tangerang", "Bangun Baru", "Belum", "", "Denah Rumah Sudah Diberikan", "", "Follow Up", "", "5x10", "", "4 April 2020", "Keterangan", "082124553727", ""));
        leadList.add(setDummy("Leads 4", "", "Renovasi", "", "", "", "", "Follow Up", "", "", "", "5 April 2020", "Keterangan", "081908222444", ""));
        leadList.add(setDummy("Leads 5", "", "", "", "", "", "", "Follow Up", "", "", "", "5  April 2020", "Keterangan", "081298240070", ""));
        leadList.add(setDummy("Bu Dea", "", "", "", "", "", "", "Follow Up (Budget)", "150-200jt", "6x10", "", "5 April 2020", "Keterangan", "08112479889", ""));
        leadList.add(setDummy("Bu Ratna Rusli", "Jakarta Barat", "Bongkar Existing & Bangun Baru 2 Lantai", "Belum", "", "", "", "Follow up", "650jt", "7x15", "", "6 April 2020", "Keterangan", "089636516419", "089655302560"));
        leadList.add(setDummy("Bu Nina", "Prumpang Jakarta Utara", "Renovasi Rumah", "Sudah", "", "", "", "RAB + Follow Up", "250jt", "18x25", "", "6 April 2020", "", "", ""));
        leadList.add(setDummy("Pak Angky", "Bogor, Komplek IPB Loji", "Bangun Baru", "Belum", "", "", "", "Follow Up", "", "5,2x20", "", "7 April 2020", "Keterangan", "087870001258", ""));
        leadList.add(setDummy("Bu Herni", "Jati Asih", "Pembangunan Perumahan", "Sudah", "", "", "", "Minta dibuatkan siteplan perumahan", "", "", "500 m2", "7 April 2020", "Keterangan", "'081210005346", ""));
        leadList.add(setDummy("Bu Ria", "", "", "", "", "", "", "", "", "", "", "7 April 2020", "", "083893644849", ""));
        leadList.add(setDummy("Bu Lisda", "Jati Asih", "Bangun Pesantren", "Sudah", "", "", "", "Follow Up + Survey", "", "", "6000 m2", "7 April 2020", "", "0811951843", ""));
        leadList.add(setDummy("Pak Usman", "Kemang, Bogor", "Renovasi Kamar Utama + Kamar Mandi Full Kramik", "Belum", "", "", "", "Follow Up RAB", "", "", "", "8 April 2020", "", "081546037128", ""));
        leadList.add(setDummy("Pak Sugeng", "", "Bangun Baru", "", "", "", "", "Follow Up + Survey", "", "9x8", "", "8 April 2020", "", "087777064024", ""));
        leadList.add(setDummy("Pak Alfi", "Bojong Gede Bogor", "Bangun Baru", "", "", "", "", "Follow Up", "", "", "", "9 April 2020", "", "081310759666", ""));
        leadList.add(setDummy("Bu Evy", "Cikini", "Bongkar Existing & Bangun Baru 2 Lantai", "", "", "", "", "Minta RAB", "", "15x13,5", "", "10 April 2020", "", "085710193555", ""));
        leadList.add(setDummy("Pak Benny", "Palmerah Jakarta Barat", "Renovasi Rumah", "Belum", "", "", "", "Follow Up", "", "", "", "10 April 2020", "", "081294880415", ""));
        leadList.add(setDummy("Leads 18", "", "Bangun Rumah", "Belum", "", "", "", "Follow Up", "", "5x11", "", "11 April 2020", "", "082198717559", ""));
        leadList.add(setDummy("Pak Dendy", "Cikupa Tanggerang", "Bangun Baru", "Belum", "", "", "", "Follow Up", "", "", "", "11 April 2020", "", "082112423585", ""));
        leadList.add(setDummy("Pak Alfin", "Cikarang Selatan", "Bangun Baru", "Sudah 15/04/2020", "", "", "", "Minta Denah + Harga", "", "9x20", "", "11 April 2020", "", "082299223887", ""));
        leadList.add(setDummy("Bu Ida", "Bekasi", "Bangun Baru", "Belum", "", "", "", "Nunggu Survey", "", "", "", "11 April 2020", "", "", ""));
        leadList.add(setDummy("Pak Lucky", "Serua Indah Ciputat", "Renovasi Atap", "Belum", "", "", "", "Follow Up Survey", "", "", "", "12 April 2020", "", "082239434019", ""));
        leadList.add(setDummy("Leads 23", "", "", "", "ptd", "", "ofr", "", "", "", "", " April 2020", "", "", ""));
        leadList.add(setDummy("Pak Santoso", "Telaga Kahuripan, Parung", "Bangun Rumah Kebun", "Belum", "", "", "", "Minta Denah", "", "10x15", "600 m2", "12 April 2020", "", "081931199990", ""));
        leadList.add(setDummy("Bu Melly", "Villa Galaxy", "Renovasi Tambah Kamar + Dak", "Sudah 15/04/2020", "", "", "", "Minta Layout + Estimasi Harga", "", "", "", "13 April 2020", "", "08119883636", ""));
        leadList.add(setDummy("Bu Melly", "Grand Galaxy", "Renovasi Full + Tinggi Tanah", "Sudah 15/04/2020", "", "", "", "Minta Layout + Estimasi Harga", "", "", "", "13 April 2020", "", "08119883636", ""));
        leadList.add(setDummy("Bu Joshua", "Bekasi", "Pengecatan", "Belum", "", "", "", "Follow Up Client", "", "", "", "14 April 2020", "", "081343926654", ""));
        leadList.add(setDummy("Pak Slamet", "Ciseeng Bogor", "Tambah Dapur + Kanopi", "Belum", "", "", "", "Minta Layout + Estimasi Harga", "", "", "", "14 April 2020", "", "081382038068", ""));
        leadList.add(setDummy("Pak Gesrel", "", "Renovasi Rumah", "Belum", "", "", "", "Follow Up Client", "", "", "", "14 April 2020", "", "082123228250", ""));
        leadList.add(setDummy("Pak Budi", "Palmerah Jakarta Barat", "Bongkar Existing & Bangun Baru 3 Lantai", "Belum", "", "", "", "Follow Up Client", "", "", "", "14 April 2020", "", "087882325673", ""));
        leadList.add(setDummy("Pak Trisna", "Sembaris Tebet", "Renovasi Atap + Plafond", "Belum", "", "", "", "Follow Up Client", "", "", "", "14 April 2020", "", "081586059006", ""));
        // template
//        leadList.add(setDummy("", "", "", "", "", "", "", "", "", "", "", " April 2020", "", ""));
        return leadList;
    }
}