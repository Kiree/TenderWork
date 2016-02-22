'use strict';

angular.module('tenderworkApp')
    .controller('RequirementController', function ($scope, $state, Requirement, RequirementSearch, ParseLinks) {

        $scope.requirements = [];
        $scope.predicate = 'id';
        $scope.reverse = true;
        $scope.page = 0;
        $scope.loadAll = function() {
            RequirementSearch.query({query:"ownerEstimate.id:" + $scope.estimate.id}, function(result) {
                for (var i = 0; i < result.length; i++) {
                    $scope.requirements.push(result[i]);
                }
            });
            /*
            Requirement.query({page: $scope.page, size: 20, sort: [$scope.predicate + ',' + ($scope.reverse ? 'asc' : 'desc'), 'id']}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                for (var i = 0; i < result.length; i++) {
                    $scope.requirements.push(result[i]);
                }
            });*/
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
