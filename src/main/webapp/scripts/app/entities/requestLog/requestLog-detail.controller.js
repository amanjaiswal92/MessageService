'use strict';

angular.module('naradApp')
    .controller('RequestLogDetailController', function ($scope, $rootScope, $stateParams, entity, RequestLog) {
        $scope.requestLog = entity;
        $scope.load = function (id) {
            RequestLog.get({id: id}, function(result) {
                $scope.requestLog = result;
            });
        };
        var unsubscribe = $rootScope.$on('naradApp:requestLogUpdate', function(event, result) {
            $scope.requestLog = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
