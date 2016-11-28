'use strict';

describe('Controller Tests', function() {

    describe('CallRequest Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockCallRequest;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockCallRequest = jasmine.createSpy('MockCallRequest');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'CallRequest': MockCallRequest
            };
            createController = function() {
                $injector.get('$controller')("CallRequestDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'naradApp:callRequestUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
