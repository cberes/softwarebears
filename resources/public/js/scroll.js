function readMore(element, idMore) {
    document.getElementById(idMore).style.display = 'block';
    element.parentNode.style.display = 'none';
    return false;
}

function showLess(idMore, idLess) {
    document.getElementById(idMore).style.display = 'none';
    document.getElementById(idLess).style.display = 'block';
    return false;
}

$(function(){
  var detachHeader = 300;
  var className = 'detached';
  $(window).scroll(function() {
    var scroll = getCurrentScroll();
    if (scroll >= detachHeader) {
      $('body > header').addClass(className);
      $('body > nav').addClass(className);
    } else {
      $('body > header').removeClass(className);
      $('body > nav').removeClass(className);
    }
  });
  function getCurrentScroll() {
    return window.pageYOffset;
  }
});
