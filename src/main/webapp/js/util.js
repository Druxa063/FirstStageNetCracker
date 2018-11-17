function loadTable() {
    var xmlhttp, json;
    xmlhttp = new XMLHttpRequest();
    xmlhttp.onreadystatechange = function() {
        if (this.readyState == 4 && this.status == 200) {
            json = JSON.parse(this.responseText);
            paint(json);
        }
    };
    xmlhttp.open("GET", "heroes?action=ajax", true);
    xmlhttp.send();
}

function paint(json) {
    var table, x, txt="";
    for (x in json) {
        txt += "<tr data-heroAlive=\"" + json[x].alive + "\"><td>" +
                json[x].name +
            "</td><td>" +
                json[x].universe +
            "</td><td>" +
                json[x].power +
            "</td><td>" +
                json[x].description +
            "</td><td>" +
                json[x].alive +
            "</td><td>" +
                "<button id='deleteBtn' onclick='deleteRow(" + json[x].id + ")'>Delete</button>" +
            "</td><td>" +
                "<button id='updateBtn' onclick='updateRow(" + json[x].id + ")'>Update</button>" +
            "</td></tr>";
    }
    document.getElementById("tableHeroBody").innerHTML = txt;
}

function deleteRow(id) {
    var modal = document.getElementById('deleteModal');
    var deleteBtnModal = document.getElementById('deleteBtnModal');
    var cancelBtnModal = document.getElementById('cancelBtnModal');
    modal.style.display = "block";
    deleteBtnModal.onclick = function() {
        var xmlhttp = new XMLHttpRequest();
        xmlhttp.onreadystatechange = function () {
            if (this.readyState == 4 && this.status == 200) {
                modal.style.display = "none";
                loadTable();
            }
        };
        xmlhttp.open("GET", "heroes?action=delete&id=" + id, true);
        xmlhttp.send();
    };
    cancelBtnModal.onclick = function () {
        modal.style.display = "none";
    };
    window.onclick = function (ev) {
        if (ev.target == modal) {
            modal.style.display = "none";
        }
    };
}

function updateRow(id) {
    var json;
    var modal = document.getElementById('saveModal');
    var saveBtnModal = document.getElementById('saveBtnModal');
    var closeBtnModal = document.getElementById('closeBtnModal');
    var pMatches = document.getElementById("matches");
    pMatches.innerText = "";
    var xmlhttp = new XMLHttpRequest();
    xmlhttp.onreadystatechange = function() {
        if (this.readyState == 4 && this.status == 200) {
            json = JSON.parse(this.responseText);
            var form = document.forms["saveForm"];
            form["id"].value = json.id;
            form["name"].value = json.name;
            form["name"].readOnly = true;
            form["name"].removeAttribute("onkeyup");
            form["universe"].value = json.universe;
            form["power"].value = json.power;
            form["description"].value = json.description;
            form["alive"].value = json.alive;
        }
    };
    xmlhttp.open("GET", "heroes?action=update&id=" + id, true);
    xmlhttp.send();
    modal.style.display = "block";

    closeBtnModal.onclick = function () {
        modal.style.display = "none";
    };
    window.onclick = function (ev) {
        if (ev.target == modal) {
            modal.style.display = "none";
        }
    };
}

function save(id, name, universe, power, description, alive) {
    var modal = document.getElementById('saveModal');
    var xmlhttp = new XMLHttpRequest();
    xmlhttp.onreadystatechange = function () {
        if (this.readyState == 4 && this.status == 200) {
            alert("ok");
            modal.style.display = "none";
            loadTable();
        }
    };
    xmlhttp.open("POST", "heroes", true);
    xmlhttp.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
    xmlhttp.send("id=" + id + "&" + "name=" + name + "&" + "universe=" + universe + "&" + "power=" + power + "&" + "description=" + description + "&" + "alive=" + alive);
}

function openModal() {
    var modal = document.getElementById('saveModal');
    var saveBtnModal = document.getElementById('saveBtnModal');
    var closeBtnModal = document.getElementById('closeBtnModal');
    var pMatches = document.getElementById("matches");

    var form = document.forms["saveForm"];
    form["name"].removeAttribute("readonly");
    form["name"].setAttribute("onkeyup", "matchesByName(this.value)");
    pMatches.innerText = "";
    form.reset();
    modal.style.display = "block";

    closeBtnModal.onclick = function () {
        modal.style.display = "none";
    };
    window.onclick = function (ev) {
        if (ev.target == modal) {
            modal.style.display = "none";
        }
    };
}

function search(value) {
    var xmlhttp, json;
    xmlhttp = new XMLHttpRequest();
    xmlhttp.onreadystatechange = function() {
        if (this.readyState == 4 && this.status == 200) {
            json = JSON.parse(this.responseText);
            paint(json);
        }
    };
    xmlhttp.open("GET", "heroes?action=find&nameHero=" + value + "&matches=false", true);
    xmlhttp.send();
}

function matchesByName(value) {
    var xmlhttp, json, txt = "";
    var pMatches = document.getElementById("matches");
    if (value.length == 0) {
        pMatches.innerText = "";
        return;
    }
    xmlhttp = new XMLHttpRequest();
    xmlhttp.onreadystatechange = function () {
        if (this.readyState == 4 && this.status == 200) {
            json = JSON.parse(this.responseText);
            var x;
            for (x = 0; x < json.length; x++) {
                txt = txt.concat(", ", json[x].name);
            }
            if (txt.length == 0) {
                pMatches.innerText = "";
                return;
            }
            pMatches.innerText = "Hero exists already: " + txt.substring(2);
        }

    };
    xmlhttp.open("GET", "heroes?action=find&nameHero=" + value + "&matches=true", true);
    xmlhttp.send();
}

function validationSaveForm() {
    var form, id, name, universe, power, description, alive;
    form = document.forms["saveForm"];
    id = form["id"].value;
    name = form["name"].value;
    universe = form["universe"].value;
    power = form["power"].value;
    description = form["description"].value;
    alive = form["alive"].value;
    if (document.getElementById("matches").innerText.indexOf(name) !== -1) {
        errorPrint("Hero with the same name already exists");
        return;
    }
    if (name.length > 30) {
        errorPrint("The name should not be more than 30 characters");
        return;
    }
    if (power < 0 || power > 100) {
        errorPrint("The power should not be less than 0 and greater than 100");
        return;
    }
    save(id, name, universe, power, description, alive);
}

function errorPrint(errorText) {
    var modal = document.getElementById("validModal");
    var closeBtn = document.getElementById("closeBtn");
    var validText = document.getElementById("validText");

    validText.innerText = errorText;
    modal.style.display = "block";

    closeBtn.onclick = function () {
        modal.style.display = "none";
    };
    // window.onclick = function (ev) {
    //     if (ev.target == modal) {
    //         modal.style.display = "none";
    //     }
    // };
}
