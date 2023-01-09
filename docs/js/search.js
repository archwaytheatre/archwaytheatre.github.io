

var thisSite = 'archwaytheatre.github.io'
// var site = 'archwaytheatre.com'
function updateLink(inputElement, elementId) {
    console.log(inputElement.value);
    document.getElementById(elementId).href =
        'https://duckduckgo.com/?q=site%3' + thisSite + '+' +
        encodeURIComponent(inputElement.value).replaceAll('%20', '+');
    console.log(document.getElementById(elementId).href);
}
