package id.klinikrumah.internal.model;

import com.google.gson.annotations.SerializedName;

import id.klinikrumah.internal.base.BaseModel;

public class Project extends BaseModel {
    @SerializedName("client")
    Client client;

    @SerializedName("size_building")
    String sizeBuilding;

    @SerializedName("size_land")
    String sizeLand;

    @SerializedName("value")
    String value;

    @SerializedName("status")
    String status;

    @SerializedName("date_start")
    String dateStart;

    @SerializedName("date_finish")
    String dateFinish;

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public String getSizeBuilding() {
        return sizeBuilding;
    }

    public void setSizeBuilding(String sizeBuilding) {
        this.sizeBuilding = sizeBuilding;
    }

    public String getSizeLand() {
        return sizeLand;
    }

    public void setSizeLand(String sizeLand) {
        this.sizeLand = sizeLand;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDateStart() {
        return dateStart;
    }

    public void setDateStart(String dateStart) {
        this.dateStart = dateStart;
    }

    public String getDateFinish() {
        return dateFinish;
    }

    public void setDateFinish(String dateFinish) {
        this.dateFinish = dateFinish;
    }
}