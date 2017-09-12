app.controller('dockerm',['$scope','$location',"client",function($scope,$location,client){

    $scope.confd = [
        {cid : "1" , show : "低配1core,2G"},
        {cid : "2" , show : "均衡高配2core,4G"},
        {cid : "3" , show : "密集计算型4core,4G"},
    ];

}]);
