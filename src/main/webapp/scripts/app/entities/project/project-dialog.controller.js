'use strict';

angular.module('tenderworkApp').controller('ProjectDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Project', 'User', 'Estimate', 'Principal','$translate', 'Tag',
        function($scope, $stateParams, $uibModalInstance, entity, Project, User, Estimate, Principal, $translate, Tag) {
        $scope.stateTranslations = ['states.new', 'states.pending', 'states.won', 'states.lost', 'states.closed'];
        $scope.states = {
            'New':'states.new',
            'Tender pending':'states.pending',
            'Won':'states.won',
            'Lost':'states.lost',
            'Closed':'states.closed',
            'Uusi':'states.new',
            'Tarjous jätetty':'states.pending',
            'Voitettu':'states.won',
            'Hävitty':'states.lost',
            'Suljettu':'states.closed'
        };

        var createTag = function(tagTextObject) {
            return {
                //id:null,
                name:tagTextObject.text,
                counter:0
            }
        };

        var updateTag = function(tag) {
            return {
                id:tag.id,
                name:tag.name,
                counter:tag.counter + 1
            }
        };

        $scope.tags = Tag.query();

        $scope.project = entity;
        $scope.users = User.query();
        $scope.estimates = Estimate.query();

        $scope.currentUserAccount = null;

        $scope.defaultState = $translate.instant('states.new');
        if($scope.project.id === null) {
            $scope.project.state = $scope.defaultState;
        }

        $scope.stateTracker = $scope.project.state;

        $scope.load = function(id) {
            Project.get({id : id}, function(result) {
                $scope.project = result;
            });
        };

        Principal.identity().then(function(account) {
            var myAccount = copyAccount(account);
            User.get({login:myAccount.login}, function(result) {
                $scope.currentUserAccount = result;
            });
        });

        var copyAccount = function(account) {
            return {
                activated:account.activated,
                email:account.email,
                firstName:account.firstName,
                langKey: account.langKey,
                lastName: account.lastName,
                login: account.login
            }
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('tenderworkApp:projectUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            var today = new Date();
            $scope.project.tags = $scope.tags.map(createTag);
            console.log($scope.project);
            $scope.project.state = $scope.states[$scope.stateTracker];
            $scope.project.editedBy = $scope.currentUserAccount;
            $scope.project.editedDate = today;
            $scope.isSaving = true;
            if ($scope.project.id != null) {
                Project.update($scope.project, onSaveSuccess, onSaveError);
                for (var i = 0; tags.length; i++){
                    if($scope.tag.id != null){
                        Tag.update($scope.tag, onSaveSuccess, onSaveError)
                    }
                }
            } else {
                $scope.project.createdDate = today;
                $scope.project.createdBy = $scope.currentUserAccount;
                Project.save($scope.project, onSaveSuccess, onSaveError);
                for (var i = 0; tags.length; i++ ){
                    Tag.save($scope.tag, onSaveSuccess, onSaveError);
                }
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.datePickerForDeadline = {};

        $scope.datePickerForDeadline.status = {
            opened: false
        };

        $scope.datePickerForDeadlineOpen = function($event) {
            $scope.datePickerForDeadline.status.opened = true;
        };
        $scope.datePickerForCreatedDate = {};

        $scope.datePickerForCreatedDate.status = {
            opened: false
        };

        $scope.datePickerForCreatedDateOpen = function($event) {
            $scope.datePickerForCreatedDate.status.opened = true;
        };
        $scope.datePickerForEditedDate = {};

        $scope.datePickerForEditedDate.status = {
            opened: false
        };

        $scope.datePickerForEditedDateOpen = function($event) {
            $scope.datePickerForEditedDate.status.opened = true;
        };
}]);
