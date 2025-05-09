$(document).ready(function(){
    $("#email").blur(function(event){
        event.preventDefault();
        mailValidation($("#email").val());
        //ajaxPost();
        
    });
    function ajaxPost(){              
        // Do post
        $.ajax({
            type:"POST",
            contentType:"text/plain",
            url:"/users/retrieveUser",
            data:$("#email").val(),           
            // Si le retour est null, il faut omettre la clé dataType
            success:function(result){
                if(result == ""){ // No user with provided email
                    alert("L'email fourni n'est pas correct");
                    return;
                }else{
                    return;
                }
            },
            error: function(xhr, textStatus, error){                
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
    
    function modifyUserPwd(){  
        $("#changePwd").on('click', function(){             
            $("#user_form").submit();
        }); 
    }
});