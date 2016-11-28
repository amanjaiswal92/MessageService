'use strict';

angular.module('naradApp')
    .factory('MessagesendersSearch', function ($resource) {
        return $resource('api/_search/messagesenderss/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
