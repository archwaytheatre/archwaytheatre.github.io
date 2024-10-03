
function setup() {

    let el = document.getElementById('trailer__cover');

    el.style.display = 'inherit';
    el.onclick = function () {
        el.style.display = 'none';
        document.getElementById('promo-video').play();
    }

}

setup();