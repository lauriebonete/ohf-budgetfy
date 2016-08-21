/**
 * Created by Laurie on 7/4/2016.
 */
angular.module("budgetfyApp", ["selectize","angularUtils.directives.dirPagination"])
    .config(function ($httpProvider,paginationTemplateProvider) {
        $httpProvider.defaults.headers.post['Content-Type'] =  "application/json";
        paginationTemplateProvider.setPath('css/dirPagination.tpl.html');
    })
    .controller("voucherController", ["$scope","$http","voucherService","programService","activityService","particularService",function($scope,$http,voucherService,programService,activityService,particularService){
        $scope.loadVoucher = function(){
            voucherService.getAllVouchers().then(function(results){
                $scope.voucherMaxSize = results.listSize;
                $scope.voucherList = results.results;
            },function(error){

            });
        };

        $scope.viewVoucher = function(voucherId){
            $scope.selectedVoucher = voucherService.findVoucherInList($scope.voucherList,voucherId);
            particularService.findParticularsOfVoucher(voucherId).then(function(results){
                $scope.selectedVoucher.particulars = results.results
            });
            console.log($scope.selectedVoucher);
        };

        $scope.prepareNewVoucher = function(){
            $scope.createVoucher.particulars = $scope.newParticularList;
            console.log($scope.createVoucher);
            voucherService.saveVoucher($scope.createVoucher);
        };

        $scope.newParticularList = [];
        $scope.addNewParticular =function(){
            var fileId = $("#dropZone .dz-preview").attr("data-file");

            var program = programService.findProgramInList($scope.programList,$scope.addParticular.addParticularProgramModel);
            var activity = activityService.findActivityInList($scope.programActivities,$scope.addParticular.addParticularActivityModel);

            var particular = {
                "description": $scope.addParticular.description,
                "expense": $scope.addParticular.expense,
                "activity": activity,
                "program": program
            };

            if(fileId!="" &&
                fileId!=null &&
                fileId!=undefined){
                particular.receipt = {"id":fileId}
            }
            console.log(fileId,particular);

            $scope.newParticularList.push(particular);
        };

        $scope.programConfig =
        {
            valueField : 'id',
            labelField : 'programName',
            searchField: ['programName'],
            delimiter : '|',
            placeholder : 'Pick a Program',
            plugins: ['remove_button'],
            onInitialize : function (selectize) {
                // receives the selectize object as an argument
            },
            onChange: function(value) {
                activityService.getProgramActivities(value).then(function(results){
                    $scope.addParticularActivityModel = 0;
                    $scope.programActivities = results.results;
                },function(error){

                })
            },
            maxItems:1
        };

        $scope.programActivityConfig =
        {
            valueField : 'id',
            labelField : 'activityName',
            searchField: ['activityName'],
            delimiter : '|',
            placeholder : 'Pick an Activity',
            plugins: ['remove_button'],
            onInitialize : function (selectize) {
                // receives the selectize object as an argument
            },
            maxItems:1
        };

        programService.getAllPrograms().then(function(results){
            $scope.programList = results.results;
        },function(error){

        });

    }])
    .controller("programController",["$scope","$http","$filter","userService","referenceLookUpService","programService","activityService", "particularService",function($scope, $http,$filter,userService,referenceLookUpService,programService,activityService, particularService){
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
            programService.getAllPrograms().then(function successCallback(results){
                $scope.programMaxSize = results.listSize;
                $scope.programList = results.results;
            },function errorCallback(error){

            });
        };

        $scope.removeActivityProgram = function(activityId){
            activityService.removeActivityFromProgram(activityId).then(function (results){
                if(results.status){
                    $scope.selectedProgram.activities = $filter('filter')($scope.selectedProgram.activities , { id: ('!' + activityId) });
                }

                console.log(results);
            }, function (error){

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

            activityService.findActivityExpense(programId).then(function(data){
                $scope.selectedProgram.activityExpense = data.results;

                var labels = [];
                var forecastData = [];
                var actualsData = [];
                var totalActuals = 0;
                $.each(data.results,function(i,result){
                    labels.push(result.activity.activityName);
                    forecastData.push(result.activity.amount);
                    actualsData.push(result.expense);

                    totalActuals += result.expense;
                });
                var barChartData = {
                    labels: labels,
                    datasets: [{
                        label: 'Forecast',
                        backgroundColor: "#2199e8",
                        data: forecastData
                    }, {
                        label: 'Actual',
                        backgroundColor: "#79c1f1",
                        data: actualsData
                    }]

                };

                var ctxBar = document.getElementById("bar-chart").getContext("2d");
                var bar = new Chart(ctxBar, {
                    type: 'bar',
                    data: barChartData,
                    options: {
                        elements: {
                            rectangle: {
                                borderWidth: 2,
                                borderColor: "#0f598a",
                                borderSkipped: 'bottom'
                            }
                        },
                        responsive: true,
                        legend: {
                            position: 'right',
                        },
                        title: {
                            display: true,
                            text: 'Expense Chart'
                        }
                    }
                });

                $("#progressbar").progressbar({
                    value: (totalActuals/$scope.selectedProgram.totalBudget)*100
                });

                if($scope.selectedProgram.threshold<=totalActuals){
                    $("#progressbar .ui-progressbar-value").css('background-color','#ffae19');
                } else if ($scope.selectedProgram.threshold > totalActuals){
                    $("#progressbar .ui-progressbar-value").css('background-color','#3adb76');
                }

                if($scope.selectedProgram.totalBudget<totalActuals){
                    $("#progressbar .ui-progressbar-value").css('background-color','#ec5840');
                }

                $scope.unallocatedBudget = evey.addThousandsSeparator($scope.selectedProgram.totalBudget - totalActuals)

            });
        };

        $scope.viewActivityExpense = function(activityId){
            particularService.findParticularsOfActivity(activityId).then(function(result){
                $scope.activityExpense = result.results;
            }, function(error){

            });
        };

        $scope.selectActivityProgram = function(activityId){
            var found = $filter('filter')($scope.selectedProgram.activities, {id: activityId}, true);
            if(found.length>0){
                $scope.selectedActivity = found[0];
            }
        };

        $scope.updateActivity = function(){
            var foundType = $scope.activityTypeList.filter(function(type){
               return (type.id == $scope.selectedActivity.activityTypeId);
            });
            var foundCode = $scope.activityCodeList.filter(function(code){
                return (code.id == $scope.selectedActivity.activityCodeId);
            });
            if(foundCode.length>0){
                $scope.selectedActivity.activityCodeName = foundCode[0].value;
            }
            if(foundType.length>0){
                $scope.selectedActivity.activityName = foundType[0].value;
            }

            $scope.selectedActivity.activityType = {id:$scope.selectedActivity.activityTypeId};
            $scope.selectedActivity.activityCode = {id:$scope.selectedActivity.activityCodeId};
            $scope.selectedActivity.program = {id:$scope.selectedActivity.programId};
            activityService.addUpdateActivity($scope.selectedActivity).then(function(data){
                $scope.selectedProgram.activities.filter(function(activity){
                    if(activity.id==data.data.result.id){
                        activity == data.data.result;
                    }
                });
            });
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
            activityService.addUpdateActivity(activityObject).then(function(data){
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

            $scope.remainingBudget = parseInt($("#total-budget").val().replace(/\,/g,''));
            $.each($scope.addedActivityList, function(i, activity){
                $scope.remainingBudget -= activity.amount;
            });
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

        this.findProgramInList = function(programList, id){
            var foundProgram = programList.filter(function(program){
                return (program.id == id);
            });
            if(foundProgram.length>0){
                return foundProgram[0];
            }
            return null;
        };

        this.getAllPrograms = function(){
            return $http.get("/budgetfy/program/findAllSort").then(function successCallback(response){
                return response.data;
            }, function errorCallback(response){

            });
        };
    })
    .service("voucherService",function($http){
        this.getAllVouchers = function(){
            return $http.get("/budgetfy/expense/findAllSort").then(function successCallback(response){
                return response.data;
            }, function errorCallback(response){

            });
        };

        this.findVoucherInList = function(voucherList,id){
            var foundVoucher = voucherList.filter(function(voucher){
                return (voucher.id == id);
            });
            if(foundVoucher.length>0){
                return foundVoucher[0];
            }
            return null;
        };

        this.saveVoucher = function(voucher){
            return $http.post("/budgetfy/expense/",voucher)
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
    })
    .service("particularService",function($http){
        this.findParticularsOfVoucher = function(voucherId){

            var particular = {
                voucherId: voucherId
            };

            return $http.post("/budgetfy/particular/findEntity", particular).then(function successCallback(response){
                return response.data;
            }, function errorCallback(response){

            });
        };

        this.findParticularsOfActivity = function(activityId){

            var particular = {
                activityId: activityId
            };

            return $http.post("/budgetfy/particular/findEntity", particular).then(function successCallback(response){
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

        this.findActivityInList = function(activityList, id){
            var foundActivity = activityList.filter(function(activity){
                return (activity.id == id);
            });
            if(foundActivity.length>0){
                return foundActivity[0];
            }
            return null;
        };

        this.addUpdateActivity = function(activity){
            return $http.post("/budgetfy/activity", activity).then(function successCallback(response){
                return response;
            }, function errorCallback(response){

            });
        };

        this.findActivityExpense = function(programId){
            return $http.get("/budgetfy/activity/getActivityExpense",{params:{programId:programId}}).then(function successCallback(response){
                return response.data;
            }, function errorCallback(response){

            });
        };

        this.removeActivityFromProgram = function(activityId){
            return $http.get("/budgetfy/activity/countActivityExpense",{params:{activityId:activityId}}).then(function successCallback(response){
                if(response.data.status && response.data.count <= 0){
                    return $http.delete("/budgetfy/activity/"+activityId).then(function successCallback(deleteResponse){
                        return deleteResponse.data
                    }, function errorCallback(response){

                    });
                } else {
                    var returnData = {
                        "status":false,
                        "message": "cannot be delete"
                    };

                    return returnData;
                }
            }, function errorCallback(response){

            });
        };

    });

