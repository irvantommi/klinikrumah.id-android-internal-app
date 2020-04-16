package id.klinikrumah.internal.model;

import com.google.gson.annotations.SerializedName;

import id.klinikrumah.internal.base.BaseModel;

public class Action extends BaseModel {
    @SerializedName("survey")
    private String survey;

    @SerializedName("point_to_discuss")
    private String pointToDiscuss;

    @SerializedName("draft_design_link")
    private String draftDesignLink;

    @SerializedName("offering")
    private String offering;

    public String getSurvey() {
        return survey;
    }

    public void setSurvey(String survey) {
        this.survey = survey;
    }

    public String getPointToDiscuss() {
        return pointToDiscuss;
    }

    public void setPointToDiscuss(String pointToDiscuss) {
        this.pointToDiscuss = pointToDiscuss;
    }

    public String getDraftDesignLink() {
        return draftDesignLink;
    }

    public void setDraftDesignLink(String draftDesignLink) {
        this.draftDesignLink = draftDesignLink;
    }

    public String getOffering() {
        return offering;
    }

    public void setOffering(String offering) {
        this.offering = offering;
    }
}