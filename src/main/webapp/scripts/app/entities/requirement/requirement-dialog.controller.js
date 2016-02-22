'use strict';

angular.module('tenderworkApp').controller('RequirementDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Requirement', 'User', 'Principal', 'Estimate', 'Task',
        function($scope, $stateParams, $uibModalInstance, entity, Requirement, User, Principal, Estimate, Task) {

        $scope.requirement = entity;
        $scope.users = User.query();
        $scope.estimates = Estimate.query().$promise.then(function(results) {
            results.some(function(item) {
               if(item.id == $stateParams.id) {
                   $scope.attachToEstimate = item;//copyEstimate(item);
                   return true;
               }
            });
        });
        $scope.tasks = Task.query();
        $scope.estimateId = $stateParams.id;
        $scope.load = function(id) {
            Requirement.get({id : id}, function(result) {
                $scope.requirement = result;
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
                $scope.requirement.owner = $scope.currentUserAccount;
                $scope.requirement.ownerEstimate = $scope.attachToEstimate;
                Requirement.save($scope.requirement, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss({id:$scope.estimateId, message:'cancel'});
        };
}]);
