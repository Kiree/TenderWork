'use strict';

angular.module('tenderworkApp').controller('TaskDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Task', 'User', 'Principal', 'Estimate', 'Requirement',
        function($scope, $stateParams, $uibModalInstance, entity, Task, User, Principal, Estimate, Requirement) {

        $scope.task = entity;
        $scope.users = User.query();
        $scope.estimates = Estimate.query();
        $scope.requirements = Requirement.query();
        $scope.load = function(id) {
            Task.get({id : id}, function(result) {
                $scope.task = result;
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
                $scope.task.ownedBy = $scope.currentUserAccount;
                Task.save($scope.task, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
