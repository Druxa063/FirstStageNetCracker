function loadTable() {
    var xmlhttp, json;
    xmlhttp = new XMLHttpRequest();
    xmlhttp.onreadystatechange = function() {
        if (this.readyState == 4 && this.status == 200) {
            json = JSON.parse(this.responseText);
            paint(json);
        }
    };
    xmlhttp.open("GET", "heroes?action=all", true);
    xmlhttp.send();
}

function paint(json) {
    var table, x, txt="", tr;
    table = document.getElementById("tableHeroBody");
    table.innerHTML = "";
    for (x in json) {
        tr = document.createElement("tr");
        var tdName = document.createElement("td");
        tdName.innerText = json[x].name;
        tr.appendChild(tdName);
        var tdUniverse = document.createElement("td");
        tdUniverse.innerText = json[x].universe;
        tr.appendChild(tdUniverse);
        var tdPower = document.createElement("td");
        tdPower.innerText = json[x].power;
        tr.appendChild(tdPower);
        var tdDescription = document.createElement("td");
        tdDescription.innerText = json[x].description;
        tr.appendChild(tdDescription);
        var tdAlive = document.createElement("td");
        tdAlive.innerText = json[x].alive;
        tr.appendChild(tdAlive);
        var tdPhone = document.createElement("td");
        tdPhone.innerText = json[x].phone;
        tr.appendChild(tdPhone);
        var tdLogo = document.createElement("td");
        if (json[x].logo.length != 0) {
            var img = document.createElement("img");
            img.src = atob(json[x].logo);
            img.width = 40;
            img.height = 40;
            tdLogo.appendChild(img);
        }
        tr.appendChild(tdLogo);
        var tdDelete = document.createElement("td");
        var deleteBtn = document.createElement("button");
        deleteBtn.setAttribute("id", "deleteBtn");
        deleteBtn.setAttribute("onclick", "deleteRow(" + json[x].id + ")");
        deleteBtn.innerText = "Delete";
        tdDelete.appendChild(deleteBtn);
        tr.appendChild(tdDelete);
        var tdUpdate = document.createElement("td");
        var updateBtn = document.createElement("button");
        updateBtn.setAttribute("id", "updateBtn");
        updateBtn.setAttribute("onclick", "updateRow(" + json[x].id + ")");
        updateBtn.innerText = "Update";
        tdUpdate.appendChild(updateBtn);
        tr.appendChild(tdUpdate);
        tr.setAttribute("data-heroAlive", json[x].alive);
        tr.hidden = json[x].hasOwnProperty("hiddenRow") ? json[x].hiddenRow : false;
        table.appendChild(tr);
    }
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
            form["phone"].value = json.phone;
            form["logo"].value = "";
            modal.style.display = "block";
        }
    };
    xmlhttp.open("GET", "heroes?action=update&id=" + id, true);
    xmlhttp.send();

    closeBtnModal.onclick = function () {
        modal.style.display = "none";
    };
    window.onclick = function (ev) {
        if (ev.target == modal) {
            modal.style.display = "none";
        }
    };
}

