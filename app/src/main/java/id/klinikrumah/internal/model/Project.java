package id.klinikrumah.internal.model;

import com.google.gson.annotations.SerializedName;

import id.klinikrumah.internal.base.BaseModel;

public class Project extends BaseModel {
    @SerializedName("client")
    private Client client;

    @SerializedName("location")
    private String location;

    @SerializedName("lat_long")
    private String latLong;

    @SerializedName("size_building")
    private String sizeBuilding;

    @SerializedName("size_land")
    private String sizeLand;

    @SerializedName("value")
    private String value;

    @SerializedName("status")
    private String status;

    @SerializedName("date_start")
    private String dateStart;

    @SerializedName("date_finish")
    private String dateFinish;

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLatLong() {
        return latLong;
    }

    public void setLatLong(String latLong) {
        this.latLong = latLong;
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