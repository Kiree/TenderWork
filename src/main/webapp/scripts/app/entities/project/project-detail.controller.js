'use strict';

angular.module('tenderworkApp')
    .controller('ProjectDetailController', function ($window, $scope, $rootScope, $stateParams, entity, Project, User, Estimate, ParseLinks, EstimateSearch, $translate, Tag, TagSearch) {
        $scope.project = entity;
        $scope.estimates = [];
        $scope.tags = [];
        $scope.predicate = 'id';
        $scope.reverse = true;
        $scope.page = 0;
        $scope.load = function (id) {
            Project.get({id: id}, function(result) {
                $scope.project = result;
            });
        };

        // takes float and rounds it to nearest whole or half
        var roundResourcing = function(resourcing) {
            return resourcing - Math.floor(resourcing) > .5 ? Math.ceil(resourcing) : Math.floor(resourcing) + .5;
        };

        var checkIfExists = function(array, element) {
            return array.some(function(item) {
               return item.id === element.id;
            });
        };

        $scope.empty = "\u00A0";

        $scope.loadAll = function() {
            var found;
            EstimateSearch.query({query:"ownerProject.id:" + $scope.project.id}, function(result) {
                if(result === "undefined" || result === null) {
                    return;
                }
                for (var i = 0; i < result.length; i++) {
                    if(result[i].resourcing !== null && entity.resourcing !== undefined) {
                        result[i].rounded = roundResourcing(result[i].resourcing);
                    }
                    found = checkIfExists($scope.estimates, result[i]);
                    if(!found) {
                        $scope.estimates.push(result[i]);
                    }
                }
                console.log($scope.estimates);
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

        $rootScope.$on('$stateChangeSuccess', function(event, toState, toParams, fromState, fromParams) {
        //    calculateHeight();
        });

        var unsubscribe = $rootScope.$on('tenderworkApp:projectUpdate', function(event, result) {
            $scope.project = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
