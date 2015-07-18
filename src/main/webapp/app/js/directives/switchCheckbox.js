/**
 * Created by Robert on 30.01.15.
 */

'use strict';

angular.module('app.directives.switchCheckbox', [])
    .directive('ngSwitchCheckbox', function() {
    /*return {
        restrict: 'A',
        scope: {
            state: '='
        },
        link: function(scope, element) {

            element.bootstrapSwitch('state', scope.state);

           // scope.$watch(['advert.state','advertGrid.model.state'], function() {
           //     console.log('asd');
            //});

            scope.$watch('element.state', function() {
                console.log('asd');
            });
        }
    };*/

        return {
            restrict: 'A',
            require: '?ngModel',
            link: function (scope, element, attrs, ngModel) {
                element.bootstrapSwitch();

                element.on('switchChange.bootstrapSwitch', function (event, state) {
                    if (ngModel) {
                        scope.$apply(function () {
                            ngModel.$setViewValue(state);
                        });
                    }
                });

                scope.$watch(attrs.ngModel, function (newValue, oldValue) {
                    if (newValue) {
                        element.bootstrapSwitch('state', true, true);
                    } else {
                        element.bootstrapSwitch('state', false, true);
                    }
                });
            }
        }
    });

