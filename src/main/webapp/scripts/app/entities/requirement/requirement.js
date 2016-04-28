'use strict';

angular.module('tenderworkApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('requirement', {
                abstract: true
            })
            .state('requirement.new', {
                parent: 'estimate.detail',
                url: '/requirement/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                params: {
                    eid:null
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
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
                        $state.go('estimate.detail', { id:$stateParams.eid, openreqwithid:null }, { reload: true });
                    }, function() {
                        $state.go('estimate.detail', { id:$stateParams.eid , openreqwithid:null}, { reload:true });
                    })
                }]
            });
            $stateProvider.state('requirement.edit', {
                parent: 'estimate.detail',
                url: '/{rid}/requirement/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                params: {
                    eid:null,
                    rid:null
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        backdrop:'static',
                        templateUrl: 'scripts/app/entities/requirement/requirement-dialog.html',
                        controller: 'RequirementDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Requirement', function(Requirement) {
                                return Requirement.get({id : $stateParams.rid});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('estimate.detail', { id:$stateParams.eid, openreqwithid:null }, { reload: true });
                    }, function() {
                        $state.go('estimate.detail', { id:$stateParams.eid, openreqwithid:null }, { reload: true });
                    })
                }]
            });
        $stateProvider.state('requirement.delete', {
                parent: 'estimate.detail',
                url: '/{rid}/requirement/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                params: {
                    eid:null,
                    rid:null
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        backdrop:'static',
                        templateUrl: 'scripts/app/entities/requirement/requirement-delete-dialog.html',
                        controller: 'RequirementDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Requirement', function(Requirement) {
                                return Requirement.get({id : $stateParams.rid});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('estimate.detail', { id:$stateParams.eid, openreqwithid:null }, { reload: true });
                    }, function() {
                        $state.go('estimate.detail', { id:$stateParams.eid, openreqwithid:null }, { reload: true });
                    })
                }]
            });
    });
