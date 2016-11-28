'use strict';

angular.module('naradApp')
    .factory('Messagesenders', function ($resource, DateUtils) {
        return $resource('api/messagesenderss/:id', {}, {
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
