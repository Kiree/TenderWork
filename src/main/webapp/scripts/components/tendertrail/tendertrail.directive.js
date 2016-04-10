/**
 * Created by jukka on 6.4.2016.
 */
angular.module('tenderworkApp')
.directive('tenderTrail', function() {
    return {
        controller:'TenderTrailController',
        templateUrl:'scripts/components/tendertrail/tendertrail.template.html'
    }
});
