var app = angular.module("app", ["ngResource", "ngRoute"])
    .constant("apiUrl", "http://mopital.herokuapp.com/api")
    .config(function ( $httpProvider) {
        $httpProvider.defaults.useXDomain = true;
        delete $httpProvider.defaults.headers.common['X-Requested-With'];
        $httpProvider.defaults.headers.common["Accept"] = "application/json";
        $httpProvider.defaults.headers.common["Content-Type"] = "application/json";
    })
    .config(["$routeProvider", function($routeProvider) {
        return $routeProvider.when("/patient/add/treatment", {
            templateUrl: "/assets/html/patient-add-treatment.html",
            controller: "AddTreatmentCtrl",
            activetab : "addtreatment"
        }).when("/patient/:id", {
            templateUrl: "/assets/html/patient-detail.html",
            controller: "AppCtrl",
            activetab : "patient"
        }).when("/patient", {
            templateUrl: "/assets/html/patient-detail.html",
            controller: "AppCtrl",
            activetab : "patient"
        }).otherwise({
            redirectTo: "/patient"
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
app.controller("AppCtrl", ["$scope", "$resource", "$location", "apiUrl","$routeParams", function($scope, $resource, $location, apiUrl, $routeParams) {

    console.log("id: " + $routeParams.id);

    var Patient = $resource(apiUrl + "/get/patient/" + $routeParams.id); // a RESTful-capable resource object
    Patient.get(function(response) {
        $scope.patient = response.data;

    });

    $scope.isActive = function(viewLocation) {

        console.log(viewLocation);

        if(viewLocation === "addtreatment" && $location.path().indexOf("add/treatment") != -1)
            return true;
        if(viewLocation === "patient" && $location.path().indexOf("add/treatment") == -1)
            return true;

        return false;
    };


}]);

// the global controller
app.controller("AddTreatmentCtrl", ["$scope","$resource", "$location", "apiUrl", "$route", function($scope, $resource, $location, apiUrl, $route) {

    $scope.$route = $route;
    console.log($location.search()['id']);

    var Patient = $resource(apiUrl + "/get/patient/" + "54f25aea811b526501cc5b87"); // a RESTful-capable resource object
    Patient.get(function(response) {
        $scope.patient = response.data;

    });

}]);
