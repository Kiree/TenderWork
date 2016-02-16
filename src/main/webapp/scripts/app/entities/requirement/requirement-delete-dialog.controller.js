'use strict';

angular.module('tenderworkApp')
	.controller('RequirementDeleteController', function($scope, $uibModalInstance, entity, Requirement) {

        $scope.requirement = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Requirement.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
