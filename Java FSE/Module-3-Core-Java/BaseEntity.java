package com.ragulsj.eventmanagement.model;

import java.time.LocalDateTime;

public abstract class BaseEntity implements Manageable {

    protected int id;
    protected LocalDateTime createdAt;
    protected LocalDateTime updatedAt;

    public BaseEntity() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    public void touch() {
        this.updatedAt = LocalDateTime.now();
    }

    public abstract String getEntityType();

    @Override
    public String getSummary() {
        return String.format("[%s] id=%d created=%s", getEntityType(), id, createdAt.toLocalDate());
    }
}
