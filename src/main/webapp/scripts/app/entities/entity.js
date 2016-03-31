'use strict';

angular.module('tenderworkApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('entity', {
                abstract: true,
                parent: 'site'
            });
    })
    .run(function($rootScope, EntityHelperFactory) {
        $rootScope.helperFunctions = new EntityHelperFactory();
});
