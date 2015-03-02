var app = angular.module("app", ["ngResource", "ngRoute"])
    .constant("apiUrl", "http://mopital.herokuapp.com/api")
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

    var apiList = [];
    apiList[0] = {};
    apiList[0].type = "POST";
    apiList[0].path = "/api/add/patient/";
    apiList[0].params = "bedNumber: Int, name: String, age: Int, weight: Double, height:Double, bloodType: Option[String], fileNo: Option[String], admissionDate: Option[String]";


    apiList[1] = {};
    apiList[1].type = "GET"
    apiList[1].path = "/api/allPatients"
    apiList[1].params = "none"


    apiList[2] = {};
    apiList[2].type = "POST";
    apiList[2].path = "/api/add/beacon";
    apiList[2].params = "uuid:String, major:Int, minor:Int";


    apiList[3] = {};
    apiList[3].type = "GET";
    apiList[3].path = "/api/allBeacons";
    apiList[3].params = "none";


    apiList[4] = {};
    apiList[4].type = "POST";
    apiList[4].path = "/api/add/bed";
    apiList[4].params = "bed_no:Int";


    apiList[5] = {};
    apiList[5].type = "POST";
    apiList[5].path = "/api/set/bed/beacon";
    apiList[5].params = "bed_id:String, beacon_id:String";


    apiList[6] = {};
    apiList[6].type = "GET";
    apiList[6].path = "/api/allBeds";
    apiList[6].params = "none";

    apiList[7] = {};
    apiList[7].type = "GET";
    apiList[7].path = "/api/beacon/get/patient/";
    apiList[7].params = "uuid:String";

    apiList[8] = {};
    apiList[8].type = "POST";
    apiList[8].path = "/api/add/treatment";
    apiList[8].params = "patientId: String, date: Option[String], time: Option[String], tension: Option[String], temperature: Option[String], pulse: Option[String], respiration: Option[String], pain: Option[String], definition: Option[String]";


    $scope.methods = apiList;

}]);/**
 * Created by ahmetkucuk on 21/02/15.
 */
