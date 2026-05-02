<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Update Course</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; }
        h2 { color: #333; }
        form { margin-top: 20px; }
        label { display: block; margin-bottom: 5px; font-weight: bold; }
        input[type="text"], textarea {
            width: 300px;
            padding: 8px;
            margin-bottom: 10px;
            border: 1px solid #ddd;
            border-radius: 4px;
        }
        textarea {
            resize: vertical;
            min-height: 80px;
        }
        input[type="submit"] {
            background-color: #007bff;
            color: white;
            padding: 10px 20px;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            font-size: 16px;
        }
        input[type="submit"]:hover {
            background-color: #0056b3;
        }
        .back-link {
            display: inline-block;
            margin-top: 20px;
            color: #007bff;
            text-decoration: none;
        }
        .back-link:hover {
            text-decoration: underline;
        }
    </style>
</head>
<body>
    <h2>Update Course</h2>

    <form action="/courses/update/${course.id}" method="post">
        <input type="hidden" name="id" value="${course.id}">

        <label for="title">Title:</label>
        <input type="text" id="title" name="title" value="${course.title}" required><br>

        <label for="description">Description:</label>
        <textarea id="description" name="description" required>${course.description}</textarea><br>

        <input type="submit" value="Update Course">
    </form>

    <a href="/courses" class="back-link">Back to Course List</a>
</body>
</html>
