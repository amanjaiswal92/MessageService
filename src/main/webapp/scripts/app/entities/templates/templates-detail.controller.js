'use strict';

angular.module('naradApp')
    .controller('TemplatesDetailController', function ($scope, $rootScope, $stateParams, entity, Templates) {
        $scope.templates = entity;
        $scope.load = function (id) {
            Templates.get({id: id}, function(result) {
                $scope.templates = result;
            });
        };
        var unsubscribe = $rootScope.$on('naradApp:templatesUpdate', function(event, result) {
            $scope.templates = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
