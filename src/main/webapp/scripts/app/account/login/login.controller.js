'use strict';

angular.module('naradApp')
    .controller('LoginController', function ($rootScope, $scope, $state, $timeout, Auth) {
        $scope.user = {};
        $scope.errors = {};

        $scope.rememberMe = true;
        $timeout(function (){angular.element('[ng-model="username"]').focus();});
        $scope.login = function(event) {
        	  event.preventDefault();
        	  Auth.login().then(function() {
        	    $scope.authenticationError = false;
        	    if ($rootScope.previousStateName === 'register') {
        	      $state.go('home');
        	    } else {
        	      $rootScope.back();
        	    }
        	  }).catch(function() {
        	    $scope.authenticationError = true;
        	  });
        	};
    });
