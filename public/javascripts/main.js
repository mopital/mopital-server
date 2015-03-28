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
            }).when("/beacons", {
                templateUrl: "/assets/html/beacon-list.html",
                controller: "AppCtrl"
              }).when("/equipments", {
                templateUrl: "/assets/html/equipment-list.html",
                controller: "AppCtrl"
            }).when("/addBeacon", {
                templateUrl: "/assets/html/add-beacon.html",
                controller: "beaconController"
             }).when("/addPatient", {
                templateUrl: "/assets/html/add-patient.html",
                controller: "patientController"
             }).when("/patientBeaconLinker", {
               templateUrl: "/assets/html/beacon-bed-linker.html",
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


    var PatientList = $resource(apiUrl + "/patient/all"); // a RESTful-capable resource object
    PatientList.get(function(response) {
        $scope.patients = response.data.patientList;

    });

    var BeaconList = $resource(apiUrl + "/beacon/all"); // a RESTful-capable resource object
    BeaconList.get(function(response) {
        console.log(response);
        $scope.beacons = response.data.beaconList;

    });

    var BedList = $resource(apiUrl + "/bed/all"); // a RESTful-capable resource object
    BedList.get(function(response) {
        console.log(response);
        $scope.beds = response.data.bedList;

    });

    var EquipmentList = $resource(apiUrl + "/equipment/all"); // a RESTful-capable resource object
    EquipmentList.get(function(response) {
        console.log(response);
        $scope.equipments = response.data.equipmentList;

    });


    $scope.onClickDetail = function(detailUrl) {
        window.location = detailUrl;
    };

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
        var CreateBeaconFromRequested = $resource(apiUrl + "/beacon/add")
        CreateBeaconFromRequested.save($scope.beacon, function(response) {
            //if($rootScope.checkError(response))
    });
}
}]);


app.controller("patientController", ["$scope","$resource", "$location", "apiUrl", function($scope, $resource, $location, apiUrl) {

       $scope.addPatient = function() {
            console.log($scope.patient)
            var CreatePatientFromRequested = $resource(apiUrl + "/patient/add")
            CreatePatientFromRequested.save($scope.patient, function(response) {
                //if($rootScope.checkError(response))
        });
    }
}]);

app.controller("beaconPatientLinkerController", ["$scope","$resource", "$location", "apiUrl", function($scope, $resource, $location, apiUrl) {


   $scope.linkBeaconWithPatient = function() {
        var CreateLinkFromRequested = $resource(apiUrl + "/beacon/set/bed")
        CreateLinkFromRequested.save($scope.linker, function(response) {
            //if($rootScope.checkError(response))
         });
    }
}]);
