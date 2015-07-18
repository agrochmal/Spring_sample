'use strict';

angular.module('app.directives.modal', [])
.directive('ngModal', function() {
  return {
    restrict: 'A',
    link: function(scope, element, attr) {
      scope.dismiss = function($event) {
        $('.modal.in').modal('hide');
      };
    }
  }
});