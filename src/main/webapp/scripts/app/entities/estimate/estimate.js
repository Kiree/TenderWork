'use strict';

angular.module('tenderworkApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('estimate', {
                parent: 'entity',
                url: '/estimates',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'tenderworkApp.estimate.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/estimate/estimates.html',
                        controller: 'EstimateController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('estimate');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('estimate.detail', {
                parent: 'entity',
                url: '/estimate/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'tenderworkApp.estimate.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/estimate/estimate-detail.html',
                        controller: 'EstimateDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('estimate');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Estimate', function($stateParams, Estimate) {
                        return Estimate.get({id : $stateParams.id});
                    }]
                }
            })
            .state('estimate.new', {
                parent: 'project.detail',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/estimate/estimate-dialog.html',
                        controller: 'EstimateDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    workdaysInMonth: null,
                                    desiredProjectDuration: null,
                                    dailyPrice: null,
                                    specificationFactor: null,
                                    testingFactor: null,
                                    implementationFactor: null,
                                    synergyBenefit: null,
                                    totalPrice: null,
                                    totalDuration: null,
                                    resourcing: null,
                                    totalSynergyBenefit: null,
                                    id: null,
                                    projectId:$stateParams.id
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('project.detail', { id:result.ownerProject.id }, { reload: true });
                    }, function(reason) {
                        $state.go('project.detail', { id:reason.id});
                    })
                }]
            })
            .state('estimate.edit', {
                parent: 'estimate',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/estimate/estimate-dialog.html',
                        controller: 'EstimateDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Estimate', function(Estimate) {
                                return Estimate.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('project.detail', { id:result.ownerProject.id }, { reload: true });
                    }, function() {
                        $state.go('project.detail');
                    })
                }]
            })
            .state('estimate.delete', {
                parent: 'estimate',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/estimate/estimate-delete-dialog.html',
                        controller: 'EstimateDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Estimate', function(Estimate) {
                                return Estimate.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('estimate', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
