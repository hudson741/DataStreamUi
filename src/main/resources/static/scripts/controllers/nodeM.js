app.controller('NodeMCtrl', ["$scope", "client", function ($scope, client) {
    $scope.showLoading = true;
    client.nodeM(function (data, status) {
        $scope.showLoading = false;

        $scope.nodes = data;
    }, function () {
        defaultFail($scope)
    });
}]);


app.controller('nodeManager',['$scope','$location',"client",function($scope,$location,client){

    $scope.delPNodes = function(jobId,ip){
        if(jobId){
            if(!confirm('确认删除并停止'+ip+"?")){
                return false;
            }
        }

        client.delPNodes(jobId,function(){
            window.location.reload();

        },function(){
            defaultFail($scope)
        });
    }

    $scope.checkConnect = function(id){
        client.checkConnect(id,function(result){
            alert(result);

        },function(){
            defaultFail($scope)
        });
    }

    $scope.runningProcess = function(id){
        client.runningProcess(id,function(result){
            alert(result);

        },function(){
            defaultFail($scope)
        });
    }

    $scope.remoteInstall = function(jobId,ip){
        if(jobId){
            if(!confirm('确认部署hadoop-yarn至'+ip+"?")){
                return false;
            }
        }

        client.remoteInstall(jobId,function(result){
            alert(result);
        },function(){
            defaultFail($scope)
        });
    }

    $scope.confSyn = function(jobId,ip){
        if(jobId){
            if(!confirm('确认同步配置文件至'+ip+"?，同步之前，请先确认您已对配置文件修改完毕")){
                return false;
            }
        }

        client.confSyn(jobId,function(result){
           alert(result);
        },function(){
            defaultFail($scope)
        });
    }

    $scope.ycmds = [
        {cmd : "rstart" , show : "启动yarn主节点"},
        {cmd : "nstart" , show : "启动yarn从节点"},
        {cmd : "rstop" ,  show : "停止yarn主节点"},
        {cmd : "nstop" , show : "停止yarn从节点"}
    ];

    $scope.hcmds = [
        {cmd : "hinit" , show : "格式化namenode"},
        {cmd : "hnstart" , show : "启动namenode"},
        {cmd : "hdstart" , show : "启动datanode"},
        {cmd : "hnstop" , show : "停止namenode"},
        {cmd : "dnstop" , show : "停止datanode"}
    ];

    $scope.exehcmd = function(hcmd,key){
        client.exehcmd(hcmd,key,function(result){
            alert(result);
        },function(){
            defaultFail($scope)
        });
    }

    $scope.logio = function(jobId){
        window.open("logio?key="+jobId)
    }

}]);
