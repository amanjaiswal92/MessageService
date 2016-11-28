'use strict';

angular.module('naradApp')
    .controller('Audit_logsDetailController', function ($scope, $rootScope, $stateParams, entity, Audit_logs) {
        $scope.audit_logs = entity;
        $scope.load = function (id) {
            Audit_logs.get({id: id}, function(result) {
                $scope.audit_logs = result;
            });
        };
        var unsubscribe = $rootScope.$on('naradApp:audit_logsUpdate', function(event, result) {
            $scope.audit_logs = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
