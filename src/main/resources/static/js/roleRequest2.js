$(document).ready(function(){
    $("#nomDeRole").blur(function(event){
        event.preventDefault();
        ajaxPost();
    });
    function ajaxPost(){
        $.ajax({
            type:"POST",
            contentType:"text/plain",
            url:"/roles/retrieveRole",            
            data:$("#nomDeRole").val(),            
            dataType:"json",
            success:function(result){
               if(result==null){// No role found using search keyword
                    console.log("Server returned no data");               
            } else{
                console.log("Fetched role = "+result);
                console.log("Received data:", result);                
                    document.getElementById("name").value= result.name;
                    document.getElementById("roleName").value= result.roleName;
                    document.getElementById("id").value= result.id;
                    // Count how many main menus the role is associated with
                    var JSONResult = JSON.stringify(result);
                    var resultParse = $.parseJSON(JSONResult); // role record with all his/her menus
                    console.log("Number of main menus associated with current role = "+resultParse.menus.length);
                    var mainMenus = $('[name="menus"]');
                    console.log("Main menus for current role "+mainMenus);
                    $.each(mainMenus, function(){
                        if(resultParse.menus.length > 0){
                            for(var i = 0;i < resultParse.menus.length; i++){
                                var $this = $(this); // One input type checkbox
                                   var value = $this.attr("value"); // Menu id that is sent when clicked
                                   if(value == result.menus[i].id){
                                        $this.prop("checked", "checked");
                                        console.log("Menu ID = " + $this.attr("value"));
                                        console.log("Menu ID from DB = " + result.menus[i].id);
                                    }
                            }
                        } else{
                            // Uncheck all checked checboxes because the current role doesn't have any main menu associated with it
                            $("input[type='checkbox']").prop("checked", false);
                        }                                             
                    });
            }
            },
            error: function(xhr, textStatus, error){
                    document.getElementById("name").value= "";
                    document.getElementById("roleName").value= "";
                    //document.getElementById("").value= "";
                    console.log(xhr.statusText);
                    console.log(textStatus);
                    console.log(error);
            }
        }); 
    }
});