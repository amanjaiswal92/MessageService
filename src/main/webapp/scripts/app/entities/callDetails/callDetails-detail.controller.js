'use strict';

angular.module('naradApp')
    .controller('CallDetailsDetailController', function ($scope, $rootScope, $stateParams, DataUtils, entity, CallDetails) {
        $scope.callDetails = entity;
        $scope.load = function (id) {
            CallDetails.get({id: id}, function(result) {
                $scope.callDetails = result;
            });
        };
        var unsubscribe = $rootScope.$on('naradApp:callDetailsUpdate', function(event, result) {
            $scope.callDetails = result;
        });
        $scope.$on('$destroy', unsubscribe);

        $scope.byteSize = DataUtils.byteSize;
    });
