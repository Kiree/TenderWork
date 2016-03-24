'use strict';

angular.module('tenderworkApp')
    .controller('TaskDetailController', function ($scope, $rootScope, $stateParams, entity, Task, User, Estimate, Requirement, Tag, TagSearch) {
        $scope.task = entity;
        $scope.tags = [];
        $scope.load = function (id) {
            Task.get({id: id}, function(result) {
                $scope.task = result;
            });
        };

        var checkIfExists = function(array, element) {
            return array.some(function(item) {
                return item === element;
            });
        };

        $scope.loadAll = function () {
            var found;
            TagSearch.query({query:"belongsToTaskss.id:" + $scope.task.id}, function(result) {
                if(result === "undefined" || result === null) {
                    return;
                }
                for (var i = 0; i < result.length; i++) {
                    found = checkIfExists($scope.tags, result[i]);
                    if(!found) {
                        $scope.tags.push(result[i]);
                    }
                }
            });
        };
        
        var unsubscribe = $rootScope.$on('tenderworkApp:taskUpdate', function(event, result) {
            $scope.task = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
