'use strict';

angular.module('tenderworkApp')
    .factory('Estimate', function ($resource, DateUtils) {
        return $resource('api/estimates/:id', {}, {
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
