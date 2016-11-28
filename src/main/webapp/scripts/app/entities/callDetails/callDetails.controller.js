'use strict';

angular.module('naradApp')
    .controller('CallDetailsController', function ($scope, $state, DataUtils, CallDetails, CallDetailsSearch, ParseLinks) {

        $scope.callDetailss = [];
        $scope.predicate = 'id';
        $scope.reverse = true;
        $scope.page = 1;
        $scope.loadAll = function() {
            CallDetails.query({page: $scope.page - 1, size: 20, sort: [$scope.predicate + ',' + ($scope.reverse ? 'asc' : 'desc'), 'id']}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.totalItems = headers('X-Total-Count');
                $scope.callDetailss = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();


        $scope.search = function () {
            CallDetailsSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.callDetailss = result;
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
            $scope.callDetails = {
                requestId: null,
                callerId: null,
                dialedNumber: null,
                startTime: null,
                endTime: null,
                timeToAnswer: null,
                duration: null,
                location: null,
                agentUniqueID: null,
                status: null,
                response: null,
                count: null,
                orderid: null,
                id: null
            };
        };

        $scope.abbreviate = DataUtils.abbreviate;

        $scope.byteSize = DataUtils.byteSize;
    });
