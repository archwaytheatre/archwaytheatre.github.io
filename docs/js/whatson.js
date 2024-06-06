

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