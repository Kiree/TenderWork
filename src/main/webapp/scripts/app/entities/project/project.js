'use strict';

angular.module('tenderworkApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('project', {
                parent: 'entity',
                url: '/projects',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'tenderworkApp.project.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/project/projects.html',
                        controller: 'ProjectController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('project');
                        $translatePartialLoader.addPart('global');
                        $translatePartialLoader.addPart('states');
                        return $translate.refresh();
                    }]
                }
            })
            .state('project.detail', {
                parent: 'project',
                url: '/project/{pid}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'tenderworkApp.project.detail.title'
                },
                params:{pid:null,generateDefaultEstimate:null},
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/project/project-detail.html',
                        controller: 'ProjectDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('states');
                        $translatePartialLoader.addPart('project');
                        $translatePartialLoader.addPart('estimate');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Project', function($stateParams, Project) {
                        return Project.get({id : $stateParams.pid});
                    }]
                }
            })
            .state('project.new', {
                parent: 'project',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal, Estimate) {
                    $uibModal.open({
                        backdrop:'static',
                        templateUrl: 'scripts/app/entities/project/project-dialog.html',
                        controller: 'ProjectDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    name: null,
                                    description: null,
                                    client: null,
                                    deadline: null,
                                    createdDate: null,
                                    editedDate: null,
                                    docLocation: null,
                                    state: null,
                                    stateDescription: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        var createDefaultEstimate = function(project) {
                            return {
                                createdBy:project.createdBy,
                                dailyPrice:500,
                                desiredProjectDuration:3,
                                id:null,
                                implementationFactor:1,
                                ownerProject:project,
                                resourcing:null,
                                specificationFactor:1,
                                synergyBenefit:0,
                                testingFactor:1,
                                totalDuration:null,
                                totalPrice:null,
                                totalSynergyBenefit:null,
                                workdaysInMonth:20
                            };
                        };
                        $state.go('project.detail', { pid:result.id, generateDefaultEstimate:createDefaultEstimate(result) }, { reload: true });
                    }, function() {
                        $state.go('project');
                    })
                }]
            })
            .state('project.edit', {
                parent: 'project.detail',
                url: '/{pid}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        backdrop:'static',
                        templateUrl: 'scripts/app/entities/project/project-edit-dialog.html',
                        controller: 'ProjectDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['$translate', 'Project', function($translate, Project) {
                                return Project.get({id : $stateParams.pid}).$promise.then(function(result) {
                                    result.state = $translate.instant(result.state);
                                    return result;
                                }, function(result) { return result; },
                                    function(result) { return result; });
                                //return Project.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('project.detail', {pid:result.id, generateDefaultEstimate:false} , { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('project.delete', {
                parent: 'project.detail',
                url: '/{pid}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                params: {
                    pid:null
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        backdrop:'static',
                        templateUrl: 'scripts/app/entities/project/project-delete-dialog.html',
                        controller: 'ProjectDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Project', function(Project) {
                                return Project.get({id : $stateParams.pid});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('project', null, { reload: true });
                    }, function() {
                        $state.go('project.detail', { pid:$stateParams.pid, generateDefaultEstimate:false } );
                    })
                }]
            });
    });
