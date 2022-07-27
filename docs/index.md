---
# Feel free to add content and custom Front Matter to this file.
# To modify the layout, see https://jekyllrb.com/docs/themes/#overriding-theme-defaults

layout: default
---

<script>

  function showPopup(content) {
    document.getElementById('popupText').innerText = content;
    document.getElementById('popup').style.display = 'inherit';
  }
  function hidePopup() {
    document.getElementById('popup').style.display = 'none';
  }

</script>

<div class="popupContainer">
<div id="popup" onclick="javascript:hidePopup();">
<div id="popupText">
Popup
</div>
</div>
</div>

<div class="events">
{% for event in site.data.whatson %}

    {% if event.location == "Main House" %}
  <div class="event main">
    {% elsif event.location == "The Studio" %}
  <div class="event studio">
    {% elsif event.location == "Online" %}
  <div class="event online">
    {% else %}
  <div class="event unknown">
    {% endif %}
    <div class="eventimage">
      <img src="{{ event.imageurl }}">
    </div>
    <div class="eventdata">
      <div class="eventdatum fade1">
        {{ event.location }}
      </div>
      <div class="eventdatum fade2 bold">
        {{ event.name }}
      </div>
      <div class="eventdatum fade3">
        <a href="javascript:showPopup(&quot;{{ event.about }}&quot;);">About</a>
      </div>
      <div class="eventdatum fade4">
        {{ event.dates }}
      </div>
      <div class="eventdatum fade5 bold">
        <a href="https://www.ticketsource.co.uk/archwaytheatre/" class="button fade1">
          Buy Tickets
        </a> 
      </div>
<!-- 
-->
    </div>
  </div>

{% endfor %}
</div>
