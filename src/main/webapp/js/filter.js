'use strict';

angular.module('rootApp.filters', ['ngRoute'])

    .filter('more', function () {
        return function (cond, len) {
            return angular.isUndefined(cond) ? '' : ((cond.length > len) ? cond.substr(0, len-4) + '...' : cond);
        };
    })
    .filter('ceil', function () {
        return function (cond) {
            return angular.isUndefined(cond) ? '' : Math.ceil(cond);
        };
    })
    .filter('reverse', function() {
        return function(items) {
            return items.slice().reverse();
        };
    })
    .filter('theOption', function() {
            return function(key, options) {
                if (angular.isUndefined(options)) {
                    return null;
                } else {
                    var tmp;
                    options.forEach(function(o) {
                        var tempb = o.key == "true" ? true : (o.key == "false" ? false : o.key)
                        if (tempb == key) {
                            tmp = o.value;
                        }
                    });
                    return tmp;
                }
            }
        });
