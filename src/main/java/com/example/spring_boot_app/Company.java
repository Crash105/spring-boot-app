package com.example.spring_boot_app;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a company with its name and optional details.
 */
public class Company {
    
    @JsonProperty("name")
    private String name;
    
    @JsonProperty("industry")
    private String industry;
    
    @JsonProperty("description")
    private String description;

    // Default constructor required for Jackson
    public Company() {
    }

    public Company(String name, String industry, String description) {
        this.name = name;
        this.industry = industry;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Company{" +
                "name='" + name + '\'' +
                ", industry='" + industry + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}




