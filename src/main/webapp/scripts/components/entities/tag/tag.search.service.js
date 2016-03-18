'use strict';

angular.module('tenderworkApp')
    .factory('TagSearch', function ($resource) {
        return $resource('api/_search/tags/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
