'use strict';

angular.module('rootApp.temp', [])

    .directive('pageToolbar', function($templateCache) {
        return {
            restrict : 'E',
            templateUrl : 'temp/pageToolbar.html',
            replace : true
        };
    })
    .directive('searchToolbar', function($templateCache) {
        return {
            restrict : 'E',
            templateUrl : 'temp/searchToolbar.html',
            replace : true
        };
    })
    .directive('buttonToolbar', function($templateCache) {
        return {
            restrict : 'E',
            templateUrl : 'temp/buttonToolbar.html',
            replace : true
        };
    })
    .directive('contentToolbar', function($templateCache) {
        return {
            restrict : 'E',
            templateUrl : 'temp/contentToolbar.html',
            replace : true
        };
    })
    .directive('contentButton', function($templateCache) {
        return {
            restrict : 'E',
            templateUrl : 'temp/contentButton.html',
            replace : true
        };
    })
    .directive('crudShowModel', function($templateCache) {
        return {
            restrict : 'E',
            templateUrl : 'temp/crudShowModel.html',
            replace : true
        };
    })
    .directive('overviewModel', function($templateCache) {
        return {
            restrict : 'E',
            templateUrl : 'temp/overviewModel.html',
            replace : true
        };
    })
    .directive('crudInfoModel', function($templateCache) {
        return {
            restrict : 'E',
            templateUrl : 'temp/crudInfoModel.html',
            replace : true
        };
    })
    .directive('warnModel', function($templateCache) {
        return {
            restrict : 'E',
            templateUrl : 'temp/warnModel.html',
            replace : true
        };
    })
    .directive('userInfoModel', function($templateCache) {
        return {
            restrict : 'E',
            templateUrl : 'temp/userInfoModel.html',
            replace : true
        };
    })
    .directive('logoModel', function($templateCache) {
        return {
            restrict : 'E',
            templateUrl : 'temp/logoModel.html',
            replace : true
        };
    })
    .directive('summaryModel', function($templateCache) {
        return {
            restrict : 'E',
            templateUrl : 'temp/summaryModel.html',
            replace : true
        };
    })