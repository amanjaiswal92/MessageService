'use strict';

angular.module('naradApp')
    .factory('VariablesSearch', function ($resource) {
        return $resource('api/_search/variabless/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
