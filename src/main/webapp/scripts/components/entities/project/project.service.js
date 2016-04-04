'use strict';

angular.module('tenderworkApp')
    .factory('Project', function ($resource, DateUtils) {
        return $resource('api/projects/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.deadline = DateUtils.convertLocaleDateFromServer(data.deadline);
                    data.createdDate = DateUtils.convertDateTimeFromServer(data.createdDate);
                    data.editedDate = DateUtils.convertDateTimeFromServer(data.editedDate);
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    data.deadline = DateUtils.convertLocaleDateToServer(data.deadline);
                    return angular.toJson(data);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    data.deadline = DateUtils.convertLocaleDateToServer(data.deadline);
                    return angular.toJson(data);
                }
            },
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
