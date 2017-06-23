
angular.module('anotherStormUiApp')
  .controller('OverviewCtrl', ["$scope", "client", function ($scope, client) {
    $scope.showLoading = true;
    client.overview(function (data, status) {
      $scope.showLoading = false;

      $scope.overview = data;
    }, function () {
      defaultFail($scope)
    });
  }]);
