var TPS = 10;
var TL = 1000 / TPS;
var term; // Can't be initialized yet because DOM is not ready
var tl = {};
var cookie;
var keypresses = [];

// Technically you could start ex nihilo (only this script on index.html, and
// eval being the only hook), but I'm a practical fuck.
var hooks = {
  "eval": eval,
};

function updateSidebar(tf) {
  var sidebar = document.getElementById("sidebar");
  sidebar.innerHTML = tf instanceof Function ? tf(sidebar.innerHTML) : tf;
}

// Called after HTML is loaded, initializes UnicodeTiles Viewport
// and displays something.
function start() {
	term = new ut.Viewport(document.getElementById("game"), 32, 32, "auto", "true");
	term.clear();
	term.putString("Connecting...", 0, 0, 204, 204, 255);
	term.render();
  updateSidebar('<div class="paleblue">Pending connection...</div>');
  connect();
  ut.initInput(register);
  ut.setKeyRepeatInterval(TL);
  window.setInterval(sendKeys, TL);
}

function execIssues(lines) {
  for (var i = 0; i < (lines.length >> 1); ++i) {
    var issue = lines[i << 1];
    var content = lines[(i << 1) + 1].replace("\\n", "\n");
    hooks[issue](content);
  }
}

function connect() {
  function init(response) {
    var lines = response.trim().split("\n");
    cookie = lines.shift();
    execIssues(lines);
  }
  var request = new XMLHttpRequest();
  request.open("POST", "/game/login", false);
  request.send();
  if (request.status != 200) {
    if (request.status == 400) {
      updateSidebar('<div class="red">You are already connected from a different tab!</div>');
      close();
      return;
    }
    var success = false;
    while (!success) {
      var password = prompt("You have set a password. Please enter it.", "");
      if (password != null && password !== "") {
        var request = new XMLHttpRequest();
        request.open("POST", "/game/login", false);
        request.send(password);
        success = request.status == 200;
        init(request.response);
      } else {
        close();
        return;
      }
    }
  } else {
    init(request.response);
  }
  updateSidebar('<div class="palegreen">Logged in.</div>');
}

function register(keyCode) {
  keypresses.push(keyCode);
}

function answerInquiryResponse() {
  var lines = this.response.trim().split("\n");
  execIssues(lines);
}

function sendKeys() {
  var request = new XMLHttpRequest();
  request.addEventListener("load", answerInquiryResponse);
  request.open("POST", "/game/inquire");
  request.send(cookie + "\n" + keypresses.join(" "));
  keypresses = [];
}
