<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Hero Form</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
<h3><a href="/heroes">Home</a></h3>
<div class="form-style-2-heading">${param.action == 'create' ? 'Create hero' : 'Edit hero'}</div>
<div class="form-style-2">
    <jsp:useBean id="hero" type="model.Hero" scope="request"/>
    <form method="post" action="/heroes">
        <input type="hidden" name="id" value="${hero.id}">
        <label for="name"><span>Name <span class="required">*</span></span><input type="text" class="input-field" name="name" value="${hero.name}" required></label>
        <label for="universe"><span>Universe <span class="required">*</span></span><input type="text" class="input-field" name="universe" value="${hero.universe}" required></label>
        <label for="power"><span>Power <span class="required">*</span></span><input type="number" class="input-field" name="power" value="${hero.power}" min="0" max="100"></label>
        <label for="description"><span>Description <span class="required">*</span></span><input type="text" class="input-field" name="description" value="${hero.description}" required></label>
        <label for="alive"><span>Alive </span><input type="radio" class="input-field" name="alive" value="true" checked></label>
        <label for="alive"><span>Dead </span><input type="radio" class="input-field" name="alive" value="false"></label>
        <label><span> </span><button type="submit">Save</button></label>
    </form>
</div>
</body>
</html>
