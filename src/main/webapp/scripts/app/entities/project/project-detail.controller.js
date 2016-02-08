'use strict';

angular.module('tenderworkApp')
    .controller('ProjectDetailController', function ($scope, $rootScope, $stateParams, entity, Project, Estimate) {
        $scope.project = entity;
        $scope.load = function (id) {
            Project.get({id: id}, function(result) {
                $scope.project = result;
            });
        };
        var unsubscribe = $rootScope.$on('tenderworkApp:projectUpdate', function(event, result) {
            $scope.project = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
