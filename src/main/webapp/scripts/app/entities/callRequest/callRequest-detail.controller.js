'use strict';

angular.module('naradApp')
    .controller('CallRequestDetailController', function ($scope, $rootScope, $stateParams, DataUtils, entity, CallRequest) {
        $scope.callRequest = entity;
        $scope.load = function (id) {
            CallRequest.get({id: id}, function(result) {
                $scope.callRequest = result;
            });
        };
        var unsubscribe = $rootScope.$on('naradApp:callRequestUpdate', function(event, result) {
            $scope.callRequest = result;
        });
        $scope.$on('$destroy', unsubscribe);

        $scope.byteSize = DataUtils.byteSize;
    });
