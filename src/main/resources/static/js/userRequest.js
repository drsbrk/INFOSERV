$(document).ready(function(){
    $("#userEmail").blur(function(event){
        
        event.preventDefault();
        mailValidation($("#userEmail").val());
        ajaxPost();
    });
    function ajaxPost(){     
        // Do post
        $.ajax({
            type:"POST",
            contentType:"text/plain",
            url:"/users/retrieveUser",
            
            data:$("#userEmail").val(),
            
            dataType:"json",
            success:function(result){
                if(result == null){
                    console.log("Server returned no data");
                }else{
                    console.log("Received data:", result);
                    $("#password").parent().remove(); // HIDE PASS FIELD
                    // User found, change h1 heading
                    $("h3").text("BI::MODIFICATION D'UN COMPTE UTILISATEUR");
                    document.getElementById("id").value= result.id;
                    document.getElementById("firstName").value= result.firstName;
                    document.getElementById("lastName").value= result.lastName;
                    document.getElementById("email").value= result.email;
                    //set the select value from ajax result
                    $("#role").val(result.role.id);
                    // trigger the change event
                    $("#role").select2().trigger("change");
                    console.log("Role ID = "+result.role.id);       
                }
            },
            error: function(xhr, textStatus, error){
                    document.getElementById("firstName").value= "";
                    document.getElementById("lastName").value= "";
                    document.getElementById("email").value= "";
                    console.log(xhr.statusText);
                    console.log(textStatus);
                    console.log(error);
            }
        });
    }
    
    function mailValidation(val) {
    var expr = /^([\w-\.]+)@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.)|(([\w-]+\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\]?)$/;

        if (!expr.test(val)) {
            alert("Veuillez entrer un email valide");
            return;
        }
    }
    
    
});