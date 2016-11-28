'use strict';

angular.module('naradApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('variables', {
                parent: 'entity',
                url: '/variabless',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Variabless'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/variables/variabless.html',
                        controller: 'VariablesController'
                    }
                },
                resolve: {
                }
            })
            .state('variables.detail', {
                parent: 'entity',
                url: '/variables/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Variables'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/variables/variables-detail.html',
                        controller: 'VariablesDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'Variables', function($stateParams, Variables) {
                        return Variables.get({id : $stateParams.id});
                    }]
                }
            })
            .state('variables.new', {
                parent: 'variables',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/variables/variables-dialog.html',
                        controller: 'VariablesDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    name: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('variables', null, { reload: true });
                    }, function() {
                        $state.go('variables');
                    })
                }]
            })
            .state('variables.edit', {
                parent: 'variables',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/variables/variables-dialog.html',
                        controller: 'VariablesDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Variables', function(Variables) {
                                return Variables.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('variables', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
