package com.br.italofeitosa.quicktranslate.retrofit;

import com.google.gson.annotations.SerializedName;

/**
 * @author  italofeitosa on 16/06/17.
 */

public class ResourceTO {

    @SerializedName("created_at")
    private String createdAt;

    @SerializedName("updated_at")
    private String updatedAt;

    @SerializedName("resource_id")
    private String resourceId;

    @SerializedName("module_id")
    private String moduleId;

    @SerializedName("value")
    private String value;

    @SerializedName("language_id")
    private String languageId;

    @SerializedName("user_modified")
    private String userModified;

    public String getCreatedAt() {
        return createdAt;
    }


    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }


    public String getUpdatedAt() {
        return updatedAt;
    }


    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getResourceId() {
        return resourceId;
    }


    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }


    public String getModuleId() {
        return moduleId;
    }


    public void setModuleId(String moduleId) {
        this.moduleId = moduleId;
    }


    public String getValue() {
        return value;
    }


    public void setValue(String value) {
        this.value = value;
    }


    public String getLanguageId() {
        return languageId;
    }


    public void setLanguageId(String languageId) {
        this.languageId = languageId;
    }


    public String getUserModified() {
        return userModified;
    }


    public void setUserModified(String userModified) {
        this.userModified = userModified;
    }
}
