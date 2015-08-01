'use strict';

angular.module('app.directives.editor', [])
.directive('ngEditor', function() {
  return {
    restrict: 'EA',
    replace: true,
    template: function(element, attrs) {
      var name = attrs.name;
      var modelName = attrs.modelName;
      var rows = attrs.rows;
      var required = attrs.hasOwnProperty('required') ? "required='required'" : "";
      var charLimit = attrs.charLimit || 5000;

      var htmlText = '<div> <textarea id="editor" class="form-control" name="'+ name +'" ng-model="'+ modelName +'" rows="'+ rows +'"' + required +'></textarea> '+
                     '<small><span id="left_char" style="font-weight: bold;">'+ charLimit +'</span> znaków pozostalo </small> </div>';
      return htmlText;
    },
    link: function(scope, element, attrs, ngModelCtrl) {
      $(element.find('#editor')).keyup(function(){
        var limit = attrs.charLimit || 5000;
        var charsno = $(this).val().length;
        $('#left_char').html(limit-charsno);
      });
    }
  }
});