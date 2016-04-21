'use strict';

angular.module('tenderworkApp').controller('EstimateDialogController',
    ['$scope', '$rootScope','$stateParams', '$uibModalInstance', 'entity', 'Estimate', 'User', 'Project', 'Requirement', 'Principal',
        function($scope, $rootScope, $stateParams, $uibModalInstance, entity, Estimate, User, Project, Requirement, Principal) {
        $scope.slider= {
            options: {
                step:.01,
                floor:0,
                ceil:1,
                precision:1
            }
        };
        var defaultValues = function(entity) {
            if (entity.id === null) {
                entity.workdaysInMonth = 21;
                entity.dailyPrice = 500;
                entity.desiredProjectDuration = 3;
                entity.testingFactor = 1;
                entity.implementationFactor = 1;
                entity.specificationFactor = 1;
            }
            return entity;
        };
        $scope.page = 0;
        $scope.estimate = defaultValues(entity);
        $scope.users = User.query();
        $scope.attachToProject = Project.get({id:$stateParams.pid}, function() {
            $rootScope.$broadcast('rzSliderForceRender');  // get project and refresh sliders
        });

        $scope.currentUserAccount = null;

        $scope.load = function(id) {
            Estimate.get({id : id}, function(result) {
                $scope.estimate = result;
            });
        };

        Principal.identity().then(function(account) {
            $scope.myAccount = $scope.helperFunctions.copyAccount(account);
            User.get({login: $scope.myAccount.login}, function(result) {
                $scope.currentUserAccount = result;
            });
        });

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
