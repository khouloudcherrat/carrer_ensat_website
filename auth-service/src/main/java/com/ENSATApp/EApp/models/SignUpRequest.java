package com.ENSATApp.EApp.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.Map;

@Document(collection = "sign_up_requests")
public class SignUpRequest {
    @Id
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String cinCard;
    private String branch;
    private String role; // "student" or "alumni"
    @Field("form_details")
    private Map<String, Object> formDetails;
    private LocalDateTime createdAt = LocalDateTime.now();
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getCinCard() {
        return cinCard;
    }
    public void setCinCard(String cinCard) {
        this.cinCard = cinCard;
    }
    public String getBranch() {
        return branch;
    }
    public void setBranch(String branch) {
        this.branch = branch;
    }
    public String getRole() {
        return role;
    }
    public void setRole(String role) {
        this.role = role;
    }
    public Map<String, Object> getFormDetails() {
        return formDetails;
    }
    public void setFormDetails(Map<String, Object> formDetails) {
        this.formDetails = formDetails;
    }
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}

