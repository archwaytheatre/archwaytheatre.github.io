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

// hide 'more...' on load

function hideMore(id) {
    if (document.getElementById('overflow-a-' + id).offsetHeight > document.getElementById('overflow-b-' + id).offsetHeight) {
        document.getElementById('overflow-a-' + id).classList.remove('fadeable');
        document.getElementById('aboutmore-' + id).style.display = 'none';
    }
}

function openAbout(id) {
    console.log(id);
    document.getElementById('eventabout-' + id).style.display = 'block';
    document.getElementById('eventdata-' + id).style.display = 'none';
    document.getElementById('eventimage-' + id).style.display = 'none';
    return false;
}
function closeAbout(id) {
    document.getElementById('eventabout-' + id).style.display = 'none';
    document.getElementById('eventdata-' + id).style.display = 'flex';
    document.getElementById('eventimage-' + id).style.display = 'block';
    return false;
}
