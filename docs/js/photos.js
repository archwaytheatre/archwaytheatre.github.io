
// todo: shared code!!! with carousel!
function fisherYatesShuffle(a,b,c,d){//array,placeholder,placeholder,placeholder
    c=a.length;while(c)b=Math.random()*c--|0,d=a[c],a[c]=a[b],a[b]=d
}

function selectionForNow(items) {
    const itemsPerLoad = 10;
    let selectionFactor = Math.ceil(items.length / itemsPerLoad);
    // console.log('selectionFactor: ' + selectionFactor);
    let target = Date.now() % selectionFactor;
    return items.filter(function(value, index) {
        return (index % selectionFactor) === target;
    });
}

const debugMode = false;
const delay = debugMode ? 200 : 3200;


function setTagLine(text) {
    document.getElementById('tagline').innerText = text ? text : 'A thriving theatre in the heart of Horley.';
}
// setTagLine('Photo by ' + photographer + '; Used with permission.')

function startTopPhotos() {

    let topPhotos= document.getElementById('topphotos');
    let motto = document.getElementById('motto');
    let imageData = selectionForNow(topPhotoData);
    let imageIdx = 0;

    fisherYatesShuffle(imageData);
    topPhotos.appendChild(document.createElement('div'));

    let fadeOutImage = null;

    let fadeInNextImage = function () {
        let imageDatum = imageData[imageIdx++];
        imageIdx%=imageData.length;
        let yOffset = imageDatum[0];
        let photographer = imageDatum[2];
        let src = 'https://archwaytheatre.s3.eu-west-2.amazonaws.com/site/' + imageDatum[1];
        // console.log('fadeInNextImage:  ->  ' + yOffset + "  " + src );

        if (motto) {
            motto.classList.remove('fadein');
            motto.classList.add('fadeout');
        }

        let holder = document.createElement('div');
        let img = new Image ();
        img.classList.add('fadein');
        img.onload = function () {
            topPhotos.firstElementChild.remove();
            holder.appendChild(img);
            topPhotos.appendChild(holder);
            let top = img.offsetHeight * yOffset;
            img.setAttribute('style', 'top: -' + top + 'px');
            if (debugMode) {
                console.log(imageDatum[1]);
            } else {
                setTimeout(fadeOutImage, 6000);
            }
        }
        img.onerror = function () {
            setTimeout(fadeOutImage, delay * 2);
        }
        img.src = src;
    }

    fadeOutImage = function () {
        topPhotos.firstElementChild.classList.add("fadeout");
        if (motto) {
            motto.classList.remove('fadeout');
            motto.classList.add('fadein');
        }
        setTimeout(fadeInNextImage, delay);
    }

    fadeInNextImage();

    if (debugMode) {
        document.getElementById('topphotos').onclick = function () {
            fadeOutImage();
        }
    }
}

// startTopPhotos();

function makeCredit(photographer) {
    if (photographer == 'Archway Archive') {
        return ['Photo from the Archway Archive.', ''];
    } else {
        return ['Photo by ' + photographer + '.', 'Used with Permission.'];
    }
}

function makeCaption(imageDatum) {

    let firstText;
    let secondText;

    if (imageDatum.length === 2) {
        [firstText, secondText] = makeCredit(imageDatum[1]);
    } else {
        firstText = '"' + imageDatum[3] + '" (' + imageDatum[2] + ')';
        secondText = makeCredit(imageDatum[1]).join(' ');
    }

    let firstLine = document.createElement('div');
    firstLine.appendChild(document.createTextNode(firstText));
    let secondLine = document.createElement('div');
    secondLine.appendChild(document.createTextNode(secondText));
    let credit = document.createElement('div');
    credit.appendChild(firstLine);
    credit.appendChild(secondLine);

    return credit;
}

function startCarousel() {

    let carousel = document.getElementById('photoCarousel')
    carousel.classList.add('photo-container');
    let imageData = photoData;
    if (imageData.length === 0) {
        carousel.remove();
        return;
    }

    fisherYatesShuffle(imageData);

    let imageIdx = 0;
    let currentImageElement = document.createElement('p');
    let oldImageElement = document.createElement('p');

    let nextImage = function () {
        let imageDatum = imageData[imageIdx++];
        imageIdx%=imageData.length;
        let src = 'https://archwaytheatre.s3.eu-west-2.amazonaws.com/site/' + imageDatum[0];
        var img = new Image ();
        img.onload = function () {
            let newElement = document.createElement('div');
            let newBack = document.createElement('div');
            let newCredit = document.createElement('div');

            newBack.classList.add('photo-back');
            newBack.style.backgroundImage="url("+ src +")";

            newCredit.classList.add('photo-credit');
            newCredit.appendChild(makeCaption(imageDatum));

            newElement.classList.add('photo-swap');
            newElement.classList.add("carouselnew");
            newElement.appendChild(newBack);
            newElement.appendChild(newCredit);

            carousel.appendChild(newElement);

            oldImageElement.remove();
            oldImageElement = currentImageElement;
            currentImageElement = newElement;
            oldImageElement.classList.add("carouselold");
            setTimeout(nextImage, 6000);
        };
        img.onerror = function () {
            setTimeout(nextImage, 6000);
        }
        img.src = src;
    }

    nextImage();
}

function initPhotos() {
    // TODO: js suspend on lost focus?

    // if (typeof photoData === 'undefined') {
    //     startTopPhotos();
    // } else {
    //     startCarousel();
    // }

    let carousel = document.getElementById('photoCarousel')
    if (carousel) {
        startCarousel();
    } else {
        startTopPhotos();
    }
}

initPhotos();
