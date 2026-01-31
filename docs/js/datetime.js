
const now = Date.now();

function composeLabels() {
    const timelabels = document.getElementsByClassName('timelabel');
    console.log('composing labels...' + timelabels.length);
    for (let i = 0; i < timelabels.length; i++) {
        const timelabel = timelabels.item(i);
        const start = timelabel.getAttribute('data-start');
        const end = timelabel.getAttribute('data-end');
        const soldOut = timelabel.getAttribute('data-sold-out');
        const oneDay = timelabel.getAttribute('data-one-day');
        let extraLabel = '';
        let labelClass = '';
        if (soldOut === 'true') {
            extraLabel = 'Sold Out!';
            labelClass = 'soldOut';
        } else if (start < now && now < end) {
            extraLabel = oneDay === 'true' ? 'Tonight!' : 'On Now!';
            labelClass = 'onNow';
        }
        if (extraLabel) {
            let onNow = document.createElement("div");
            onNow.classList.add('eventLabel', labelClass);
            onNow.appendChild(document.createTextNode(extraLabel));
            timelabel.appendChild(onNow);
        }
    }

    const banners = document.getElementsByClassName('event-overview__banner');
    console.log('adding banners...' + banners.length);
    for (let i = 0; i < banners.length; i++) {
        const banner = banners.item(i);
        const start = banner.getAttribute('data-start');
        const end = banner.getAttribute('data-end');
        const soldOut = banner.getAttribute('data-sold-out');
        const oneDay = banner.getAttribute('data-one-day');
        if (soldOut === 'true') {
            banner.textContent = 'Sold Out!';
            banner.style.display = 'block';
        } else if (start < now && now < end) {
            banner.textContent = oneDay === 'true' ? 'Today!' : 'On Now!';
            banner.style.display = 'block';
        }
    }

}

function hideEvents() {
    const disappearables = document.getElementsByClassName('disappearable');
    const hash = window.location.hash ? window.location.hash.substring(1) : null;
    for (let i = disappearables.length - 1; i >= 0; i--) {
        let disappearable = disappearables.item(i);
        let end = disappearable.getAttribute('data-end');
        if (end && end < now) {
            disappearable.parentNode.removeChild(disappearable);
        }
        let id = disappearable.getAttribute('data-id');
        console.log('considering: ' + id);
        if (hash && id !== hash && hash !== '0') {
            disappearable.parentNode.removeChild(disappearable);
        }
    }
}

composeLabels();
hideEvents();

window.onhashchange = hideEvents;
