'use strict';

angular.module('tenderworkApp')
    .controller('EstimateDetailController', function ($scope, $rootScope, $stateParams, entity, Estimate, User, Project, Requirement) {
        $scope.estimate = entity;

        $scope.load = function (id) {
            Estimate.get({id: id}, function(result) {
                $scope.estimate = result;

            });
        };
        // takes float and rounds it to nearest whole or half

        if(entity.resourcing !== null && entity.resourcing !== undefined) {
            $scope.roundedResourcing = $scope.helperFunctions.roundResourcing(entity.resourcing);
        }

        var unsubscribe = $rootScope.$on('tenderworkApp:estimateUpdate', function(event, result) {
            $scope.estimate = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
