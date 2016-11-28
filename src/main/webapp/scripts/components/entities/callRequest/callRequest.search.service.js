'use strict';

angular.module('naradApp')
    .factory('CallRequestSearch', function ($resource) {
        return $resource('api/_search/callRequests/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
