package id.klinikrumah.internal.model;

import com.google.gson.annotations.SerializedName;

import id.klinikrumah.internal.base.BaseModel;

public class Lead extends BaseModel {
    @SerializedName("client")
    Client client;

    @SerializedName("project")
    Project project;

    @SerializedName("action")
    Action action;

    @SerializedName("description")
    String description;

    @SerializedName("budget")
    String budget;

    @SerializedName("to_do")
    String toDo;

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBudget() {
        return budget;
    }

    public void setBudget(String budget) {
        this.budget = budget;
    }

    public String getToDo() {
        return toDo;
    }

    public void setToDo(String toDo) {
        this.toDo = toDo;
    }
}