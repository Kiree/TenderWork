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
/*
[
    {
    check-id:string,
    target-id:string
    }
]
 */
angular.module('tenderworkApp')
    .directive('tndrHeightMaxCheck', function() {
       return {
           restrict:'A',
           link:
               function (scope, element, attrs) {
                   var recalculate = function() {
                       if (scope.currentMaxHeight === null || typeof scope.currentMaxHeight == 'undefined') {
                           scope.currentMaxHeight = {};
                       }
                       scope.currentMaxHeight[attrs.tndrHeightMaxCheck] = 0;

                       var initial_value = 0;
                       if(scope.calculated_max_height === null || typeof scope.calculated_max_height == 'undefined') {
                           scope.calculated_max_height = {};
                       }
                       if(typeof scope.calculated_max_height[attrs.tndrHeightMaxCheck] == 'undefined') {
                           scope.calculated_max_height[attrs.tndrHeightMaxCheck] = initial_value;
                       }
                       var current_height = parseInt(element.height());

                       if (scope.currentMaxHeight[attrs.tndrHeightMaxCheck] < current_height) {
                           scope.currentMaxHeight[attrs.tndrHeightMaxCheck] = current_height;
                       }
                       var newHeight = initial_value + scope.currentMaxHeight[attrs.tndrHeightMaxCheck];
                       if(scope.calculated_max_height[attrs.tndrHeightMaxCheck] < newHeight) {
                           scope.calculated_max_height[attrs.tndrHeightMaxCheck] = newHeight;
                       }
                       scope.needToRecalculate = false;
                   };
                   scope.$watch('needToRecalculate', recalculate);
               }
           }
}).directive('tndrHeightMaxTarget', function() {
    return {
        restrict:'A',
        link:
            function(scope, element, attrs) {
                scope.$watchCollection('calculated_max_height', function(newVal, oldVal) {
                    if(!newVal) {
                        return;
                    }
                    if($(window).innerWidth() >= 768) {
                        //console.log("i need to change: ", oldVal, "->", newVal);
                        element.height(newVal[attrs.tndrHeightMaxTarget]);
                    }
                });
            }
    }
});
