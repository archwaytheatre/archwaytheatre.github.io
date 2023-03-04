
let visibility = {
    'volunteer': true,
    'audition': true,
    'other': true
};

function toggle(element) {
    let elementClass = element.getAttribute('data-kind');
    visibility[elementClass] = !visibility[elementClass];
    if (visibility[elementClass]) {
        element.innerHTML = element.innerHTML.replace('Show', 'Hide');
        element.classList.add('shown');
        element.classList.remove('hidden');
        console.log(element.innerHTML);
    } else {
        element.innerHTML = element.innerHTML.replace('Hide', 'Show');
        element.classList.add('hidden');
        element.classList.remove('shown');
        console.log(element.innerHTML);
    }
    let auditionElements = document.getElementsByClassName(elementClass);
    for (var i = 0; i < auditionElements.length; i++) {
        if (visibility[elementClass]) {
            auditionElements.item(i).style.display = "block";
        } else {
            auditionElements.item(i).style.display = "none";
        }
    }
}

