package models;

public class Student {
    private int admissionId;
    private String fullName;
    private String departmentName;
    private String rollNo;
    private String reasonForHallSeat;

    public Student(int admissionId, String fullName, String departmentName, String rollNo, String reasonForHallSeat) {
        this.admissionId = admissionId;
        this.fullName = fullName;
        this.departmentName = departmentName;
        this.rollNo = rollNo;
        this.reasonForHallSeat = reasonForHallSeat;
    }

    // Getters and setters
    public int getAdmissionId() { return admissionId; }
    public void setAdmissionId(int admissionId) { this.admissionId = admissionId; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getDepartmentName() { return departmentName; }
    public void setDepartmentName(String departmentName) { this.departmentName = departmentName; }
    public String getRollNo() { return rollNo; }
    public void setRollNo(String rollNo) { this.rollNo = rollNo; }
    public String getReasonForHallSeat() { return reasonForHallSeat; }
    public void setReasonForHallSeat(String reasonForHallSeat) { this.reasonForHallSeat = reasonForHallSeat; }
}