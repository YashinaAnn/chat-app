var stompClient = null;

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    }
    $("#messages").html("");
}

function connect() {
    var socket = new SockJS('/ws');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/messages', function (message) {
            console.log(message)
            showMessage(JSON.parse(message.body));
        });
        stompClient.subscribe('/topic/users/join', function (user) {
            showOnlineUser(JSON.parse(user.body));
        });
        stompClient.subscribe('/topic/users/left', function (user) {
            showOfflineUser(JSON.parse(user.body));
        });

        stompClient.send('/app/users/join', {},
                    JSON.stringify({'name': getUsername(), 'email': getUsername()+"@test.com"}));

        showUsers();
        showMessages();
    });
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

function sendMessage() {
    let message = $("#message").val();
    $("#message").val('');
    stompClient.send('/app/chat', {}, JSON.stringify(
        {'userName': getUsername(), 'userEmail': getUsername() + '@test.com', 'text': message}));
}

function showMessages() {
    $.getJSON('/messages', function(result) {
        console.log(result);
        for (var message of result.reverse()) {
           showMessage(message);
        }
    });
}

function showUsers() {
    $.getJSON('/users/active', function(result) {
        for (var user of result) {
            if (user.name == getUsername()) continue;
            showOnlineUser(user);
        }
    });
}

function removeUser(user) {
    var index = null;
    var table = document.getElementById("users");
    for (var i = 0, row; row = table.rows[i]; i++) {
        if (row.cells[0].innerText == user.name) {
            index = i;
            break;
        }
    }
    table.deleteRow(index);
}

function joinChat() {
    let username = $("#username").val();
    sessionStorage.setItem("username", username);
    location.href = "/chat.html";
}

function leaveChat() {
    stompClient.send('/app/users/left', {},
        JSON.stringify({'name': getUsername(), 'email': getUsername()+"@test.com"}));
    disconnect();
    location.href = "/";
}

function getUsername() {
    return sessionStorage.getItem("username");
}

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $( "#connect" ).click(function() { connect(); });
    $( "#disconnect" ).click(function() { disconnect(); });
    $( "#send" ).click(function() { sendMessage(); });
    $( "#join-chat" ).click(function() { joinChat(); });
    $( "#leave-chat" ).click(function() { leaveChat(); });
});

function showMessage(message) {
    if (message.userName == getUsername()) {
        showMyMessage(message)
    } else {
        showOtherMessage(message)
    }
}

function showOtherMessage(message) {
    var ul = document.getElementById("messages");
    ul.innerHTML += "<li class=\"clearfix\"><div class=\"message-data text-right\">"
    + "<span class=\"message-data-user\">" + message.userName + ", </span>"
    + "<span class=\"message-data-time\">" + message.time + "</span></div>"
    + "<div class=\"message other-message float-right\"> "+ message.text + " </div></li>";
}

function showMyMessage(message) {
    var ul = document.getElementById("messages");
    ul.innerHTML += "<li class=\"clearfix\"><div class=\"message-data\">"
    + "<span class=\"message-data-time\">" + message.time + "</span>"
    + "</div><div class=\"message my-message\">" + message.text + "</div></li>";
}

function showOnlineUser(user) {
    var changed = changeUserStatus(user, 'offline', 'online');
    if (!changed) {
        var ul = document.getElementById("users");
        ul.innerHTML += "<li class=\"clearfix active\"><div class=\"about\">"
        + "<div class=\"name\">" + user.name + "</div>"
        + "<div class=\"status\"> <i class=\"fa fa-circle online\"></i> online </div></div></li>"
    }
}

function showOfflineUser(user) {
    changeUserStatus(user, 'online', 'offline');
}

function changeUserStatus(user, oldStatus, newStatus) {
    console.log("offline----")
    var ul = document.getElementById("users");
    var items = ul.getElementsByTagName("li");
    for (var i = 0; i < items.length; ++i) {
      var userElem = items[i].querySelector(".about");
      if (userElem.querySelector(".name").innerText == user.name) {
        console.log("name" + userElem.querySelector(".name").innerText);
        var statusElem = userElem.querySelector(".status");
        statusElem.innerHTML = "<i class=\"fa fa-circle " + newStatus + "\"></i>" + newStatus;
        return true;
      }
    }
    return false;
}

