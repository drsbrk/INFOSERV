$(document).ready(function(){
    toggleCheckBox();

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
                    document.getElementById("id").value=result.id;
                    console.log("Received data enabled: " + result.enabled);
                    
                    if(result.enabled == 1){
                        $("div#text-left1").replaceWith('<div class="text-left" id="text-left1"><input type="checkbox" name="enabled" id="enabled" checked="checked" value="1" class="m-2" />Utilisateur Activé</div>');
                        console.log($("#text-left1 input[type='checkbox']").val());
                    }else{
                        $("div#text-left1").replaceWith('<div class="text-left" id="text-left1"><input type="checkbox" name="enabled" id="enabled"  value="0" class="m-2" />Utilisateur Désactivé</div>');
                        console.log($("#text-left1 input[type='checkbox']").val());
                    }
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
    
    function toggleCheckBox(){
    
        $("#saveUser").on('click', function(event){
        event.preventDefault();
        var $checkbox = $("#text-left1 input[type='checkbox']");
        if($checkbox.prop('checked', true)){
            $checkbox.value="1";
        }else{
            $checkbox.value="0";
        }         
            $("#user_form").submit();
        }); 
    }
    
    
});