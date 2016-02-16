'use strict';

angular.module('tenderworkApp').controller('RequirementDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Requirement', 'User', 'Estimate', 'Task',
        function($scope, $stateParams, $uibModalInstance, entity, Requirement, User, Estimate, Task) {

        $scope.requirement = entity;
        $scope.users = User.query();
        $scope.estimates = Estimate.query();
        $scope.tasks = Task.query();
        $scope.load = function(id) {
            Requirement.get({id : id}, function(result) {
                $scope.requirement = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('tenderworkApp:requirementUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.requirement.id != null) {
                Requirement.update($scope.requirement, onSaveSuccess, onSaveError);
            } else {
                Requirement.save($scope.requirement, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
