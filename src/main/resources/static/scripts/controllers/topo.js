app.controller('TopoCtrl', ['$scope', '$location', "client", function ($scope, $location, client) {
  var params = $location.search();

  $scope.showLoading = true;
  client.topo(params.tabId, function (data, status) {
    $scope.showAPIFail = false;
    $scope.showLoading = false;
    $scope.topo = data;
  }, function () {
    defaultFail($scope)
  });

  $scope.isSpoutsShow = [];
  $scope.isBoltsShow = [];

  $scope.data = [
    {
      "title": "title1",
      "content": "content1  " + ((new Date()).toUTCString())
    },
    {
      "title": "title2",
      "content": "content1"

    }
  ];

}]);


app.controller('TopoMCtrl', ['$scope', '$location', "client", function ($scope, $location, client) {
    var params = $location.search();
    $scope.showLoading = true;
    client.topom(params.topoid,function (data, status) {
        $scope.showAPIFail = false;
        $scope.showLoading = false;
        $scope.topo = data;
    }, function () {
        defaultFail($scope)
    });

    $scope.isSpoutsShow = [];
    $scope.isBoltsShow = [];

    $scope.data = [
        {
            "title": "title1",
            "content": "content1  " + ((new Date()).toUTCString())
        },
        {
            "title": "title2",
            "content": "content1"

        }
    ];

}]);





