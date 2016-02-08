'use strict';

angular.module('tenderworkApp')
    .factory('Testity', function ($resource, DateUtils) {
        return $resource('api/testitys/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
