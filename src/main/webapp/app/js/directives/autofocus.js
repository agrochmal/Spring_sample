'use strict';

angular.module('app.directives.autofocus', [])
.directive('ngAutofocus', ['$timeout', function($timeout) {
    return {
        restrict: 'A',
        link : function($scope, $element) {
            $timeout(function () {
                $element[0].focus();
            });
        }
    }
}]);
