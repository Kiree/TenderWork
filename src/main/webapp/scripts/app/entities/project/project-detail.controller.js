'use strict';

angular.module('tenderworkApp')
    .controller('ProjectDetailController', function ($window, $state, $scope, $rootScope, $stateParams, entity, Project, User, Estimate, ParseLinks, EstimateSearch, $translate, Tag, TagSearch, Principal) {
        if($stateParams.generateDefaultEstimate) {
            Estimate.save($stateParams.generateDefaultEstimate, function(r) {
                $state.go('project.detail', {pid:entity.id, generateDefaultEstimate:false }, { reload:true });
            }, function(r) {});
        }
        $scope.project = entity;
        $scope.estimates = [];
        $scope.tags = [];
        $scope.predicate = 'id';
        $scope.reverse = true;
        $scope.page = 0;
        $scope.currentMaxHeight = {};
        $scope.calclulated_max_height = {};
        $scope.load = function (id) {
            Project.get({id: id}, function(result) {
                $scope.project = result;
            });
        };
        $scope.empty = "\u00A0";

        if($scope.project.$resolved === false) {
            $scope.project.$promise.then(function(result) {
                $rootScope.needToRecalculate += 1;
            });
        } else {
            $rootScope.needToRecalculate += 1;
        }

        $scope.loadAll = function() {
            var found;
            EstimateSearch.query({query:"ownerProject.id:" + $scope.project.id}, function(result) {
                if(result === "undefined" || result === null) {
                    return;
                }
                for (var i = 0; i < result.length; i++) {
                    if(result[i].resourcing !== null && result[i].resourcing !== "undefined") {
                        result[i].rounded = $scope.helperFunctions.roundResourcing(result[i].resourcing);
                    }
                    found = $scope.helperFunctions.checkIfExists($scope.estimates, result[i]);
                    if(!found) {
                        $scope.estimates.push(result[i]);
                    }
                }
                $scope.project = $scope.helperFunctions.fillEmptyEntityDetails($scope.project);
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
        $scope.isCreator = function(ent) {
            return Principal.isCreator(ent.createdBy);
        };
        $rootScope.$on('$stateChangeSuccess', function(event, toState, toParams, fromState, fromParams) {
            //$rootScope.needToRecalculate = true;
        });

        var unsubscribe = $rootScope.$on('tenderworkApp:projectUpdate', function(event, result) {
            $scope.project = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
