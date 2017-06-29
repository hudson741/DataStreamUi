
angular.module('anotherStormUiApp')
    .controller('YarnIndexCtrl', ["$scope", "client", function ($scope, client) {
        $scope.showLoading = true;
        client.yarnindex(function (data, status) {
            $scope.showLoading = false;

            $scope.yarn = data;
        }, function () {
            defaultFail($scope)
        });
    }]);
