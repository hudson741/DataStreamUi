
app.controller('SettingsCtrl', ['$scope', "client",
  function ($scope,client) {
      client.conf(function (data){
        $scope.settingDatas = data;
      },function() {
        defaultFail($scope)
      });

  }]);
