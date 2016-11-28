'use strict';

angular.module('naradApp').controller('Audit_logsDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Audit_logs',
        function($scope, $stateParams, $modalInstance, entity, Audit_logs) {

        $scope.audit_logs = entity;
        $scope.load = function(id) {
            Audit_logs.get({id : id}, function(result) {
                $scope.audit_logs = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('naradApp:audit_logsUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.audit_logs.id != null) {
                Audit_logs.update($scope.audit_logs, onSaveFinished);
            } else {
                Audit_logs.save($scope.audit_logs, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
