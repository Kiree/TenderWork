'use strict';

describe('Controller Tests', function() {

    describe('Tag Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockTag, MockProject, MockRequirement, MockTask;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockTag = jasmine.createSpy('MockTag');
            MockProject = jasmine.createSpy('MockProject');
            MockRequirement = jasmine.createSpy('MockRequirement');
            MockTask = jasmine.createSpy('MockTask');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Tag': MockTag,
                'Project': MockProject,
                'Requirement': MockRequirement,
                'Task': MockTask
            };
            createController = function() {
                $injector.get('$controller')("TagDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'tenderworkApp:tagUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
