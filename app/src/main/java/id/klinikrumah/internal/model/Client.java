package id.klinikrumah.internal.model;

import com.google.gson.annotations.SerializedName;

import id.klinikrumah.internal.base.BaseModel;

public class Client extends BaseModel {
    @SerializedName("address")
    private String address;

    @SerializedName("contact")
    private String contact;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }
}