var stompClient = null;

function joinChat() {
    let username = $("#username").val();
    sessionStorage.setItem("username", username);
    $.ajax({
      type: "POST",
      url: "/users",
      data: JSON.stringify({"name": username}),
      contentType: "application/json",
      success: function (result) {
        console.log(result);
        location.href = "/chat.html";
      },
      error: function (result, status) {
        console.log(result);
        console.log(status);
        console.log(result.responseText);
        alert(result.responseText);
      }
    });
}

function leaveChat() {
    stompClient.send('/app/users/left', {}, JSON.stringify({'name': getUsername()}));
    disconnect();
    location.href = "/";
}

function prepareChat() {
    if (getUsername() == null) {
        location.href = "/";
    }
    connect();
    showMessages();
    showUsers();
}

function connect() {
    var socket = new SockJS('/ws');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/messages', function (message) {
            console.log(message)
            showMessage(JSON.parse(message.body));
        });
        stompClient.subscribe('/topic/users/join', function (user) {
            console.log(user);
            showOnlineUser(JSON.parse(user.body));
        });
        stompClient.subscribe('/topic/users/left', function (user) {
            console.log(user);
            showOfflineUser(JSON.parse(user.body));
        });

        //stompClient.send('/app/users/join', {}, JSON.stringify({'name': getUsername()}));
    });
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    console.log("Disconnected");
}

function sendMessage() {
    let message = $("#message").val();
    $("#message").val('');
    stompClient.send('/app/chat', {}, JSON.stringify({'username': getUsername(), 'text': message}));
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
            showOnlineUser(user);
        }
    });
}

function showMessage(message) {
    if (message.username == getUsername()) {
        showMyMessage(message)
    } else {
        showOtherMessage(message)
    }
}

function showOtherMessage(message) {
    var ul = document.getElementById("messages");
    ul.innerHTML += "<li class=\"clearfix\"><div class=\"message-data text-right\">"
    + "<span class=\"message-data-user\">" + message.username + ", </span>"
    + "<span class=\"message-data-time\">" + message.time + "</span></div>"
    + "<div class=\"message other-message float-right\"> "+ message.text + " </div></li>";
}

function showMyMessage(message) {
    var ul = document.getElementById("messages");
    ul.innerHTML += "<li class=\"clearfix\"><div class=\"message-data\">"
    + "<span class=\"message-data-user\">You, </span>"
    + "<span class=\"message-data-time\">" + message.time + "</span></div>"
    + "<div class=\"message my-message\">" + message.text + "</div></li>";
}

function showOnlineUser(user) {
    var changed = changeUserStatus(user, 'offline', 'online');
    if (!changed) {
        var ul = document.getElementById("users");
        var cssClass = "clearfix";
        if (user.name == getUsername()) {
            cssClass += " active";
        }
        ul.innerHTML += "<li class=\"" + cssClass + "\"><div class=\"about\">"
        + "<div class=\"name\">" + user.name + "</div>"
        + "<div class=\"status\"> <i class=\"fa fa-circle online\"></i> online </div></div></li>"
    }
}

function showOfflineUser(user) {
    changeUserStatus(user, 'online', 'offline');
}

function changeUserStatus(user, oldStatus, newStatus) {
    var ul = document.getElementById("users");
    var items = ul.getElementsByTagName("li");
    for (var i = 0; i < items.length; ++i) {
      var userElem = items[i].querySelector(".about");
      if (userElem.querySelector(".name").innerText == user.name) {
        var statusElem = userElem.querySelector(".status");
        statusElem.innerHTML = "<i class=\"fa fa-circle " + newStatus + "\"></i> " + newStatus;
        if (user.name == getUsername()) {
            items[i].className += " active";
        }
        return true;
      }
    }
    return false;
}

function getUsername() {
    return sessionStorage.getItem("username");
}

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $( "#send" ).click(function() { sendMessage(); });
    $( "#join-chat" ).click(function() { joinChat(); });
    $( "#leave-chat" ).click(function() { leaveChat(); });
});

