package com.zarkcigarettes.DailyDeepDive_ERP.persistence.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Objects;

@Entity
public class ActivityLog {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String user_details;
    private String module;
    @Column(columnDefinition="TEXT")
    private String activity_description;
    private LocalDate date;

    public ActivityLog() {
    }

    public ActivityLog(String user_details, String module, String activity_description, LocalDate date) {
        this.user_details = user_details;
        this.module = module;
        this.activity_description = activity_description;
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUser_details() {
        return user_details;
    }

    public void setUser_details(String user_details) {
        this.user_details = user_details;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getActivity_description() {
        return activity_description;
    }

    public void setActivity_description(String activity_description) {
        this.activity_description = activity_description;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ActivityLog that = (ActivityLog) o;
        return Objects.equals(id, that.id) && Objects.equals(user_details, that.user_details) && Objects.equals(module, that.module) && Objects.equals(activity_description, that.activity_description) && Objects.equals(date, that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user_details, module, activity_description, date);
    }

    @Override
    public String toString() {
        return "ActivityLog{" +
                "id=" + id +
                ", user_details='" + user_details + '\'' +
                ", module='" + module + '\'' +
                ", activity_description='" + activity_description + '\'' +
                ", date=" + date +
                '}';
    }
}
