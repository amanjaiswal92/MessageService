'use strict';

angular.module('naradApp').controller('RequestLogDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'RequestLog',
        function($scope, $stateParams, $modalInstance, entity, RequestLog) {

        $scope.requestLog = entity;
        $scope.load = function(id) {
            RequestLog.get({id : id}, function(result) {
                $scope.requestLog = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('naradApp:requestLogUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.requestLog.id != null) {
                RequestLog.update($scope.requestLog, onSaveFinished);
            } else {
                RequestLog.save($scope.requestLog, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
