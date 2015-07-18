'use strict';

/*
 $rootScope.zipPattern =/^(\d{2}(-\d{3}))$/;
 $rootScope.onlyNumbers = /^[0-9]+$/;
 $rootScope.accountPattern= /^(\d{14})$/;
 */

angular.module('app.directives.validation', [])
.directive('ngValidateEquals', function() {
  return {
    require: 'ngModel',
    restrict: 'A',
    link: function(scope, elm, attrs, ngModelCtrl) {

      /*  $parsers sa odpalane gdy zmieniamy model z view - jak u nas !!! */
      ngModelCtrl.$parsers.push(function(viewValue) {
        var valid = (viewValue === scope.$eval(attrs.ngValidateEquals));
        ngModelCtrl.$setValidity('equal', valid);
        return valid ? viewValue : undefined;
      });

      /*  $formatters sa odpalane gdy zmieniamy model ale z kodu- u nas nie ustawiam modelu z kodu wiec nie potrzebne !!! */
      /*ngModelCtrl.$formatters.push(validateEqual);*/

      scope.$watch(attrs.ngValidateEquals, function() {
        ngModelCtrl.$setViewValue(ngModelCtrl.$viewValue);
      });
    }
  };
})
.directive('ngValidateEmail', function() {
  return {
    require: 'ngModel',
    restrict: 'A',
    link: function(scope, elm, attrs, ngModelCtrl) {

      ngModelCtrl.$parsers.push( function(viewValue) {
        if(angular.isUndefined(viewValue))
          return undefined;

        var EMAIL_REGEXP = /^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,4}$/;
        var valid = EMAIL_REGEXP.test(viewValue);
        ngModelCtrl.$setValidity('email', valid);
        return valid ? viewValue : undefined;
      });
    }
  };
})
.directive('ngValidateNumber', function() {
  return {
    require: 'ngModel',
    restrict: 'A',
    link: function(scope, elm, attrs, ngModelCtrl) {

      ngModelCtrl.$parsers.push(function(viewValue) {
        if(angular.isUndefined(viewValue))
          return undefined;
        var regexp = /^[0-9]+$/;
        var valid = regexp.test(viewValue);
        ngModelCtrl.$setValidity('number', valid);
        return valid ? viewValue : undefined;
      });
    }
  };
})
.directive('ngValidatePassword', function() {
  return {
    require: 'ngModel',
    restrict: 'A',
    link: function(scope, elm, attrs, ngModelCtrl) {

      ngModelCtrl.$parsers.push(function (viewValue) {
        if (angular.isUndefined(viewValue))
          return undefined;
        var valid = viewValue.length >= 8;
        ngModelCtrl.$setValidity('password', valid);
        return valid ? viewValue : undefined;
      });
    }
  };
});