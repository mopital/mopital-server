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
             }).when("/addPatient", {
                templateUrl: "/assets/html/add-patient.html",
                controller: "patientController"
             }).when("/patientBeaconLinker", {
               templateUrl: "/assets/html/beacon-patient-linker.html",
               controller: "beaconPatientLinkerController"
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

    $scope.isActive = function(viewLocation) {

        var active = false;

        if(viewLocation === $location.path()){
            active = true;
        }

        return active;
    };
}]);


    app.controller("beaconController", ["$scope","$resource", "$location", "apiUrl", function($scope, $resource, $location, apiUrl) {


       $scope.addBeacon = function() {
        console.log($scope.beacon)
        var CreateBeaconFromRequested = $resource(apiUrl + "/add/beacon")
        CreateBeaconFromRequested.save($scope.beacon, function(response) {
            //if($rootScope.checkError(response))
    });
}
}]);


    app.controller("patientController", ["$scope","$resource", "$location", "apiUrl", function($scope, $resource, $location, apiUrl) {


       $scope.addPatient = function() {
        console.log($scope.patient)
        var CreatePatientFromRequested = $resource(apiUrl + "/add/patient")
        CreatePatientFromRequested.save($scope.patient, function(response) {
            //if($rootScope.checkError(response))
    });
}
}]);

    app.controller("beaconPatientLinkerController", ["$scope","$resource", "$location", "apiUrl", function($scope, $resource, $location, apiUrl) {


       $scope.linkBeaconWithPatient = function() {
        var CreateLinkFromRequested = $resource(apiUrl + "/set/bed/beacon")
        CreateLinkFromRequested.save($scope.linker, function(response) {
            //if($rootScope.checkError(response))
    });
}
}]);
