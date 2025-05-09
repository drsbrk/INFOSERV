$(document).ready(function(){
    

    $("#userEmail").blur(function(event){
        
        event.preventDefault();
        mailValidation($("#userEmail").val());
        ajaxPost();
    });
    // What happens when button #active is clicked
    $("#deactivateUser").click(function(event){
        
        ajaxActivateUserPost();
    });
    function ajaxPost(){      
        // Do post
        $.ajax({
            type:"POST",
            contentType:"text/plain",
            url:"/users/retrieveForActivationDeactivation",
            
            data:$("#userEmail").val(),
            
            dataType:"json",
            success:function(result){
                if(result == null){
                    console.log("Server returned no data");
                }else{
                    console.log("Received data: " + JSON.stringify(result));
                    // Append checkbox to form with activation status
                    let $formTag = "<form method='post' th:action='@{/users/activation}' th:object='${userDTO}'>";
                    console.log("Value of enabled = " + result.enabled);
                    if(result.enabled==1){
                        let string =  $formTag.concat( "<input type='checkbox' value='"+result.enabled+"' checked='checked' id='active' name='enabled'/> Utilisateur Activé <button style='float:right' class='btn btn-sm btn-danger' id='deactivateUser' type='submit'>Désactiver</button><input type='hidden' id='email' name='email' value='"+result.email+"' /></form>");
                        $("#activation").html(string);
                    }else{
                        let string =  $formTag.concat( "<input type='checkbox' value='"+result.enabled+"' id='disabling' name='enabled'/> Utilisateur désactivé <button style='float:right' class='btn btn-sm btn-danger' id='activateUser' type='submit'>Activer</button><input type='hidden' id='email' name='email' value='"+result.email+"' /></form>");
                        $("#activation").html(string);
                    }
                }
            },
            error: function(xhr, textStatus, error){
                    $formTag.append("<div class='error'>Une erreur est survenue</div>");                   
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
    
    function ajaxActivateUserPost(){
        var inputData = {
            "email":$("#email").val(), 
            "enabled":$("#enabled").val()
        };
        
        $.ajax({
            type:"POST",
            dataType:"json",
            contentType : "text/plain",
            accept : 'application/json',
            data:JSON.stringify({"email":$("#email").val(),"enabled":$("#enabled").val()}),
            url:"/users/activation",
            success:function(result){
                console.log("SUBMITTED DATA = " + data);
                console.log(" RETURN OBJECT FROM CONTROLLER = "+JSON.stringify(result));
                
            },
            
            error: function(xhr, textStatus, error){
                    $formTag.append("<div class='error'>Une erreur est survenue</div>");
                    console.log("Submitted inputData data = " + data);                   
                    console.log(xhr.statusText);
                    console.log(textStatus);
                    console.log(error);
            }
        });
    }
    
    
});