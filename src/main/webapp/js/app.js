'use strict';

angular.module('rootApp',[
    'ngRoute',
    'ngResource',
    'ipCookie',
    'rootApp.components.template', // 模板组件
    'rootApp.service', // 后台AJAX组件
    'rootApp.view', // 页面显示组件
    'rootApp.filters',
    'rootApp.temp'
]).controller('rootCtrl', function($scope) {
//    $scope.menus = [{url:'view',view:'视图'},{url:'tree',view:'列表'}];
});