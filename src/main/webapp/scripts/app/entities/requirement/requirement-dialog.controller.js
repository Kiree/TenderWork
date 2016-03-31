
'use strict';

angular.module('tenderworkApp').controller('RequirementDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Requirement', 'User', 'Principal', 'Estimate', 'Task', 'Tag',
        function($scope, $stateParams, $uibModalInstance, entity, Requirement, User, Principal, Estimate, Task, Tag) {

        var createTag = function(tagTextObject) {
            console.log(tagTextObject);
            if(tagTextObject.id === undefined) {
                return {
                    id:null,
                    name:tagTextObject.name
                };
            }
            return tagTextObject;
        };

        var updateTag = function(tag) {
            return {
                id:tag.id,
                name:tag.name,
            }
        };

        $scope.tags = entity.id === null ? [] : entity.tags;
        $scope.tagCloud = Tag.query();
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
            $scope.requirement.tags = $scope.tags.map(createTag);
            $scope.isSaving = true;
            var proper = $scope.requirement.tags.map(function(item) {
                $scope.tagCloud.some(function(cloudItem) {
                    if(item.name === cloudItem.name) {
                        item = cloudItem;
                        return true;
                    }
                    return false;
                });
                return item;
            });
            $scope.requirement.tags = proper;
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
