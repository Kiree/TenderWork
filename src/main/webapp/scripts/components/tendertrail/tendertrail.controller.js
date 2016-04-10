/**
 * Created by jukka on 6.4.2016.
 */
angular.module('tenderworkApp').controller('TenderTrailController', function($state, $scope, $rootScope) {
    $scope.trail = [];
    var buildTrail = function(toParams, fromParams) {
        var projectId = typeof fromParams.projectId != 'undefined' ? fromParams.projectId : null;
        projectId = typeof toParams.projectId != 'undefined' ? toParams.projectId : projectId;
        var estimateId = typeof fromParams.estimateId != 'undefined' ? fromParams.estimateId : null;
        estimateId = typeof toParams.estimateId != 'undefined' ? toParams.estimateId : estimateId;
        $scope.trail = [];
        var currentState = $state.current;
        while(typeof currentState.parent != 'undefined') {
            if(currentState.abstract != true) {
                $scope.trail.unshift({name:currentState.name, params:{projectId:projectId, estimateId:estimateId}});
            }
            currentState = $state.get(currentState.parent);
        }
    };

    $rootScope.$on('$stateChangeSuccess', function(event, toState, toParams, fromState, fromParams) {
        buildTrail(toParams, fromParams);
    });
});
