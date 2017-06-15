package com.br.italofeitosa.quicktranslate.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * @author italofeitosa on 14/06/17.
 */
public class Resource extends RealmObject{

    @PrimaryKey
    private long id;

    private Date createdAt;

    private String updatedAt;

    private String resourceId;

    private String moduleId;

    private String value;

    private String languageId;

    private String userModified;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
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

    private static int lastResourceId = 0;

    public static List<Resource> createResourceList(int numContacts, int offset) {
        List<Resource> resourceList = new ArrayList<>();

        for (int i = 1; i <= numContacts; i++) {

                Resource resource =  new Resource();
                resource.setResourceId("Filter.Stop.Location.Description " + ++lastResourceId + " offset: " + offset);
                resource.setUpdatedAt("2011-05-12T20:09:39Z");
                resource.setValue("There are still % events on queue. Do you really wish to reload the route?");
                resourceList.add(resource);

        }

        return resourceList;
    }
}
