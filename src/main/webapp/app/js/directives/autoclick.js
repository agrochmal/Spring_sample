'use strict';

angular.module('app.directives.autoclick', [])
.directive('ngAutoclick', function() {
  return {
    restrict: 'A',
    link: function(scope, element, attrs, ngModelCtrl) {
      var targetId = attrs.targetId;
      element.bind('keyup', function(event){
        if(event.keyCode == 13) {
          $('#' + targetId).click();
        }
      });
    }
  }
});