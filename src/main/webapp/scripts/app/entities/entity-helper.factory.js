/**
 * Created by jukka on 29.3.2016.
 */

angular.module('tenderworkApp').factory('EntityHelperFactory', function() {
    var __createTag = function(tagJSObject) {
        if(tagJSObject.id === "undefined") {
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
    var __roundResourcing = function (resourcing) {
        if(resourcing === null) {
            return 'n/a';
        }
        return resourcing - Math.floor(resourcing) > .5 ? Math.ceil(resourcing) : Math.floor(resourcing) + .5;
    };

    var __checkIfExists = function(array, element) {
        return array.some(function(item) {
            return item.id === element.id;
        });
    };
    var __tagCloudFiltering = function(query, input) {
        return input.filter(function(item) {
            return item.name.toLowerCase().indexOf(query.toLowerCase()) != -1;
        })
    };
    // prefills all properties of that were not filled
    var __fillEmptyEntityDetails = function(project) {
        for (var property in project) {
            if(project.hasOwnProperty(property)) {
                project[property] = project[property] === null ? 'n/a' : project[property];
            }
        }
        return project;
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
    var __copyRequirement = function(inputResource) {
        return {
            id:inputResource.id,
            description:inputResource.description,
            durationImplementation:inputResource.durationImplementation,
            durationSpecification:inputResource.durationSpecification,
            durationTesting:inputResource.durationTesting,
            name:inputResource.name,
            owner:inputResource.owner,
            ownerEstimate:inputResource.ownerEstimate,
            synergyBenefit:inputResource.synergyBenefit,
            totalDuration:inputResource.totalDuration
        }
    };
    var __copyEstimate = function(inputResource) {
        return {
            createdBy:inputResource.createdBy,
            dailyPrice:inputResource.dailyPrice,
            desiredProjectDuration:inputResource.desiredProjectDuration,
            id:inputResource.id,
            implementationFactor:inputResource.implementationFactor,
            ownerProject:inputResource.ownerProject,
            resourcing:inputResource.resourcing,
            specificationFactor:inputResource.specificationFactor,
            synergyBenefit:inputResource.synergyBenefit,
            testingFactor:inputResource.testingFactor,
            totalDuration:inputResource.totalDuration,
            totalPrice:inputResource.totalPrice,
            totalSynergyBenefit:inputResource.totalSynergyBenefit,
            workdaysInMonth:inputResource.workdaysInMonth
        }
    };
    var entityHelper = function($scope) {
        return {
            createTag:__createTag,
            updateTag:__updateTag,
            roundResourcing:__roundResourcing,
            copyAccount:__copyAccount,
            copyProject:__copyProject,
            checkIfExists:__checkIfExists,
            copyEstimate:__copyEstimate,
            copyRequirement:__copyRequirement,
            tagCloudFilter:__tagCloudFiltering,
            fillEmptyEntityDetails:__fillEmptyEntityDetails
        }
    };
    return entityHelper;
});
