
'use strict';

angular.module('tenderworkApp').controller('RequirementDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Requirement', 'User', 'Principal', 'Estimate', 'Task', 'Tag',
        function($scope, $stateParams, $uibModalInstance, entity, Requirement, User, Principal, Estimate, Task, Tag) {

        var updateTag = function(tagTextObject) {
            if(tagTextObject.id === undefined) {
                return {
                    id: tagTextObject.id,
                    name: tagTextObject.name
                }
            }
            return tagTextObject;
        };
        if(entity.$resolved) {
            console.log('requirement resolved');
            $scope.tags = entity.id === null ? [] : entity.tags;
        } else {
            if(typeof entity.$resolved == 'undefined') {
                $scope.tags = entity.id === null ? [] : entity.tags;
            } else {
                console.log('requirement not yet resolved');
                entity.$promise.then(function() {
                    $scope.tags = entity.id === null ? [] : entity.tags;
                });
            }
        }
        $scope.tagCloud = Tag.query();
        $scope.tagFilter = function($query) {
            return $scope.helperFunctions.tagCloudFilter($query, $scope.tagCloud);
        };
        $scope.page = 0;
        $scope.requirement = entity;
        $scope.users = User.query();
        var loadEstimates = function() {
            $scope.estimates = Estimate.query({
                page:$scope.page,
                size:20
            }).$promise.then(function(results) {
                results.some(function(item) {
                   if(item.id == $stateParams.eid) {
                       $scope.attachToEstimate = $scope.helperFunctions.copyEstimate(item);//copyEstimate(item);
                       return true;
                   }
                });
                if(!$scope.attachToEstimate) {
                    $scope.page += 1;
                    loadEstimates();
                } else {
                    $scope.page = 0;
                }

            });
        };
        loadEstimates(0);
        $scope.tasks = Task.query();
        $scope.eid = $stateParams.eid;
        $scope.load = function(id) {
            Requirement.get({id : id}, function(result) {
                $scope.requirement = result;
            });
        };

        Principal.identity().then(function(account) {
            $scope.myAccount = $scope.helperFunctions.copyAccount(account);
            User.get({login: $scope.myAccount.login}, function(result) {
                $scope.currentUserAccount = result;
            });
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('tenderworkApp:requirementUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.requirement.tags = typeof $scope.tags == 'undefined' ? [] : $scope.tags.map($scope.helperFunctions.createTag);
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
            $uibModalInstance.dismiss({id:$scope.eid, message:'cancel'});
        };
}]);
