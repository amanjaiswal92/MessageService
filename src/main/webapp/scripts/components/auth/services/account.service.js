'use strict';

angular.module('naradApp')
    .factory('Account', function Account($q, localStorageService) {
  return {
    get: function() {
      var defered = $q.defer();
      var account = localStorageService.get('account');
      if (account) {
        defered.resolve({
          data: account
        });
      } else {
        defered.reject({});
      }
      return {
        $promise: defered.promise
      };
    }
  };
});