'use strict';

angular.module('naradApp')
    .factory('TemplatesSearch', function ($resource) {
        return $resource('api/_search/templatess/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
