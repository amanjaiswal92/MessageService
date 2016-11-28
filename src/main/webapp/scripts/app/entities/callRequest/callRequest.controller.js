'use strict';

angular.module('naradApp')
    .controller('CallRequestController', function ($scope, $state, DataUtils, CallRequest, CallRequestSearch) {

        $scope.callRequests = [];
        $scope.loadAll = function() {
            CallRequest.query(function(result) {
               $scope.callRequests = result;
            });
        };
        $scope.loadAll();


        $scope.search = function () {
            CallRequestSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.callRequests = result;
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
            $scope.callRequest = {
                requestId: null,
                phoneno: null,
                consignmentid: null,
                response: null,
                body: null,
                cancelled: null,
                id: null
            };
        };

        $scope.abbreviate = DataUtils.abbreviate;

        $scope.byteSize = DataUtils.byteSize;
    });
