'use strict';

angular.module('tenderworkApp')
	.controller('TestityDeleteController', function($scope, $uibModalInstance, entity, Testity) {

        $scope.testity = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Testity.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
