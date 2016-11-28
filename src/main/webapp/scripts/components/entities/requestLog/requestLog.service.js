'use strict';

angular.module('naradApp')
    .factory('RequestLog', function ($resource, DateUtils) {
        return $resource('api/requestLogs/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.update_time = DateUtils.convertDateTimeFromServer(data.update_time);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
