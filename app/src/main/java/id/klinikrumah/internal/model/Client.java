package id.klinikrumah.internal.model;

import com.google.gson.annotations.SerializedName;

import id.klinikrumah.internal.base.BaseModel;

public class Client extends BaseModel {
    @SerializedName("address")
    String address;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}