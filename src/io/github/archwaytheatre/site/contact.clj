(ns io.github.archwaytheatre.site.contact
  (:require
    [clojure.string :as string]
    [io.github.archwaytheatre.site.core :as core]))

(defn table [rows]
  (into [:div.table {:role "grid"}]
        (for [row rows]
          (let [[title description] row]
            [:div.row {:role "row"}
             [:div.cell {:role  "gridcell"
                         :style "font-size:larger;"}
              (string/replace title " " "&nbsp;")]
             [:div.cell.noglow {:role "gridcell"} description]]))))

(core/page "contact" "The Archway Theatre"
  [:div

   [:div.container
    [:div "You can get in touch with us by post, email, phone or using the contact form below."]
    [:br]
    (table [["Box Office Online" [:a {:href "https://www.ticketsource.co.uk/archwaytheatre/"} "ticketsource.co.uk"]]
            ["Box Office Phone" [:div
                                 [:div [:a {:href "tel:0333-666-3366"} "0333 666 3366"] "(handled by TicketSource)"]
                                 [:div "Open: Mon-Fri 9:00-19:00, Sat 9:00-17:00"]
                                 [:div "£1.75 phone booking fee. Call charges may also apply."]]]
            ["General enquiries" [:a {:href "tel:01293-784-398"} "(01293) 784 398"]]
            ["Address" [:pre (string/join "\n"
                               ["Archway Theatre Company Limited"
                                "The Drive"
                                "Horley"
                                "Surrey"
                                "RH6 7NQ"])]]])]

   [:iframe#JotFormIFrame-230046557100040
    {:title "Contact Us"
     :onload "window.parent.scrollTo(0,0)"
     :allowtransparency "true"
     :allowfullscreen "true"
     :allow "geolocation; microphone; camera"
     :src "https://form.jotform.com/230046557100040"
     :frameborder "0" :style "
      min-width: 100%;
      height:100vh;
      border:none;"
     :scrolling "no"
     }]
   [:script {:type "text/javascript"} "var ifr = document.getElementById(\"JotFormIFrame-230046557100040\");
    if (ifr) {
      var src = ifr.src;
      var iframeParams = [];
      if (window.location.href && window.location.href.indexOf(\"?\") > -1) {
        iframeParams = iframeParams.concat(window.location.href.substr(window.location.href.indexOf(\"?\") + 1).split('&'));
      }
      if (src && src.indexOf(\"?\") > -1) {
        iframeParams = iframeParams.concat(src.substr(src.indexOf(\"?\") + 1).split(\"&\"));
        src = src.substr(0, src.indexOf(\"?\"))
      }
      iframeParams.push(\"isIframeEmbed=1\");
      ifr.src = src + \"?\" + iframeParams.join('&');
    }
    window.handleIFrameMessage = function(e) {
      if (typeof e.data === 'object') { return; }
      var args = e.data.split(\":\");
      if (args.length > 2) { iframe = document.getElementById(\"JotFormIFrame-\" + args[(args.length - 1)]); } else { iframe = document.getElementById(\"JotFormIFrame\"); }
      if (!iframe) { return; }
      switch (args[0]) {
        case \"scrollIntoView\":
          iframe.scrollIntoView();
          break;
        case \"setHeight\":
          iframe.style.height = args[1] + \"px\";
          break;
        case \"collapseErrorPage\":
          if (iframe.clientHeight > window.innerHeight) {
            iframe.style.height = window.innerHeight + \"px\";
          }
          break;
        case \"reloadPage\":
          window.location.reload();
          break;
        case \"loadScript\":
          if( !window.isPermitted(e.origin, ['jotform.com', 'jotform.pro']) ) { break; }
          var src = args[1];
          if (args.length > 3) {
              src = args[1] + ':' + args[2];
          }
          var script = document.createElement('script');
          script.src = src;
          script.type = 'text/javascript';
          document.body.appendChild(script);
          break;
        case \"exitFullscreen\":
          if      (window.document.exitFullscreen)        window.document.exitFullscreen();
          else if (window.document.mozCancelFullScreen)   window.document.mozCancelFullScreen();
          else if (window.document.mozCancelFullscreen)   window.document.mozCancelFullScreen();
          else if (window.document.webkitExitFullscreen)  window.document.webkitExitFullscreen();
          else if (window.document.msExitFullscreen)      window.document.msExitFullscreen();
          break;
      }
      var isJotForm = (e.origin.indexOf(\"jotform\") > -1) ? true : false;
      if(isJotForm && \"contentWindow\" in iframe && \"postMessage\" in iframe.contentWindow) {
        var urls = {\"docurl\":encodeURIComponent(document.URL),\"referrer\":encodeURIComponent(document.referrer)};
        iframe.contentWindow.postMessage(JSON.stringify({\"type\":\"urls\",\"value\":urls}), \"*\");
      }
    };
    window.isPermitted = function(originUrl, whitelisted_domains) {
      var url = document.createElement('a');
      url.href = originUrl;
      var hostname = url.hostname;
      var result = false;
      if( typeof hostname !== 'undefined' ) {
        whitelisted_domains.forEach(function(element) {
            if( hostname.slice((-1 * element.length - 1)) === '.'.concat(element) ||  hostname === element ) {
                result = true;
            }
        });
        return result;
      }
    };
    if (window.addEventListener) {
      window.addEventListener(\"message\", handleIFrameMessage, false);
    } else if (window.attachEvent) {
      window.attachEvent(\"onmessage\", handleIFrameMessage);
    }"]

   ])
