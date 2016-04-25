'use strict';

angular.module('tenderworkApp')
    .controller('RequirementController', function ($rootScope, $scope, $state, Requirement, RequirementSearch, ParseLinks, Task, TaskSearch, Principal, Estimate) {
        $scope.requirements = [];
        $scope.tasks = [];
        $scope.predicate = 'id';
        $scope.reverse = true;
        $scope.page = 0;
        $scope.empty = "\u00A0";
        var populated = false;
        /*
        // searches for requirements and their tasks and builds an array out of the results
        // the array is an associative array - key being the parent requirement id
        // and the contents being an array with all tasks associated with that particular
        // requirement
        // ie:
        {
        requirement-id:n,
        tasks:[]
        }
            req-id = 1:
                [
                    task-1,
                    task 3
                ]
            req-id = 2:
                [
                    task 4
                ]
            req-id = 3:
                [
                    task-2,
                    task-5
                ]
         */
        $scope.loadAll = function() {
            if($scope.estimate) {
                $rootScope.roundedResourcing = $scope.helperFunctions.roundResourcing($scope.estimate.resourcing);
                RequirementSearch.query({query: "ownerEstimate.id:" + $scope.estimate.id}, function (result) {
                    if (populated) {
                        return;
                    }
                    for (var i = 0; i < result.length; i++) {
                        $scope.requirements.push($scope.helperFunctions.fillEmptyEntityDetails(result[i]));
                        TaskSearch.query({query: "ownerRequirement.id:" + result[i].id}, function(results_tasks) {
                            $scope.needToRecalculate = true;
                            if(results_tasks.length > 0) {
                                var tasksContainer = {requirementId: results_tasks[0].ownerRequirement.id, tasks: []};
                                var tasksArray = [];
                                for (var j = 0; j < results_tasks.length; j++) {
                                    tasksArray.push($scope.helperFunctions.fillEmptyEntityDetails(results_tasks[j]));
                                }
                                tasksContainer.tasks = tasksArray;
                                $scope.tasks.push(tasksContainer);
                            }
                        });
                    }
                    populated = true;
                });
            } else {
                Requirement.query({
                    page: $scope.page,
                    size: 20,
                    sort: [$scope.predicate + ',' + ($scope.reverse ? 'asc' : 'desc'), 'id']
                }, function (result, headers) {
                    $scope.links = ParseLinks.parse(headers('link'));
                    for (var i = 0; i < result.length; i++) {
                        $scope.requirements.push(result[i]);
                    }
                });
            }
        };
        $scope.reset = function() {
            $scope.page = 0;
            $scope.requirements = [];
            $scope.loadAll();
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        if($scope.estimate.$resolved === false) {
            console.log("estimate not resolved yet!");
            $scope.estimate.$promise.then($scope.loadAll);
            $scope.needToRecalculate = true;

        } else {
            console.log("estimate resolved!");
            $scope.loadAll();
            $scope.needToRecalculate = true;
        }

        $scope.isCreator = function(ent) {
            return Principal.isCreator(ent.owner);
        };
        $scope.search = function () {
            RequirementSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.requirements = result;
            }, function(response) {
                if(response.status === 404) {
                    $scope.loadAll();
                }
            });
        };

        $scope.refresh = function () {
            $scope.reset();
            $scope.clear();
        };

        $scope.copyRequirement = function(reqId) {
            Requirement.copy({id:reqId}, function(result) {
                Estimate.update($scope.estimate, function() {
                    console.log('succes');
                        $state.go($state.current, {id:$scope.estimate.id }, {reload:true});
                }, function() {
                    console.log('fail');
                        $state.go($state.current, {id:$scope.estimate.id }, {reload:true});
                }
                );

            });

        };

        $scope.clear = function () {
            $scope.requirement = {
                name: null,
                description: null,
                totalDuration: null,
                durationSpecification: null,
                durationImplementation: null,
                durationTesting: null,
                synergyBenefit: null,
                id: null
            };
        };
    });
