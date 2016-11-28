'use strict';

angular.module('naradApp')
    .controller('VariablesDetailController', function ($scope, $rootScope, $stateParams, entity, Variables, Templates) {
        $scope.variables = entity;
        $scope.load = function (id) {
            Variables.get({id: id}, function(result) {
                $scope.variables = result;
            });
        };
        var unsubscribe = $rootScope.$on('naradApp:variablesUpdate', function(event, result) {
            $scope.variables = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
