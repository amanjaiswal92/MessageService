'use strict';

angular.module('naradApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('requestLog', {
                parent: 'entity',
                url: '/requestLogs',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'RequestLogs'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/requestLog/requestLogs.html',
                        controller: 'RequestLogController'
                    }
                },
                resolve: {
                }
            })
            .state('requestLog.detail', {
                parent: 'entity',
                url: '/requestLog/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'RequestLog'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/requestLog/requestLog-detail.html',
                        controller: 'RequestLogDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'RequestLog', function($stateParams, RequestLog) {
                        return RequestLog.get({id : $stateParams.id});
                    }]
                }
            })
            .state('requestLog.new', {
                parent: 'requestLog',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/requestLog/requestLog-dialog.html',
                        controller: 'RequestLogDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    requestId: null,
                                    type: null,
                                    request: null,
                                    update_time: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('requestLog', null, { reload: true });
                    }, function() {
                        $state.go('requestLog');
                    })
                }]
            })
            .state('requestLog.edit', {
                parent: 'requestLog',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/requestLog/requestLog-dialog.html',
                        controller: 'RequestLogDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['RequestLog', function(RequestLog) {
                                return RequestLog.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('requestLog', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
