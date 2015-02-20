var app = angular.module("app", ["ngResource", "ngRoute"])
    .constant("apiUrl", "http://mopital.herokuapp.com/api")
    .config(function ( $httpProvider) {
        $httpProvider.defaults.useXDomain = true;
        delete $httpProvider.defaults.headers.common['X-Requested-With'];
        $httpProvider.defaults.headers.common["Accept"] = "application/json";
        $httpProvider.defaults.headers.common["Content-Type"] = "application/json";
    })
    .config(["$routeProvider", function($routeProvider) {
        return $routeProvider.when("/", {
            templateUrl: "/assets/html/patient-list.html",
            controller: "AppCtrl"
        }).when("/patients", {
            templateUrl: "/assets/html/patient-list.html",
            controller: "AppCtrl"
        }).when("/edit/:id", {
            templateUrl: "/views/detail",
            controller: ""
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

// the global controller
app.controller("AppCtrl", ["$scope","$resource", "$location", "apiUrl", function($scope, $resource, $location, apiUrl) {


    var PatientList = $resource(apiUrl + "/allPatients"); // a RESTful-capable resource object
    PatientList.get(function(response) {
        $scope.patients = response.data;

    });
}]);

// the global controller
app.controller("NewCtrl", ["$scope","$resource", "$location", "apiUrl", function($scope, $resource, $location, apiUrl) {

}]);