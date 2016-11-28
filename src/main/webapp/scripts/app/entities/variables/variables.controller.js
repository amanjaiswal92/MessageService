'use strict';

angular.module('naradApp')
    .controller('VariablesController', function ($scope, Variables, VariablesSearch, ParseLinks) {
        $scope.variabless = [];
        $scope.page = 0;
        $scope.loadAll = function() {
            Variables.query({page: $scope.page, size: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.variabless = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Variables.get({id: id}, function(result) {
                $scope.variables = result;
                $('#deleteVariablesConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Variables.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteVariablesConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.search = function () {
            VariablesSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.variabless = result;
            }, function(response) {
                if(response.status === 404) {
                    $scope.loadAll();
                }
            });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.variables = {
                name: null,
                id: null
            };
        };
    });
