'use strict';

angular.module('naradApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('callDetails', {
                parent: 'entity',
                url: '/callDetailss',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'CallDetailss'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/callDetails/callDetailss.html',
                        controller: 'CallDetailsController'
                    }
                },
                resolve: {
                }
            })
            .state('callDetails.detail', {
                parent: 'entity',
                url: '/callDetails/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'CallDetails'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/callDetails/callDetails-detail.html',
                        controller: 'CallDetailsDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'CallDetails', function($stateParams, CallDetails) {
                        return CallDetails.get({id : $stateParams.id});
                    }]
                }
            })
            .state('callDetails.new', {
                parent: 'callDetails',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/callDetails/callDetails-dialog.html',
                        controller: 'CallDetailsDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    requestId: null,
                                    callerId: null,
                                    dialedNumber: null,
                                    startTime: null,
                                    endTime: null,
                                    timeToAnswer: null,
                                    duration: null,
                                    location: null,
                                    agentUniqueID: null,
                                    status: null,
                                    response: null,
                                    count: null,
                                    orderid: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('callDetails', null, { reload: true });
                    }, function() {
                        $state.go('callDetails');
                    })
                }]
            })
            .state('callDetails.edit', {
                parent: 'callDetails',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/callDetails/callDetails-dialog.html',
                        controller: 'CallDetailsDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['CallDetails', function(CallDetails) {
                                return CallDetails.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('callDetails', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('callDetails.delete', {
                parent: 'callDetails',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/callDetails/callDetails-delete-dialog.html',
                        controller: 'CallDetailsDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['CallDetails', function(CallDetails) {
                                return CallDetails.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('callDetails', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
