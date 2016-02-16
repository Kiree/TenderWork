'use strict';

angular.module('tenderworkApp').controller('EstimateDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Estimate', 'User', 'Project', 'Requirement',
        function($scope, $stateParams, $uibModalInstance, entity, Estimate, User, Project, Requirement) {

        $scope.estimate = entity;
        $scope.users = User.query();
        $scope.projects = Project.query();
        $scope.requirements = Requirement.query();
        $scope.load = function(id) {
            Estimate.get({id : id}, function(result) {
                $scope.estimate = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('tenderworkApp:estimateUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.estimate.id != null) {
                Estimate.update($scope.estimate, onSaveSuccess, onSaveError);
            } else {
                Estimate.save($scope.estimate, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
