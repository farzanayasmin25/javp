package models;

import java.sql.Date;

public class Complaint {
    private int id;
    private String studentId;
    private String description;
    private String status;
    private Date complaintDate;
    private Date resolvedDate;

    public Complaint(int id, String studentId, String description, String status, Date complaintDate, Date resolvedDate) {
        this.id = id;
        this.studentId = studentId;
        this.description = description;
        this.status = status;
        this.complaintDate = complaintDate;
        this.resolvedDate = resolvedDate;
    }

    public int getId() { return id; }
    public String getStudentId() { return studentId; }
    public String getDescription() { return description; }
    public String getStatus() { return status; }
    public Date getComplaintDate() { return complaintDate; }
    public Date getResolvedDate() { return resolvedDate; }

    public void setStatus(String status) { this.status = status; }
    public void setResolvedDate(Date resolvedDate) { this.resolvedDate = resolvedDate; }
}
