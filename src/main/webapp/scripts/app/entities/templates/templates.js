'use strict';

angular.module('naradApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('templates', {
                parent: 'entity',
                url: '/templatess',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Templatess'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/templates/templatess.html',
                        controller: 'TemplatesController'
                    }
                },
                resolve: {
                }
            })
            .state('templates.detail', {
                parent: 'entity',
                url: '/templates/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Templates'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/templates/templates-detail.html',
                        controller: 'TemplatesDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'Templates', function($stateParams, Templates) {
                        return Templates.get({id : $stateParams.id});
                    }]
                }
            })
            .state('templates.new', {
                parent: 'templates',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/templates/templates-dialog.html',
                        controller: 'TemplatesDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    template_id: null,
                                    message: null,
                                    status: null,
                                    creation_time: null,
                                    approval_time: null,
                                    disabled_time: null,
                                    type: null,
                                    module: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('templates', null, { reload: true });
                    }, function() {
                        $state.go('templates');
                    })
                }]
            })
            .state('templates.edit', {
                parent: 'templates',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/templates/templates-dialog.html',
                        controller: 'TemplatesDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Templates', function(Templates) {
                                return Templates.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('templates', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
