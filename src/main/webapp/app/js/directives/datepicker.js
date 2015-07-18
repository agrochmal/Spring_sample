'use strict';
/*
 Don't used now. I use directive from AngularJS UI
 */
angular.module('app.directives.datepicker', [])
.directive('dateTimePicker', function() {
  return {
    restrict: 'A',
    require : 'ngModel',
    scope: {}, // now the scope is isolated ... by default it always inherits parent scope

    link : function (scope, element, attrs, ngModelCtrl) {
      $(function(){
        element.datepicker({
          dateFormat:'dd/mm/yy',
          onSelect:function (date) {
            scope.$apply(function () {
              ngModelCtrl.$setViewValue(date);
            });
          }
        });
      });
    }
  }
});