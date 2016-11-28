'use strict';

angular.module('naradApp')
    .factory('Audit_logs', function ($resource, DateUtils) {
        return $resource('api/audit_logss/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.updated_time = DateUtils.convertDateTimeFromServer(data.updated_time);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
