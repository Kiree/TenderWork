'use strict';

angular.module('tenderworkApp')
    .factory('ProjectSearch', function ($resource) {
        return $resource('api/_search/projects/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
