'use strict';

angular.module('naradApp')
    .factory('Templates', function ($resource, DateUtils) {
        return $resource('api/templatess/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.creation_time = DateUtils.convertDateTimeFromServer(data.creation_time);
                    data.approval_time = DateUtils.convertDateTimeFromServer(data.approval_time);
                    data.disabled_time = DateUtils.convertDateTimeFromServer(data.disabled_time);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
