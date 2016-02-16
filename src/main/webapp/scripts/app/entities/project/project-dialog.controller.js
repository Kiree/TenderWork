'use strict';

angular.module('tenderworkApp').controller('ProjectDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Project', 'User', 'Estimate',
        function($scope, $stateParams, $uibModalInstance, entity, Project, User, Estimate) {

        $scope.project = entity;
        $scope.users = User.query();
        $scope.estimates = Estimate.query();
        $scope.load = function(id) {
            Project.get({id : id}, function(result) {
                $scope.project = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('tenderworkApp:projectUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.project.id != null) {
                Project.update($scope.project, onSaveSuccess, onSaveError);
            } else {
                Project.save($scope.project, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.datePickerForDeadline = {};

        $scope.datePickerForDeadline.status = {
            opened: false
        };

        $scope.datePickerForDeadlineOpen = function($event) {
            $scope.datePickerForDeadline.status.opened = true;
        };
        $scope.datePickerForCreatedDate = {};

        $scope.datePickerForCreatedDate.status = {
            opened: false
        };

        $scope.datePickerForCreatedDateOpen = function($event) {
            $scope.datePickerForCreatedDate.status.opened = true;
        };
        $scope.datePickerForEditedDate = {};

        $scope.datePickerForEditedDate.status = {
            opened: false
        };

        $scope.datePickerForEditedDateOpen = function($event) {
            $scope.datePickerForEditedDate.status.opened = true;
        };
}]);
