'use strict';

angular.module('tenderworkApp')
    .factory('TestitySearch', function ($resource) {
        return $resource('api/_search/testitys/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
