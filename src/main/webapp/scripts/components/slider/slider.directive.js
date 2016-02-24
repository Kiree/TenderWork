/**
 * Created by jukka on 24.2.2016.
 */
angular.module('tenderworkApp').directive('tndSlider', [function() {
    return {
        restrict:'E',
        scope: {
            minimum:'=',
            maximum:'='
        },
        templateUrl:'slider-template.html'
    };
}]);
