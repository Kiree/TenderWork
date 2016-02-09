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
                        return $translate.refresh();
                    }]
                }
            })
            .state('project.detail', {
                parent: 'entity',
                url: '/project/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'tenderworkApp.project.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/project/project-detail.html',
                        controller: 'ProjectDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('project');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Project', function($stateParams, Project) {
                        return Project.get({id : $stateParams.id});
                    }]
                }
            })
            .state('project.new', {
                parent: 'project',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
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
                                    lastEditor: null,
                                    creator: null,
                                    createdDate: null,
                                    editedDate: null,
                                    docLocation: null,
                                    state: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('project', null, { reload: true });
                    }, function() {
                        $state.go('project');
                    })
                }]
            })
            .state('project.edit', {
                parent: 'project',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/project/project-dialog.html',
                        controller: 'ProjectDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Project', function(Project) {
                                return Project.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('project', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('project.delete', {
                parent: 'project',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/project/project-delete-dialog.html',
                        controller: 'ProjectDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Project', function(Project) {
                                return Project.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('project', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });

