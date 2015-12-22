var term; // Can't be initialized yet because DOM is not ready

// Called after HTML is loaded, initializes UnicodeTiles Viewport
// and displays something.
function start() {
	term = new ut.Viewport(document.getElementById("game"), 32, 32, "auto", "true");
	term.clear();
	term.putString("Not connected to the server!", 0, 0, 255, 0, 0);
	term.render();
}
