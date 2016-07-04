/**
 * Created by Laurie on 7/4/2016.
 */
angular.module("budgetfyApp", ["selectize"])
    .controller("programController",["$scope","$http","userService",function($scope, $http,userService){
        $scope.userSelectizeModel = 0;
        $scope.userSelectConfig =
        {
            valueField : 'id',
            labelField : 'username',
            searchField: ['username','firstname','lastname'],
            delimiter : '|',
            placeholder : 'Pick something',
            plugins: ['remove_button'],
            onInitialize : function (selectize) {
                // receives the selectize object as an argument
            },
            maxItems:1
        };

        userService.getAllUsers().then(function(result){
            $scope.allUsersList = result;
        },function(error){
            console.log(error);
        });

    }])
    .service("userService", function($http){
        this.getAllUsers = function(){
            return $http.get("/user/findAll").then(function successCallback(response){
                return response.data.results;
            }, function errorCallback(response){

            });
        };
    });

