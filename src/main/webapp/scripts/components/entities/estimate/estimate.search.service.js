'use strict';

angular.module('tenderworkApp')
    .factory('EstimateSearch', function ($resource) {
        return $resource('api/_search/estimates/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
