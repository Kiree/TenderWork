'use strict';

angular.module('tenderworkApp')
    .controller('EstimateDetailController', function ($scope, $rootScope, $stateParams, entity, Estimate, User, Project) {
        $scope.estimate = entity;
        $scope.load = function (id) {
            Estimate.get({id: id}, function(result) {
                $scope.estimate = result;
            });
        };
        var unsubscribe = $rootScope.$on('tenderworkApp:estimateUpdate', function(event, result) {
            $scope.estimate = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
