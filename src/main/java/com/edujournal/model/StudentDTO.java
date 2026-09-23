package com.edujournal.model;

public class StudentDTO {
    private Integer id;
    private Integer userId;

    private String username;
    private String firstName;
    private String lastName;
    private String phone;
    private String email;

    private String studentNumber;
    private Integer academicGroupId;

    public StudentDTO() {}

    public StudentDTO(Integer id, Integer userId, String username, String firstName, String lastName, String phone, String email, String studentNumber, Integer academicGroupId) {
        this.id = id;
        this.userId = userId;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.email = email;
        this.studentNumber = studentNumber;
        this.academicGroupId = academicGroupId;
    }

    public Integer getStudentId() { return id; }
    public void setStudentId(Integer id) { this.id = id; }

    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getStudentNumber() { return studentNumber; }
    public void setStudentNumber(String studentNumber) { this.studentNumber = studentNumber; }

    public Integer getAcademicGroupId() { return academicGroupId; }
    public void setAcademicGroupId(Integer academicGroupId) { this.academicGroupId = academicGroupId; }
}
