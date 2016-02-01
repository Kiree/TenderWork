'use strict';

angular.module('tenderworkApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });


