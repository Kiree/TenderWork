'use strict';

angular.module('tenderworkApp').controller('TaskDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Task', 'User', 'Principal', 'Estimate', 'Requirement', 'Tag',
        function($scope, $stateParams, $uibModalInstance, entity, Task, User, Principal, Estimate, Requirement, Tag) {

        $scope.tags = entity.id === null ? [] : entity.tags;
        $scope.task = entity;
        $scope.users = User.query();
        $scope.tagCloud = Tag.query();
        $scope.tagFilter = function($query) {
            return $scope.helperFunctions.tagCloudFilter($query, $scope.tagCloud);
        };
        var setDefaultsForTask = function() {
            if($scope.task === null) {
                return;
            }
            $scope.task.ownerEstimate = $scope.estimateLinkedToTask;
            $scope.task.ownerRequirement = $scope.requirementLinkedToTask;
        };

        // this does nothing as of yet, need to finish html views
        Requirement.get({id:$stateParams.requirementId}, function(responseRequirement) {
            if(responseRequirement === null) {
                $scope.requirementLinkedToTask = null;
                $scope.estimateLinkedToTask = null;
                return;
            }
            $scope.requirementLinkedToTask = $scope.helperFunctions.copyRequirement(responseRequirement);
            $scope.requirements = [$scope.requirementLinkedToTask];
            Estimate.get({id:responseRequirement.ownerEstimate.id}, function(responseEstimate) {
                if(responseEstimate === null) {
                    return;
                }
                $scope.estimateLinkedToTask = $scope.helperFunctions.copyEstimate(responseEstimate);
                console.log($scope.estimateLinkedToTask);
                $scope.estimates = [$scope.estimateLinkedToTask];
                setDefaultsForTask();
            });
        });

        $scope.load = function(id) {
            Task.get({id : id}, function(result) {
                $scope.task = result;
            });
        };

        Principal.identity().then(function(account) {
            $scope.myAccount = $scope.helperFunctions.copyAccount(account);
            User.get({login: $scope.myAccount.login}, function(result) {
                $scope.currentUserAccount = result;
                });
            });

            var onSaveSuccess = function (result) {
            $scope.$emit('tenderworkApp:taskUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.task.tags = $scope.tags.map($scope.helperFunctions.createTag);
            $scope.isSaving = true;
            var proper = $scope.task.tags.map(function(item) {
                $scope.tagCloud.some(function(cloudItem) {
                    if(item.name === cloudItem.name) {
                        item = cloudItem;
                        return true;
                    }
                    return false;
                });
                return item;
            });
            $scope.task.tags = proper;
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
