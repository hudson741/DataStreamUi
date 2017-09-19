app.controller('logoutCtr', ['$scope', '$location', "client", function ($scope, $location, client) {

    client.logout(function (data, status) {
        window.location.reload();

    }, function () {
        defaultFail($scope)
    });
}]);