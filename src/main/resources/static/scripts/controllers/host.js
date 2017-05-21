'use strict';

/**
 * @ngdoc function
 * @name anotherStormUiApp.controller:HostCtrl
 * @description
 * # MainCtrl
 * Controller of the anotherStormUiApp
 */
angular.module('anotherStormUiApp')
  .controller('HostCtrl', ["$scope", '$anchorScroll', '$location', "client",
    function ($scope, $anchorScroll, $location, client) {
      $scope.showLoading = true;

      client.hosts(function (data, status) {
        $scope.showLoading = false;

        $scope.showAPIFail = false;


        $scope.hosts = data;

//                $anchorScroll();
//                var params = $location.search();
//                var archor = params.targetHost;
//                if (archor) {
//                    console.log(archor);
//                    $location.hash(archor);
//                    $anchorScroll();
//                }
//                setting location has will auto scroll to it
//                $anchorScroll();

      }, function () {
        defaultFail($scope)
      });

      $scope.gotoAnchor = function (x) {
        var newHash = $location.search().targetHost;

        if ($location.hash() !== newHash) {
          // set the $location.hash to `newHash` and
          // $anchorScroll will automatically scroll to it

//                    $location.hash(newHash);
          $anchorScroll();
        } else {
          // call $anchorScroll() explicitly,
          // since $location.hash hasn't changed

          $anchorScroll();
        }
      };

    }]);
