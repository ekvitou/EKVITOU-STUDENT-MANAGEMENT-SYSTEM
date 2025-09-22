package com.ekvitou.studentmanagement;

import com.ekvitou.studentmanagement.model.Student;
import com.ekvitou.studentmanagement.model.dto.StudentUpdateDto;
import com.ekvitou.studentmanagement.model.repository.StudentRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final StudentRepository repo = new StudentRepository();
    private static final Scanner scanner = new Scanner(System.in);

    private static void thumbnail() {
        System.out.println("=".repeat(25));
        System.out.println("       Student Management System      ");
        System.out.println("=".repeat(25));
        System.out.println("""
                1. Add Student
                2. View All Students
                3. Update Student
                4. Delete Student
                5. Search Students by Grade
                6. Top-Performing Students
                7. Average Score
                8. Bulk Insert Students
                9. Exit
                """);
    }

    private static int option() {
        System.out.print("[+] Insert option: ");
        return scanner.nextInt();
    }

    private static void pressEnterToContinue() {
        System.out.print(">> Press Enter to continue..");
        scanner.nextLine();
        scanner.nextLine();
    }

    private static String inputGrade() {
        System.out.print("[+] Grade (A-F): ");
        String grade = scanner.next();
        if (!grade.matches("[A-F]")) {
            System.out.println("[!] Invalid grade. Please enter A, B, C, D, or F.");
            return null;
        }
        return grade;
    }

    private static boolean validateScore(int score) {
        if (score < 0 || score > 100) {
            System.out.println("[!] Invalid score. Must be between 0 and 100.");
            return false;
        }
        return true;
    }

    private static boolean validateAge(int age) {
        if (age <= 0) {
            System.out.println("[!] Invalid age. Must be positive.");
            return false;
        }
        return true;
    }

    private static boolean validateEmail(String email) {
        if (!email.matches("^\\S+@\\S+\\.\\S+$")) {
            System.out.println("[!] Invalid email format.");
            return false;
        }
        return true;
    }

    public static void home() {
        while (true) {
            thumbnail();
            switch (option()) {
                case 1 -> { // Add Student
                    System.out.println("Add Student");

                    System.out.print("[+] Name: ");
                    scanner.nextLine();
                    String name = scanner.nextLine();

                    System.out.print("[+] Age: ");
                    int age = scanner.nextInt();
                    if (!validateAge(age)) break;

                    System.out.print("[+] Email: ");
                    String email = scanner.next();
                    if (!validateEmail(email)) break;

                    String grade = inputGrade();
                    if (grade == null) break;

                    System.out.print("[+] Score (0-100): ");
                    int score = scanner.nextInt();
                    if (!validateScore(score)) break;

                    Student student = new Student(null, name, age, email, grade, score);
                    Student saved = repo.saveStudent(student);
                    if (saved != null) {
                        System.out.println("[✓] Student added successfully!");
                    } else {
                        System.out.println("[!] Failed to add student.");
                    }
                    pressEnterToContinue();
                }
                case 2 -> { // View All
                    List<Student> students = repo.findAllStudents();
                    if (students.isEmpty()) {
                        System.out.println("[!] No students found.");
                    } else {
                        System.out.println("All Students:");
                        students.forEach(System.out::println);
                    }
                    pressEnterToContinue();
                }
                case 3 -> { // Update
                    System.out.print("[+] Student ID to update: ");
                    int id = scanner.nextInt();
                    Student existing = repo.findStudentById(id);
                    if (existing == null) {
                        System.out.println("[!] Student ID not found.");
                        pressEnterToContinue();
                        break;
                    }

                    System.out.print("[+] New Name: ");
                    scanner.nextLine();
                    String name = scanner.nextLine();

                    System.out.print("[+] New Age: ");
                    int age = scanner.nextInt();
                    if (!validateAge(age)) break;

                    System.out.print("[+] New Email: ");
                    String email = scanner.next();
                    if (!validateEmail(email)) break;

                    String grade = inputGrade();
                    if (grade == null) break;

                    System.out.print("[+] New Score: ");
                    int score = scanner.nextInt();
                    if (!validateScore(score)) break;

                    StudentUpdateDto updateDto = StudentUpdateDto.builder()
                            .name(name).age(age).email(email).grade(grade).score(score).build();

                    Student updated = repo.updateStudent(id, updateDto);
                    if (updated != null) {
                        System.out.println("[✓] Student updated successfully!");
                    } else {
                        System.out.println("[!] Failed to update student.");
                    }
                    pressEnterToContinue();
                }
                case 4 -> { // Delete
                    System.out.print("[+] Student ID to delete: ");
                    int id = scanner.nextInt();
                    int deleted = repo.deleteStudent(id);
                    if (deleted > 0) {
                        System.out.println("[✓] Student deleted successfully!");
                    } else {
                        System.out.println("[!] Student ID not found.");
                    }
                    pressEnterToContinue();
                }
                case 5 -> { // Search by grade
                    String grade = inputGrade();
                    if (grade == null) break;

                    List<Student> studentsByGrade = repo.findStudentsByGrade(grade);
                    if (studentsByGrade.isEmpty()) {
                        System.out.println("[!] No students found with grade " + grade);
                    } else {
                        System.out.println("Students with grade " + grade + ":");
                        studentsByGrade.forEach(System.out::println);
                    }
                    pressEnterToContinue();
                }
                case 6 -> { // Top-Performing
                    List<Student> top = repo.topPerformingStudents();
                    if (top.isEmpty()) {
                        System.out.println("[!] No top-performing students found.");
                    } else {
                        System.out.println("Top-Performing Students (score >= 90):");
                        top.forEach(System.out::println);
                    }
                    pressEnterToContinue();
                }
                case 7 -> { // Average
                    double avg = repo.calculateAverageScore();
                    System.out.println("[✓] Average Score of all students: " + avg);
                    pressEnterToContinue();
                }
                case 8 -> { // Bulk insert
                    System.out.print("[+] How many students to insert in bulk? ");
                    int n = scanner.nextInt();
                    List<Student> bulkStudents = new ArrayList<>();

                    for (int i = 0; i < n; i++) {
                        System.out.println("Student " + (i + 1));

                        System.out.print("[+] Name: ");
                        scanner.nextLine();
                        String name = scanner.nextLine();

                        System.out.print("[+] Age: ");
                        int age = scanner.nextInt();
                        if (!validateAge(age)) break;

                        System.out.print("[+] Email: ");
                        String email = scanner.next();
                        if (!validateEmail(email)) break;

                        String grade = inputGrade();
                        if (grade == null) break;

                        System.out.print("[+] Score: ");
                        int score = scanner.nextInt();
                        if (!validateScore(score)) break;

                        bulkStudents.add(new Student(null, name, age, email, grade, score));
                    }

                    repo.bulkInsertStudents(bulkStudents);
                    System.out.println("[✓] Bulk insert completed!");
                    pressEnterToContinue();
                }
                case 9 -> {
                    System.out.println("        SYSTEM EXIT..       ");
                    System.exit(0);
                }
                default -> {
                    System.out.println("[!] Invalid option :(");
                    pressEnterToContinue();
                }
            }
        }
    }

    public static void main(String[] args) {
        home();
    }
}
