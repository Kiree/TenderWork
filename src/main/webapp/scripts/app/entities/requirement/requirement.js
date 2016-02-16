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
                parent: 'requirement',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
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
                        $state.go('requirement', null, { reload: true });
                    }, function() {
                        $state.go('requirement');
                    })
                }]
            })
            .state('requirement.edit', {
                parent: 'requirement',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/requirement/requirement-dialog.html',
                        controller: 'RequirementDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Requirement', function(Requirement) {
                                return Requirement.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('requirement', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('requirement.delete', {
                parent: 'requirement',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/requirement/requirement-delete-dialog.html',
                        controller: 'RequirementDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Requirement', function(Requirement) {
                                return Requirement.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('requirement', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
