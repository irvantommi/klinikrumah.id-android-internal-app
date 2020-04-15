package id.klinikrumah.internal.base;

import com.google.gson.annotations.SerializedName;

public abstract class BaseModel {
    @SerializedName("id")
    String id;

    @SerializedName("name")
    String name;

    @SerializedName("create_date")
    String createDate;

    @SerializedName("update_at")
    String updateAt;

    @SerializedName("is_delete")
    boolean isDelete;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(String updateAt) {
        this.updateAt = updateAt;
    }

    public boolean isDelete() {
        return isDelete;
    }

    public void setDelete(boolean delete) {
        isDelete = delete;
    }
}