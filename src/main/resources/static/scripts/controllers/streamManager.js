app.controller('StreamManagerCtrl', ['$scope', '$location', "client", function ($scope, $location, client) {

    $scope.showLoading = true;
    client.streamManager(function (data, status) {

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

    $scope.topoWorkNumModify = function(id,total,num){

        if(total+num<1){
            alert("剩余工作节点不足，无法操作");
            return false;
        }

        if (id) {
            if(num>0){
                if (!confirm('确认为 '+ id+ " 增加一个工作节点?")) {
                    return false;
                }
            }else{
                if (!confirm('确认为 '+ id+ " 减少一个工作节点?")) {
                    return false;
                }
            }
        }

        client.topoWorkNumModify(id,total+num,function (){
            window.location.reload();

        }, function () {
            defaultFail($scope)
        });

    }

    $scope.topoCExeModify = function(tid,cid,total,num){

        if(total+num<1){
            alert("剩余线程数不足，无法操作");
            return false;
        }

        if (tid) {
            if(num>0){
                if (!confirm('确认为 '+ tid+ " 增加一个执行线程?")) {
                    return false;
                }
            }else{
                if (!confirm('确认为 '+ tid+ " 减少一个执行线程?")) {
                    return false;
                }
            }
        }

        client.topoCExeModify(tid,cid,total+num,function (){
            window.location.reload();

        }, function () {
            defaultFail($scope)
        });

    }

}]);


