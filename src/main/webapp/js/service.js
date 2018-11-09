'use strict';

angular.module('rootApp.service', ['ngResource'])
    .factory('myServer', ['$http', '$log', '$q', '$resource', '$timeout', function($http, $log, $q, $resource, $timeout) {
        var URLPrefix = "rest/";
        return {
            URLPrefix: URLPrefix,
            call: function(uri, data, m) {
                var method = angular.isDefined(m) ? m : 'POST';
               // console.log(">>>>>>>>>>>>>>>>"+uri + ":" + method + m);
                var deferred = $q.defer();
                var conf = {
                    url : URLPrefix + uri,
                    method : method,
                    headers : {'Content-Type' : 'application/json'},
                    data : data
                };
                $http(conf).then(function successCallback(response) {
                    // 重定向
                    if (angular.isDefined(response.data.httpStatus) && response.data.httpStatus == 302) {
                        uri = response.data.Location;
                        window.location.href = uri;
                    }
                    var ret = {status: response.status, data: response.data};
                    deferred.resolve(ret);  // 声明执行成功，即请求数据成功，可以返回数据了
                },function errorCallback(response) {
                    var ret = {status: response.status, data: response.data};
                    deferred.reject(ret);   // 声明执行失败，即服务器返回错误
                });
                return deferred.promise;
            }
        };
    }]);