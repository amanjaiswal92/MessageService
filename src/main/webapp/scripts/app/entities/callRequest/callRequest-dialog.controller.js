'use strict';

angular.module('naradApp').controller('CallRequestDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'DataUtils', 'entity', 'CallRequest',
        function($scope, $stateParams, $uibModalInstance, DataUtils, entity, CallRequest) {

        $scope.callRequest = entity;
        $scope.load = function(id) {
            CallRequest.get({id : id}, function(result) {
                $scope.callRequest = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('naradApp:callRequestUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.callRequest.id != null) {
                CallRequest.update($scope.callRequest, onSaveSuccess, onSaveError);
            } else {
                CallRequest.save($scope.callRequest, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };

        $scope.abbreviate = DataUtils.abbreviate;

        $scope.byteSize = DataUtils.byteSize;
}]);
