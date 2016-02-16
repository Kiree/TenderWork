'use strict';

angular.module('tenderworkApp').controller('TaskDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Task', 'User', 'Estimate', 'Requirement',
        function($scope, $stateParams, $uibModalInstance, entity, Task, User, Estimate, Requirement) {

        $scope.task = entity;
        $scope.users = User.query();
        $scope.estimates = Estimate.query();
        $scope.requirements = Requirement.query();
        $scope.load = function(id) {
            Task.get({id : id}, function(result) {
                $scope.task = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('tenderworkApp:taskUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.task.id != null) {
                Task.update($scope.task, onSaveSuccess, onSaveError);
            } else {
                Task.save($scope.task, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
