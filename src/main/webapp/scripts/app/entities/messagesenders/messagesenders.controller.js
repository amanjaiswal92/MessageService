'use strict';

angular.module('naradApp')
    .controller('MessagesendersController', function ($scope, Messagesenders, MessagesendersSearch, ParseLinks) {
        $scope.messagesenderss = [];
        $scope.page = 0;
        $scope.loadAll = function() {
            Messagesenders.query({page: $scope.page, size: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.messagesenderss = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Messagesenders.get({id: id}, function(result) {
                $scope.messagesenders = result;
                $('#deleteMessagesendersConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Messagesenders.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteMessagesendersConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.search = function () {
            MessagesendersSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.messagesenderss = result;
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
            $scope.messagesenders = {
                sender_id: null,
                name: null,
                type: null,
                status: null,
                id: null
            };
        };
    });
