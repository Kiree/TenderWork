'use strict';

angular.module('tenderworkApp')
    .controller('RequirementDetailController', function ($scope, $rootScope, $stateParams, entity, User, Estimate, Requirement, Task, Tag, TagSearch) {
        $scope.requirement = entity;
        $scope.tags = [];
        $scope.load = function (id) {
            Requirement.get({id: id}, function(result) {
                $scope.requirement = result;
            });
        };

        var checkIfExists = function(array, element) {
            return array.some(function(item) {
                return item === element;
            });
        };

        $scope.loadAll = function () {
            var found;
            TagSearch.query({query:"belongsToRequirementss.id:" + $scope.requirement.id}, function(result) {
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

        var unsubscribe = $rootScope.$on('tenderworkApp:requirementUpdate', function(event, result) {
            $scope.requirement = result;
        });
        $scope.$on('$destroy', unsubscribe);

        $(function() {
            var $research = $('.research');
            $research.find("tr").not('.accordion').hide();
            $research.find("tr").eq(0).show();

            $research.find(".accordion").click(function(){
                $research.find('.accordion').not(this).siblings().fadeOut(500);
                $(this).siblings().fadeToggle(500);
            }).eq(0).trigger('click');
        });

    });
