'use strict';

angular.module('tenderworkApp')
    .controller('TaskDetailController', function ($scope, $rootScope, $stateParams, entity, Task, User, Estimate, Requirement) {
        $scope.task = entity;
        $scope.load = function (id) {
            Task.get({id: id}, function(result) {
                $scope.task = result;
            });
        };
        var unsubscribe = $rootScope.$on('tenderworkApp:taskUpdate', function(event, result) {
            $scope.task = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
