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
        var roundResourcing = function(resourcing) {
            return resourcing - Math.floor(resourcing) > .5 ? Math.ceil(resourcing) : Math.floor(resourcing) + .5;
        };
        if(entity.resourcing !== null && entity.resourcing !== undefined) {
            $scope.roundedResourcing = roundResourcing(entity.resourcing);
        }

        var unsubscribe = $rootScope.$on('tenderworkApp:estimateUpdate', function(event, result) {
            $scope.estimate = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
