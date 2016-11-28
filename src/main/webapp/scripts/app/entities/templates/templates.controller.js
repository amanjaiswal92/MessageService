'use strict';

angular.module('naradApp')
    .controller('TemplatesController', function ($scope, Templates, TemplatesSearch, ParseLinks) {
        $scope.templatess = [];
        $scope.page = 0;
        $scope.loadAll = function() {
            Templates.query({page: $scope.page, size: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.templatess = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Templates.get({id: id}, function(result) {
                $scope.templates = result;
                $('#deleteTemplatesConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Templates.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteTemplatesConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.search = function () {
            TemplatesSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.templatess = result;
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
            $scope.templates = {
                template_id: null,
                message: null,
                status: null,
                creation_time: null,
                approval_time: null,
                disabled_time: null,
                type: null,
                module: null,
                id: null
            };
        };
    });
