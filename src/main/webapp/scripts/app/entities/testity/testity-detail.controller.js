'use strict';

angular.module('tenderworkApp')
    .controller('TestityDetailController', function ($scope, $rootScope, $stateParams, entity, Testity) {
        $scope.testity = entity;
        $scope.load = function (id) {
            Testity.get({id: id}, function(result) {
                $scope.testity = result;
            });
        };
        var unsubscribe = $rootScope.$on('tenderworkApp:testityUpdate', function(event, result) {
            $scope.testity = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
