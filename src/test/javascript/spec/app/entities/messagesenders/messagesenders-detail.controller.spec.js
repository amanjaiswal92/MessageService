'use strict';

describe('Messagesenders Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, MockMessagesenders;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        MockMessagesenders = jasmine.createSpy('MockMessagesenders');
        

        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'Messagesenders': MockMessagesenders
        };
        createController = function() {
            $injector.get('$controller')("MessagesendersDetailController", locals);
        };
    }));


    describe('Root Scope Listening', function() {
        it('Unregisters root scope listener upon scope destruction', function() {
            var eventType = 'naradApp:messagesendersUpdate';

            createController();
            expect($rootScope.$$listenerCount[eventType]).toEqual(1);

            $scope.$destroy();
            expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
        });
    });
});
