var app = angular.module("app", ["ngResource", "ngRoute"])
    .constant("apiUrl", "http://mopital.herokuapp.com/")
    .config(function ( $httpProvider) {
        $httpProvider.defaults.useXDomain = true;
        delete $httpProvider.defaults.headers.common['X-Requested-With'];
        $httpProvider.defaults.headers.common["Accept"] = "application/json";
        $httpProvider.defaults.headers.common["Content-Type"] = "application/json";
    })
    .config(["$routeProvider", function($routeProvider) {
        return $routeProvider.when("/apiList", {
            templateUrl: "/assets/html/api-list.html",
            controller: "ApiCtrl"
        }).otherwise({
            redirectTo: "/"
        });
    }
    ]).config([
        "$locationProvider", function($locationProvider) {
            return $locationProvider.html5Mode({
                enabled: true,
                requireBase: false
            }).hashPrefix("!"); // enable the new HTML5 routing and history API
            // return $locationProvider.html5Mode(true).hashPrefix("!"); // enable the new HTML5 routing and history API
        }
    ]);

// the global ApiCtrl
app.controller("ApiCtrl", ["$scope","$resource", "$location", "apiUrl", function($scope, $resource, $location, apiUrl) {

    var ApiListData = $resource(apiUrl + "apiListData"); // a RESTful-capable resource object
    ApiListData.get(function(response) {
        $scope.methodsAndData = response.api.methodsAndPaths;
        $scope.requestObjects = response.api.requestObjects;
        console.log(response);

    });

}]);/**
 * Created by ahmetkucuk on 21/02/15.
 */
