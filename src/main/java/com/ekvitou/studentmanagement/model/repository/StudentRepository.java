package com.ekvitou.studentmanagement.model.repository;

import com.ekvitou.studentmanagement.config.DatabaseConnectionConfig;
import com.ekvitou.studentmanagement.model.Student;
import com.ekvitou.studentmanagement.model.dto.StudentUpdateDto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentRepository {

    // Create Student
    public Student saveStudent(Student student) {
        String sql = "INSERT INTO Student(name, age, email, grade, score) VALUES(?,?,?,?,?)";

        // Validation
        if (!validateStudent(student)) return null;

        try (Connection con = DatabaseConnectionConfig.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, student.getName());
            stmt.setInt(2, student.getAge());
            stmt.setString(3, student.getEmail());
            stmt.setString(4, student.getGrade());
            stmt.setInt(5, student.getScore());

            int rowAffected = stmt.executeUpdate();

            if (rowAffected > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        student.setId(generatedKeys.getInt(1));
                    }
                }
                System.out.println("New Student has been inserted: " + student.getName());
                return student;
            }

            System.out.println("Cannot add new Student");

        } catch (Exception e) {
            System.out.println("Problem during insert new Student: " + e.getMessage());
        }

        return null;
    }

    // ===== All Students =====
    public List<Student> findAllStudents() {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT * FROM Student";

        try (Connection con = DatabaseConnectionConfig.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Student student = new Student(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("age"),
                        rs.getString("email"),
                        rs.getString("grade"),
                        rs.getInt("score")
                );
                students.add(student);
            }

        } catch (Exception e) {
            System.out.println("Problem during fetch all Students: " + e.getMessage());
        }

        return students;
    }

    // ===== Update =====
    public Student updateStudent(int id, StudentUpdateDto updateDto) {
        String sql = "UPDATE Student SET name=?, age=?, email=?, grade=?, score=? WHERE id=?";

        if (!validateStudent(updateDto)) return null;

        try (Connection con = DatabaseConnectionConfig.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, updateDto.name());
            stmt.setInt(2, updateDto.age());
            stmt.setString(3, updateDto.email());
            stmt.setString(4, updateDto.grade());
            stmt.setInt(5, updateDto.score());
            stmt.setInt(6, id);

            int rowAffected = stmt.executeUpdate();
            if (rowAffected > 0) {
                System.out.println("Student has been updated. ID: " + id);
                // Return updated student instead of null
                return new Student(id, updateDto.name(), updateDto.age(), updateDto.email(), updateDto.grade(), updateDto.score());
            }
            System.out.println("Student cannot update");

        } catch (Exception e) {
            System.out.println("Problem during update Student: " + e.getMessage());
        }

        return null;
    }

    // ===== Delete =====
    public int deleteStudent(int id) {
        String sql = "DELETE FROM Student WHERE id=?";

        try (Connection con = DatabaseConnectionConfig.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1, id);
            int rowAffected = stmt.executeUpdate();
            if (rowAffected > 0) {
                System.out.println("Student has been deleted");
                return rowAffected;
            }

            System.out.println("Cannot delete Student");

        } catch (Exception e) {
            System.out.println("Problem during delete Student: " + e.getMessage());
        }

        return 0;
    }

    // ===== Search by Grade =====
    public List<Student> findStudentsByGrade(String grade) {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT * FROM Student WHERE grade=?";

        try (Connection con = DatabaseConnectionConfig.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, grade);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Student student = new Student(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("age"),
                        rs.getString("email"),
                        rs.getString("grade"),
                        rs.getInt("score")
                );
                students.add(student);
            }

        } catch (Exception e) {
            System.out.println("Problem during search Students by grade: " + e.getMessage());
        }

        return students;
    }

    // ===== Top-Performing Students =====
    public List<Student> topPerformingStudents() {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT * FROM Student WHERE score >= 90";

        try (Connection con = DatabaseConnectionConfig.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Student student = new Student(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("age"),
                        rs.getString("email"),
                        rs.getString("grade"),
                        rs.getInt("score")
                );
                students.add(student);
            }

        } catch (Exception e) {
            System.out.println("Problem during fetch top-performing Students: " + e.getMessage());
        }

        return students;
    }

    // ===== Average Score =====
    public double calculateAverageScore() {
        String sql = "SELECT AVG(score) AS avg_score FROM Student";

        try (Connection con = DatabaseConnectionConfig.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) return rs.getDouble("avg_score");

        } catch (Exception e) {
            System.out.println("Problem during calculate average score: " + e.getMessage());
        }

        return 0;
    }

    // ===== Bulk Insert (Batch) =====
    public void bulkInsertStudents(List<Student> students) {
        String sql = "INSERT INTO Student(name, age, email, grade, score) VALUES(?,?,?,?,?)";

        try (Connection con = DatabaseConnectionConfig.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            con.setAutoCommit(false);

            for (Student s : students) {
                if (!validateStudent(s)) continue;
                stmt.setString(1, s.getName());
                stmt.setInt(2, s.getAge());
                stmt.setString(3, s.getEmail());
                stmt.setString(4, s.getGrade());
                stmt.setInt(5, s.getScore());
                stmt.addBatch();
            }

            stmt.executeBatch();
            con.commit();
            System.out.println("Bulk insert completed successfully");

        } catch (Exception e) {
            System.out.println("Problem during bulk insert: " + e.getMessage());
        }
    }

    // ===== Validation =====
    private boolean validateStudent(Student student) {
        if (student.getAge() <= 0) {
            System.out.println("Invalid age");
            return false;
        }
        if (!student.getEmail().matches("^[\\w-.]+@[\\w-]+\\.[a-z]{2,}$")) {
            System.out.println("Invalid email");
            return false;
        }
        if (!student.getGrade().matches("[A-F]")) {
            System.out.println("Invalid grade");
            return false;
        }
        if (student.getScore() < 0 || student.getScore() > 100) {
            System.out.println("Invalid score");
            return false;
        }
        return true;
    }

    private boolean validateStudent(StudentUpdateDto dto) {
        if (dto.age() <= 0) {
            System.out.println("Invalid age");
            return false;
        }
        if (!dto.email().matches("^[\\w-.]+@[\\w-]+\\.[a-z]{2,}$")) {
            System.out.println("Invalid email");
            return false;
        }
        if (!dto.grade().matches("[A-F]")) {
            System.out.println("Invalid grade");
            return false;
        }
        if (dto.score() < 0 || dto.score() > 100) {
            System.out.println("Invalid score");
            return false;
        }
        return true;
    }

    // ===== Find Student by ID =====
    public Student findStudentById(int id) {
        String sql = "SELECT * FROM Student WHERE id=?";
        try (Connection con = DatabaseConnectionConfig.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Student(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("age"),
                        rs.getString("email"),
                        rs.getString("grade"),
                        rs.getInt("score")
                );
            }

        } catch (Exception e) {
            System.out.println("Problem during find Student by ID: " + e.getMessage());
        }

        return null;
    }
}
