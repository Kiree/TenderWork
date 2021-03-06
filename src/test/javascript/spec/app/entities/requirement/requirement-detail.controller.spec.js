'use strict';

describe('Controller Tests', function() {

    describe('Requirement Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockRequirement, MockUser, MockEstimate, MockTask;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockRequirement = jasmine.createSpy('MockRequirement');
            MockUser = jasmine.createSpy('MockUser');
            MockEstimate = jasmine.createSpy('MockEstimate');
            MockTask = jasmine.createSpy('MockTask');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Requirement': MockRequirement,
                'User': MockUser,
                'Estimate': MockEstimate,
                'Task': MockTask
            };
            createController = function() {
                $injector.get('$controller')("RequirementDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'tenderworkApp:requirementUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
