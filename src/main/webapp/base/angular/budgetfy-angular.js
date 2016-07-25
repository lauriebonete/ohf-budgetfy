/**
 * Created by Laurie on 7/4/2016.
 */
angular.module("budgetfyApp", ["selectize"])
    .config(function ($httpProvider) {
        $httpProvider.defaults.headers.post['Content-Type'] =  "application/json";
    })
    .controller("programController",["$scope","$http","userService","referenceLookUpService","programService","activityService",function($scope, $http,userService,referenceLookUpService,programService,activityService){
        $scope.userSelectizeModel = 0;
        $scope.activityTypeSelectizeModel = 0;
        $scope.userSelectConfig =
        {
            valueField : 'id',
            labelField : 'userDisplay',
            searchField: ['username','firstName','lastName'],
            delimiter : '|',
            placeholder : 'Pick something',
            plugins: ['remove_button'],
            onInitialize : function (selectize) {
                // receives the selectize object as an argument
            },
            maxItems:1
        };

        $scope.activityTypeSelectConfig =
        {
            valueField : 'id',
            labelField : 'value',
            searchField: ['value'],
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

        referenceLookUpService.getReferenceLookUpByCategory("ACTIVITY_TYPE").then(function(results){
            $scope.activityTypeList = results;
        },function(error){

        });

        referenceLookUpService.getReferenceLookUpByCategory("ACTIVITY_CODE").then(function(results){
            $scope.activityCodeList = results;
        },function(error){

        });

        $scope.loadPrograms = function(){
            programService.getAllPrograms().then(function(results){
                $scope.programList = results.results;
            },function(error){

            });
        };

        $scope.createProgram = function(){
            var programName = $("#program-name").val();
            var totalBudget = $("#total-budget").val();
            var percentage = $("#percentage").val();
            var threshold = $("#threshold").val();
            var programStart = $("#program-start").val();
            var programEnd = $("#program-end").val();

            var userAccessList = [];
            $.each($("#added-user-access div.row"),function(i,row){
                var userId = $(row).attr("data-userid");

                var programAccessSet = [];
                if($(row).find(".read-program-access").is(":checked")){
                    programAccessSet.push({access:"PROGRAM_READ_ACCESS"});
                }
                if($(row).find(".write-program-access").is(":checked")){
                    programAccessSet.push({access:"PROGRAM_WRITE_ACCESS"});
                }
                if($(row).find(".update-program-access").is(":checked")){
                    programAccessSet.push({access:"PROGRAM_UPDATE_ACCESS"});
                }
                if($(row).find(".delete-program-access").is(":checked")){
                    programAccessSet.push({access:"PROGRAM_DELETE_ACCESS"});
                }

                var userAccess = {
                    userId:userId,
                    programAccessSet: programAccessSet
                };

                userAccessList.push(userAccess);
            });

            var programObject = {
                programName:programName,
                totalBudget:Number(totalBudget.replace(/,/g, '')),
                percentage:Number(percentage),
                threshold:Number(threshold.replace(/,/g, '')),
                programStart:programStart,
                programEnd:programEnd,
                activities:addedActivityList,
                userAccessList:userAccessList
            };

            console.log(programObject);
            programService.createNewProgram(programObject);
        };

        $scope.viewProgram = function(programId){
            activityService.getProgramActivities(programId).then(function(data){
                $scope.programActivities = data.results;
            });
        }

        $scope.addedActivityList = [];
        $scope.addActivity = function(){
            var activityType = $("#program-activity .selectize-input div.item").text();
            var activityId = $("#program-activity .selectize-input div.item").attr("data-value");
            var activityCodeId = $("#activity-code option:selected").val();
            var activityCodeDisplay = $("#activity-code option:selected").text();
            var activityBudget = $("#activity-budget").val();

            var activityObject = {
                activityTypeId:activityId,
                activityName:activityType,
                activityCodeId:activityCodeId,
                amount: activityBudget,
                activityCodeName:activityCodeDisplay
            };

            $scope.addedActivityList.push(activityObject);
        }

    }])
    .service("userService", function($http){
        this.getAllUsers = function(){
            return $http.get("/user/findAll").then(function successCallback(response){
                return response.data.results;
            }, function errorCallback(response){

            });
        };
    })
    .service("referenceLookUpService",function($http){
        this.getReferenceLookUpByCategory = function(categoryName){
            return $http.get("/reference/getReferenceLookUpByCategory/"+categoryName).then(function successCallback(response){
                return response.data;
            }, function errorCallback(response){

            });
        };
    })
    .service("programService",function($http){
        this.createNewProgram = function(program){
            $http.post("/program/create-program/create",program)
                .then(function(response){
                    /*window.location = evey.getHome()+"/program"*/
                }, function(error) {
                    console.log(error);
                });
        };

        this.getAllPrograms = function(){
            return $http.get("/program/findAllSort").then(function successCallback(response){
                return response.data;
            }, function errorCallback(response){

            });
        };
    })
    .service("activityService",function($http){
        this.getProgramActivities = function(programId){

            var activity = {
                programId : programId
            };

            return $http.post("/activity/findEntity", activity).then(function successCallback(response){
                return response.data;
            }, function errorCallback(response){

            });
        }
    });

