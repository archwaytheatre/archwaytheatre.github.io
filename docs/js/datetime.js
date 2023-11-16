
const now = Date.now();

function composeLabels() {
    var timelabels = document.getElementsByClassName('timelabel');
    console.log('composing labels...' + timelabels.length);
    for (let i = 0; i < timelabels.length; i++) {
        const timelabel = timelabels.item(i);
        const start = timelabel.getAttribute('data-start');
        const end = timelabel.getAttribute('data-end');
        const soldOut = timelabel.getAttribute('data-sold-out');
        let extraLabel = '';
        let labelClass = '';
        if (soldOut === 'true') {
            extraLabel = 'Sold Out!';
            labelClass = 'soldOut';
        } else if (start < now && now < end) {
            extraLabel = 'On Now!';
            labelClass = 'onNow';
        }
        if (extraLabel) {
            let onNow = document.createElement("div");
            onNow.classList.add('eventLabel', labelClass);
            onNow.appendChild(document.createTextNode(extraLabel));
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
