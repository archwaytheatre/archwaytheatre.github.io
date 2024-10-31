

function about(id) {
    document.getElementById('event-overview-'+id).classList.remove('event-focus');
    document.getElementById('event-trailer-'+id)?.classList.remove('event-focus');
    document.getElementById('event-about-'+id).classList.add('event-focus');
    document.getElementById('event-trailer__video-'+id)?.pause();
}

function trailer(id) {
    document.getElementById('event-overview-'+id).classList.remove('event-focus');
    document.getElementById('event-trailer-'+id).classList.add('event-focus');
    document.getElementById('event-about-'+id).classList.remove('event-focus');
    document.getElementById('event-trailer__video-'+id).play();
}

function poster(id) {
    document.getElementById('event-overview-'+id).classList.add('event-focus');
    document.getElementById('event-trailer-'+id)?.classList.remove('event-focus');
    document.getElementById('event-about-'+id).classList.remove('event-focus');
    document.getElementById('event-trailer__video-'+id)?.pause();
}

function hideMore(id) {
    const text = document.getElementById('event-overview__text-' + id);
    const pre = document.getElementById('event-overview__pre-' + id);
    const more = document.getElementById('event-overview__more-' + id);
    if (text.offsetHeight >= pre.offsetHeight) {
        text.classList.remove('event-overview__text__faded');
        more.style.display = 'none';
    }
}

function considerHiding() {
    const args = Array.prototype.slice.call(arguments);
    args.forEach(hideMore);
}