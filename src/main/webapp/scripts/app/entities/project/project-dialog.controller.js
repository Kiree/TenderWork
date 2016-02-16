'use strict';

angular.module('tenderworkApp').controller('ProjectDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Project', 'User', 'Estimate', 'Principal',
        function($scope, $stateParams, $uibModalInstance, entity, Project, User, Estimate, Principal) {

        $scope.project = entity;
        $scope.users = User.query();
        $scope.estimates = Estimate.query();
        $scope.states = ['Uusi', 'Tarjous jätetty', 'Voitettu', 'Hävitty', 'Suljettu'];
        $scope.defaultState = 'Uusi';

        $scope.load = function(id) {
            Project.get({id : id}, function(result) {
                $scope.project = result;
            });
        };

        Principal.identity().then(function(account) {
            $scope.myAccount = copyAccount(account);
            User.get({login: $scope.myAccount.login}, function(result) {
                $scope.currentUserAccount = result;
            });
        });


        var copyAccount = function(account) {
            return {
                activated:account.activated,
                email:account.email,
                firstName:account.firstName,
                langKey: account.langKey,
                lastName: account.lastName,
                login: account.login
            }
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
            var today = new Date();
            $scope.project.editedBy = $scope.currentUserAccount;
            $scope.project.editedDate = today;
            $scope.isSaving = true;
            if ($scope.project.id != null) {
                Project.update($scope.project, onSaveSuccess, onSaveError);
            } else {
                $scope.project.createdDate = today;
                $scope.project.createdBy = $scope.currentUserAccount;
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
