
function fisherYatesShuffle(a,b,c,d){//array,placeholder,placeholder,placeholder
    c=a.length;while(c)b=Math.random()*c--|0,d=a[c],a[c]=a[b],a[b]=d
}

function allImages(_) {
    return true;
}

function recentImages(item) {
    return item[0];
}

function imagesForPlay(title) {
    return function (item) {
        return title === item[2];
    }
}

function startCarousel(elementId, predicate, labelled) {

    let carousel = document.getElementById(elementId);
    if (labelled) {
        let labelBacking = document.createElement('div');
        labelBacking.classList.add('carouselBacking');
        labelBacking.appendChild(document.createTextNode(' '));
        carousel.appendChild(labelBacking);
    }

    let imageData = carouselData.filter(predicate).map(item => item.slice(1));
    fisherYatesShuffle(imageData);

    let imageIdx = 0;
    let currentImageElement = document.createElement('p');
    let oldImageElement = document.createElement('p');

    let nextImage = function () {
        let imageDatum = imageData[imageIdx++];
        imageIdx%=imageData.length;
        let src = 'https://archwaytheatre.s3.eu-west-2.amazonaws.com/site/' + imageDatum[2];
        var img = new Image ();
        img.onload = function () {
            let newElement = document.createElement('div');
            if (labelled) {
                let title = document.createElement('div');
                title.appendChild(document.createTextNode(imageDatum[1] + ' (' + imageDatum[0] + ')'));
                newElement.appendChild(title);
            }
            newElement.classList.add("carouselimg");
            newElement.classList.add("carouselnew");
            newElement.style.backgroundImage="url("+ src +")";
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