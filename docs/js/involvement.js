
let visibility = {
    'volunteer': true,
    'audition': true,
    'other': true
};

function toggle(elementClass) {
    visibility[elementClass] = !visibility[elementClass];
    let auditionElements = document.getElementsByClassName(elementClass);
    for (var i = 0; i < auditionElements.length; i++) {
        if (visibility[elementClass]) {
            auditionElements.item(i).style.display = "block";
        } else {
            auditionElements.item(i).style.display = "none";
        }
    }
}

