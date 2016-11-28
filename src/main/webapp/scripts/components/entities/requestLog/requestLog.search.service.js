'use strict';

angular.module('naradApp')
    .factory('RequestLogSearch', function ($resource) {
        return $resource('api/_search/requestLogs/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
