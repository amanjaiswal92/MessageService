'use strict';

angular.module('naradApp')
    .factory('Audit_logsSearch', function ($resource) {
        return $resource('api/_search/audit_logss/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
