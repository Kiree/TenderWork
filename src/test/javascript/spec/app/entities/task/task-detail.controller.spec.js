'use strict';

describe('Controller Tests', function() {

    describe('Task Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockTask, MockUser, MockEstimate, MockRequirement;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockTask = jasmine.createSpy('MockTask');
            MockUser = jasmine.createSpy('MockUser');
            MockEstimate = jasmine.createSpy('MockEstimate');
            MockRequirement = jasmine.createSpy('MockRequirement');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Task': MockTask,
                'User': MockUser,
                'Estimate': MockEstimate,
                'Requirement': MockRequirement
            };
            createController = function() {
                $injector.get('$controller')("TaskDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'tenderworkApp:taskUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
