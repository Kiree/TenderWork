'use strict';

angular.module('tenderworkApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('testity', {
                parent: 'entity',
                url: '/testitys',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'tenderworkApp.testity.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/testity/testitys.html',
                        controller: 'TestityController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('testity');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('testity.detail', {
                parent: 'entity',
                url: '/testity/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'tenderworkApp.testity.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/testity/testity-detail.html',
                        controller: 'TestityDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('testity');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Testity', function($stateParams, Testity) {
                        return Testity.get({id : $stateParams.id});
                    }]
                }
            })
            .state('testity.new', {
                parent: 'testity',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/testity/testity-dialog.html',
                        controller: 'TestityDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    nimi: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('testity', null, { reload: true });
                    }, function() {
                        $state.go('testity');
                    })
                }]
            })
            .state('testity.edit', {
                parent: 'testity',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/testity/testity-dialog.html',
                        controller: 'TestityDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Testity', function(Testity) {
                                return Testity.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('testity', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('testity.delete', {
                parent: 'testity',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/testity/testity-delete-dialog.html',
                        controller: 'TestityDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Testity', function(Testity) {
                                return Testity.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('testity', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
