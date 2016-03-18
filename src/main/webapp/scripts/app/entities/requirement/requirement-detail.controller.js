'use strict';

angular.module('tenderworkApp')
    .controller('RequirementDetailController', function ($scope, $rootScope, $stateParams, entity, Requirement, User, Estimate, Task) {
        $scope.requirement = entity;
        $scope.load = function (id) {
            Requirement.get({id: id}, function(result) {
                $scope.requirement = result;
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
