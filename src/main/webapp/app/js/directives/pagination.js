'use strict';

angular.module('app.directives.pagination', [])
.directive('ngPagination', function() {
  return {
    restrict:'E',
    replace:true,
    templateUrl:'app/partials/pagination.tpl.html',
    scope: {
      numPages: '=',
      currentPage: '=',
      selectPageFn: '&'
    },
    controller: function($scope){

      $scope.paginationCmd = {

        isActive : function (page) {
          return $scope.currentPage === page;
        },

        selectPage : function (page) {
          if (!this.isActive(page)) {
            $scope.currentPage = page;
            $scope.selectPageFn({ currentPage: $scope.currentPage, currentPageSize: $scope.currentPageSize });
          }
        },

        noNext : function () {
          return $scope.currentPage === $scope.numPages;
        },

        noPrev : function () {
          return $scope.currentPage === 1;
        },

        selectNext : function () {
          if (!this.noNext()) {
            this.selectPage($scope.currentPage + 1);
          }
        },

        selectPrevious : function () {
          if (!this.noPrev()) {
            this.selectPage($scope.currentPage - 1);
          }
        }
      }
    },
    link: function($scope) {

      $scope.$watch('numPages', function (value) {
        $scope.pages = [];
        for (var i = 1; i <= value; i++) {
          $scope.pages.push(i);
        }
        if ($scope.currentPage > value) {
          $scope.selectPage(value);
        }
      });

      $scope.$watch('currentPageSize', function (newValue, oldValue) {

        if (oldValue != newValue) {
          $scope.currentPage = 1;
          $scope.currentPageSize = value;
          $scope.selectPageFn({currentPage: $scope.currentPage, currentPageSize: $scope.currentPageSize});
         }

      });
    }
  }
});