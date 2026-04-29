package com.onlineexam.model;

public class Exam {
    private int id;
    private String title;
    private int durationMinutes;
    private String status;

    public Exam(int id, String title, int durationMinutes, String status) {
        this.id = id;
        this.title = title;
        this.durationMinutes = durationMinutes;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }

    public String getStatus() {
        return status;
    }
}

