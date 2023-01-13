
const now = Date.now();

function composeLabels() {
    var timelabels = document.getElementsByClassName('timelabel');
    console.log('composing labels...' + timelabels.length);
    for (var i = 0; i < timelabels.length; i++) {
        var timelabel = timelabels.item(i);
        var start = timelabel.getAttribute('data-start');
        var end = timelabel.getAttribute('data-end');
        if (start < now && now < end) {
            let onNow = document.createElement("div");
            onNow.className = 'onnow';
            onNow.appendChild(document.createTextNode("On Now!"));
            timelabel.appendChild(onNow);
        }
    }
}

function hidePastEvents() {
    var disappearables = document.getElementsByClassName('disappearable');
    for (var i = disappearables.length - 1; i >= 0; i--) {
        let disappearable = disappearables.item(i);
        let end = disappearable.getAttribute('data-end');
        if (end < now) {
            console.log("Removing " + disappearable.id + " as it has passed.");
            disappearable.parentNode.removeChild(disappearable);
        }
    }
}

composeLabels();
hidePastEvents();
