<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<html>
<head>
    <title>List hero</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
<a href="heroes?action=create">Add Hero</a>
<form method="get" action="heroes">
    <input type="hidden" name="action" value="find">
    Name Hero : <input type="text" name="nameHero">
    <input type="submit" value="Search">
</form>
<table border="1">
    <thead>
    <th>Name</th>
    <th>Universe</th>
    <th>Power</th>
    <th>Description</th>
    <th>Alive</th>
    <th>Delete</th>
    <th>Update</th>
    </thead>
    <tbody>
    <c:forEach items="${heroes}" var="hero">
        <jsp:useBean id="hero" scope="page" type="model.Hero"/>
        <tr data-heroAlive="${hero.alive}">
            <td>${hero.name}</td>
            <td>${hero.universe}</td>
            <td>${hero.power}</td>
            <td>${hero.description}</td>
            <td>${hero.alive}</td>
            <td><a href="heroes?action=delete&id=${hero.id}">Delete</a></td>
            <td><a href="heroes?action=update&id=${hero.id}">Update</a></td>
        </tr>
    </c:forEach>
    </tbody>
</table>
</body>
</html>
