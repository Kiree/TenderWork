'use strict';

angular.module('tenderworkApp')
    .controller('RequirementController', function ($scope, $state, Requirement, RequirementSearch, ParseLinks, Task, TaskSearch) {

        $scope.requirements = [];
        $scope.tasks = [];
        $scope.predicate = 'id';
        $scope.reverse = true;
        $scope.page = 0;
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
                RequirementSearch.query({query: "ownerEstimate.id:" + $scope.estimate.id}, function (result) {
                    if (populated) {
                        return;
                    }
                    for (var i = 0; i < result.length; i++) {
                        $scope.requirements.push(result[i]);
                        TaskSearch.query({query: "ownerRequirement.id:" + result[i].id}, function(results_tasks) {
                            var tasksContainer = {requirementId:results_tasks[0].ownerRequirement.id, tasks:[]};
                            var tasksArray = [];
                            console.log($scope.requirements);
                            for (var j = 0; j < results_tasks.length; j++) {
                                tasksArray.push(results_tasks[j]);
                            }
                            tasksContainer.tasks = tasksArray;
                            console.log($scope.tasks, tasksContainer);
                            $scope.tasks.push(tasksContainer);
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
        $scope.loadAll();


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
