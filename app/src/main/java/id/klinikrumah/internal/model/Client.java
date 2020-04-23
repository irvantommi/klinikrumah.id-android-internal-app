package id.klinikrumah.internal.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import id.klinikrumah.internal.base.BaseModel;

public class Client extends BaseModel {
    @SerializedName("address")
    private String address;

    @SerializedName("contact_list")
    private List<String> contactList;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<String> getContactList() {
        return contactList;
    }

    public void setContactList(List<String> contactList) {
        this.contactList = contactList;
    }
}