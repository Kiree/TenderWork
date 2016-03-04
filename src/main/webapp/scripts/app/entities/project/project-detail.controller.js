'use strict';

angular.module('tenderworkApp')
    .controller('ProjectDetailController', function ($window, $scope, $rootScope, $stateParams, entity, Project, User, Estimate, ParseLinks, EstimateSearch, $translate) {
        $scope.project = entity;
        $scope.estimates = [];
        $scope.predicate = 'id';
        $scope.reverse = true;
        $scope.page = 0;
        $scope.load = function (id) {
            Project.get({id: id}, function(result) {
                $scope.project = result;
            });
        };
        // 768 is the limit for change
        // @ 1200 is the max
        var calculateHeight = function() {
            if($scope.project.docLocation===undefined) {
                return 0;
            }
            var initial_value = 150;
            var row_height = 8;
            var width = $window.innerWidth < 1200 ? $window.innerWidth : 1200;
            width = width < 768 ? 768 : width;
            width = width < 992 ? 992 * .2 : width;
            var multiplier = 1200 / width;
            var approximate_rows = Math.ceil($scope.project.docLocation.length / (100 * multiplier));
            return initial_value + (row_height * multiplier) * approximate_rows; //$scope.calculated_height = initial_value * multiplier;
        };

        // takes float and rounds it to nearest whole or half
        var roundResourcing = function(resourcing) {
            return resourcing - Math.floor(resourcing) > .5 ? Math.ceil(resourcing) : Math.floor(resourcing) + .5;
        };

        $scope.loadAll = function() {
            $scope.calculated_height = calculateHeight();
            EstimateSearch.query({query:"ownerProject.id:" + $scope.project.id}, function(result) {
                for (var i = 0; i < result.length; i++) {
                    if(result[i].resourcing !== null && entity.resourcing !== undefined) {
                        result[i].rounded = roundResourcing(result[i].resourcing);
                    }
                    $scope.estimates.push(result[i]);
                }
            });
/*
            Estimate.query({page: $scope.page, size: 20, sort: [$scope.predicate + ',' + ($scope.reverse ? 'asc' : 'desc'), 'id']}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                result = result.filter(filterByProjectId);
                for (var i = 0; i < result.length; i++) {
                    $scope.estimates.push(result[i]);
                }
            }); */
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

        $rootScope.$on('$stateChangeSuccess', function(event, toState, toParams, fromState, fromParams) {
            $scope.calculated_height = calculateHeight();
        });

        var unsubscribe = $rootScope.$on('tenderworkApp:projectUpdate', function(event, result) {
            $scope.project = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
