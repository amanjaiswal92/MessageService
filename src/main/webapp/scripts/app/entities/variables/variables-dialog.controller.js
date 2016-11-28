'use strict';

angular.module('naradApp').controller('VariablesDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Variables', 'Templates',
        function($scope, $stateParams, $modalInstance, entity, Variables, Templates) {

        $scope.variables = entity;
        $scope.templatess = Templates.query();
        $scope.load = function(id) {
            Variables.get({id : id}, function(result) {
                $scope.variables = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('naradApp:variablesUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.variables.id != null) {
                Variables.update($scope.variables, onSaveFinished);
            } else {
                Variables.save($scope.variables, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
