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
                params: {
                    estimateId:null
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('estimate');
                        $translatePartialLoader.addPart('requirement');
                        $translatePartialLoader.addPart('task');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Estimate', function($stateParams, Estimate) {
                        if ($stateParams.estimateId) {
                            return Estimate.get({id : $stateParams.estimateId});
                        }
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
                        backdrop:'static',
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
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('project.detail', { id:result.ownerProject.id }, { reload: true });
                    }, function() {
                        $state.go('project.detail', { id:$stateParams.id});
                    })
                }]
            })
            .state('estimate.edit', {
                parent: 'estimate.detail',
                url: '/estimate/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        backdrop:'static',
                        templateUrl: 'scripts/app/entities/estimate/estimate-dialog.html',
                        controller: 'EstimateDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Estimate', function(Estimate) {
                                return Estimate.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('estimate.detail', { id:result.id }, { reload: true });
                    }, function() {
                        $state.go('estimate.detail', { id:$stateParams.id} );
                    })
                }]
            })
            .state('estimate.delete', {
                parent: 'estimate.detail',
                url: '/{id}/estimate/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                params: {
                    projectId:null
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        backdrop:'static',
                        templateUrl: 'scripts/app/entities/estimate/estimate-delete-dialog.html',
                        controller: 'EstimateDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Estimate', function(Estimate) {
                                return Estimate.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('project.detail', { id:$stateParams.projectId }, { reload: true });
                    }, function() {
                        $state.go('estimate.detail', { id:$stateParams.id }, { reload: true });
                    })
                }]
            });
    });
