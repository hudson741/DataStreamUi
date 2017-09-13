app.controller('dockerm',['$scope','$location',"client",function($scope,$location,client){

    $scope.confd = [
        {cid : "0" , show : "低配1core,1G(通常是测试使用，生成环境中不建议)"},
        {cid : "1" , show : "中配1core,2G"},
        {cid : "2" , show : "均衡高配2core,4G"},
        {cid : "3" , show : "密集计算型4core,4G"},
    ];

}]);
