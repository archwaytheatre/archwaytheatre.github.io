# archwaytheatre.github.io
Website for the Archway Theatre

Currently available at:
[https://archwaytheatre.github.io](https://archwaytheatre.github.io)

## Work in progress

### Dev:

Requires the Clojure 'clj' command line tool.

`clj -X:watch`

Remote debugging can be achieved. Currently, the setup I've used is:
Android device:
  enable USB debugging under 'Developer Options'
  plug in USB cable
  under connection popup options select 'PTP'
Desktop:
  chrome://inspect/#devices
    tick discover USB devices, and enable port forwarding for whatever port IntelliJ wants to use 
    (currently '63342 -> localhost:63342')
  IntelliJ
    open an output file from the 'local' directory in the browser by right-clicking in IntelliJ, grab the magic URL
  chrome://inspect/#devices
    navigate to magic url ('inspect' should open a connected browser window)

### Essential

#### Plumbing
* Domain name record (once site reaches minimum quality bar)

#### Process
* ~~Mechanism for updating content~~
* Train the owner of the content updates

#### Content
* ~~What's on page (with links to buy tickets)~~
* Get Involved page
* ~~Find us page~~
* ~~Contact page~~
* Membership link

### Wish List

#### Presentation
* ~~better sizing on desktop~~
* better about popups (with buy button?)
* better favicon
* ~~shrink 'what's on' play title font size when they are really long~~

#### Content
* History
* Past Productions
* Youth Workshops
* Hire a space
* Management / Backstage
* Legal
* LTG
* Social media links


TODO: need a picture / map / diagram of where the function room and studio entrances are, 
      need a better picture of the main entrance,
      pictures of 
        main entrance
        studio entrance
          studio space pictures
        function room entrance
          function room pictures
        main auditorium
        bar
        People getting involved
        People auditioning
        People volunteering / front of house / bar
        

- bar / auditorium photos - about us page?

; notes
- trailer video is a separate thing at the top (prolly just 1)
- ~~background is pale pink???~~
- ~~use the pink from the logo~~
- ~~click on poster -> straight to ticketsource~~
- ~~separate contact from find us~~
- ~~contact form...?~~
- box office page with auditorium layout
- membership page (with benefits! - possibly in a table)
- archive page (1 entry per production with trailer, poster and photos)
- get involved - highlight volunteeriness (see behind the scenes pages on old website) + auditions
- ~~filter on the what's on? page~~
  - ~~main house, studio, digital, other (e.g. open days)~~
- ↗
- ↗ 
- ➚

~~show/hide buttons on get involved page - simpler wording, more visible~~
~~membership joining page~~
hire a space page
update data on front page

- can you serve photos direct from google drive? - yes: https://stackoverflow.com/questions/10311092/displaying-files-e-g-images-stored-in-google-drive-on-a-website#:~:text=Right%2Dclick%20on%20the%20image,link%20to%20publish%20your%20image.

- pictures on get-involved page
- about us page - copy from old website - top level link!
  - photos of bar and auditorium, studio + function room
  - carousel of highlights of production photos? or an uncontrollable slideshow, fullscreen with music
    - also link to past productions page that indexes ALL the photos
  - The Archway Theatre is a thriving “little theatre” located under the railway arches in Horley, near Gatwick.
    95 seats accommodation.
    10 full productions every year.
    10 Performances over a two-week run.
    Youth Workshops and Young Adults Workshop & productions.
    Studio Theatre and Function Room.
    We pride ourselves on a professional approach, and we are rather ambitious with some of our productions. We try all aspects of theatre, from musicals through drama to pantomime. We open to the public, but also as a club  our members can buy tickets at a reduced rate, take advantage of the youth workshop facilities, and visit the members’ bar. Those wishing to go on stage or backstage are members of the company, who put on all the shows.
- hassle E.B. about their todo list
- ~~membership more subtle color usage - less shine!~~
- news from newsletter, reviews from plays, photos etc.
  - where on website?
  - news and reviews are a paid for member benefit
  - M.H.
- photos of past productions + reviews (maybe not reviews)
- re-order get-involved things
- sort get-involved events by 'us and them!'?
- separate tab for forthcoming auditions
  - maybe just change the controls to 'jump to...', sort by us-ness
- P.T. can do DNS for us!
- photos should have credits, and they should not be gray (teal?)
- G.A. will film stuff - do we want a theatre arrival video?
- test the search functionality


digital content link somewhere! (What's On? page possibly - 3rd item down?)


2023-09-20

DONE: limit how big the boxes get on the landing page
DONE: links not underlined, but a different color
Color
  bg, fg, highlight + variation

trailer for theatre on front page

DONE: sane limits on font size (and everything size - just don't scale it too far!)

DONE: inline the about text... smaller font, 

DONE: "Main House | dates | times" - all this on one small line at the top, maybe text-transform: uppercase;

PROBABLY NOT: menu bar glow at sides of white lines ???

DONE: mobile: text goes below the poster
DONE: also: make the text chunk a bit wider than the poster
DONE: also: alternate the sides that the poster is on

DONE - title size for plays to be the same for all plays

NO - fancy font on buy tickets buttons?

DONE: re-style buttons
