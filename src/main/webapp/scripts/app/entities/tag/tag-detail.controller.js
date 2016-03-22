'use strict';

angular.module('tenderworkApp')
    .controller('TagDetailController', function ($scope, $rootScope, $stateParams, entity, Tag, Project) {
        $scope.tag = entity;
        $scope.load = function (id) {
            Tag.get({id: id}, function(result) {
                $scope.tag = result;
            });
        };
        var unsubscribe = $rootScope.$on('tenderworkApp:tagUpdate', function(event, result) {
            $scope.tag = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
