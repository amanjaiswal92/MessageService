'use strict';

angular.module('naradApp').controller('TemplatesDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Templates',
        function($scope, $stateParams, $modalInstance, entity, Templates) {

        $scope.templates = entity;
        $scope.load = function(id) {
            Templates.get({id : id}, function(result) {
                $scope.templates = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('naradApp:templatesUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.templates.id != null) {
                Templates.update($scope.templates, onSaveFinished);
            } else {
                Templates.save($scope.templates, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
