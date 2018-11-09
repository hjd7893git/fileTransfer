'use strict';

angular.module('rootApp.view', ['ngRoute', 'rootApp.service'])
    .config(['$routeProvider', function($routeProvider) {
        $routeProvider.when('/login', {
            templateUrl: 'login.html',
            controller: 'loginCtrl'
        }).when('/overview', {
            templateUrl: 'overview.html',
            controller: 'overviewCtrl'
        }).when('/:viewId', { // 其他view都是crud
            templateUrl: 'view.html',
            controller: 'crudCtrl'
        }).otherwise({
            redirectTo: '/login'
        });
    }])
    .controller('loginCtrl', ['$log', '$scope', '$rootScope', '$timeout', '$location', '$document', 'ipCookie', 'myServer', function($log, $scope, $rootScope, $timeout, $location, $document, ipCookie, myServer) {
        $scope.rebuild = function() {
            $scope.auth = false;
            $scope.changePwd = false;
            $rootScope.user = {};
            if (angular.isDefined($rootScope.menus))
                delete $rootScope.menus;
            $scope.user = {};
            $scope.user.username = '';
            $scope.user.hashpass = '';
            $scope.user.timestamp = '';
            $scope.user.sign = '';
            $scope.hashpass2 = '';
        }

        document.getElementById("userSign").addEventListener('input', function() {
            $scope.$apply(function() {
                $scope.auth = true;
            });
        });

        document.getElementById("userSign").addEventListener('change', function() { //增加js事件
            var promise = myServer.call("auth/login", $scope.user);
            promise.then(function(ret) {
                if (ret.status == 200) {
                    $rootScope.user = $scope.user;          //登陆成功后，将当前所登陆的用户信息赋给 根域名的User
                    $location.path("overview").replace();
                } else if (ret.status == 202) {
                    document.getElementById("HxUKeyDevInfo").innerHTML = "需要设置新密码";
                    $scope.changePwd = true;
                }
            }, function(ret) { // 处理错误 .reject
                document.getElementById("HxUKeyDevInfo").innerHTML = "<font color=\"red\">" + ret.data.message + "</font>";
            });
        });

        /* 获取设备信息 */
        $scope.checkDev = function() {
            console.log("checkDev");
            try {
                var evt = document.createEvent("CustomEvent");
                evt.initCustomEvent("ukeyCheckDev", true, false, {isManager: "false"});
                document.dispatchEvent(evt);
            } catch (er) {
                if (er.name == "NotSupportedError") {
                    document.getElementById("HxUKeyDevInfo").innerHTML = "<font color=\"red\">未安装/未启用 UKey浏览器插件</font>";
                }
            }
        };

        /* UKey认证 */
        $scope.ukeyLogin = function(keyPass) {
            try {
                var evt = document.createEvent("CustomEvent");
                evt.initCustomEvent('ukeyLogin', true, false, {isManager: "false", password: keyPass});
                document.dispatchEvent(evt);
            } catch (er) {
                if (er.name == "NotSupportedError") {
                    document.getElementById("HxUKeyDevInfo").innerHTML = "未安装/未启用 UKey浏览器插件";
                }
            }
        }

        $scope.doLogin = function(keyPass) {
            $rootScope.user = $scope.user;
            $scope.tempPwd = $scope.user.hashpass;
            $scope.user.hashpass = sm3($scope.user.username + $scope.user.hashpass);
            var promise = myServer.call('auth/report', null, 'GET');
            promise.then(function(ret) {
                if (ret.status == 200 || ret.status == 201) {
                    $scope.user.timestamp = ret.data.timestamp;
                }
                try{
                    var evt = document.createEvent("CustomEvent");
                    var sm3res = sm3($scope.user.username + $scope.user.timestamp, '1234567812345678', $scope.pk);
//                    var sm3res = sm3($scope.user.username + 1516262927432, '1234567812345678', $scope.pk);
//                    console.log($scope.user.username + $scope.user.timestamp + ':' + $scope.pk + ' -> ' + sm3res);
                    evt.initCustomEvent('ukeySignData', true, false, {isManager: "false", data: sm3res, password: keyPass});
                    document.dispatchEvent(evt);
                } catch (er) {
                    if (er.name == "NotSupportedError") {
                        document.getElementById("HxUKeyDevInfo").innerHTML = "未安装/未启用 UKey浏览器插件";
                    }
                }
            }, function(ret) {
                alert(ret);
            });
        }
        $scope.doChange = function() {
            if ($scope.tmpPwd != sm3($scope.user.username + $scope.user.hashpass))
                $scope.user.hashpass = sm3($scope.user.username + $scope.user.hashpass);
            $scope.hashpass2 = sm3($scope.user.username + $scope.hashpass2);
            if ($scope.user.hashpass != $scope.hashpass2) {
                document.getElementById("HxUKeyDevInfo").innerHTML = "<font color=\"red\">两次输入的密码不一致</font>";
                $scope.hashpass2 = "";
                return;
            }
            var promise = myServer.call("auth/changePwd", $scope.user);
            promise.then(function(ret) {
                if (ret.status == 200) {
                    $rootScope.user = $scope.user;
                    $location.path("overview").replace();
                }
            }, function(ret) { // 处理错误 .reject
                document.getElementById("HxUKeyDevInfo").innerHTML = "<font color=\"red\">" + ret.data.message + "</font>";
            });
        }

        $scope.rebuild();

        $timeout(function() {
            $scope.checkDev();
        });
    }])
    .controller('overviewCtrl', ['$log', '$rootScope', '$scope', '$location', 'myServer', '$interval', function($log, $rootScope, $scope, $location, myServer, $interval) {
        $scope.path = $location.path();
        $scope.warn = {};
        $scope.options = {};
        $scope.id = "fileStatusBar";
        $scope.dateData = [];
        $scope.findData = [];
        $scope.finishedData = [];
        $scope.timer;
        //获取用户登录信息
        if (angular.isUndefined($rootScope.user)) {
            var promise = myServer.call('auth/getUserInfo', null, 'GET');
            promise.then(function(ret) {
                if (ret.data.httpStatus == "200") {
                    $rootScope.user = {};
                    $rootScope.user.username = ret.data.username;
                } else {
                    $location.path(ret.data.Location).replace();
                }
            }, function(ret) {
                $scope.showModal(ret, 'auth/getUserInfo', 'GET', '获取用户信息失败');
            });
        }
        //获取菜单信息
        if (angular.isUndefined($rootScope.menus) || $rootScope.menus == null) {
            var promise = myServer.call('auth/getMenu', null, 'GET');
            promise.then(function(ret) {
                $rootScope.menus = ret.data;
            }, function(ret) {
                $scope.showModal(ret, 'auth/getMenu', 'GET', '获取目录失败');
            });
        }

        // 暂时用jquery实现动画
        $scope.hideContent = function() {
            $(".sidebar").animate({left: '-16.66666667%'});
            $(".main").animate({width: '100%', marginLeft: '0%'});
            $(".controlbar").animate({width: '100%', marginLeft: '0%'});
            $(".nav-sidebar-button").animate({left: '0%'});
            $(".nav-sidebar-button-middle-left").toggle();
            $(".nav-sidebar-button-middle-right").toggle();
        };
        $scope.showContent = function() {
            $(".sidebar").animate({left: '0px'});
            $(".main").animate({width: '83.33333333%', marginLeft: '16.66666667%'});
            $(".controlbar").animate({width: '83.33333333%', marginLeft: '16.66666667%'});
            $(".nav-sidebar-button").animate({left: '16.66666667%'});
            $(".nav-sidebar-button-middle-left").toggle();
            $(".nav-sidebar-button-middle-right").toggle();
        };

        $scope.showModal = function(ret, url, action, title) {
            $scope.warn.url = url;
            $scope.warn.action = action;
            if (angular.isDefined(ret)) {
                if (angular.isDefined(title)) {
                    $scope.warn.title = title;
                } else {
                    $scope.warn.title = '服务错误';
                }
                $scope.warn.errorCode = ret.status;
                if (ret.status == 400 || ret.status == 401) {
                    $scope.warn.errorMsg = ret.data.message;
                } else if (angular.isDefined(ret.data) && ret.data != null) {
                    var json = JSON.parse(ret.data);
                    $scope.warn.errorMsg = json.error;
                } else
                    $scope.warn.errorMsg = '服务端异常(错误未知)';
            } else {
                $scope.warn.title = '通讯错误';
                $scope.warn.errorCode = '-1';
                $scope.warn.errorMsg = '服务端未启动或网络原因.';
            }
            $('#warnModal').modal('show');
        };
        $scope.logout = function() {
            var promise = myServer.call('auth/logout', 'POST');
            promise.then(function(ret) {
                $location.path("login").replace();
            }, function(ret) {
                $scope.showModal(ret, 'auth/logout', 'POST', '系统登出错误');
            });
        }

        var timer = $interval(function(){
            // 切换页面则停止刷新
            if ($location.path() != "/overview") {
                $interval.cancel(timer);
                if ($scope.timer) {
                    window.clearInterval($scope.timer);
                }
            }
            var promise = myServer.call('monitor/overview', null, 'GET');
            promise.then(function(ret) {
                $scope.summary = ret.data.summary;
                $scope.dateData = ret.data.dateData;
                $scope.findData = ret.data.findData;
                $scope.finishedData = ret.data.finishedData;
            }, function(ret) {
                $scope.showModal(ret, 'monitor/overview', 'GET', '获取总览信息失败');
            });
        }, 2000);
    }])

    .controller('crudCtrl', ['$log', '$rootScope', '$scope', '$location', 'myServer', function($log, $rootScope, $scope, $location, myServer) {
        $scope.path = $location.path();
        $scope.searchs = [];
        $scope.isSearch = false;
        $scope.warn = {};
        $scope.crud = {};
        $scope.options = {};
        $scope.crudOptions = [{'key': 'Insert', 'value': '新建'}, {'key': 'Update', 'value': '修改'}, {'key': 'Check', 'value': '查询'}, {'key': 'Delete', 'value': '删除'}];
        $scope.range10 = [];
        for (var i=0; i<10; i++) {
            $scope.range10[i] = i+1;
        }
        //获取用户登录信息
        if (angular.isUndefined($rootScope.user)) {
            var promise = myServer.call('auth/getUserInfo', null, 'GET');
            promise.then(function(ret) {
                if (ret.data.httpStatus == "200") {
                    $rootScope.user = {};
                    $rootScope.user.username = ret.data.username;
                } else {
                    $location.path(ret.data.Location).replace();
                }
            }, function(ret) {
                $scope.showModal(ret, 'auth/getUserInfo', 'GET', '获取用户信息失败');
            });
        }

        if (angular.isUndefined($rootScope.menus) || $rootScope.menus == null) {
            var promise = myServer.call('auth/getMenu', null, 'GET');
            promise.then(function(ret) {
                $rootScope.menus = ret.data;
            }, function(ret) {
                $scope.showModal(ret, 'auth/getMenu', 'GET', '获取目录失败');
            });
        }

        $scope.gotoPage = function(path, page) {
            $scope.crud.page = page;
            $scope.pageCtu = page;
            var promise = myServer.call('crud/Select' + path, $scope.crud);
            promise.then(function(ret){
                $scope.crud = ret.data;
                // 菜单权限
                $rootScope.menus.forEach(function(menu) {
                    if (menu.url == $scope.crud.table.db_name)
                        $scope.db_type = menu.type;
                })

                var fields = angular.copy($scope.crud.table.fields);
                //查看关联option关系信息
                fields.forEach(function(field) {
                    if (field.db_option && angular.isUndefined($scope.options[field.db_optionid])) {
                        var promise2 = myServer.call('option/' + field.db_optionid, null, 'GET');
                        promise2.then(function(ret) {
                            var tmp = [];
                            for(var key in ret.data){
                                tmp.push({'key':key,'value':ret.data[key]});
                            }
                            $scope.options[field.db_optionid] = tmp;
                        }, function(ret) {
                            $scope.showModal(ret, 'option/' + field.db_optionid, 'GET');
                        });
                    }
                });
                $scope.show = [];
                $scope.crud.table.fields.forEach(function(field) {
                    $scope.show.push({'key':field.db_name,'value':''});
                });
                if (angular.isUndefined($scope.crud.requests) || $scope.crud.requests == null) {
                    $scope.searchs = $scope.crud.table.fields;  //查询页面值详细赋予
                    $scope.searchs.forEach(function(field) {
                        field.db_regex = "";
                    });
                }

            }, function(ret){
                $scope.showModal(ret, 'crud/Select' + path, 'POST');
            });
        };
        $scope.cudOne = function(path, data) {
            $('#crudModal').modal('hide');
            var p = {};
            p.datas = [data];
            var promise = myServer.call('crud/' + $scope.action + path, p);
            promise.then(function(ret){
                $scope.gotoPage(path, $scope.crud.page);
            }, function(ret){
                $scope.showModal(ret, 'crud/' + $scope.action + path, p);
            });
        };
        $scope.viewChosen = function(event, data) {
            var cb = event.target;
            var obj = document.getElementsByName("cbox");
            for (i=0; i<obj.length; i++){
                //判斷obj集合中的i元素是否為cb，若否則表示未被點選
                if (obj[i]!=cb) obj[i].checked = false;
                //若是 但原先未被勾選 則變成勾選；反之 則變為未勾選
                else {
                    obj[i].checked = cb.checked;
                    $scope.show = {};
                }
                //若要至少勾選一個的話，則把上面那行else拿掉，換用下面那行
                //else obj[i].checked = true;
            }
            $scope.show = angular.copy(data);
            $scope.show.forEach(function(s) {
                s.value = s.value + "";
            });
        };
        $scope.doSearch = function() {
            $scope.crud.requests = [];
            $scope.searchs.forEach(function(s) {
                if (s.db_regex != "")
                    $scope.crud.requests.push(s);
            });
            $scope.gotoPage("/" + $scope.crud.table.db_name, $scope.crud.page);
        };
        $scope.hideContent = function() {
            $(".sidebar").animate({left: '-16.66666667%'});
            $(".main").animate({width: '100%', marginLeft: '0%'});
            $(".controlbar").animate({width: '100%', marginLeft: '0%'});
            $(".nav-sidebar-button").animate({left: '0%'});
            $(".nav-sidebar-button-middle-left").toggle();
            $(".nav-sidebar-button-middle-right").toggle();
        };
        $scope.showContent = function() {
            $(".sidebar").animate({left: '0px'});
            $(".main").animate({width: '83.33333333%', marginLeft: '16.66666667%'});
            $(".controlbar").animate({width: '83.33333333%', marginLeft: '16.66666667%'});
            $(".nav-sidebar-button").animate({left: '16.66666667%'});
            $(".nav-sidebar-button-middle-left").toggle();
            $(".nav-sidebar-button-middle-right").toggle();
        };
        $scope.changeSearch = function() {
            $scope.isSearch = !$scope.isSearch;
            $(".search").toggle();
            if (!$scope.isSearch) {
                $scope.crud.requests = null; //查询前数据初始化
                $scope.gotoPage("/" + $scope.crud.table.db_name, $scope.crud.page);
            }
        };
        $scope.showEdit = function(action) {
            if ((action == "Update" || action == "Delete") && $scope.show[0].value == "") {
                alert("请勾选修改项");
            } else if (action == "Delete") {
                $scope.action = "Delete";
                $scope.cudOne('/'+ $scope.crud.table.db_name, $scope.show);
            } else {
                $scope.action = action;
                $('#crudModal').modal('show');
            }
        };
        // 更新节点配置信息
        $scope.useConfig = function() {
            if ($scope.show[0].value == "") {
                alert("请勾选修改项");
            } else {
                var p = {};
                p.datas = [$scope.show];
                var promise = myServer.call('xml/Update/', p);
                promise.then(function(ret){
                    if (ret.status == 200) {
                        $scope.gotoPage('/'+ $scope.crud.table.db_name, $scope.crud.page);
                    } else {
                        $scope.showModal(ret, 'xml/Update/', p);
                    }
                }, function(ret){
                    $scope.showModal(ret, 'xml/Update/', p);
                });
            }
        }
        $scope.objToList = function(option) {
            var tmp = [];
            for(var key in option) {
                tmp.push({'key':key,'value':option[key]});
            }
            return tmp;
        };
        $scope.logout = function() {
            var promise = myServer.call('auth/logout', 'POST');
            promise.then(function(ret) {
                $location.path("login").replace();
            }, function(ret) {
                $scope.showModal(ret, 'auth/logout', 'POST', '系统登出错误');
            });
        }
        $scope.showModal = function(ret, url, action, title) {
            $scope.warn.url = url;
            $scope.warn.action = action;
            if (angular.isDefined(ret)) {
                if (angular.isDefined(title)) {
                    $scope.warn.title = title;
                } else {
                    $scope.warn.title = '服务错误';
                }
                $scope.warn.errorCode = ret.status;
                if (ret.status == 400 || ret.status == 401) {
                    $scope.warn.errorMsg = ret.data.message;
                } else if (angular.isDefined(ret.data) && ret.data != null) {
                    var json = JSON.parse(ret.data);
                    $scope.warn.errorMsg = json.error;
                } else
                    $scope.warn.errorMsg = '服务端异常(错误未知)';
            } else {
                $scope.warn.title = '通讯错误';
                $scope.warn.errorCode = '-1';
                $scope.warn.errorMsg = '服务端未启动或网络原因.';
            }
            $('#warnModal').modal('show');
        };

        $scope.gotoPage($scope.path, 1);
    }])
    .directive('fileStatusBar', function() {
        return {
            restrict : 'A',
            template : '<div></div>',
            link : function($scope, element, attrs, controller) {
                var myChart = echarts.init(document.getElementById($scope.id));
                setTimeout(function() {
                    var option = {
                        title : {
                            text: '文件发现与完成关系图',
                            x: 'center',
                            align: 'right'
                        },
                        grid: {
                            bottom: 80
                        },
                        toolbox: {
                            feature: {
                                dataZoom: {
                                    yAxisIndex: 'none'
                                },
                                restore: {},
                                saveAsImage: {}
                            }
                        },
                        tooltip : {
                            trigger: 'axis',
                            axisPointer: {
                                type: 'cross',
                                animation: false,
                                label: {
                                    backgroundColor: '#505765'
                                }
                            }
                        },
                        legend: {
                            data:['发现','完成'],
                            x: 'left'
                        },
                        dataZoom: [
                            {
                                show: true,
                                realtime: true,
                                start: 70,
                                end: 100
                            },
                            {
                                type: 'inside',
                                realtime: true,
                                start: 70,
                                end: 100
                            }
                        ],
                        xAxis : [
                            {
                                type : 'category',
                                boundaryGap : false,
                                axisLine: {onZero: false},
                                data : $scope.dateData
                            }
                        ],
                        yAxis: [
                            {
                                name: '数量(个)',
                                type: 'value'
                            }
                        ],
                        series: [
                            {
                                name:'发现',
                                type:'line',
                                animation: false,
                                areaStyle: {
                                    normal: {}
                                },
                                lineStyle: {
                                    normal: {
                                        width: 1
                                    }
                                },
                                markArea: {
                                    silent: true
                                },
                                data: $scope.findData
                            },
                            {
                                name:'完成',
                                type:'line',
                                animation: false,
                                areaStyle: {
                                    normal: {}
                                },
                                lineStyle: {
                                    normal: {
                                        width: 1
                                    }
                                },
                                markArea: {
                                    silent: true
                                },
                                data: $scope.finishedData
                            }
                        ]
                    };
                    myChart.setOption(option);

                    $scope.timer = setInterval(function() {
                        myChart.setOption({
                            xAxis: {
                                data: $scope.dateData
                            },
                            series: [
                                {
                                    data: $scope.findData
                                }, {
                                    data: $scope.finishedData
                                }
                            ]
                        });
                    }, 2000);
                }, 100);
            }
        };
    })