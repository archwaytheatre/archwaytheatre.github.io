
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

const delay = 3200;

function startTopPhotos() {

    let topPhotos =  document.getElementById('topphotos');
    let imageData = selectionForNow(topPhotoData);
    let imageIdx = 0;

    fisherYatesShuffle(imageData);
    topPhotos.appendChild(document.createElement('div'));

    let fadeOutImage = null;

    let fadeInNextImage = function () {
        let imageDatum = imageData[imageIdx++];
        imageIdx%=imageData.length;
        let yOffset = imageDatum[0];
        let src = 'https://archwaytheatre.s3.eu-west-2.amazonaws.com/site/' + imageDatum[1];
        // console.log('fadeInNextImage:  ->  ' + yOffset + "  " + src );

        let holder = document.createElement('div');
        let img = new Image ();
        img.classList.add('fadein');
        img.onload = function () {
            topPhotos.firstElementChild.remove();
            holder.appendChild(img);
            topPhotos.appendChild(holder);
            let top = img.offsetHeight * yOffset;
            img.setAttribute('style', 'top: -' + top + 'px');
            setTimeout(fadeOutImage, 6000);
        }
        img.onerror = function () {
            setTimeout(fadeOutImage, delay * 2);
        }
        img.src = src;
    }

    fadeOutImage = function () {
        topPhotos.firstElementChild.classList.add("fadeout");
        setTimeout(fadeInNextImage, delay);
    }

    fadeInNextImage();

    // document.getElementById('topphotos').onclick = function () {
    //     fadeOutImage();
    // }
}

startTopPhotos();

