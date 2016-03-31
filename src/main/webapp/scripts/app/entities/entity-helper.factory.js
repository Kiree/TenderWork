/**
 * Created by jukka on 29.3.2016.
 */

angular.module('tenderworkApp').factory('EntityHelperFactory', function() {
    var __createTag = function(tagJSObject) {
        console.log("create tag called");
        if(tagJSObject.id === undefined) {
            return {
                id:null,
                name:tagJSObject.text
            };
        }
        return tagJSObject;
    };
    var __updateTag = function(tagJSObject) {
       console.log("update called");
    };
    var __roundResourcing = function (givenValue) {
        console.log("round called");
    };
    var __copyAccount = function(account) {
        return {
            activated:account.activated,
            email:account.email,
            firstName:account.firstName,
            langKey: account.langKey,
            lastName: account.lastName,
            login: account.login
        }
    };
    var __copyProject = function(project) {
        return {
            id:project.id,
            client:project.client,
            createdBy:project.createdBy,
            createdDate:project.createdDate,
            deadline:project.deadline,
            description:project.description,
            editedBy:project.editedBy,
            editedDate:project.editedDate,
            name:project.name,
            state:project.state,
            stateDescription:project.stateDescription
        }
    };
    var entityHelper = function($scope) {
        return {
            createTag:__createTag,
            updateTag:__updateTag,
            roundResourcing:__roundResourcing,
            copyAccount:__copyAccount,
            copyProject:__copyProject
        }
    };
    return entityHelper;
});
