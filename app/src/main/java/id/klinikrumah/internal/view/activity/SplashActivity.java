package id.klinikrumah.internal.view.activity;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import id.klinikrumah.internal.base.BaseActivity;
import id.klinikrumah.internal.model.Action;
import id.klinikrumah.internal.model.Client;
import id.klinikrumah.internal.model.Lead;
import id.klinikrumah.internal.model.Project;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*Call<List<Lead>> leadListCall = api.getLeadList();
        leadListCall.enqueue(new Callback<List<Lead>>() {
            @Override
            public void onResponse(Call<List<Lead>> call, Response<List<Lead>> response) {
                response.body();
            }

            @Override
            public void onFailure(Call<List<Lead>> call, Throwable t) {
                Log.e("Retrofit Get", t.toString());
            }
        });*/
        // temp, dummy
        LeadListActivity.show(this, app.getGson().toJson(getDummy()));
    }

    @NotNull
    private Lead setDummy(String clientName, String location, String projectName, String survey,
                          String pointToDiscuss, String draftDesign, String offering, String desc,
                          String budget, String sizeBuilding, String sizeLand, String leadDate,
                          String todo, String contact) {
        Client client = new Client();
        client.setName(clientName);
        client.setContact(contact);

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
        leadList.add(setDummy("Leads 1", "", "", "", "", "", "", "", "", "", "", "", "", ""));
        leadList.add(setDummy("Leads 2", "", "", "", "", "", "", "", "", "", "", "", "", "08114564382"));
        leadList.add(setDummy("Pak Herman", "Desa Belimbing, Kosambi, Tangerang", "Bangun Baru", "Belum", "", "Denah Rumah Sudah Diberikan", "", "Follow Up", "", "5x10", "", "4 April 2020", "Keterangan", "082124553727"));
        leadList.add(setDummy("Leads 4", "", "Renovasi", "", "", "", "", "Follow Up", "", "", "", "5 April 2020", "Keterangan", "081908222444"));
        leadList.add(setDummy("Leads 5", "", "", "", "", "", "", "Follow Up", "", "", "", "5  April 2020", "Keterangan", "081298240070"));
        leadList.add(setDummy("Bu Dea", "", "", "", "", "", "", "Follow Up (Budget)", "150-200jt", "6x10", "", "5 April 2020", "Keterangan", "08112479889"));
        leadList.add(setDummy("Bu Ratna Rusli", "Jakarta Barat", "Bongkar Existing & Bangun Baru 2 Lantai", "Belum", "", "", "", "Follow up", "650jt", "7x15", "", "6 April 2020", "Keterangan", "089636516419 & 089655302560"));
        leadList.add(setDummy("Bu Nina", "Prumpang Jakarta Utara", "Renovasi Rumah", "Sudah", "", "", "", "RAB + Follow Up", "250jt", "18x25", "", "6 April 2020", "", ""));
        leadList.add(setDummy("Pak Angky", "Bogor, Komplek IPB Loji", "Bangun Baru", "Belum", "", "", "", "Follow Up", "", "5,2x20", "", "7 April 2020", "Keterangan", "087870001258"));
        leadList.add(setDummy("Bu Herni", "Jati Asih", "Pembangunan Perumahan", "Sudah", "", "", "", "Minta dibuatkan siteplan perumahan", "", "", "500 m2", "7 April 2020", "Keterangan", "'081210005346"));
        leadList.add(setDummy("Bu Ria", "", "", "", "", "", "", "", "", "", "", "7 April 2020", "", "083893644849"));
        leadList.add(setDummy("Bu Lisda", "Jati Asih", "Bangun Pesantren", "Sudah", "", "", "", "Follow Up + Survey", "", "", "6000 m2", "7 April 2020", "", "0811951843"));
        leadList.add(setDummy("Pak Usman", "Kemang, Bogor", "Renovasi Kamar Utama + Kamar Mandi Full Kramik", "Belum", "", "", "", "Follow Up RAB", "", "", "", "8 April 2020", "", "081546037128"));
        leadList.add(setDummy("Pak Sugeng", "", "Bangun Baru", "", "", "", "", "Follow Up + Survey", "", "9x8", "", "8 April 2020", "", "087777064024"));
        leadList.add(setDummy("Pak Alfi", "Bojong Gede Bogor", "Bangun Baru", "", "", "", "", "Follow Up", "", "", "", "9 April 2020", "", "081310759666"));
        leadList.add(setDummy("Bu Evy", "Cikini", "Bongkar Existing & Bangun Baru 2 Lantai", "", "", "", "", "Minta RAB", "", "15x13,5", "", "10 April 2020", "", "085710193555"));
        leadList.add(setDummy("Pak Benny", "Palmerah Jakarta Barat", "Renovasi Rumah", "Belum", "", "", "", "Follow Up", "", "", "", "10 April 2020", "", "081294880415"));
        leadList.add(setDummy("Leads 18", "", "Bangun Rumah", "Belum", "", "", "", "Follow Up", "", "5x11", "", "11 April 2020", "", "082198717559"));
        leadList.add(setDummy("Pak Dendy", "Cikupa Tanggerang", "Bangun Baru", "Belum", "", "", "", "Follow Up", "", "", "", "11 April 2020", "", "082112423585"));
        leadList.add(setDummy("Pak Alfin", "Cikarang Selatan", "Bangun Baru", "Sudah 15/04/2020", "", "", "", "Minta Denah + Harga", "", "9x20", "", "11 April 2020", "", "082299223887"));
        leadList.add(setDummy("Bu Ida", "Bekasi", "Bangun Baru", "Belum", "", "", "", "Nunggu Survey", "", "", "", "11 April 2020", "", ""));
        leadList.add(setDummy("Pak Lucky", "Serua Indah Ciputat", "Renovasi Atap", "Belum", "", "", "", "Follow Up Survey", "", "", "", "12 April 2020", "", "082239434019"));
        leadList.add(setDummy("Leads 23", "", "", "", "ptd", "", "ofr", "", "", "", "", " April 2020", "", ""));
        leadList.add(setDummy("Pak Santoso", "Telaga Kahuripan, Parung", "Bangun Rumah Kebun", "Belum", "", "", "", "Minta Denah", "", "10x15", "600 m2", "12 April 2020", "", "081931199990"));
        leadList.add(setDummy("Bu Melly", "Villa Galaxy", "Renovasi Tambah Kamar + Dak", "Sudah 15/04/2020", "", "", "", "Minta Layout + Estimasi Harga", "", "", "", "13 April 2020", "", "08119883636"));
        leadList.add(setDummy("Bu Melly", "Grand Galaxy", "Renovasi Full + Tinggi Tanah", "Sudah 15/04/2020", "", "", "", "Minta Layout + Estimasi Harga", "", "", "", "13 April 2020", "", "08119883636"));
        leadList.add(setDummy("Bu Joshua", "Bekasi", "Pengecatan", "Belum", "", "", "", "Follow Up Client", "", "", "", "14 April 2020", "", "081343926654"));
        leadList.add(setDummy("Pak Slamet", "Ciseeng Bogor", "Tambah Dapur + Kanopi", "Belum", "", "", "", "Minta Layout + Estimasi Harga", "", "", "", "14 April 2020", "", "081382038068"));
        leadList.add(setDummy("Pak Gesrel", "", "Renovasi Rumah", "Belum", "", "", "", "Follow Up Client", "", "", "", "14 April 2020", "", "082123228250"));
        leadList.add(setDummy("Pak Budi", "Palmerah Jakarta Barat", "Bongkar Existing & Bangun Baru 3 Lantai", "Belum", "", "", "", "Follow Up Client", "", "", "", "14 April 2020", "", "087882325673"));
        leadList.add(setDummy("Pak Trisna", "Sembaris Tebet", "Renovasi Atap + Plafond", "Belum", "", "", "", "Follow Up Client", "", "", "", "14 April 2020", "", "081586059006"));
        // template
//        leadList.add(setDummy("", "", "", "", "", "", "", "", "", "", "", " April 2020", "", ""));
        return leadList;
    }
}