function save(id, name, universe, power, description, alive, phone, logo) {
    var modal = document.getElementById('saveModal');
    var xmlhttp = new XMLHttpRequest();
    xmlhttp.onreadystatechange = function () {
        if (this.readyState == 4 && this.status == 200) {
            modal.style.display = "none";
            loadTable();
        }
        if (this.readyState == 4 && this.status == 400) {
            errorPrint(this.responseText)
        }
    };
    phone = encodeURIComponent(phone);
    if (logo !== undefined) {
        var reader = new FileReader();
        var base64;
        reader.onloadend = function () {
            base64 = reader.result;
            xmlhttp.open("POST", "heroes", true);
            xmlhttp.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
            xmlhttp.send("id=" + id + "&" + "name=" + name + "&" + "universe=" + universe + "&" + "power=" + power + "&" + "description=" + description + "&" + "alive=" + alive + "&" + "phone=" + phone + "&" + "logo=" + btoa(base64));
        };
        reader.readAsDataURL(logo);
    } else {
        xmlhttp.open("POST", "heroes", true);
        xmlhttp.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
        xmlhttp.send("id=" + id + "&" + "name=" + name + "&" + "universe=" + universe + "&" + "power=" + power + "&" + "description=" + description + "&" + "alive=" + alive + "&" + "phone=" + phone);
    }
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
    form["id"].value = "";
    form["power"].value = 0;
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

function search() {
    var xmlhttp, json, nameHero;
    nameHero = document.getElementById("nameHero");
    xmlhttp = new XMLHttpRequest();
    xmlhttp.onreadystatechange = function() {
        if (this.readyState == 4 && this.status == 200) {
            json = JSON.parse(this.responseText);
            paint(json);
        }
    };
    xmlhttp.open("GET", "heroes?action=find&nameHero=" + nameHero.value + "&matches=false", true);
    xmlhttp.send();
}

function searchWithoutAjax(value) {
    var table = document.getElementById("tableHeroBody");
    var x;
    var childNodes = table.childNodes;
    for (x = 0; x < childNodes.length; x++) {
        if (childNodes[x].childNodes[0].childNodes[0].nodeValue.toLowerCase().indexOf(value.toLowerCase()) !== -1) {
           childNodes[x].hidden = false;
        } else {
            childNodes[x].hidden = true;
        }
    }
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
            if (json.length == 0) {
                pMatches.innerText = "";
                return;
            }
            pMatches.innerText = "Hero exists already";
        }

    };
    xmlhttp.open("GET", "heroes?action=find&nameHero=" + value + "&matches=true", true);
    xmlhttp.send();
}

function validationSaveForm() {
    var form, id, name, universe, power, description, alive, phone, logo;
    form = document.forms["saveForm"];
    id = form["id"].value;
    name = form["name"].value;
    universe = form["universe"].value;
    power = form["power"].value;
    description = form["description"].value;
    alive = form["alive"].value;
    phone = form["phone"].value;
    logo = form["logo"].files[0];
    if (name.length == 0) {
        errorPrint("The name must not be empty");
        return;
    }
    if (document.getElementById("matches").innerText.length != 0) {
        errorPrint("Hero with the same name already exists");
        return;
    }
    if (name.length > 30) {
        errorPrint("The name must not be more than 30 characters");
        return;
    }
    if (power < 0 || power > 100 || power == "") {
        errorPrint("The power must not be less than 0 and greater than 100");
        return;
    }
    if (phone.length != 0) {
        if (!/[+]7-\d{3}-\d{3}-\d{2}-\d{2}/.test(phone)) {
            errorPrint("The number phone no correct");
            return;
        }
    }
    save(id, name, universe, power, description, alive, phone, logo);
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
}

function sortNameOrPower(sortParam) {
    var xmlhttp, json, nameHero;
    nameHero = document.getElementById("nameHero");
    xmlhttp = new XMLHttpRequest();
    xmlhttp.onreadystatechange = function() {
        if (this.readyState == 4 && this.status == 200) {
            json = JSON.parse(this.responseText);
            paint(json);
        }
    };
    xmlhttp.open("GET", "heroes?action=sort&sortParam=" + sortParam + "&nameHero=" + nameHero.value, true);
    xmlhttp.send();
}

function sortNameJS() {
    var table = document.getElementById("tableHeroBody");
    var rows = table.rows;
    rows = Array.prototype.slice.call(rows);
    rows.sort(function (a, b) {
        var x = a.cells[0].innerText.toLowerCase();
        var y = b.cells[0].innerText.toLowerCase();
        if (x < y) {return -1;}
        if (x > y) {return 1;}
        return 0;
    });
    for (var x=0; x < rows.length; x++) {
        table.appendChild(rows[x]);
    }
}
function sortPowerJS() {
    var table = document.getElementById("tableHeroBody");
    var rows = table.rows;
    rows = Array.prototype.slice.call(rows);
    rows.sort(function (a, b) {
        var x = a.cells[2].innerText;
        var y = b.cells[2].innerText;
        if (x < y) {return -1;}
        if (x > y) {return 1;}
        return 0;
    });
    for (var x=0; x < rows.length; x++) {
        table.appendChild(rows[x]);
    }
}
