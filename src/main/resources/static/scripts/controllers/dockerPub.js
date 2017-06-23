
app.controller('SettingsCtrl', ['$scope', '$rootScope', '$cookies', '$timeout', "client",
    function ($scope, $rootScope, $cookies, $timeout, client) {
        $scope.dockerPubURL = $cookies.dockerPubURL;

        $scope.setDockerPubURL = function () {

            $scope.tobeset = $scope.dockerPubURL;

            client.checkDockerURL($scope.dockerPubURL, function (data, status) {

                $scope.showMessage = true;
                $scope.showFailMessage = !$scope.showMessage;


                $scope.dockerPubURL = data.dockerRestHost;
                $cookies.dockerPubURL = data.dockerRestHost;

                client.topos(function (topos, status) {
                    updateTabs($rootScope, topos, status);
                }, function () {
                    updateTabsOnFail($rootScope)
                });

                $timeout(function () {
                    $scope.showMessage = false;
                }, 5000, true);
            }, function (data, status, headers, config) {
                $scope.showFailMessage = true;
                $scope.showMessage = !$scope.showFailMessage;
                $timeout(function () {
                    $scope.showFailMessage = false;
                }, 10000, true);
            });
        };
    }]);
