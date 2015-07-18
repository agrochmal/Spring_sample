/**
 * Created by Robert on 25.01.15.
 */
'use strict';

angular.module('app.directives.navigationBar', [])
    .directive('ngNavigationBar', function() {
        return {
            restrict:'E',
            replace:true,
            templateUrl:'app/partials/navigation-bar.tpl.html',
            scope: {
                shift: '='
            },
            link: function($scope, element) {
                $(document).scroll(function() {
                    if ($(this).scrollTop() > $scope.shift) {
                        element.css({"position" : 'fixed', "top" : 0 });
                        $scope.showButton=true;
                    } else {
                        element.css({ "position" : 'relative', "top" : 0 });
                        $scope.showButton=false;
                    }
                });
            }
        }
    });