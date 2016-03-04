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
        // calculates the height for the doclocation description box at runtime, which is then set as the min- & max-height
        // for both columns via ng-style.  items searched through are tagged with check-for-height
        var calculateHeight = function() {
            var initial_value = 100;
            $scope.calculated_height = initial_value;
            if($scope.project.docLocation===undefined) {
                return;
            }
            var current_max = 0;
            var current_height = 0;
            $('.check-for-height').each(function(item) {
                current_height = parseInt($(this).css('height'));
                if (current_max < current_height) {
                    current_max = current_height;
                }
            });
            $scope.calculated_height = initial_value + current_max;
        };

        // takes float and rounds it to nearest whole or half
        var roundResourcing = function(resourcing) {
            return resourcing - Math.floor(resourcing) > .5 ? Math.ceil(resourcing) : Math.floor(resourcing) + .5;
        };

        $scope.loadAll = function() {
            calculateHeight();
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
