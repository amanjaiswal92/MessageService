'use strict';

angular.module('naradApp')
	.controller('CallRequestDeleteController', function($scope, $uibModalInstance, entity, CallRequest) {

        $scope.callRequest = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            CallRequest.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
