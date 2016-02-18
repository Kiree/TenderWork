'use strict';

angular.module('tenderworkApp').controller('EstimateDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Estimate', 'User', 'Principal', 'Project', 'Requirement',
        function($scope, $stateParams, $uibModalInstance, entity, Estimate, User, Principal, Project, Requirement) {

        $scope.estimate = entity;
        $scope.users = User.query();
        $scope.projects = Project.query();
        $scope.requirements = Requirement.query();
            console.log($scope.estimate);
        $scope.load = function(id) {
            Estimate.get({id : id}, function(result) {
                $scope.estimate = result;
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
                $scope.estimate.createdBy = $scope.currentUserAccount;
                Estimate.save($scope.estimate, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            console.log($scope.estimate);
            if($scope.estimate.projectId != null) {
                $uibModalInstance.dismiss({message:'cancel', id:$scope.estimate.projectId});
            }
        };
}]);
