'use strict';

angular.module('tenderworkApp')
    .controller('RequirementDetailController', function ($scope, $rootScope, $stateParams, entity, Requirement, User, Estimate, Task) {
        $scope.requirement = entity;
        $scope.load = function (id) {
            Requirement.get({id: id}, function(result) {
                $scope.requirement = result;
            });
        };
        var unsubscribe = $rootScope.$on('tenderworkApp:requirementUpdate', function(event, result) {
            $scope.requirement = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
