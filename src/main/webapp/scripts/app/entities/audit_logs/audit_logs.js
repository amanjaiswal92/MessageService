'use strict';

angular.module('naradApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('audit_logs', {
                parent: 'entity',
                url: '/audit_logss',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Audit_logss'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/audit_logs/audit_logss.html',
                        controller: 'Audit_logsController'
                    }
                },
                resolve: {
                }
            })
            .state('audit_logs.detail', {
                parent: 'entity',
                url: '/audit_logs/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Audit_logs'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/audit_logs/audit_logs-detail.html',
                        controller: 'Audit_logsDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'Audit_logs', function($stateParams, Audit_logs) {
                        return Audit_logs.get({id : $stateParams.id});
                    }]
                }
            })
            .state('audit_logs.new', {
                parent: 'audit_logs',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/audit_logs/audit_logs-dialog.html',
                        controller: 'Audit_logsDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    requestId: null,
                                    event: null,
                                    message: null,
                                    updated_time: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('audit_logs', null, { reload: true });
                    }, function() {
                        $state.go('audit_logs');
                    })
                }]
            })
            .state('audit_logs.edit', {
                parent: 'audit_logs',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/audit_logs/audit_logs-dialog.html',
                        controller: 'Audit_logsDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Audit_logs', function(Audit_logs) {
                                return Audit_logs.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('audit_logs', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
