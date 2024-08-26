
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
    document.getElementById(dropDownId).classList.toggle("menu__drop-down-dropped");
}

function dropDownHover(dropDownId) {
    document.getElementById(dropDownId).classList.add("show");
    document.getElementById(dropDownId).classList.add("menu__drop-down-dropped");
}

function dropDownUnhover(dropDownId) {
    document.getElementById(dropDownId).classList.remove('show');
    document.getElementById(dropDownId).classList.remove("menu__drop-down-dropped");
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
    if (!event.target.matches('.menu__button')) {
        // console.log('other click');
        const dropDowns = document.getElementsByClassName("menu__drop-down-holder");
        let i;
        for (i = 0; i < dropDowns.length; i++) {
            // console.log('other click: ' + i);
            const openDropdown = dropDowns[i];
            if (openDropdown.classList.contains('menu__drop-down-dropped')) {
                // console.log('other click: ' + i + ' changing...');
                openDropdown.classList.remove('menu__drop-down-dropped');
            }
        }
    }
}

// function dropDownTap(dropDownId) {
//     document.getElementById(dropDownId).classList.toggle("menu__drop-down-dropped");
// }
//
// function dropDownHover(dropDownId) {
//     document.getElementById(dropDownId).classList.add("menu__drop-down-dropped");
// }
//
// function dropDownUnhover(dropDownId) {
//     document.getElementById(dropDownId).classList.remove('menu__drop-down-dropped');
// }
//
// window.onclick = function(event) {
//     if (!event.target.matches('.menu__button')) {
//         const dropDowns = document.getElementsByClassName("menu__drop-down-holder");
//         let i;
//         for (i = 0; i < dropDowns.length; i++) {
//             const openDropdown = dropDowns[i];
//             if (openDropdown.classList.contains('menu__drop-down-dropped')) {
//                 openDropdown.classList.remove('menu__drop-down-dropped');
//             }
//         }
//     }
// }