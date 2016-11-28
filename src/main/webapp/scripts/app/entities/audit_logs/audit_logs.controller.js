'use strict';

angular.module('naradApp')
    .controller('Audit_logsController', function ($scope, Audit_logs, Audit_logsSearch, ParseLinks) {
        $scope.audit_logss = [];
        $scope.page = 0;
        $scope.loadAll = function() {
            Audit_logs.query({page: $scope.page, size: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.audit_logss = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Audit_logs.get({id: id}, function(result) {
                $scope.audit_logs = result;
                $('#deleteAudit_logsConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Audit_logs.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteAudit_logsConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.search = function () {
            Audit_logsSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.audit_logss = result;
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
            $scope.audit_logs = {
                requestId: null,
                event: null,
                message: null,
                updated_time: null,
                id: null
            };
        };
    });
