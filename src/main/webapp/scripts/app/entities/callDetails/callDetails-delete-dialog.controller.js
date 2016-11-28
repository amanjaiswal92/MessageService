'use strict';

angular.module('naradApp')
	.controller('CallDetailsDeleteController', function($scope, $uibModalInstance, entity, CallDetails) {

        $scope.callDetails = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            CallDetails.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
