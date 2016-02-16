'use strict';

describe('Controller Tests', function() {

    describe('Estimate Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockEstimate, MockUser, MockProject, MockRequirement;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockEstimate = jasmine.createSpy('MockEstimate');
            MockUser = jasmine.createSpy('MockUser');
            MockProject = jasmine.createSpy('MockProject');
            MockRequirement = jasmine.createSpy('MockRequirement');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Estimate': MockEstimate,
                'User': MockUser,
                'Project': MockProject,
                'Requirement': MockRequirement
            };
            createController = function() {
                $injector.get('$controller')("EstimateDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'tenderworkApp:estimateUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
