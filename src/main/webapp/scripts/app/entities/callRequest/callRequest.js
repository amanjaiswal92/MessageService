'use strict';

angular.module('naradApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('callRequest', {
                parent: 'entity',
                url: '/callRequests',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'CallRequests'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/callRequest/callRequests.html',
                        controller: 'CallRequestController'
                    }
                },
                resolve: {
                }
            })
            .state('callRequest.detail', {
                parent: 'entity',
                url: '/callRequest/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'CallRequest'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/callRequest/callRequest-detail.html',
                        controller: 'CallRequestDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'CallRequest', function($stateParams, CallRequest) {
                        return CallRequest.get({id : $stateParams.id});
                    }]
                }
            })
            .state('callRequest.new', {
                parent: 'callRequest',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/callRequest/callRequest-dialog.html',
                        controller: 'CallRequestDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    requestId: null,
                                    phoneno: null,
                                    consignmentid: null,
                                    response: null,
                                    body: null,
                                    cancelled: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('callRequest', null, { reload: true });
                    }, function() {
                        $state.go('callRequest');
                    })
                }]
            })
            .state('callRequest.edit', {
                parent: 'callRequest',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/callRequest/callRequest-dialog.html',
                        controller: 'CallRequestDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['CallRequest', function(CallRequest) {
                                return CallRequest.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('callRequest', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('callRequest.delete', {
                parent: 'callRequest',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/callRequest/callRequest-delete-dialog.html',
                        controller: 'CallRequestDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['CallRequest', function(CallRequest) {
                                return CallRequest.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('callRequest', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
