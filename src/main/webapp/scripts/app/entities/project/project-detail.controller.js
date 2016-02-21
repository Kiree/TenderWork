'use strict';

angular.module('tenderworkApp')
    .controller('ProjectDetailController', function ($scope, $rootScope, $stateParams, entity, Project, User, Estimate, ParseLinks) {
        $scope.project = entity;
        $scope.estimates = [];
        $scope.predicate = 'id';
        $scope.reverse = true;
        $scope.page = 0;
        $scope.load = function (id) {
            console.log("loadding");
            Project.get({id: id}, function(result) {
                $scope.project = result;
            });
        };

        var filterByProjectId = function(estimate) {
            return (estimate.ownerProject.id == $scope.project.id);
        };

        $scope.loadAll = function() {
            console.log("loading all");
            var debug = {page:$scope.page, reverse:$scope.reverse, predicate:$scope.predicate};
            Estimate.query({page: $scope.page, size: 20, sort: [$scope.predicate + ',' + ($scope.reverse ? 'asc' : 'desc'), 'id']}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                result = result.filter(filterByProjectId);
                for (var i = 0; i < result.length; i++) {
                    $scope.estimates.push(result[i]);
                }
            });
        };
        $scope.reset = function() {
            console.log("in reset");
            $scope.page = 0;
            $scope.estimates = [];
            $scope.loadAll();
        };
        $scope.loadPage = function(page) {
            console.log("load page");
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();



        var unsubscribe = $rootScope.$on('tenderworkApp:projectUpdate', function(event, result) {
            console.log("unsubbing");
            $scope.project = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
