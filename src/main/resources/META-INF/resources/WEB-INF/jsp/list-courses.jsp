<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Course List</title>
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
    <h2>Course List</h2>

    <a href="/courses/new" class="button">Add New Course</a>
    <a href="/students" class="button">View Students</a>

    <table>
        <thead>
            <tr>
                <th>ID</th>
                <th>Title</th>
                <th>Description</th>
                <th>Enrolled Students</th>
                <th>Actions</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="course" items="${courses}">
                <tr>
                    <td>${course.id}</td>
                    <td>${course.title}</td>
                    <td>${course.description}</td>
                    <td>
                        <c:forEach var="student" items="${course.students}">
                            ${student.name}<br/>
                        </c:forEach>
                    </td>
                    <td>
                        <a href="/courses/edit/${course.id}" class="button edit">Edit</a>
                        <a href="/courses/delete/${course.id}" class="button delete" onclick="return confirm('Are you sure you want to delete this course?');">Delete</a>
                    </td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
</body>
</html>
