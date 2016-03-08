/**
 * Created by jukka on 5.3.2016.
 */
// attaching tndr-height-max-check (or data-tndr-height-max-check for standard compliency) into a html element will
// take all thus bound elements and compare their heights, saving the maximum value.  this maximum value is then
// used in an element with tndr-max-height-target -tag (or with data- prefix again, for compliency)  you can insert an
// initial value by supplying it to the max-height-check, so the final result is initial_value + height
// e.g.
// <div data-tndr-height-max-check="100"> .... </div> <!-- take the height of this div, use base value of 100 -->
// <div data-tndr-height-max-target> .... </div> <!-- this element gets its height set -->
angular.module('tenderworkApp')
    .directive('tndrHeightMaxCheck', function() {
       return {
           restrict:'A',
           link:
               function (scope, element, attrs) {
                   var recalculate = function() {
                       if (scope.currentMaxHeight === null || scope.currentMaxHeight === undefined) {
                           scope.currentMaxHeight = 0;
                       }
                       var initial_value = 0;
                       if(scope.calculated_max_height === null || scope.calculated_max_height === undefined) {
                           scope.calculated_max_height = initial_value;
                       }
                       var current_height = parseInt(element.height());
                       if (scope.currentMaxHeight < current_height) {
                           scope.currentMaxHeight = current_height;
                       }
                       scope.calculated_max_height = initial_value + scope.currentMaxHeight;
                   };
                   scope.$watch(attrs.tndrHeightMaxCheck, recalculate);
               }
           }
}).directive('tndrHeightMaxTarget', function() {
    return {
        restrict:'A',
        link:
            function(scope, element, attrs) {
                scope.$watch('calculated_max_height', function(newVal, oldVal) {
                    if(!newVal) {
                        return;
                    }
                    element.height(newVal);
                });
            }
    }
});
