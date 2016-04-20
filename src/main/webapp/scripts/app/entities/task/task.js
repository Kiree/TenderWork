'use strict';

angular.module('tenderworkApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('task', {
                abstract: true
            })
            .state('task.new', {
                parent: 'estimate.detail',
                url: '/task/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                params: {
                    rid:null,
                    eid:null,
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
                        $state.go('estimate.detail', { id:$stateParams.eid }, { reload: true });
                    }, function() {
                        $state.go('estimate.detail', { id:$stateParams.eid });
                    })
                }]
            })
            .state('task.edit', {
                parent: 'estimate.detail',
                url: '/{tid}/task/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                params:{
                    eid:null,
                    rid:null,
                    tid:null
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        backdrop:'static',
                        templateUrl: 'scripts/app/entities/task/task-dialog.html',
                        controller: 'TaskDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Task', function(Task) {
                                return Task.get({id : $stateParams.tid});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('estimate.detail', { id:$stateParams.eid }, { reload: true });
                    }, function() {
                        $state.go('estimate.detail', { id:$stateParams.eid });
                    })
                }]
            });
    });
