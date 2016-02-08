'use strict';

angular.module('tenderworkApp')
	.controller('EstimateDeleteController', function($scope, $uibModalInstance, entity, Estimate) {

        $scope.estimate = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Estimate.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
