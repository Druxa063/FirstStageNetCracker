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
<button onclick="openModal()">Add Hero</button>
<div class="form-style-2">
    <label for="nameHero">
        <span>Name Hero </span>
        <input class="input-field" type="text" id="nameHero" name="nameHero" onkeyup="searchWithoutAjax(this.value)" placeholder="Enter name hero for search">
        <button onclick="search()">Search</button>
    </label>
</div>
<div class="form-style-2">
    <label>
        <span>Sort name/power </span>
        <button onclick="sortNameOrPower('name')">Name</button>
        <button onclick="sortNameOrPower('power')">Power</button>
    </label>
</div>
<div class="form-style-2">
    <label>
        <span>Sort name/power JS</span>
        <button onclick="sortNameJS()">Name</button>
        <button onclick="sortPowerJS()">Power</button>
    </label>
</div>
<table id="tableHero" border="1">
    <thead>
    <th>Name</th>
    <th>Universe</th>
    <th>Power</th>
    <th>Description</th>
    <th>Alive</th>
    <th>Logo</th>
    <th>Delete</th>
    <th>Update</th>
    </thead>
    <tbody id="tableHeroBody">
    </tbody>
</table>
<div id="deleteModal" class="modal">
    <div class="modal-content-delete">
        <p>Are you sure?</p>
        <button id="deleteBtnModal" class="buttonDelete">Delete</button>
        <button id="cancelBtnModal" class="buttonDelete">Cancel</button>
    </div>
</div>
<div id="saveModal" class="modal">
    <div class="modal-content">
        <div class="form-style-2">
            <form name="saveForm">
                <input type="hidden" name="id" value="${hero.id}">
                <label for="name"><span>Name <span class="required">*</span></span><input type="text" class="input-field" name="name" value="${hero.name}"></label>
                <label><p id="matches"></p></label>
                <label for="universe"><span>Universe <span class="required">*</span></span><input type="text" class="input-field" name="universe" value="${hero.universe}"></label>
                <label for="power"><span>Power <span class="required">*</span></span><input type="number" class="input-field" name="power" value="${hero.power}" min="0" max="100"></label>
                <label for="description"><span>Description <span class="required">*</span></span><textarea name="description" class="textarea-field"></textarea></label>
                <label for="alive"><span>Alive </span><input type="radio" class="input-field" name="alive" value="true" checked></label>
                <label for="alive"><span>Dead </span><input type="radio" class="input-field" name="alive" value="false"></label>
                <label for="logo"><span>Logo </span><input type="file" class="input-field" name="logo"></label>
            </form>
            <label><span> </span>
                <button id="saveBtnModal" class="button" onclick="validationSaveForm()">Save</button>
                <button id="closeBtnModal" class="button">Close</button>
            </label>

        </div>
    </div>
</div>
<div id="validModal" class="modal">
    <div class="modal-content-validation">
        <span id="closeBtn" class="closeBtn">&times;</span>
        <p id="validText"></p>
    </div>
</div>
</body>
</html>
