angular.module('ngRouteExample', ['ngRoute'])
        .controller('HomeController', function ($scope, $route) {
            alert("ffff");
            $scope.$route = $route;

        })
        .controller('AboutController', function ($scope, $route) {
            alert("dd");
            $scope.$route = $route;
        })
        .config(function ($routeProvider) {
            $routeProvider.
            when('/home', {
                templateUrl: 'views/conf.html',
                controller: 'HomeController'
            }).
            when('/about', {
                templateUrl: 'views/conf.html',
                controller: 'AboutController'
            }).
            otherwise({
                // redirectTo: '/about'
            });
        });
