app.controller('YarnIndexCtrl', ["$scope", "client", function ($scope, client) {
        $scope.showLoading = true;
        client.yarnindex(function (data, status) {
            $scope.showLoading = false;

            $scope.yarn = data;
        }, function () {
            defaultFail($scope)
        });
    }]);


app.controller('yarnManage',['$scope','$location',"client",function($scope,$location,client){

    $scope.yarnRestart = function(jobId){
        if(jobId){
            if(!confirm('确认重启'+jobId+"?")){
                return false;
            }
        }

        client.yarnRestart(jobId,function(){
            window.location.reload();

        },function(){
            defaultFail($scope)
        });
    }

    $scope.yarnKillApp = function(appId){
        if(appId){
            if(!confirm('确认kill'+appId+"?")){
                return false;
            }
        }

        client.yarnKillApp(appId,function(){
            window.location.reload();

        },function(){
            defaultFail($scope)
        });
    }

    $scope.yarnStopDockerJob = function(jobId){
        if(jobId){
            if(!confirm('确认停止'+jobId+"?")){
                return false;
            }
        }

        client.yarnStopDockerJob(jobId,function(){
            window.location.reload();

        },function(){
            defaultFail($scope)
        });
    }

}]);
