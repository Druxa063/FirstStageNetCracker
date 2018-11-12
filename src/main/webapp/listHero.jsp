<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<html>
<head>
    <title>List hero</title>
    <link rel="stylesheet" href="css/style.css">
    <link rel="stylesheet" href="css/modal.css">
    <script type="text/javascript" src="js/util.js"></script>
</head>
<body onload="loadTable()">
<button onclick="openModal(null, 'create')">Add Hero</button>
<div class="form-style-2">
<form method="get" action="heroes">
    <input type="hidden" name="action" value="find">
    <label for="nameHero"><span>Name Hero </span><input class="input-field" type="text" name="nameHero"></label>
    <label><span> </span><button type="submit">Search</button></label>
</form>
</div>
<table id="tableHero" border="1">
    <thead>
    <th>Name</th>
    <th>Universe</th>
    <th>Power</th>
    <th>Description</th>
    <th>Alive</th>
    <th>Delete</th>
    <th>Update</th>
    </thead>
    <tbody id="tableHeroBody">
    </tbody>
</table>
<div id="deleteModal" class="modal">
    <div class="modal-content">
        <button id="deleteBtnModal">Delete</button>
        <button id="cancelBtnModal">Cancel</button>
    </div>
</div>
<div id="saveModal" class="modal">
    <div class="modal-content">
        <div class="form-style-2">
            <form name="saveForm">
                <input type="hidden" name="id" value="${hero.id}">
                <label for="name"><span>Name <span class="required">*</span></span><input type="text" class="input-field" name="name" value="${hero.name}" maxlength="30" required></label>
                <label for="universe"><span>Universe <span class="required">*</span></span><input type="text" class="input-field" name="universe" value="${hero.universe}" required></label>
                <label for="power"><span>Power <span class="required">*</span></span><input type="number" class="input-field" name="power" value="${hero.power}" min="0" max="100"></label>
                <label for="description"><span>Description <span class="required">*</span></span><input type="text" class="input-field" name="description" value="${hero.description}" required></label>
                <label for="alive"><span>Alive </span><input type="radio" class="input-field" name="alive" value="true" checked></label>
                <label for="alive"><span>Dead </span><input type="radio" class="input-field" name="alive" value="false"></label>
            </form>
            <label><span> </span><button id="saveBtnModal" onclick="save()">Save</button></label>
            <label><span> </span><button id="closeBtnModal">Close</button></label>
        </div>
    </div>
</div>
</body>
</html>
