'use strict';

angular.module('naradApp')
    .controller('MessagesendersDetailController', function ($scope, $rootScope, $stateParams, entity, Messagesenders) {
        $scope.messagesenders = entity;
        $scope.load = function (id) {
            Messagesenders.get({id: id}, function(result) {
                $scope.messagesenders = result;
            });
        };
        var unsubscribe = $rootScope.$on('naradApp:messagesendersUpdate', function(event, result) {
            $scope.messagesenders = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
