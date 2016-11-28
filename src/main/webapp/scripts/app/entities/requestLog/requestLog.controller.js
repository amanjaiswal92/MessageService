'use strict';

angular.module('naradApp')
    .controller('RequestLogController', function ($scope, RequestLog, RequestLogSearch, ParseLinks) {
        $scope.requestLogs = [];
        $scope.page = 0;
        $scope.loadAll = function() {
            RequestLog.query({page: $scope.page, size: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.requestLogs = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            RequestLog.get({id: id}, function(result) {
                $scope.requestLog = result;
                $('#deleteRequestLogConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            RequestLog.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteRequestLogConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.search = function () {
            RequestLogSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.requestLogs = result;
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
            $scope.requestLog = {
                requestId: null,
                type: null,
                request: null,
                update_time: null,
                id: null
            };
        };
    });
