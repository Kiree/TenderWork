'use strict';

angular.module('tenderworkApp').controller('EstimateDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Estimate', 'User', 'Project', 'Requirement', 'Principal',
        function($scope, $stateParams, $uibModalInstance, entity, Estimate, User, Project, Requirement, Principal) {
        $scope.estimateId = $stateParams.id;
        $scope.estimate = entity;
        $scope.users = User.query();
        $scope.projects = Project.query().$promise.then(function(results) {
            results.some(function(item) {
                if (item.id == $stateParams.id) {
                    $scope.attachToProject = copyProject(item);
                    return true;
                }
            });
        });
        $scope.requirements = Requirement.query();
        $scope.currentUserAccount = null;

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

        var copyProject = function(project) {
            console.log("Got: ", project);
            return {
                id:project.id,
                client:project.client,
                createdBy:project.createdBy,
                createdDate:project.createdDate,
                deadline:project.deadline,
                description:project.description,
                editedBy:project.editedBy,
                editedDate:project.editedDate,
                name:project.name,
                state:project.state,
                stateDescription:project.stateDescription
            }
        };

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
                $scope.estimate.ownerProject = $scope.attachToProject;
                Estimate.save($scope.estimate, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');

        };
}]);
