'use strict';

angular.module('app.directives.uniqueEmail', [])
.directive('ngUniqueEmail', function(UniqueService, $q) {
  return {
    restrict: 'A',
    require: 'ngModel',
    link: function (scope, element, attrs, ngModelCtrl) {
      ngModelCtrl.$asyncValidators.unique = function (modelValue, viewValue) {

        var deferred = $q.defer(), currentValue = modelValue || viewValue;
        UniqueService.checkUniqueValue(currentValue)
            .then(function (result) {
              var unique = result.data;
              if (unique) {
                deferred.resolve(); //It's unique
              }
              else {
                deferred.reject(); //Add unique to $errors
              }
            });

        return deferred.promise;
      };
    }
  }
});