'use strict';

angular.module('tenderworkApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('requirement', {
                parent: 'entity',
                url: '/requirements',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'tenderworkApp.requirement.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/requirement/requirements.html',
                        controller: 'RequirementController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('requirement');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('requirement.detail', {
                parent: 'entity',
                url: '/requirement/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'tenderworkApp.requirement.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/requirement/requirement-detail.html',
                        controller: 'RequirementDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('requirement');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Requirement', function($stateParams, Requirement) {
                        return Requirement.get({id : $stateParams.id});
                    }]
                }
            })
            .state('requirement.new', {
                parent: 'estimate.detail',
                url: '/requirement/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                params: {
                    estimateId:null
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    console.log($stateParams);
                    $uibModal.open({
                        backdrop:'static',
                        templateUrl: 'scripts/app/entities/requirement/requirement-dialog.html',
                        controller: 'RequirementDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    name: null,
                                    description: null,
                                    totalDuration: null,
                                    durationSpecification: null,
                                    durationImplementation: null,
                                    durationTesting: null,
                                    synergyBenefit: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('estimate.detail', { id:$stateParams.estimateId }, { reload: true });
                    }, function() {
                        $state.go('estimate.detail', { id:$stateParams.estimateId }, { reload:true });
                    })
                }]
            })
            .state('requirement.edit', {
                parent: 'estimate.detail',
                url: '/{id}/requirement/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                params: {
                    estimateId:null
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        backdrop:'static',
                        templateUrl: 'scripts/app/entities/requirement/requirement-dialog.html',
                        controller: 'RequirementDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Requirement', function(Requirement) {
                                return Requirement.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('estimate.detail', { id:$stateParams.estimateId }, { reload: true });
                    }, function() {
                        $state.go('estimate.detail', { id:$stateParams.estimateId }, { reload: true });
                    })
                }]
            })
            .state('requirement.delete', {
                parent: 'estimate.detail',
                url: '/{id}/requirement/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                params: {
                    estimateId:null
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        backdrop:'static',
                        templateUrl: 'scripts/app/entities/requirement/requirement-delete-dialog.html',
                        controller: 'RequirementDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Requirement', function(Requirement) {
                                return Requirement.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('estimate.detail', { id:$stateParams.estimateId }, { reload: true });
                    }, function() {
                        $state.go('estimate.detail', { id:$stateParams.estimateId }, { reload: true });
                    })
                }]
            });
    });
