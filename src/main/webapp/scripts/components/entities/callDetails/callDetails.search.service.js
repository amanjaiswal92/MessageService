'use strict';

angular.module('naradApp')
    .factory('CallDetailsSearch', function ($resource) {
        return $resource('api/_search/callDetailss/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
