'use strict';

describe('Audit_logs Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, MockAudit_logs;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        MockAudit_logs = jasmine.createSpy('MockAudit_logs');
        

        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'Audit_logs': MockAudit_logs
        };
        createController = function() {
            $injector.get('$controller')("Audit_logsDetailController", locals);
        };
    }));


    describe('Root Scope Listening', function() {
        it('Unregisters root scope listener upon scope destruction', function() {
            var eventType = 'naradApp:audit_logsUpdate';

            createController();
            expect($rootScope.$$listenerCount[eventType]).toEqual(1);

            $scope.$destroy();
            expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
        });
    });
});
