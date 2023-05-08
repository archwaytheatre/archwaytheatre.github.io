
//
//
// function menudown(x) {
//     console.log('menudrop');
//     console.log(x);
//
//     x.classList.add('dropped');
//     let div = document.createElement('div');
//     div.id = 'droppedmenu';
//     div.appendChild(document.createTextNode('Volunteering'));
//     x.appendChild(div);
// }
//
// function menuup(x) {
//     document.getElementById('droppedmenu').remove();
// }


function dropDownTap(dropDownId) {
    document.getElementById(dropDownId).classList.toggle("show");
}

function dropDownHover(dropDownId) {
    document.getElementById(dropDownId).classList.add("show");
}

function dropDownUnhover(dropDownId) {
    document.getElementById(dropDownId).classList.remove('show');
}

window.onclick = function(event) {
    if (!event.target.matches('.dropDownButton')) {
        const dropDowns = document.getElementsByClassName("dropDownContainer");
        let i;
        for (i = 0; i < dropDowns.length; i++) {
            const openDropdown = dropDowns[i];
            if (openDropdown.classList.contains('show')) {
                openDropdown.classList.remove('show');
            }
        }
    }
}