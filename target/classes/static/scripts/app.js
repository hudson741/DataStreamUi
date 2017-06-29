
var app = angular
  .module('anotherStormUiApp', [
    'ngAnimate',
    'ngCookies',
    'ngResource',
    'ngRoute',
    'ngSanitize',
    'ngTouch',
    'flowChart',
    'ui.bootstrap'
  ]);


app.run(function ($rootScope, $routeParams, $anchorScroll, $location) {
  $rootScope.orderByField = {};
  $rootScope.reverseSort = {};

});

function updateTabsOnFail($rootScope) {
  $rootScope.tabs = [];
  $rootScope.tabs = [
      {tabName: "总览", tabId: "总览", tabLink: "/overview"},
      {tabName: "拓扑发布", tabId: "拓扑发布", tabLink: "/stormUpload"},
      {tabName: "conf", tabId: "conf", tabLink: "/conf"},
      {tabName: "yarn", tabId: "yarn", tabLink: "/yarnindex"},
      {tabName: "storm集群设置", tabId: "storm集群设置", tabLink: "/dockerPub"},
      {tabName: "stormDocker组件发布", tabId: "stormDocker组件发布", tabLink: "/stormPub"},
      {tabName: "节点性能监控", tabId: "节点性能监控", tabLink: "/overview"},
      {tabName: "stream管理",tabId: "stream管理",tabLink:"/streamManager"},
      {tabName: "Host", tabId: "Host", tabLink: "/host"}
  ];
}

function defaultFail($scope) {

}


function updateTabs($rootScope, topos, status) {
  $rootScope.tabs = [];
  $rootScope.tabs = [
      {tabName: "总览", tabId: "总览", tabLink: "/overview"},
      {tabName: "拓扑发布", tabId: "拓扑发布", tabLink: "/stormUpload"},
      {tabName: "conf", tabId: "conf", tabLink: "/conf"},
      {tabName: "yarn", tabId: "yarn", tabLink: "/yarnindex"},
      {tabName: "storm集群设置", tabId: "storm集群设置", tabLink: "/dockerPub"},
      {tabName: "stormDocker组件发布", tabId: "stormDocker组件发布", tabLink: "/stormPub"},
      {tabName: "节点性能监控", tabId: "节点性能监控", tabLink: "/overview"},
      {tabName: "stream管理",tabId: "stream管理",tabLink:"/streamManager"},
      {tabName: "Host", tabId: "Host", tabLink: "/host"}
  ];

  for (var i = 0; i < topos.length; i++) {
    console.log("added" + topos[i].name);
    $rootScope.tabs.push({tabName: topos[i].name, tabId: topos[i].id, tabLink: "/topo"});
  }
}

app.factory('client', ['$http', function ($http) {
  var request = function (restPath) {
    return $http.get("/" + restPath);
  };

  return {
    topos: function (callback, failcallback) {
      request('topolist').success(callback).error(failcallback);
    },
    overview: function (callback, failcallback) {
      request('overview').success(callback).error(failcallback);
    },

      conf: function(callback , failcallback) {
      request('conf').success(callback).error(failcallback);
    },
    yarnindex: function (callback, failcallback) {
      request('yarnindex').success(callback).error(failcallback);
    },
    hosts: function (callback, failcallback) {
      request('hosts').success(callback).error(failcallback);
    },
    topo: function (topoid, callback, failcallback) {
      request('topo?topoid=' + topoid).success(callback).error(failcallback);
    },
    topom: function (topoid,callback,failcallback) {
      request('topom?topoid=' + topoid).success(callback).error(failcallback);
    },
    killTopo: function(topoid,callback,failcallback) {
      request('killTopology?topoid=' + topoid).success(callback).error(failcallback);
    },
    topoWorkNumModify: function(topoid,num,callback,failcallback) {
      request('topoWorkNumModify?topoid=' + topoid+"&num="+num).success(callback).error(failcallback);
    },
    topoCExeModify: function(tid,cid,num,callback,failcallback) {
      request('topoCExeModify?topoid=' + tid+"&cid="+cid+"&num="+num).success(callback).error(failcallback);
    },
    activeTopo: function(topoid,callback,failcallback) {
      request('activeTopo?topoid=' + topoid).success(callback).error(failcallback)
    },
    deactiveTopo: function(topoid,callback,failcallback) {
      request('deactiveTopo?topoid=' + topoid).success(callback).error(failcallback)
    } ,
    streamManager:function(callback,failcallback) {
      request('streamManager').success(callback).error(failcallback);
    },
    fileu: function (newStormRestHost, callback, failcallback) {
      request('fileu').success(callback).error(failcallback);
    }
  };
}]);


app.config(function ($routeProvider) {
  $routeProvider
    .when('/', {
      redirectTo: '/conf'
    })
      .when('/overview', {
      templateUrl: 'views/overview.html',
      controller: 'OverviewCtrl'
    })
      .when('/conf', {
          templateUrl: 'views/conf.html',
          controller: 'SettingsCtrl'
      })
      .when('/yarnindex',{
          templateUrl: 'views/yarnindex.html',
          controller: 'YarnIndexCtrl'
      })
      .when('/dockerPub',{
       templateUrl: 'views/dockerPub.html'
      })
      .when('/stormPub',{
          templateUrl: 'views/stormPub.html'
      })
      .when('/streamManager', {
          templateUrl: 'views/streamManager.html',
          controller: 'StreamManagerCtrl'
      })
    .when('/topo', {
      templateUrl: 'views/topo.html',
      controller: 'TopoCtrl'
    })
      .when('/topom',{
          templateUrl: 'views/topom.html',
          controller: 'TopoMCtrl'
    })
      .when('/host', {
      templateUrl: 'views/host.html',
      controller: 'HostCtrl'
    }).when('/stormUpload', {
      templateUrl: 'views/stormUpload.html',
      controller: 'StormUploadCtrl'
    }).when('/download',{
      templateUrl: 'views/download.html',
      controller: 'DownloadCtrl'
    }).otherwise({
      redirectTo: '/'
    });
});
