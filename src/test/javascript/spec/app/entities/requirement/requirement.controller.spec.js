/**
 * Created by jukka on 17.3.2016.
 */
'use strict';

describe('Task Listing', function() {
    describe('Requirement Controller', function() {
        var $scope, $rootScope, $httpBackend;
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
                $injector.get('$controller')("RequirementController", locals);
            };
            $httpBackend = $injector.get('$httpBackend');
            $httpBackend.whenGET(/api\/task.*/).respond({id:'0', test:'1'});

        }));

        describe('Task Search', function() {
            it('Gets all', function() {
                createController();
                $httpBackend.expectGET('/api/task/1');
                //expect($scope.httpTestGet()).toEqual({id:'0', test:'1'});
            });

            it('Gets a response', function() {
                $httpBackend.flush();
                createController();
                expect($scope.httpTestData).toBe({id:'0', test:'1'});


            });
        });


    });
});
