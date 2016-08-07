/**
 * Created by Laurie on 7/4/2016.
 */
angular.module("budgetfyApp", ["selectize","angularUtils.directives.dirPagination"])
    .config(function ($httpProvider,paginationTemplateProvider) {
        $httpProvider.defaults.headers.post['Content-Type'] =  "application/json";
        paginationTemplateProvider.setPath('css/dirPagination.tpl.html');
    })
    .controller("programController",["$scope","$http","$filter","userService","referenceLookUpService","programService","activityService",function($scope, $http,$filter,userService,referenceLookUpService,programService,activityService){
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
                $scope.programMaxSize = results.listSize;
                $scope.programList = results.results;
            },function(error){

            });
        };

        $scope.addedUserList = [];
        $scope.addUser = function(){
            var row = $("#add-user-template").parents("div.row");

            var userId = $(row).find("selectize").val();
            var userName = $(row).find(".selectize-input .item").text();
            var readAccess = $(row).find("#read-access").is(":checked");
            var writeAccess = $(row).find("#write-access").is(":checked");
            var updateAccess = $(row).find("#update-access").is(":checked");
            var deleteAccess = $(row).find("#delete-access").is(":checked");

            var user = {
                id:userId,
                userName:userName,
                readAccess:readAccess,
                writeAccess:writeAccess,
                updateAccess:updateAccess,
                deleteAccess:deleteAccess,
                accessSummary:""
            };
            $scope.addedUserList.push(user);
        };

        $scope.showSummary = function(){
            var programName = $("#program-name").val();
            var totalBudget = $("#total-budget").val();
            var percentage = $("#percentage").val();
            var threshold = $("#threshold").val();
            var programStart = $("#program-start").val();
            var programEnd = $("#program-end").val();

            var userAccessList = [];
            $.each($scope.addedUserList, function(i,user){
                var programAccessSet = [];
                if(user.readAccess){
                    programAccessSet.push({access:"PROGRAM_READ_ACCESS"});
                    user.accessSummary += "Read";
                }
                if(user.writeAccess){
                    programAccessSet.push({access:"PROGRAM_WRITE_ACCESS"});
                    if(user.accessSummary!=null && user.accessSummary!="" && user.accessSummary.length>0){
                        user.accessSummary += ", ";
                    }
                    user.accessSummary += "Write";
                }
                if(user.updateAccess){
                    programAccessSet.push({access:"PROGRAM_UPDATE_ACCESS"});
                    if(user.accessSummary!=null && user.accessSummary!="" && user.accessSummary.length>0){
                        user.accessSummary += ", ";
                    }
                    user.accessSummary += "Update";
                }
                if(user.deleteAccess){
                    programAccessSet.push({access:"PROGRAM_DELETE_ACCESS"});
                    if(user.accessSummary!=null && user.accessSummary!="" && user.accessSummary.length>0){
                        user.accessSummary += ", ";
                    }
                    user.accessSummary += "Delete";
                }
                var userAccess = {
                    userId:user.id,
                    programAccessSet: programAccessSet
                };

                userAccessList.push(userAccess);
            });

            $scope.programObject = {
                programName:programName,
                totalBudget:Number(totalBudget.replace(/,/g, '')),
                percentage:Number(percentage),
                threshold:Number(threshold.replace(/,/g, '')),
                programStart:programStart,
                programEnd:programEnd,
                activities: $scope.addedActivityList,
                userAccessList:userAccessList
            };
        };

        $scope.createProgram = function(){
            programService.createNewProgram($scope.programObject).then(function(results){
                if(results!=null && results){
                    $scope.programObject = null;
                    $scope.addedActivityList = [];
                    $scope.addedUserList = [];
                    window.location = evey.getHome()+"/budgetfy/program";
                }
            },function(error){

            });
        };

        $scope.viewProgram = function(programId){
            var found = $filter('filter')($scope.programList, {id: programId}, true);
            if(found.length>0){
                $scope.selectedProgram = found[0];
            }

            activityService.getProgramActivities(programId).then(function(data){
                $scope.selectedProgram.activities = data.results;
            });
        };

        $scope.selectActivityProgram = function(activityId){
            var found = $filter('filter')($scope.selectedProgram.activities, {id: activityId}, true);
            if(found.length>0){
                $scope.selectedActivity = found[0];
            }
        };

        $scope.updateActivity = function(){
            $scope.selectedActivity.activityType = {id:$scope.selectedActivity.activityTypeId};
            /*$scope.selectedActivity.activityCode = {id:$scope.se}*/
            console.log($scope.selectedActivity);
        };

        $scope.addActivityToProgram = function(){
            var activityType = $("#activity-form .selectize-input div.item").text();
            var activityId = $("#activity-form .selectize-input div.item").attr("data-value");
            var activityCodeId = $("#activity-form option:selected").val();
            var activityCodeDisplay = $("#activity-form option:selected").text();
            var activityBudget = $("#activity-budget").val();

            var activityObject = {
                activityTypeId:activityId,
                activityType : {id:activityId},
                activityName:activityType,
                activityCodeId:activityCodeId,
                activityCode:{id:activityCodeId},
                amount: activityBudget,
                activityCodeName:activityCodeDisplay,
                program: {id:$scope.selectedProgram.id}
            };
            activityService.addActivityToProgram(activityObject).then(function(data){
                $scope.selectedProgram.activities.unshift(data.data.result);
            });
        };

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

            $scope.addedActivityList.unshift(activityObject);
        };

    }])
    .service("userService", function($http){
        this.getAllUsers = function(){
            return $http.get("/budgetfy/user/findAll").then(function successCallback(response){
                return response.data.results;
            }, function errorCallback(response){

            });
        };
    })
    .service("referenceLookUpService",function($http){
        this.getReferenceLookUpByCategory = function(categoryName){
            return $http.get("/budgetfy/reference/getReferenceLookUpByCategory/"+categoryName).then(function successCallback(response){
                return response.data;
            }, function errorCallback(response){

            });
        };
    })
    .service("programService",function($http){
        this.createNewProgram = function(program){
            return $http.post("/budgetfy/program/create-program/create",program)
                .then(function(response){
                    if(response.data.success){
                        return true;
                    } else {
                        console.log("error");
                    }
                }, function(error) {
                    console.log(error);
                });
        };

        this.getAllPrograms = function(){
            return $http.get("/budgetfy/program/findAllSort").then(function successCallback(response){
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

            return $http.post("/budgetfy/activity/findEntity", activity).then(function successCallback(response){
                return response.data;
            }, function errorCallback(response){

            });
        };

        this.addActivityToProgram = function(activity){
            return $http.post("/budgetfy/activity", activity).then(function successCallback(response){
                return response;
            }, function errorCallback(response){

            });
        }
    });

