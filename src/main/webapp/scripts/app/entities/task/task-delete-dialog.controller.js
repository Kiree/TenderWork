'use strict';

angular.module('tenderworkApp')
	.controller('TaskDeleteController', function($scope, $uibModalInstance, entity, Task) {

        $scope.task = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Task.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
