'use strict';

describe('Variables Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, MockVariables, MockTemplates;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        MockVariables = jasmine.createSpy('MockVariables');
        MockTemplates = jasmine.createSpy('MockTemplates');
        

        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'Variables': MockVariables,
            'Templates': MockTemplates
        };
        createController = function() {
            $injector.get('$controller')("VariablesDetailController", locals);
        };
    }));


    describe('Root Scope Listening', function() {
        it('Unregisters root scope listener upon scope destruction', function() {
            var eventType = 'naradApp:variablesUpdate';

            createController();
            expect($rootScope.$$listenerCount[eventType]).toEqual(1);

            $scope.$destroy();
            expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
        });
    });
});
