<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Student List</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; }
        h2 { color: #333; }
        table { width: 100%; border-collapse: collapse; margin-top: 20px; }
        th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }
        th { background-color: #f2f2f2; }
        .button {
            display: inline-block;
            padding: 8px 15px;
            margin-top: 10px;
            background-color: #007bff;
            color: white;
            text-decoration: none;
            border-radius: 5px;
        }
        .button.edit { background-color: #ffc107; }
        .button.delete { background-color: #dc3545; }
        .button:hover { opacity: 0.9; }
    </style>
</head>
<body>
    <h2>Student List</h2>

    <a href="/students/new" class="button">Add New Student</a>
    <a href="/courses" class="button">View Courses</a>

    <table>
        <thead>
            <tr>
                <th>ID</th>
                <th>Name</th>
                <th>Email</th>
                <th>Enrolled Courses</th>
                <th>Actions</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="student" items="${students}">
                <tr>
                    <td>${student.id}</td>
                    <td>${student.name}</td>
                    <td>${student.email}</td>
                    <td>
                        <c:forEach var="course" items="${student.courses}">
                            ${course.title}<br/>
                        </c:forEach>
                    </td>
                    <td>
                        <a href="/students/edit/${student.id}" class="button edit">Edit</a>
                        <a href="/students/delete/${student.id}" class="button delete" onclick="return confirm('Are you sure you want to delete this student?');">Delete</a>
                    </td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
</body>
</html>
