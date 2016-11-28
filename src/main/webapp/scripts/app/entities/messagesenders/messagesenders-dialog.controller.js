'use strict';

angular.module('naradApp').controller('MessagesendersDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Messagesenders',
        function($scope, $stateParams, $modalInstance, entity, Messagesenders) {

        $scope.messagesenders = entity;
        $scope.load = function(id) {
            Messagesenders.get({id : id}, function(result) {
                $scope.messagesenders = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('naradApp:messagesendersUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.messagesenders.id != null) {
                Messagesenders.update($scope.messagesenders, onSaveFinished);
            } else {
                Messagesenders.save($scope.messagesenders, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
