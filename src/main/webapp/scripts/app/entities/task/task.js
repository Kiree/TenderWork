'use strict';

angular.module('tenderworkApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('task', {
                parent: 'entity',
                url: '/tasks',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'tenderworkApp.task.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/task/tasks.html',
                        controller: 'TaskController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('task');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('task.detail', {
                parent: 'entity',
                url: '/task/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'tenderworkApp.task.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/task/task-detail.html',
                        controller: 'TaskDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('task');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Task', function($stateParams, Task) {
                        return Task.get({id : $stateParams.id});
                    }]
                }
            })
            .state('task.new', {
                parent: 'estimate.detail',
                url: '/task/new',
                params: {
                    requirementId:null
                },
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        backdrop:'static',
                        templateUrl: 'scripts/app/entities/task/task-dialog.html',
                        controller: 'TaskDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    name: null,
                                    description: null,
                                    estimateSpecification: null,
                                    estimateImplementation: null,
                                    estimateTesting: null,
                                    estimateSynergy: null,
                                    synergyCheck: false,
                                    specificationTotal: null,
                                    implementationTotal: null,
                                    testingTotal: null,
                                    synergyTotal: null,
                                    estimateTotal: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('estimate.detail', { id:$stateParams.id }, { reload: true });
                    }, function() {
                        $state.go('estimate.detail', { id:$stateParams.id });
                    })
                }]
            })
            .state('task.edit', {
                parent: 'estimate.detail',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        backdrop:'static',
                        templateUrl: 'scripts/app/entities/task/task-dialog.html',
                        controller: 'TaskDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Task', function(Task) {
                                return Task.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('estimate.detail', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
