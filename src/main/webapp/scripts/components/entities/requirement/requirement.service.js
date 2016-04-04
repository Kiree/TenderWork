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
            'update': { method:'PUT' },
            'copy': {
                method: 'POST',
                url: 'api/requirements/:id',
                params:{ id: '@id'},
                transformRequest: function(data) {
                    return angular.toJson(data);
                }
            }
        });
    });
