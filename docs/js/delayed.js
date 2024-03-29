
const emails = {
    'Space Hire': [98, 111, 111, 107, 105, 110, 103, 115, 64, 97, 114, 99, 104, 119, 97, 121, 116, 104, 101, 97, 116, 114, 101, 46, 99, 111, 46, 117, 107],
    'Box Office': [98, 111, 120, 111, 102, 102, 105, 99, 101, 64, 97, 114, 99, 104, 119, 97, 121, 116, 104, 101, 97, 116, 114, 101, 46, 99, 111, 46, 117, 107],
    'General Enquiries': [97, 114, 99, 104, 64, 97, 114, 99, 104, 119, 97, 121, 116, 104, 101, 97, 116, 114, 101, 46, 99, 111, 46, 117, 107],
    'House Management': [104, 111, 117, 115, 101, 109, 97, 110, 97, 103, 101, 109, 101, 110, 116, 64, 97, 114, 99, 104, 119, 97, 121, 116, 104, 101, 97, 116, 114, 101, 46, 99, 111, 46, 117, 107],
    'Little Theatre Guild Representative': [108, 116, 103, 114, 101, 112, 64, 97, 114, 99, 104, 119, 97, 121, 116, 104, 101, 97, 116, 114, 101, 46, 99, 111, 109],
    'Membership': [109, 101, 109, 98, 101, 114, 115, 104, 105, 112, 64, 97, 114, 99, 104, 119, 97, 121, 116, 104, 101, 97, 116, 114, 101, 46, 99, 111, 109],
    'Repertory Committee': [114, 101, 112, 115, 117, 112, 112, 111, 114, 116, 64, 97, 114, 99, 104, 119, 97, 121, 116, 104, 101, 97, 116, 114, 101, 46, 99, 111, 109],
    'Safeguarding': [115, 97, 102, 101, 103, 117, 97, 114, 100, 105, 110, 103, 64, 97, 114, 99, 104, 119, 97, 121, 116, 104, 101, 97, 116, 114, 101, 46, 99, 111, 46, 117, 107],
    'Costume Hire & Wardrobe': [119, 97, 114, 100, 114, 111, 98, 101, 64, 97, 114, 99, 104, 119, 97, 121, 116, 104, 101, 97, 116, 114, 101, 46, 99, 111, 109],
    'Young Adults Workshops': [101, 108, 108, 97, 46, 115, 111, 119, 116, 111, 110, 64, 109, 97, 105, 108, 46, 99, 111, 109],
    'Youth Workshops': [115, 116, 97, 103, 101, 108, 101, 102, 116, 50, 64, 98, 116, 105, 110, 116, 101, 114, 110, 101, 116, 46, 99, 111, 109]
};

function addEmail(link) {
    let emailAddress = emails[link.textContent].map(x => String.fromCharCode(x)).join('');
    link.setAttribute('href', 'mailto:' + emailAddress);
    link.textContent = emailAddress;
}

function addEmails() {
    var emailElements = document.getElementsByClassName("delayedEmail");
    for (var i = 0; i < emailElements.length; i++) {
        setTimeout(addEmail, 257, emailElements.item(i));
    }
}

addEmails();
