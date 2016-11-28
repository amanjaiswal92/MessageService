'use strict';

angular.module('naradApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });


