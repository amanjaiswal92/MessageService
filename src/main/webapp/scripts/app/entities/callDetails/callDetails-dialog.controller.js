'use strict';

angular.module('naradApp').controller('CallDetailsDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'DataUtils', 'entity', 'CallDetails',
        function($scope, $stateParams, $uibModalInstance, DataUtils, entity, CallDetails) {

        $scope.callDetails = entity;
        $scope.load = function(id) {
            CallDetails.get({id : id}, function(result) {
                $scope.callDetails = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('naradApp:callDetailsUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.callDetails.id != null) {
                CallDetails.update($scope.callDetails, onSaveSuccess, onSaveError);
            } else {
                CallDetails.save($scope.callDetails, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };

        $scope.abbreviate = DataUtils.abbreviate;

        $scope.byteSize = DataUtils.byteSize;
        $scope.datePickerForStartTime = {};

        $scope.datePickerForStartTime.status = {
            opened: false
        };

        $scope.datePickerForStartTimeOpen = function($event) {
            $scope.datePickerForStartTime.status.opened = true;
        };
        $scope.datePickerForEndTime = {};

        $scope.datePickerForEndTime.status = {
            opened: false
        };

        $scope.datePickerForEndTimeOpen = function($event) {
            $scope.datePickerForEndTime.status.opened = true;
        };
}]);
