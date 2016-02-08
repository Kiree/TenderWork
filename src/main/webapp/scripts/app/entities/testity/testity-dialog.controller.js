'use strict';

angular.module('tenderworkApp').controller('TestityDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Testity',
        function($scope, $stateParams, $uibModalInstance, entity, Testity) {

        $scope.testity = entity;
        $scope.load = function(id) {
            Testity.get({id : id}, function(result) {
                $scope.testity = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('tenderworkApp:testityUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.testity.id != null) {
                Testity.update($scope.testity, onSaveSuccess, onSaveError);
            } else {
                Testity.save($scope.testity, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
