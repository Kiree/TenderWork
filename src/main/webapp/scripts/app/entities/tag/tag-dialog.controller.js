'use strict';

angular.module('tenderworkApp').controller('TagDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Tag', 'Project', 'Requirement', 'Task',
        function($scope, $stateParams, $uibModalInstance, entity, Tag, Project, Requirement, Task) {

        $scope.tag = entity;
        $scope.projects = Project.query();
        $scope.requirements = Requirement.query();
        $scope.tasks = Task.query();
        $scope.load = function(id) {
            Tag.get({id : id}, function(result) {
                $scope.tag = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('tenderworkApp:tagUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.tag.id != null) {
                Tag.update($scope.tag, onSaveSuccess, onSaveError);
            } else {
                Tag.save($scope.tag, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
