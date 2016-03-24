'use strict';

angular.module('tenderworkApp').controller('TaskDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Task', 'User', 'Principal', 'Estimate', 'Requirement', 'Tag',
        function($scope, $stateParams, $uibModalInstance, entity, Task, User, Principal, Estimate, Requirement, Tag) {
            
        var createTag = function(tagTextObject) {
            return {
                id:null,
                name:tagTextObject.text,
            }
        };

        var updateTag = function(tag) {
            return {
                id:tag.id,
                name:tag.name,
            }
        };
            
        $scope.tags = Tag.query();
        $scope.task = entity;
        $scope.users = User.query();
        var setDefaultsForTask = function() {
            if($scope.task === null) {
                return;
            }
            $scope.task.ownerEstimate = $scope.estimateLinkedToTask;
            $scope.task.ownerRequirement = $scope.requirementLinkedToTask;
        };

        // creates a copy of a resource object stripping unnecessary info
        // params: Requirement   Resource-object
        var copyRequirement = function(inputResource) {
            return {
                id:inputResource.id,
                description:inputResource.description,
                durationImplementation:inputResource.durationImplementation,
                durationSpecification:inputResource.durationSpecification,
                durationTesting:inputResource.durationTesting,
                name:inputResource.name,
                owner:inputResource.owner,
                ownerEstimate:inputResource.ownerEstimate,
                synergyBenefit:inputResource.synergyBenefit,
                totalDuration:inputResource.totalDuration
            }
        };

        // creates a copy of a estimate resource stripping bunch of stuff
        // params: Estimate Resource-object
        var copyEstimate = function(inputResource) {
            return {
                createdBy:inputResource.createdBy,
                dailyPrice:inputResource.dailyPrice,
                desiredProjectDuration:inputResource.desiredProjectDuration,
                id:inputResource.id,
                implementationFactor:inputResource.implementationFactor,
                ownerProject:inputResource.ownerProject,
                resourcing:inputResource.resourcing,
                specificationFactor:inputResource.specificationFactor,
                synergyBenefit:inputResource.synergyBenefit,
                testingFactor:inputResource.testingFactor,
                totalDuration:inputResource.totalDuration,
                totalPrice:inputResource.totalPrice,
                totalSynergyBenefit:inputResource.totalSynergyBenefit,
                workdaysInMonth:inputResource.workdaysInMonth
            }
        };

        // this does nothing as of yet, need to finish html views
        Requirement.get({id:$stateParams.requirementId}, function(responseRequirement) {
            if(responseRequirement === null) {
                $scope.requirementLinkedToTask = null;
                $scope.estimateLinkedToTask = null;
                return;
            }
            $scope.requirementLinkedToTask = copyRequirement(responseRequirement);
            $scope.requirements = [$scope.requirementLinkedToTask];
            Estimate.get({id:responseRequirement.ownerEstimate.id}, function(responseEstimate) {
                if(responseEstimate === null) {
                    return;
                }
                $scope.estimateLinkedToTask = copyEstimate(responseEstimate);
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
            $scope.task.tags = $scope.tags.map(createTag);
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
