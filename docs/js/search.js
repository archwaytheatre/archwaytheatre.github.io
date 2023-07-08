
// const thisSite = 'archwaytheatre.github.io';
const thisSite = 'archwaytheatre.com';

function updateLink(inputElement, elementId) {
    console.log(inputElement.value);
    document.getElementById(elementId).href =
        'https://duckduckgo.com/?q=site%3A' + thisSite + '+' +
        encodeURIComponent(inputElement.value).replaceAll('%20', '+');
    console.log(document.getElementById(elementId).href);
}
