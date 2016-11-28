'use strict';

angular.module('naradApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('messagesenders', {
                parent: 'entity',
                url: '/messagesenderss',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Messagesenderss'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/messagesenders/messagesenderss.html',
                        controller: 'MessagesendersController'
                    }
                },
                resolve: {
                }
            })
            .state('messagesenders.detail', {
                parent: 'entity',
                url: '/messagesenders/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Messagesenders'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/messagesenders/messagesenders-detail.html',
                        controller: 'MessagesendersDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'Messagesenders', function($stateParams, Messagesenders) {
                        return Messagesenders.get({id : $stateParams.id});
                    }]
                }
            })
            .state('messagesenders.new', {
                parent: 'messagesenders',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/messagesenders/messagesenders-dialog.html',
                        controller: 'MessagesendersDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    sender_id: null,
                                    name: null,
                                    type: null,
                                    status: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('messagesenders', null, { reload: true });
                    }, function() {
                        $state.go('messagesenders');
                    })
                }]
            })
            .state('messagesenders.edit', {
                parent: 'messagesenders',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/messagesenders/messagesenders-dialog.html',
                        controller: 'MessagesendersDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Messagesenders', function(Messagesenders) {
                                return Messagesenders.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('messagesenders', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
