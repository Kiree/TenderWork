'use strict';

angular.module('tenderworkApp')
    .controller('TaskController', function ($scope, $state, Task, TaskSearch, ParseLinks, Principal) {

        //$scope.tasks = [];
        $scope.predicate = 'id';
        $scope.reverse = true;
        $scope.page = 0;
        $scope.loadAll = function() {
            Task.query({page: $scope.page, size: 20, sort: [$scope.predicate + ',' + ($scope.reverse ? 'asc' : 'desc'), 'id']}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                for (var i = 0; i < result.length; i++) {
         //           $scope.tasks.push(result[i]);
                }
            });
        };
        $scope.isCreator = function(task) {
            return Principal.isCreator(task.ownedBy);
        };
        $scope.deleteTask = function(task) {
            if(Principal.isCreator(task.ownedBy)) {
                Task.delete({id: task.id}, function() {
                    $state.go($state.current, {}, {reload: true});
                });
            }
        };
        $scope.reset = function() {
            $scope.page = 0;
            $scope.tasks = [];
            $scope.loadAll();
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();


        $scope.search = function () {
            TaskSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.tasks = result;
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
            $scope.task = {
                name: null,
                description: null,
                estimateSpecification: null,
                estimateImplementation: null,
                estimateTesting: null,
                estimateSynergy: null,
                synergyCheck: false,
                specificationTotal: null,
                implementationTotal: null,
                testingTotal: null,
                synergyTotal: null,
                estimateTotal: null,
                id: null
            };
        };
    });
