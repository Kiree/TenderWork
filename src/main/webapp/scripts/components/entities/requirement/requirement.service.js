'use strict';

angular.module('tenderworkApp')
    .factory('Requirement', function ($resource, DateUtils) {
        return $resource('api/requirements/:id', {}, {
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
