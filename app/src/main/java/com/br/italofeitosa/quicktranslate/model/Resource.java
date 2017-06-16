package com.br.italofeitosa.quicktranslate.model;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * @author italofeitosa on 14/06/17.
 */
public class Resource extends RealmObject{

    @PrimaryKey
    private Long id;

    private String createdAt;

    private String updatedAt;

    private String resourceId;

    private String moduleId;

    private String value;

    private String languageId;

    private String userModified;

    public Resource() {}

    public Resource(ResourceTO resourceTO, Long id) {

        this.id = id;
        this.createdAt = resourceTO.getCreatedAt();
        this.updatedAt = resourceTO.getUpdatedAt();
        this.resourceId = resourceTO.getResourceId();
        this.moduleId = resourceTO.getModuleId();
        this.value = resourceTO.getValue();
        this.languageId = resourceTO.getLanguageId();
        this.userModified = resourceTO.getUserModified();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


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
