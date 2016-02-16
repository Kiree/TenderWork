'use strict';

angular.module('tenderworkApp')
    .factory('RequirementSearch', function ($resource) {
        return $resource('api/_search/requirements/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
