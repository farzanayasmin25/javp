package models;

public class Complaint {
    private int id;
    private String studentId;
    private String description;
    private String status;

    public Complaint(int id, String studentId, String description, String status) {
        this.id = id;
        this.studentId = studentId;
        this.description = description;
        this.status = status;
    }

    public int getId() { return id; }
    public String getStudentId() { return studentId; }
    public String getDescription() { return description; }
    public String getStatus() { return status; }

    public void setStatus(String status) { this.status = status; }
}
