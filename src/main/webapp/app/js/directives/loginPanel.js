/**
 * Created by robertsikora on 09.08.15.
 */

'use strict';

angular.module('app.directives.loginPanel', [])
.directive('ngLoginPanel', function($rootScope) {
    return {
        restrict:'E',
        replace:true,
        templateUrl:'app/partials/login-panel.tpl.html'
    }
});
