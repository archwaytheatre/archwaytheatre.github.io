function showPopup(elementId) {
    hidePopups();
    document.getElementById(elementId).style.display = 'flex';
}
function hidePopups() {
    var popups = document.getElementsByClassName("popup");
    for (var i = 0; i < popups.length; i++) {
        popups.item(i).style.display = 'none';
    }
}
