 'use strict';

angular.module('tenderworkApp')
    .factory('notificationInterceptor', function ($q, AlertService) {
        return {
            response: function(response) {
                var alertKey = response.headers('X-tenderworkApp-alert');
                if (angular.isString(alertKey)) {
                    AlertService.success(alertKey, { param : response.headers('X-tenderworkApp-params')});
                }
                return response;
            }
        };
    });
