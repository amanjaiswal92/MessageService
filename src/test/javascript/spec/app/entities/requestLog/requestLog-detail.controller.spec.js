'use strict';

describe('RequestLog Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, MockRequestLog;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        MockRequestLog = jasmine.createSpy('MockRequestLog');
        

        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'RequestLog': MockRequestLog
        };
        createController = function() {
            $injector.get('$controller')("RequestLogDetailController", locals);
        };
    }));


    describe('Root Scope Listening', function() {
        it('Unregisters root scope listener upon scope destruction', function() {
            var eventType = 'naradApp:requestLogUpdate';

            createController();
            expect($rootScope.$$listenerCount[eventType]).toEqual(1);

            $scope.$destroy();
            expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
        });
    });
});
