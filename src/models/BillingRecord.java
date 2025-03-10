package models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class BillingRecord {
    private final StringProperty studentId;
    private final StringProperty amountDue;
    private final StringProperty status;

    public BillingRecord(String studentId, String amountDue, String status) {
        this.studentId = new SimpleStringProperty(studentId);
        this.amountDue = new SimpleStringProperty(amountDue);
        this.status = new SimpleStringProperty(status);
    }

    // Getter methods for TableView binding
    public StringProperty studentIdProperty() { return studentId; }
    public StringProperty amountDueProperty() { return amountDue; }
    public StringProperty statusProperty() { return status; }

    // Standard Getters
    public String getStudentId() { return studentId.get(); }
    public String getAmountDue() { return amountDue.get(); }
    public String getStatus() { return status.get(); }

    // Standard Setters
    public void setStudentId(String studentId) { this.studentId.set(studentId); }
    public void setAmountDue(String amountDue) { this.amountDue.set(amountDue); }
    public void setStatus(String status) { this.status.set(status); }
}
