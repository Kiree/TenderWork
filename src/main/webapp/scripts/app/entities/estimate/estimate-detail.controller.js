'use strict';

angular.module('tenderworkApp')
    .controller('EstimateDetailController', function ($scope, $rootScope, $stateParams, entity, Estimate, User, Project, Requirement, Principal ) {
        $scope.estimate = $scope.helperFunctions.fillEmptyEntityDetails(entity);

        $scope.load = function (id) {
            Estimate.get({id: id}, function(result) {
                $scope.estimate = result;

            });
        };

        $scope.isCreator = function(ent) {
            return Principal.isCreator(ent.createdBy);
        };
        // takes float and rounds it to nearest whole or half

        if(entity.resourcing !== null && entity.resourcing !== undefined) {
            $scope.roundedResourcing = $scope.helperFunctions.roundResourcing(entity.resourcing);
        }

        $scope.copyEstimate = function(estId) {
            // do copy here
            $state.go($state.current, {id:estId}, {reload:true});
        };

        var unsubscribe = $rootScope.$on('tenderworkApp:estimateUpdate', function(event, result) {
            $scope.estimate = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
