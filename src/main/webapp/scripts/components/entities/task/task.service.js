'use strict';

angular.module('tenderworkApp')
    .factory('Task', function ($resource, DateUtils) {
        return $resource('api/tasks/:id', {}, {
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
                url: 'api/projects/:id',
                params:{ id: '@id'},
                transformRequest: function(data) {
                    return angular.toJson(data);
                }
        }
        });
    });
