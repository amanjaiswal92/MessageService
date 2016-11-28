'use strict';

angular.module('naradApp')
    .factory('CallRequest', function ($resource, DateUtils) {
        return $resource('api/callRequests/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
