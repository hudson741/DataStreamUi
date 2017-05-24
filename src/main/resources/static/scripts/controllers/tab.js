app.controller("TabCtrl", ['$rootScope', '$location', '$scope', 'client', function ($rootScope, $location, $scope, client) {
  client.topos(function (topos, status) {
    updateTabs($rootScope, topos, status)
  }, function () {
    updateTabsOnFail($rootScope)
  });

  $scope.isActive = function (tabId) {
    var params = $location.search();

    if (params.tabId === tabId) {
      return true;
    }

    if (($location.path() === "/" || $location.path() === "/overview") && tabId === "Overview") {
      return true;
    }

  }
}]);
