'use strict';

angular.module('tenderworkApp')
    .controller('EstimateController', function ($scope, $state, Estimate, EstimateSearch, ParseLinks) {

        $scope.estimates = [];
        $scope.predicate = 'id';
        $scope.reverse = true;
        $scope.page = 0;
        $scope.loadAll = function() {
            Estimate.query({page: $scope.page, size: 20, sort: [$scope.predicate + ',' + ($scope.reverse ? 'asc' : 'desc'), 'id']}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                for (var i = 0; i < result.length; i++) {
                    $scope.estimates.push(result[i]);
                }
            });
        };
        $scope.reset = function() {
            $scope.page = 0;
            $scope.estimates = [];
            $scope.loadAll();
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();


        $scope.search = function () {
            EstimateSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.estimates = result;
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
            $scope.estimate = {
                creator: null,
                workDays: null,
                duration: null,
                cost: null,
                multiSpec: null,
                multiImp: null,
                multiTest: null,
                multiSyn: null,
                overallCost: null,
                overallDuration: null,
                overallResources: null,
                overallGain: null,
                id: null
            };
        };
    });
