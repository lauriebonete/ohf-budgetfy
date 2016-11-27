/**
 * Created by Laurie on 7/4/2016.
 */
angular.module("budgetfyApp", ["selectize", "ngStorage", "angularUtils.directives.dirPagination",'zingchart-angularjs', 'colorpicker.module'])
    .config(function ($httpProvider, paginationTemplateProvider, yearServiceProvider, userServiceProvider) {
        $httpProvider.defaults.headers.post['Content-Type'] =  "application/json";
        paginationTemplateProvider.setPath('css/dirPagination.tpl.html');

        yearServiceProvider.$get().getYears();

    })
    .controller("reportController", ["$scope", "voucherService", "$sessionStorage", "programService", function($scope, voucherService, $sessionStorage, programService){
        $scope.voucherConfig = {
            valueField : 'id',
            labelField : 'vcNumber',
            searchField: ['vcNumber'],
            delimiter : '|',
            placeholder : 'Pick something',
            plugins: ['remove_button'],
            onInitialize : function (selectize) {
                // receives the selectize object as an argument
            },
            maxItems: 1
        };

        $scope.loadInitData = function(){
            voucherService.getAllVouchers().then(function(results){
                $scope.voucherList = results.results;
            });

            programService.getAllPrograms().then(function(results){
                $scope.programList = results.results;
            });
            $scope.years = $sessionStorage.years;
            $scope.currentYear = new Date().getFullYear().toString();
            $scope.user = $sessionStorage.user;
        };

        $scope.generateVoucherReport = function(){
            window.location.href = evey.getHome()+"/budgetfy/reports/create-voucher/"+$scope.selectedVoucherReport;
        };

        $scope.generateTotalPerProgram = function(){

            var year = $scope.currentYear;
            var programId = $scope.selectedProgram != undefined ? $scope.selectedProgram : 0;
            var programName = $("#selected-program option:selected").text() != "" ? $("#selected-program option:selected").text() : "ALL_PROGRAM";

            window.location.href = evey.getHome()+"/budgetfy/reports/program-total?year="+year+"&programId="+programId+"&programName="+programName;
        };

        $scope.generateDisbursementByDate = function(){
            var fromDate = $("#from-date").val();
            var toDate = $("#to-date").val();
            window.location.href = evey.getHome()+"/budgetfy/reports/create-disbursement?fromDate="+fromDate+"&toDate="+toDate;
        };

        $scope.yearFilter = function(program){
            if($scope.currentYear != null &&
                $scope.currentYear != "" &&
                $scope.currentYear != undefined){
                return (program.year == $scope.currentYear);
            } else {
                return program;
            }
        };
    }])
    .controller("systemController", function($scope, $sessionStorage, programService, activityService, userService){
        $scope.init = function(){
            $scope.years = $sessionStorage.years;
            $scope.currentYear = new Date().getFullYear().toString();

            userService.getCurrentUser().then(function(){
                $scope.user = $sessionStorage.user;
            });

            programService.getAllPrograms().then(function(data){
                $scope.programList = data.results;
            });
            $scope.changeYear($scope.currentYear);
            $scope.selectProgram($scope.programModel);
        };

        $scope.programConfig = {
            valueField : 'id',
            labelField : 'programName',
            searchField: ['programName'],
            delimiter : '|',
            placeholder : 'Pick something',
            plugins: ['remove_button'],
            onInitialize : function (selectize) {
                // receives the selectize object as an argument
            },
            maxItems: 1
        };
        $scope.programModel = 1;

        $scope.selectProgram = function(){
            activityService.getProgramActivities($scope.programModel).then(function(data){
                if(data.status){
                    var holder  = {};
                    var expectedSeries = [];
                    $.each(data.results, function(i, val){
                        var color = getRandColor(3);
                        holder[val.id] = color;
                        var expected = {
                            "values" : [val.amount],
                            "target": "graph",
                            "text": val.activityName,
                            "backgroundColor": color,
                            "legendText": "%t<br><b>P %v</b>",
                            "legendMarker":{
                                "type": "circle",
                                "size": 4,
                                "borderColor": color,
                                "borderWidth": 4,
                                "backgroundColor":"#fff"
                            },
                            "legendItem":{
                                "backgroundColor": color
                            },
                            "tooltip":{
                                "backgroundColor": color
                            }
                        };
                        expectedSeries.push(expected);
                    });

                    activityService.getActualExpensePerActivity($scope.programModel).then(function(data){
                        var actualSeries = [];
                        $.each(data.actual, function(i, val){
                            color = holder[val.id];
                            var actual = {
                                "values" : [val.amount],
                                "target": "graph",
                                "text": val.activityName,
                                "backgroundColor": color,
                                "legendText": "%t<br><b>P %v</b>",
                                "legendMarker":{
                                    "type": "circle",
                                    "size": 4,
                                    "borderColor": color,
                                    "borderWidth": 4,
                                    "backgroundColor":"#fff"
                                },
                                "legendItem":{
                                    "backgroundColor": color
                                },
                                "tooltip":{
                                    "backgroundColor": color
                                }
                            };
                            actualSeries.push(actual);
                        });

                        $scope.activityBudgetActualConfig = {
                            "type": "pie",

                            "title":{
                                "text": "Activities (Actuals)",
                                "align": "center",
                                "fontColor": "#616161"
                            },
                            "legend":{
                                "text":"%t<br>",
                                "width": 150,
                                "verticalAlign": "middle",
                                "borderWidth": 0,
                                "toggleAction": "remove",
                                "item":{
                                    "padding": 3,
                                    "borderRadius": 3,
                                    "fontColor": "#fff",
                                    "align": "left",
                                    "width": 120
                                },
                                "header":{
                                    "text":"Programs",
                                    "align": "center",
                                    "fontSize": 13,
                                    "bold": true,
                                    "fontColor": "#616161"
                                },
                                "itemOff":{
                                    "alpha": 0.7,
                                    "textAlpha": 1,
                                    "fontColor": "#616161",
                                    "textDecoration": "line-through",
                                    "lineWidth": 2
                                },
                                "markerOff":{
                                    "alpha": 0.2
                                }
                            },
                            "plot":{
                                "refAngle": 270,
                                "decimals": 2,
                                "align": "center",
                                "thousandsSeparator": ",",
                                "detach": false,
                                "valueBox":{
                                    "decimals": 2
                                },
                                "animation":{
                                    "effect": 3,
                                    "method": 1,
                                    "sequence": 1,
                                    "onLegendToggle": false
                                }
                            },
                            "tooltip":{
                                "text":"%t<br>P %v",
                                "placement": "node:out",
                                "offsetR": 2,
                                "width": 110,
                                "fontColor": "#fff",
                                "borderRadius": 3,
                                "bold": true,
                                "align": "right"
                            },
                            "scale":{
                                "sizeFactor": 1
                            },
                            "series": actualSeries
                        };
                    });

                    $scope.activityBudgetConfig = {
                        "type": "pie",

                        "title":{
                            "text": "Activities (Expected)",
                            "align": "center",
                            "fontColor": "#616161"
                        },
                        "legend":{
                            "text":"%t<br>",
                            "width": 150,
                            "verticalAlign": "middle",
                            "borderWidth": 0,
                            "toggleAction": "remove",
                            "item":{
                                "padding": 3,
                                "borderRadius": 3,
                                "fontColor": "#fff",
                                "align": "left",
                                "width": 120
                            },
                            "header":{
                                "text":"Programs",
                                "align": "center",
                                "fontSize": 13,
                                "bold": true,
                                "fontColor": "#616161"
                            },
                            "itemOff":{
                                "alpha": 0.7,
                                "textAlpha": 1,
                                "fontColor": "#616161",
                                "textDecoration": "line-through",
                                "lineWidth": 2
                            },
                            "markerOff":{
                                "alpha": 0.2
                            }
                        },
                        "plot":{
                            "refAngle": 270,
                            "decimals": 2,
                            "align": "center",
                            "thousandsSeparator": ",",
                            "detach": false,
                            "valueBox":{
                                "decimals": 2
                            },
                            "animation":{
                                "effect": 3,
                                "method": 1,
                                "sequence": 1,
                                "onLegendToggle": false
                            }
                        },
                        "tooltip":{
                            "text":"%t<br>P %v",
                            "placement": "node:out",
                            "offsetR": 2,
                            "width": 110,
                            "fontColor": "#fff",
                            "borderRadius": 3,
                            "bold": true,
                            "align": "right"
                        },
                        "scale":{
                            "sizeFactor": 1
                        },
                        "series": expectedSeries
                    };
                }
            });
        };

        function getRandColor(brightness){
            //6 levels of brightness from 0 to 5, 0 being the darkest
            var rgb = [Math.random() * 256, Math.random() * 256, Math.random() * 256];
            var mix = [brightness*51, brightness*51, brightness*51]; //51 => 255/5
            var mixedrgb = [rgb[0] + mix[0], rgb[1] + mix[1], rgb[2] + mix[2]].map(function(x){ return Math.round(x/2.0)})
            return "rgb(" + mixedrgb.join(",") + ")";
        };

        $scope.changeYear = function(){
            programService.getProgramBudgetOverview($scope.currentYear)
                .then(function(data){
                    if(data.success){
                        var expectedSeries = [];
                        $.each(data.expected, function(i, val){
                            var expected = {
                                "values" : [val.totalBudget],
                                "target": "graph",
                                "text": val.programName,
                                "backgroundColor": val.hexColor,
                                "legendText": "%t<br><b>P %v</b>",
                                "legendMarker":{
                                    "type": "circle",
                                    "size": 4,
                                    "borderColor": val.hexColor,
                                    "borderWidth": 4,
                                    "backgroundColor":"#fff"
                                },
                                "legendItem":{
                                    "backgroundColor": val.hexColor
                                },
                                "tooltip":{
                                    "backgroundColor": val.hexColor
                                }
                            };
                            expectedSeries.push(expected);
                        });

                        var actualSeries = [];
                        $.each(data.actual, function(i, val){
                            var actual = {
                                "values" : [val.totalBudget],
                                "target": "graph",
                                "text": val.programName,
                                "backgroundColor": val.hexColor,
                                "legendText": "%t<br><b>P %v</b>",
                                "legendMarker":{
                                    "type": "circle",
                                    "size": 4,
                                    "borderColor": val.hexColor,
                                    "borderWidth": 4,
                                    "backgroundColor":"#fff"
                                },
                                "legendItem":{
                                    "backgroundColor": val.hexColor
                                },
                                "tooltip":{
                                    "backgroundColor": val.hexColor
                                }
                            };
                            actualSeries.push(actual);
                        });

                        $scope.programActualBudgetConfig = {
                            "type": "pie",

                            "title":{
                                "text": "All Programs (Actuals)",
                                "align": "center",
                                "fontColor": "#616161"
                            },
                            "legend":{
                                "text":"%t<br>",
                                "width": 150,
                                "verticalAlign": "middle",
                                "borderWidth": 0,
                                "toggleAction": "remove",
                                "item":{
                                    "padding": 3,
                                    "borderRadius": 3,
                                    "fontColor": "#fff",
                                    "align": "left",
                                    "width": 120
                                },
                                "header":{
                                    "text":"Programs",
                                    "align": "center",
                                    "fontSize": 13,
                                    "bold": true,
                                    "fontColor": "#616161"
                                },
                                "itemOff":{
                                    "alpha": 0.7,
                                    "textAlpha": 1,
                                    "fontColor": "#616161",
                                    "textDecoration": "line-through",
                                    "lineWidth": 2
                                },
                                "markerOff":{
                                    "alpha": 0.2
                                }
                            },
                            "plot":{
                                "refAngle": 270,
                                "decimals": 2,
                                "align": "center",
                                "thousandsSeparator": ",",
                                "detach": false,
                                "valueBox":{
                                    "decimals": 2
                                },
                                "animation":{
                                    "effect": 3,
                                    "method": 1,
                                    "sequence": 1,
                                    "onLegendToggle": false
                                }
                            },
                            "tooltip":{
                                "text":"%t<br>P %v",
                                "placement": "node:out",
                                "offsetR": 2,
                                "width": 110,
                                "fontColor": "#fff",
                                "borderRadius": 3,
                                "bold": true,
                                "align": "right"
                            },
                            "scale":{
                                "sizeFactor": 1
                            },
                            "series": actualSeries
                        };

                        $scope.programBudgetConfig = {
                            "type": "pie",
                            /*"width": "50%",*/

                            "title":{
                                "text": "All Programs (Expected)",
                                "align": "center",
                                "fontColor": "#616161"
                            },
                            "legend":{
                                "text":"%t<br>",
                                "width": 150,
                                "verticalAlign": "middle",
                                "borderWidth": 0,
                                "toggleAction": "remove",
                                "item":{
                                    "padding": 3,
                                    "borderRadius": 3,
                                    "fontColor": "#fff",
                                    "align": "left",
                                    "width": 120
                                },
                                "header":{
                                    "text":"Programs",
                                    "align": "center",
                                    "fontSize": 13,
                                    "bold": true,
                                    "fontColor": "#616161"
                                },
                                "itemOff":{
                                    "alpha": 0.7,
                                    "textAlpha": 1,
                                    "fontColor": "#616161",
                                    "textDecoration": "line-through",
                                    "lineWidth": 2
                                },
                                "markerOff":{
                                    "alpha": 0.2
                                }
                            },
                            "plot":{
                                "refAngle": 270,
                                "decimals": 2,
                                "align": "center",
                                "thousandsSeparator": ",",
                                "detach": false,
                                "valueBox":{
                                    "decimals": 2
                                },
                                "animation":{
                                    "effect": 3,
                                    "method": 1,
                                    "sequence": 1,
                                    "onLegendToggle": false
                                }
                            },
                            "tooltip":{
                                "text":"%t<br>P %v",
                                "placement": "node:out",
                                "offsetR": 2,
                                "width": 110,
                                "fontColor": "#fff",
                                "borderRadius": 3,
                                "bold": true,
                                "align": "right"
                            },
                            "scale":{
                                "sizeFactor": 1
                            },
                            "series": expectedSeries
                        };
                    }
                });
        };
    })
    .controller("userRoleController", ["$scope", "$filter", "userRoleService", function($scope, $filter, userRoleService){
        $scope.loadInitData = function(){
            userRoleService.getAllUserRole().then(function(results){
                $scope.userRoleMaxSize = results.listSize;
                $scope.userRoleList = results.results;
            });

            userRoleService.getAllAuthorities().then(function(results){
                $scope.authorities = results;
                createTreeRole(results,"role-tree-update");
                $("#role-tree-update").bonsai("update");
                createTreeRole(results,"role-tree");
                $("#role-tree").bonsai('update');

            });
        };

        $scope.viewUserRole = function(id){
            $scope.selectedUserRole = userRoleService.findUserRoleInList($scope.userRoleList, id);
            createTreeRole($scope.authorities,"role-tree-update");
            $("#role-tree-update").bonsai("update");
            checkAccessRights($scope.selectedUserRole.authorities);
            $("#user-role-main").addClass("hide");
            $("#user-role-update").removeClass("hide");
        };

        $('form#create-user-role-form').on('formvalid.zf.abide', function () {
            var authorities = [];
            $("#role-tree input:checkbox:checked").each(function(i,checkbox){
                var authority = {"access": $(checkbox).attr("id")};
                authorities.push(authority);
            });

            $scope.createUserRole.authorityList = authorities;

            userRoleService.saveUserRole($scope.createUserRole).then(function(response){
                if(response.status){
                    $("#user-role-main").removeClass("hide");
                    $("#user-role-create").addClass("hide");
                    $scope.userRoleList.unshift(response.result);
                    $('#role-tree input:checkbox:checked').removeAttr('checked'); /*Jim Nov1*/
                    $scope.createUserRole.description = '';/*Jim Nov1*/
                    $scope.createUserRole.roleName = ''; /*Jim Nov1*/
                    evey.promptSuccess(response.message);
                } else{
                    evey.promptAlert(response.message);
               }
            });
        });

        $('form#update-user-role-form').on('formvalid.zf.abide', function () {
            var authorities = [];
            $("#role-tree-update input:checkbox:checked").each(function(i,checkbox){
                var authority = {"access": $(checkbox).attr("id")};
                authorities.push(authority);
            });

            $scope.selectedUserRole.authorityList = authorities;

            userRoleService.saveUserRole($scope.selectedUserRole).then(function(response){
                if(response.status){
                    $("#user-role-main").removeClass("hide");
                    $("#user-role-create").addClass("hide");
                    $("#user-role-update").addClass("hide");
                    console.log('test');
                    evey.promptSuccess(response.message);
                } else{
                    evey.promptAlert(response.message);
                }
            });
        });

        var checkAccessRights = function(data){
            console.log(data);
            $.each(data, function(i,val){
                console.log($("#role-tree-update #"+val.access));
                $("#role-tree-update #"+val.access).click();
            });
        };

        var createTreeRole = function(data, tree){
            $("#"+tree+".level-1").children().remove();
            $.each(data, function(key, value){
                var listAndName = value.split("@");
                var list = listAndName[0].split(".");

                if($("#"+tree+".level-1").children("li#"+list[0]).length>0){

                    var firstLevelNode = $("#"+tree+".level-1").children("li#"+list[0]);
                    if($(firstLevelNode).children("ul").length<=0){
                        $(firstLevelNode).append($("<ul>").addClass("level-2"));
                    }

                    var level2Node = $(firstLevelNode).find("ul.level-2").children("li#"+list[1]);
                    if($(level2Node).length>0){
                        if($(level2Node).find("ul").length<=0){
                            $(level2Node).append($("<ul>").addClass("level-3"));
                        }

                        var level3Node = $(level2Node).find("ul.level-3").children("li#"+list[2]);
                        if($(level3Node).length>0){
                            if($(level3Node).find("ul").length<=0){
                                $(level3Node).append($("<ul>").addClass("level-4"));
                            }

                            var level4Node = $(level3Node).find("ul.level-4").children("li#"+list[3]);
                            if($(level4Node).length>0){
                                if($(level4Node).find("ul").length<=0){
                                    $(level4Node).append($("<ul>").addClass("level-5"));
                                }

                                var level5Node = $(level4Node).find("ul.level-5").children("li#"+list[4]);
                                if($(level5Node).length>0) {
                                    if ($(level5Node).find("ul").length <= 0) {
                                        $(level5Node).append($("<ul>").addClass("level-6"));
                                    }
                                } else {
                                    $(level4Node).find("ul.level-5").append($("<li>").attr("id",list[4]).attr("data-id",key).append(listAndName[1]));
                                }
                            } else {
                                $(level3Node).find("ul.level-4").append($("<li>").attr("id",list[3]).attr("data-id",key).append(listAndName[1]));
                            }
                        } else {
                            $(level2Node).find("ul.level-3").append($("<li>").attr("id",list[2]).attr("data-id",key).append(listAndName[1]));
                        }
                    } else {
                        $(firstLevelNode).find("ul.level-2").append($("<li>").attr("id",list[1]).attr("data-id",key).append(listAndName[1]));
                    }
                } else {
                    $("#"+tree+".level-1").append($("<li>").attr("id",list[0]).attr("data-id",key).append(listAndName[1]));
                }
            });
        };
    }])
    .controller("userController", ["$scope","userService", "referenceLookUpService", "userRoleService", "$sessionStorage", function($scope, userService, referenceLookUpService, userRoleService, $sessionStorage){
        $scope.loadInitData = function(){
            userService.getAllUsers().then(function(results){
                $scope.userMaxSize = results.listSize;
                $scope.userList = results.results;
            });

            userRoleService.getAllUserRole().then(function(results){
                $scope.userRoleList = results.results;
            });
        };

        $scope.viewUser = function(id){
            $scope.selectedUser = userService.findUserInList($scope.userList, id);
            $("div#user-main").addClass("hide");
            $("div#user-update").removeClass("hide");
        };

        $scope.userRoleConfig =
        {
            valueField : 'id',
            labelField : 'roleName',
            searchField: ['roleName'],
            delimiter : '|',
            placeholder : 'Pick something',
            plugins: ['remove_button'],
            onInitialize : function (selectize) {
                // receives the selectize object as an argument
            }
        };

        $('form#create-user-form').on('formvalid.zf.abide', function () {
            var userRoleList = [];
            $.each($scope.createUser.userRole, function(i, role){
                var userRole = {
                    "id": role
                }
                userRoleList.push(userRole);
            });

            $scope.createUser.userRole = userRoleList;
            userService.createNewUser($scope.createUser).then(function successCallback(response){
                console.log(response);
                if(response.status){
                    $("#user-main").removeClass("hide");
                    $("#user-create").addClass("hide");
                    $("#user-create").addClass("hide");
                    $("#user-view").addClass("hide");
                    $("#user-update").addClass("hide");
                    $scope.userList.push(response.result);
                    $scope.createUser.userRole = 0;  /*Jim Nov1*/
                    evey.promptSuccess(response.message);
                } else {
                    evey.promptAlert(response.message);
                }
            }, function errorCallback(error){

            });
        });

        $('form#update-user-form').on('formvalid.zf.abide', function () {
            var userRoleList = [];
            $.each($scope.selectedUser.userRoleList, function(i, role){
                var userRole = {
                    "id": role
                }
                userRoleList.push(userRole);
            });

            $scope.selectedUser.userRole = userRoleList;
            console.log($scope.selectedUser.newPassword);
            /*$scope.selectedUser.newPassword = ''; /*Jim Nov21*/

            userService.createNewUser($scope.selectedUser).then(function successCallback(response){
                if(response.status){
                    $("#user-main").removeClass("hide");
                    $("#user-create").addClass("hide");
                    $("#user-view").addClass("hide");
                    $("#user-update").addClass("hide");
                    evey.promptSuccess(response.message);
                } else {
                    evey.promptAlert(response.message);
                }
            }, function errorCallback(error){

            });
        });


    }])
    .controller("referenceLookUpController", ["$scope", "$filter", "referenceLookUpService", function($scope, $filter, referenceLookUpService){
        $scope.loadInitData = function(){
            referenceLookUpService.getAllReference().then(function(results){
                $scope.referenceLookUpMaxSize = results.listSize;
                $scope.referenceLookUpList = results.results;
            });

            referenceLookUpService.getAllCategory().then(function(results){
                $scope.categoryList = results
            });
            $scope.user = $sessionStorage.user;
        };

        $scope.viewReference = function(id){
            $scope.selectedReference = referenceLookUpService.findReferenceInList($scope.referenceLookUpList, id);
            $("div#reference-look-up-main").addClass("hide");
            $("div#reference-look-up-update").removeClass("hide");
        };

        $scope.deleteReference = function(id) {
            referenceLookUpService.deleteReference(id).then(function(result){
                console.log(result);
            });
        };

        $('form#create-reference-form').on('formvalid.zf.abide', function () {
            referenceLookUpService.saveReference($scope.createReference).then(function successCallback(response){
                if(response.status){
                    $("div#reference-look-up-main").removeClass("hide");
                    $("div#reference-look-up-create").addClass("hide");
                    $scope.referenceLookUpList.unshift(response.result);
                    $scope.createReference.category =0;
                    $scope.createReference.key = '';
                    $scope.createReference.value ='';
                    $scope.createReference.numberValue ='';
                    evey.promptSuccess(response.message);
                } else {
                    evey.promptAlert(response.message);
                }
            }, function errorCallback(error){

            });
        });

        $('form#update-reference-form').on('formvalid.zf.abide', function () {
            referenceLookUpService.saveReference($scope.selectedReference).then(function successCallback(response){
                if(response.status){
                    $("div#reference-look-up-main").removeClass("hide");
                    $("div#reference-look-up-update").addClass("hide");
                    evey.promptSuccess(response.message);
                } else {
                    evey.promptAlert(response.message);
                }
            }, function errorCallback(error){

            });
        });
    }])
    .controller("voucherController", ["$scope","$http", "$filter","voucherService","programService","activityService","particularService","fileDetailService", "referenceLookUpService", "$sessionStorage", function($scope,$http,$filter,voucherService,programService,activityService,particularService,fileDetailService, referenceLookUpService, $sessionStorage){

        $scope.loadInitData = function(){
            voucherService.getAllVouchers().then(function(results){
                $scope.voucherMaxSize = results.listSize;
                $scope.voucherList = results.results;
            },function(error){

            });

            programService.getAllPrograms().then(function(results){
                $scope.programList = results.results;
            },function(error){

            });

            activityService.getAllActivities().then(function(results){
                $scope.activityList = results.results;
            });

            referenceLookUpService.getReferenceLookUpByCategory("VOUCHER_STATUS").then(function(results){
                $scope.voucherStatusList = results;
            });

            $scope.years = $sessionStorage.years;
            $scope.currentYear = new Date().getFullYear().toString();
            $scope.user = $sessionStorage.user;
        };

        $scope.deleteVoucher = function(voucherId){
            $scope.voucherIdToBeDeleted = voucherId;
        };

        $scope.continueDeleteVoucher = function(){
            voucherService.deleteVoucher($scope.voucherIdToBeDeleted).then(function(data){
                console.log(data);
                if(data.data.status){
                    $scope.voucherList = $filter('filter')($scope.voucherList, { id: ('!' +$scope.voucherIdToBeDeleted)});
                    evey.promptSuccess(data.data.message);
                } else {
                    evey.promptAlert(data.data.message);
                }
            });
        };

        $scope.viewVoucher = function(voucherId){
            var foundVoucher = voucherService.findVoucherInList($scope.voucherList,voucherId);
            $scope.selectedVoucher = foundVoucher;
            if ($.type(foundVoucher.date) === "string") {
                $scope.selectedVoucher.dateDisplay = foundVoucher.date;
                var date = foundVoucher.date.split("-");
                $scope.selectedVoucher.date = new Date(date[0], date[1]-1, date[2]);

            }

            particularService.findParticularsOfVoucher(voucherId).then(function(results){
                $scope.selectedVoucher.particulars = results.results

                $.each($scope.selectedVoucher.particulars,function(i, particular){
                    particular.activity = activityService.findActivityInList($scope.activityList, particular.activityId);
                    particular.activity.program = programService.findProgramInList($scope.programList, particular.activity.programId);
                });
            });
        };

        $scope.prepareNewVoucher = function(){
            var isInvalid = false;

            if(evey.isEmpty($scope.createVoucher)){
                isInvalid = true;
                $("input[ng-model='createVoucher.payee']").addClass("is-invalid-input");
                $("input[ng-model='createVoucher.payee']").parent().find("span.form-error").addClass("is-visible");
                $("input[ng-model='createVoucher.reference']").addClass("is-invalid-input");
                $("input[ng-model='createVoucher.reference']").parent().find("span.form-error").addClass("is-visible");
                $("input[ng-model='createVoucher.date']").addClass("is-invalid-input");
                $("input[ng-model='createVoucher.date']").parent().find("span.form-error").addClass("is-visible");
                $("input[ng-model='createVoucher.vcNumber']").addClass("is-invalid-input");
                $("input[ng-model='createVoucher.vcNumber']").parent().find("span.form-error").addClass("is-visible");
                $("input[ng-model='createVoucher.totalAmount']").addClass("is-invalid-input");
                $("input[ng-model='createVoucher.totalAmount']").parent().find("span.form-error").addClass("is-visible");
            } else {
                if(evey.isEmpty($scope.createVoucher.payee)){
                    $("input[ng-model='createVoucher.payee']").addClass("is-invalid-input");
                    $("input[ng-model='createVoucher.payee']").parent().find("span.form-error").addClass("is-visible");
                    isInvalid = true;
                } else {
                    $("input[ng-model='createVoucher.payee']").removeClass("is-invalid-input");
                    $("input[ng-model='createVoucher.payee']").parent().find("span.form-error").removeClass("is-visible");
                }

                if(evey.isEmpty($scope.createVoucher.reference)){
                    $("input[ng-model='createVoucher.reference']").addClass("is-invalid-input");
                    $("input[ng-model='createVoucher.reference']").parent().find("span.form-error").addClass("is-visible");
                    isInvalid = true;
                } else {
                    $("input[ng-model='createVoucher.reference']").removeClass("is-invalid-input");
                    $("input[ng-model='createVoucher.reference']").parent().find("span.form-error").removeClass("is-visible");
                }

                if(evey.isEmpty($scope.createVoucher.date)){
                    $("input[ng-model='createVoucher.date']").addClass("is-invalid-input");
                    $("input[ng-model='createVoucher.date']").parent().find("span.form-error").addClass("is-visible");
                    isInvalid = true;
                } else {
                    $("input[ng-model='createVoucher.date']").removeClass("is-invalid-input");
                    $("input[ng-model='createVoucher.date']").parent().find("span.form-error").removeClass("is-visible");
                }

                if(evey.isEmpty($scope.createVoucher.vcNumber)){
                    $("input[ng-model='createVoucher.vcNumber']").addClass("is-invalid-input");
                    $("input[ng-model='createVoucher.vcNumber']").parent().find("span.form-error").addClass("is-visible");
                    isInvalid = true;
                } else {
                    $("input[ng-model='createVoucher.vcNumber']").removeClass("is-invalid-input");
                    $("input[ng-model='createVoucher.vcNumber']").parent().find("span.form-error").removeClass("is-visible");
                }

                if(evey.isEmpty($scope.createVoucher.totalAmount)){
                    $("input[ng-model='createVoucher.totalAmount']").addClass("is-invalid-input");
                    $("input[ng-model='createVoucher.totalAmount']").parent().find("span.form-error").addClass("is-visible");
                    isInvalid = true;
                } else {
                    $("input[ng-model='createVoucher.totalAmount']").removeClass("is-invalid-input");
                    $("input[ng-model='createVoucher.totalAmount']").parent().find("span.form-error").removeClass("is-visible");
                }

                if(!isInvalid){
                    $("#expense-add").css("left","-100%");
                    $("#expense-add-summary").css("left", "0");
                    $scope.createVoucher.particulars = $scope.newParticularList;
                    $scope.createVoucher.statusDisplay =  $("#create-voucher-status option:selected").text();
                }
            }

        };

        $scope.changeStatus = function(){
            $scope.selectedVoucher.statusDisplay =  $("#update-status option:selected").text();
        };

        $scope.createVoucherObj = function(){
            $scope.createVoucher.status = {"id": $("#create-voucher-status").val()};
            $scope.createVoucher.totalAmount = Number($scope.createVoucher.totalAmount.replace(/,/g, ''));
            voucherService.saveVoucher($scope.createVoucher).then(function (result){
                if(result.data.status){
                    $scope.voucherList.unshift(result.data.result);
                    $("#expense-main").removeClass("hide");
                    $("#expense-add-container").addClass("hide");
                    $("#expense-update-container").addClass("hide");
                    evey.promptSuccess(result.data.message);
                } else{
                    evey.promptAlert(result.data.message);
                }
            });
        };

        $scope.newParticularList = [];
        $scope.addNewParticular = function(attachmentContainer){
            var fileId = null;
            var preview = $("div#"+attachmentContainer).parent(".columns").find("img.ajax-file-upload-preview");
            if(preview.length>0){
                fileId = $(preview[0]).attr("data-upload-id");
            }
            var program = programService.findProgramInList($scope.programList,$scope.addParticular.addParticularProgramModel);
            var activity = activityService.findActivityInList($scope.programActivities,$scope.addParticular.addParticularActivityModel);

            var particular = {
                "description": $scope.addParticular.description,
                "expense":Number($scope.addParticular.expense.replace(/,/g, '')) ,
                "activity": activity,
                "program": program,
                "tempId": uuid.v4()
            };

            if(fileId!=null){
                particular.receipt = {"id":fileId};
            }

            MotionUI.animateOut($('#add-expense-form'), 'slide-out-up');
            $scope.newParticularList.push(particular);

            $scope.computeVariance();
            $scope.addParticular.addParticularProgramModel = 0;
            $scope.addParticular.addParticularActivityModel = 0;
        };

        $scope.computeVariance = function(){
            $scope.createVoucher.variance = Number(String($scope.createVoucher.totalAmount).replace(/,/g, ''));
            $.each($scope.newParticularList, function(i, particular){
                $scope.createVoucher.variance -= particular.expense
            });
        };

        $scope.addNewParticularSelectedVoucher = function(attachmentContainer){
            var fileId = null;
            var preview = $("div#"+attachmentContainer).parent(".columns").find("img.ajax-file-upload-preview");
            if(preview.length>0){
                fileId = $(preview[0]).attr("data-upload-id");
            }
            var program = programService.findProgramInList($scope.programList,$scope.addParticular.addParticularProgramModel);
            var activity = activityService.findActivityInList($scope.programActivities,$scope.addParticular.addParticularActivityModel);
            activity.program = program;

            var particular = {
                "description": $scope.addParticular.description,
                "expense": Number($scope.addParticular.expense.replace(/,/g, '')),
                "displayExpense": evey.formatDisplayMoney($scope.addParticular.expense),
                "activity": activity,
                "tempId": uuid.v4()
            };

            if(fileId!=null){
                particular.receipt = {"id":fileId};
                particular.receiptId = fileId;
            }

            MotionUI.animateOut($('div#add-exist-expense-form'), 'slide-out-up');
            $scope.selectedVoucher.particulars.unshift(particular);
            $scope.computeVarianceUpdate();
            $scope.addParticular.addParticularProgramModel = 0;/*JIM nov 1*/
            $scope.addParticular.addParticularActivityModel = 0;/*JIM nov 1*/
        };

        $scope.computeVarianceUpdate = function(){
            $scope.selectedVoucher.displayTotalExpense = Number(String($scope.selectedVoucher.totalAmount).replace(/,/g, ''));
            $.each($scope.selectedVoucher.particulars, function(i, particular){
                $scope.selectedVoucher.displayTotalExpense -= particular.expense
            });
        };

        $scope.updateSelectedParticular = function(){
            $scope.selectedParticular.displayExpense = "P"+evey.addThousandsSeparator($scope.selectedParticular.expense)
        };

        $scope.viewSelectedParticular = function(particularId){
            $scope.selectedParticular = particularService.findParticularFromList($scope.selectedVoucher.particulars,particularId);

            if($scope.selectedParticular.activity!=null &&
                ($scope.selectedParticular.activityId==null ||
                $scope.selectedParticular.activityId=="" ||
                $scope.selectedParticular.activityId==undefined ||
                $scope.selectedParticular.activityId==0)){
                $scope.selectedParticular.activityId =$scope.selectedParticular.activity.id;
            }

            if($scope.selectedParticular.activity.program!=null &&
                ($scope.selectedParticular.activity.programId==null ||
                $scope.selectedParticular.activity.programId=="" ||
                $scope.selectedParticular.activity.programId==undefined ||
                $scope.selectedParticular.activity.programId==0)){
                $scope.selectedParticular.activity.programId =$scope.selectedParticular.program.id;
            }

            if($scope.selectedParticular.activity.programId!=null){
                activityService.getProgramActivities($scope.selectedParticular.activity.programId).then(function(results){
                    $scope.programActivities = results.results;
                },function(error){

                });
            }
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

        $scope.downloadAttachment = function(fileId){
            fileDetailService.downloadFile(fileId)
        };


        $scope.removeParticularOnAdd = function(particular){
            $scope.newParticularList = $filter('filter')($scope.newParticularList, { tempId: ('!' + particular.tempId)/*, activity: {id: ("!"+particular.activity.id)}, program: {id: ("!"+particular.program.id)}  */});
            $scope.computeVariance();
        };

        $scope.removeParticular = function(particular){
            if(!evey.isEmpty(particular.id)){
                particularService.deleteParticular(particular.id).then(function(results){
                    if(results.status){
                        $scope.selectedVoucher.particulars = $filter('filter')($scope.selectedVoucher.particulars, { id: ('!' +particular.id)});
                        $scope.computeVarianceUpdate();
                    } else {
                        evey.promptAlert(results.message);
                    }
                },function(error){

                });
            } else {
                $scope.selectedVoucher.particulars = $filter('filter')($scope.selectedVoucher.particulars, { tempId: ('!' +particular.tempId)});
                $scope.computeVarianceUpdate();
            }
        };

        $scope.yearFilter = function(voucher){
            if($scope.currentYear != null &&
                $scope.currentYear != "" &&
                $scope.currentYear != undefined){
                return (voucher.voucherYear == $scope.currentYear);
            } else {
                return voucher;
            }
        };


        $scope.changeDate = function(){
            $scope.selectedVoucher.dateDisplay = $scope.selectedVoucher.date.getFullYear() +"-"+($scope.selectedVoucher.date.getMonth()+1)+"-"+$scope.selectedVoucher.date.getDate()
        };

        $scope.updateVoucher = function(){
            $.each($scope.selectedVoucher.particulars, function(i, particular){
                particular.expense = Number(String(particular.expense).replace(/,/g, ''));
            });

            $scope.selectedVoucher.date = $scope.selectedVoucher.dateDisplay;
            voucherService.saveVoucher($scope.selectedVoucher).then(function(result){
                if(result.data.status){
                    console.log($scope.selectedVoucher.dateDisplay);
                    $scope.selectedVoucher.displayDate = moment($scope.selectedVoucher.dateDisplay).format("MMM DD, YYYY");

                    $("#expense-main").removeClass("hide");
                    $("#expense-add-container").addClass("hide");
                    $("#expense-update-container").addClass("hide");
                    $("#expense-update").css("left","0");
                    $("#expense-update-summary").css("left", "+100%");
                    evey.promptSuccess(result.data.message);
                } else{
                    evey.promptAlert(result.data.message);
                }
            });
        };

    }])
    .controller("programController",["$scope","$http","$filter","userService","referenceLookUpService","programService","activityService", "particularService", "voucherService", "$sessionStorage" ,function($scope, $http,$filter,userService,referenceLookUpService,programService,activityService, particularService, voucherService, $sessionStorage){
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

        referenceLookUpService.getReferenceLookUpByCategory("ACTIVITY_TYPE").then(function(results){
            $scope.activityTypeList = results;
        },function(error){

        });

        referenceLookUpService.getReferenceLookUpByCategory("ACTIVITY_CODE").then(function(results){
            $scope.activityCodeList = results;
        },function(error){

        });

        userService.getAllUsers().then(function(result){
            $scope.allUsersList = result.results;
        },function(error){
            console.log(error);
        });

        $scope.loadInitData = function(){
            programService.getAllPrograms().then(function successCallback(results){
                $scope.programMaxSize = results.listSize;
                $scope.programList = results.results;
            },function errorCallback(error){

            });

            voucherService.getAllVouchers().then(function successCallback(results){
                $scope.voucherList = results.results;
            });

            activityService.getAllActivities().then(function successCallbck(results){
                $scope.activityList = results.results;
            });

            $scope.years = $sessionStorage.years;
            $scope.currentYear = new Date().getFullYear().toString();
            $scope.user = $sessionStorage.user;
        };

        $scope.loadPrograms = function(){
            programService.getAllPrograms().then(function successCallback(results){
                $scope.programMaxSize = results.listSize;
                $scope.programList = results.results;
            },function errorCallback(error){

            });
        };

        $scope.yearFilter = function(program){
            if($scope.currentYear != null &&
                $scope.currentYear != "" &&
                $scope.currentYear != undefined){
                return (program.year == $scope.currentYear);
            } else {
                return program;
            }
        };

        $scope.removeActivityProgram = function(activityId){
            activityService.removeActivityFromProgram(activityId).then(function (results){
                console.log(activityId);
                if(results.status){
                    $scope.selectedProgram.activities = $filter('filter')($scope.selectedProgram.activities , { id: ('!' + activityId) });
                    evey.promptSuccess(results.message); /*JIM nov1*/
                } else{
                    evey.promptAlert(results.message); /*JIM nov1*/
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
            $scope.userSelectizeModel = 0;
        };

        $scope.removeUserFromList = function(id){
            $scope.addedUserList = $filter('filter')($scope.addedUserList , { id: ('!' + id) });
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
                hexColor: $scope.hexColor,
                userAccessList:userAccessList
            };
        };

        $scope.createProgram = function(){
            $scope.isDisabled = true;
            programService.createNewProgram($scope.programObject).then(function(results){
                if(results!=null && results){
                    $scope.programObject = null;
                    $scope.addedActivityList = [];
                    $scope.addedUserList = [];
                    window.location = evey.getHome()+"/budgetfy/program";
                    evey.promptSuccess(results.message)
                } else{
                    evey.promptAlert(results.message);
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

                $.each($scope.activityExpense, function(i, particular){
                    particular.voucher = voucherService.findVoucherInList($scope.voucherList, particular.voucherId);
                    particular.activity = activityService.findActivityInList($scope.activityList, particular.activityId);
                });
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
            $scope.selectedActivity.amount = Number($scope.selectedActivity.amount.replace(/,/g, '')); /*JIM Nov1*/
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
                    console.log(activity); /*JIM 20161101*/
                    if(activity.id==data.data.result.id){
                        activity == data.data.result;
                        MotionUI.animateOut($('#update-activity-form'), 'slide-out-up'); /*JIM 20161101*/
                        evey.promptSuccess(data.data.message); /*JIM 20161101*/
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


            var isInvalid = false;
            if(activityBudget == null ||
                activityBudget == undefined ||
                activityBudget == "" ||
                activityBudget <= 0){
                $("#activity-budget").addClass("is-invalid-input");
                $("#activity-budget").parent().find("span.form-error").addClass("is-visible");
                $('label[for="activity-budget"]').addClass("is-invalid-label");

                isInvalid = true;
            } else {
                $("#activity-budget").removeClass("is-invalid-input");
                $("#activity-budget").parent().find("span.form-error").removeClass("is-visible");
                $('label[for="activity-budget"]').removeClass("is-invalid-label");
            }

            if(activityId == null ||
                activityId == undefined ||
                activityId == "" ||
                activityId <= 0){
                $("#activity").parent().find("label").addClass("is-invalid-label");
                $("#activity").find("span.form-error").addClass("is-visible");

                isInvalid = true;
            } else {
                $("#activity").parent().find("label").removeClass("is-invalid-label");
                $("#activity").find("span.form-error").removeClass("is-visible");
            }

            if(!isInvalid){
                var activityObject = {
                    activityTypeId:activityId,
                    activityType : {id:activityId},
                    activityName:activityType,
                    activityCodeId:activityCodeId,
                    activityCode:{id:activityCodeId},
                    amount:Number(activityBudget.replace(/,/g, '')),
                    activityCodeName:activityCodeDisplay,
                    program: {id:$scope.selectedProgram.id}
                };
                activityService.addUpdateActivity(activityObject).then(function (data){
                    if(data.data.status){
                        MotionUI.animateOut($('#activity-form'), 'slide-out-up');
                        $scope.selectedProgram.activities.unshift(data.data.result);
                        evey.promptSuccess(data.data.message);
                    } else {
                        evey.promptAlert(data.data.message);
                    }
                });
                $scope.activityTypeSelectizeModel = 0; /*JIM NOV1*/
            }
        };

        $scope.addedActivityList = [];
        $scope.addActivity = function(){
            var activityType = $("#program-activity .selectize-input div.item").text();
            var activityId = $("#program-activity .selectize-input div.item").attr("data-value");
            var activityCodeId = $("#activity-code option:selected").val();
            var activityCodeDisplay = $("#activity-code option:selected").text();
            var activityBudget = $("#activity-budget").val();

            var isInvalid = false;
            if(activityBudget == null ||
                activityBudget == undefined ||
                activityBudget == "" ||
                activityBudget <= 0){
                $("#activity-budget").addClass("is-invalid-input");
                $("#activity-budget").parent().find("span.form-error").addClass("is-visible");
                $('label[for="activity-budget"]').addClass("is-invalid-label");

                isInvalid = true;
            } else {
                $("#activity-budget").removeClass("is-invalid-input");
                $("#activity-budget").parent().find("span.form-error").removeClass("is-visible");
                $('label[for="activity-budget"]').removeClass("is-invalid-label");
            }

            if(activityId == null ||
                activityId == undefined ||
                activityId == "" ||
                activityId <= 0){
                $("#activity").parent().find("label").addClass("is-invalid-label");
                $("#activity").find("span.form-error").addClass("is-visible");

                isInvalid = true;
            } else {
                $("#activity").parent().find("label").removeClass("is-invalid-label");
                $("#activity").find("span.form-error").removeClass("is-visible");
            }

            if(!isInvalid){
                var activityObject = {
                    activityTypeId:activityId,
                    activityName:activityType,
                    activityCodeId:activityCodeId,
                    amount:Number(activityBudget.replace(/,/g, '')),
                    activityCodeName:activityCodeDisplay
                };

                $scope.addedActivityList.unshift(activityObject);

                $scope.remainingBudget = parseInt($("#total-budget").val().replace(/\,/g,''));

                $.each($scope.addedActivityList, function(i, activity){
                    $scope.remainingBudget -= activity.amount;
                    /*$scope.remainingBudget = evey.addThousandsSeparator($scope.remainingBudget); /*JIM Nov2*/
                });
                $scope.activityTypeSelectizeModel = 0; /*JIM nov1*/
            };

        };

        $scope.removeAddedActivity = function(id){
            $scope.addedActivityList = $filter('filter')($scope.addedActivityList , { activityTypeId: ('!' + id) });
        };


    }])
    .service("userService", function($http, $sessionStorage){
        this.getAllUsers = function(){
            return $http.get("/budgetfy/user/findAll").then(function successCallback(response){
                return response.data;
            }, function errorCallback(response){

            });
        };

        this.getCurrentUser = function(){
            return $http.get("/budgetfy/login/get-logged-user")
                .then(function(response){
                    if(response.data.status){
                        $sessionStorage.user =  response.data.user;
                    }
                });
        };

        this.createNewUser = function(user){
            return $http.post("/budgetfy/user/",user)
                .then(function(response){
                    return response.data;
                }, function(error) {
                    console.log(error);
                });
        };

        this.findUserInList = function(userList, id){
            var foundUser = userList.filter(function(user){
                return (user.id == id);
            });
            if(foundUser.length>0){
                return foundUser[0];
            }
            return null;
        };
    })
    .service("referenceLookUpService",function($http){
        this.getReferenceLookUpByCategory = function(categoryName){
            return $http.get("/budgetfy/reference/getReferenceLookUpByCategory/"+categoryName).then(function successCallback(response){
                return response.data;
            }, function errorCallback(response){

            });
        };

        this.getAllReference = function(){
            return $http.get("/budgetfy/reference/findAll").then(function successCallback(response){
                return response.data;
            }, function errorCallback(response){

            });
        };

        this.getAllCategory = function(){
            return $http.get("/budgetfy/reference/findAllCategory").then(function successCallback(response){
                return response.data;
            }, function errorCallback(response){

            });
        };

        this.saveReference = function(reference){
            return $http.post("/budgetfy/reference/",reference)
                .then(function(response){
                    return response.data;
                }, function(error) {
                    console.log(error);
                });
        };

        this.deleteReference = function(id){
            console.log("performing delete ", id);
            return $http.delete("/budgetfy/reference/"+id)
                .then(function(response){
                    return response.data;
                }, function(error) {
                    console.log(error);
                });
        };

        this.findReferenceInList = function(referenceList,id){
            var foundReference = referenceList.filter(function(reference){
                return (reference.id == id);
            });
            if(foundReference.length>0){
                return foundReference[0];
            }
            return null;
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

        this.getProgramBudgetOverview = function(year){
            return $http.get("/budgetfy/overview/getProgramBudgetOverview", {params:{year:year}}).then(function successCallback(response){
                return response.data;
            }, function errorCallback(response){

            });
        };

        this.getYears = function(){
            return $http.get("/budgetfy/program/getYears")
                .then(function(response){
                    if(response.data.status){
                        return response.data.results;
                    }
                    return {"status": "false"};
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

        this.deleteVoucher = function(voucherId){
            return $http.delete("/budgetfy/expense/"+voucherId)
                .then(function successCallback(response){
                    return response
                }, function errorCallback(error) {
                    console.log(error);
                });
        };

        this.saveVoucher = function(voucher){
            return $http.post("/budgetfy/expense/",voucher)
                .then(function successCallback(response){
                    return response
                }, function errorCallback(error) {
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

        this.deleteParticular = function(particularId){
            return $http.delete("/budgetfy/particular/"+particularId).then(function successCallback(response){
                return response.data;
            }, function errorCallback(response){

            });
        };

        this.findParticularFromList = function(particularList, particularId){
            var foundParticular = particularList.filter(function(particular){
                return (particular.id == particularId);
            });
            if(foundParticular.length>0){
                return foundParticular[0];
            }
            return null;
        };

    }).service("fileDetailService", function($http){
        this.downloadFile = function(fileId){
            window.location.href = evey.getHome()+"/budgetfy/file/download/"+fileId;
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

        this.getActualExpensePerActivity = function(programId){
            return $http.get("/budgetfy/overview/getActualActivity",{params:{programId:programId}}).then(function successCallback(response){
                return response.data;
            }, function errorCallback(response){

            });
        };

        this.getAllActivities = function(){
            return $http.get("/budgetfy/activity/findAll").then(function successCallback(response){
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

    })
    .service("userRoleService", function($http){
        this.getAllUserRole = function(){
            return $http.get("/budgetfy/userRole/findAll").then(function successCallback(response){
                return response.data;
            }, function errorCallback(response){

            });
        };

        this.getAllAuthorities  = function(){
            return $http.get("/budgetfy/userRole/get-all-authorities").then(function successCallback(response){
                return response.data;
            }, function errorCallback(response){

            });
        };

        this.saveUserRole = function(userRole){
            return $http.post("/budgetfy/userRole/save-access",userRole)
                .then(function(response){
                    return response.data;
                }, function(error) {
                    console.log(error);
                });
        }

        this.findUserRoleInList = function(userRoleList,id){
            var foundUserRole = userRoleList.filter(function(userRole){
                return (userRole.id == id);
            });
            if(foundUserRole.length>0){
                return foundUserRole[0];
            }
            return null;
        };
    }).service("yearService", function($http, $sessionStorage){
        this.getYears = function(){
            $http.get("/budgetfy/program/getYears")
                .then(function(response){
                    if(response.data.status){
                        $sessionStorage.years =  response.data.results;
                    }
                });
        };
    }).filter('sumOfValue', function () {
        return function (data, key) {
            if (angular.isUndefined(data) && angular.isUndefined(key)) {
                return 0;
            }
            var sum = 0;
            angular.forEach(data,function(value){
                sum = sum + parseFloat(value[key]);
            });
            return evey.formatDisplayMoney(sum);
        }
    });

