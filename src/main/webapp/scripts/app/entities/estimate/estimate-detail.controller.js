'use strict';

angular.module('tenderworkApp')
    .controller('EstimateDetailController', function ($scope, $rootScope, $stateParams, entity, Estimate, User, Project, Requirement, Principal, $timeout ) {
        $scope.estimate = $scope.helperFunctions.fillEmptyEntityDetails(entity);
        $rootScope.needToRecalculate += 1;
        $scope.load = function (id) {
            Estimate.get({id: id}, function(result) {
                $scope.estimate = result;
                $rootScope.needToRecalculate += 1;
            });
        };
        $scope.accordionVisible = {};

        $scope.accordionRecalculate = function(toggledBy) {
            if(typeof $scope.accordionVisible[toggledBy] == 'undefined') {
                $scope.accordionVisible[toggledBy] = true;
            } else {
                $scope.accordionVisible[toggledBy] = !$scope.accordionVisible[toggledBy];
            }
            if($scope.accordionVisible[toggledBy]) {
                $timeout(function() {
                    $rootScope.needToRecalculate += 1;
                }, 500);
            }
        };

        $scope.openAll = false;

        $scope.expandRequirements = function() {

        };

        $scope.isCreator = function(ent) {
            return Principal.isCreator(ent.createdBy);
        };

        // takes float and rounds it to nearest whole or half

        if(entity.resourcing !== null && typeof entity.resourcing != 'undefined') {
            $scope.roundedResourcing = $scope.helperFunctions.roundResourcing(entity.resourcing);
        }

        $scope.copyEstimate = function(estId) {
            Estimate.copy({id:estId}, function(result) {
                $state.go($state.current, {id:estId}, {reload:true});
            });
        };

        var unsubscribe = $rootScope.$on('tenderworkApp:estimateUpdate', function(event, result) {
            $scope.estimate = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
