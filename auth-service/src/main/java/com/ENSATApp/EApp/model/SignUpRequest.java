package com.ENSATApp.EApp.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;

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
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
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
    @Override
    public String toString() {
        return "SignUpRequest {" +
                "id='" + id + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", cinCard='" + cinCard + '\'' +
                ", branch='" + branch + '\'' +
                ", role='" + role + '\'' +
                ", formDetails=" + formDetails +
                ", createdAt=" + createdAt +
                '}';
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SignUpRequest that = (SignUpRequest) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(firstName, that.firstName) &&
                Objects.equals(lastName, that.lastName) &&
                Objects.equals(email, that.email) &&
                Objects.equals(cinCard, that.cinCard) &&
                Objects.equals(branch, that.branch) &&
                Objects.equals(role, that.role) &&
                Objects.equals(formDetails, that.formDetails) &&
                Objects.equals(createdAt, that.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, email, cinCard, branch, role, formDetails, createdAt);
    }
}

