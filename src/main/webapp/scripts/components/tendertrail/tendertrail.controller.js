/**
 * Created by jukka on 6.4.2016.
 */
angular.module('tenderworkApp').controller('TenderTrailController', function($state, $scope, $rootScope) {
    $scope.trail = [];
    var buildTrail = function(toParams, fromParams) {
        var projectId = typeof fromParams.pid != 'undefined' ? fromParams.pid : null;
        projectId = typeof toParams.pid != 'undefined' ? toParams.pid : projectId;

        var estimateId = typeof fromParams.eid != 'undefined' ? fromParams.eid : null;
        estimateId = typeof toParams.eid != 'undefined' ? toParams.eid : estimateId;

        $scope.trail = [];

        var currentState = $state.current;
        while(typeof currentState.parent != 'undefined') {
            if(currentState.abstract != true) {
                $scope.trail.unshift({name:currentState.name, params:{pid:projectId, eid:estimateId}});
            }
            currentState = $state.get(currentState.parent);
        }
    };

    $rootScope.$on('$stateChangeSuccess', function(event, toState, toParams, fromState, fromParams) {
        buildTrail(toParams, fromParams);
    });
});
