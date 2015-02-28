var app = angular.module("app", ["ngResource", "ngRoute"])
    .constant("apiUrl", "http://127.0.0.1:9000/api")
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
        }).when("/beacons", {
            templateUrl: "/assets/html/beacon-list.html",
            controller: "AppCtrl"
          }).when("/addBeacon", {
                        templateUrl: "/assets/html/add-beacon.html",
                        controller: "beaconController"
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

    var BeaconList = $resource(apiUrl + "/allBeacons"); // a RESTful-capable resource object
    BeaconList.get(function(response) {
        console.log(response);
        $scope.beacons = response.data;

    });
}]);


app.controller("beaconController", ["$scope","$resource", "$location", "apiUrl", function($scope, $resource, $location, apiUrl) {


   $scope.addBeacon = function() {
    console.log($scope.beacon)
    var CreateBookFromRequested = $resource(apiUrl + "/add/beacon")
    CreateBookFromRequested.save($scope.beacon, function(response) {
        //if($rootScope.checkError(response))
    });
}
}
]);