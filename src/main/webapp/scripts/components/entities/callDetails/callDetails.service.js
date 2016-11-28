'use strict';

angular.module('naradApp')
    .factory('CallDetails', function ($resource, DateUtils) {
        return $resource('api/callDetailss/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.startTime = DateUtils.convertDateTimeFromServer(data.startTime);
                    data.endTime = DateUtils.convertDateTimeFromServer(data.endTime);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
