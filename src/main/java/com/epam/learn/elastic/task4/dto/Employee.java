package com.epam.learn.elastic.task4.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Employee {

    private String name;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date dob;
    private Address address;
    private String email;
    private String[] skills;
    private long experience;
    private double rating;
    private String description;
    private boolean verified;
    private long salary;

}
