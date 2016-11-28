'use strict';

angular.module('naradApp')
    .factory('AuthServerProvider', function loginService($http, localStorageService, $q, $window) {
  return {
    login: function() {
      var defered = $q.defer();
      var kc = new Keycloak('keycloak.json');
      kc.init({
        onLoad: 'login-required'
      }).success(function() {
        this.authorise();
        defered.resolve(kc);
      });
      return defered.promise;
    },
    authorise: function(keycloak) {
      if (keycloak.authenticated) {
        localStorageService.set('token', {
          token: keycloak.token,
          expires: new Date().getTime() + 60 * 60 * 15
        });
        this.setAccount(keycloak.tokenParsed);
        $window.location = '/';
      }
    },
    setAccount: function(account) {
      var data = {
        'login': account.preferred_username,
        'password': null,
        'firstName': account.given_name,
        'lastName': account.family_name,
        'email': account.email,
        'activated': true,
        'langKey': 'en',
        'authorities': [
          'ROLE_USER',
          'ROLE_ADMIN'
        ]
      };
      localStorageService.set('account', data);
    },
    logout: function() {
      //Stateless API : No server logout
      localStorageService.clearAll();
    },
    getToken: function() {
      return localStorageService.get('token');
    },
    hasValidToken: function() {
      var token = this.getToken();
      return token && token.expires && token.expires > new Date().getTime();
    }
  };
});
