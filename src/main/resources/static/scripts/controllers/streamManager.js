app.controller('StreamManagerCtrl', ['$scope', '$location', "client", function ($scope, $location, client) {

    $scope.showLoading = true;
    client.streamManager(function (data, status) {
        $scope.showAPIFail = false;
        $scope.showLoading = false;
        $scope.topolist = data;
    }, function () {
        defaultFail($scope)
    });
}]);

app.controller('topoManager', ['$scope', '$location', "client", function ($scope, $location, client) {

    $scope.activeTopo = function(id){

        if (id) {
            if (!confirm('确认激活'+ id+ "?")) {
                return false;
            }
        }

        client.activeTopo(id,function (){
            window.location.reload();

        }, function () {
            defaultFail($scope)
        });

    }

    $scope.deactiveTopo = function(id){

        if (id) {
            if (!confirm('确实停止 '+ id+ "?")) {
                return false;
            }
        }

        client.deactiveTopo(id,function (){
            window.location.reload();

        }, function () {
            defaultFail($scope)
        });

    }

    $scope.killTopo = function(id){

        if (id) {
            if (!confirm('确认移除 '+ id+ "?")) {
                return false;
            }
        }

        client.killTopo(id,function (){
            window.location.reload();

        }, function () {
            defaultFail($scope)
        });

    }

}]);


