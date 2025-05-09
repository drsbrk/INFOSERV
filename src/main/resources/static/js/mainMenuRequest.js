$(document).ready(function(){
    $("#searchMenu").change(function(event){
        var id=JSON.stringify({"id":$("#searchMenu").val()});
        event.preventDefault();
        $("input[type='checkbox']").prop("checked", false);
        ajaxPost(id);
    });
});
    
function ajaxPost(id){
    $.ajax({
        type:"POST",
            contentType:"application/json",
            url:"/mainmenu/search/menu",            
            data:id,            
            dataType:"json",
            success:function(result){
                if(result==null){
                    console.log("Server returned no main menu");
                }else{
                    document.getElementById("name").value= result.name;
                    document.getElementById("icon").value= result.icon;
                    document.getElementById("uri").value= result.uri;
                    document.getElementById("id").value= result.id;
                    var allSubMenus = $('[name="subMenus"]');
                    
                    console.log("Sub menus for current main menu "+result.subMenus);
                    var numberOfSubMenus = result.subMenus.length;  // Number of submenus attached to selected main menu
                    if(numberOfSubMenus>0){
                        $.each(allSubMenus, function(){
                            for (var i = 0; i < numberOfSubMenus; i++) {
                                var $this = $(this);
                                var value=$this.attr("value");
                                if(value==result.subMenus[i].id){
                                    $this.prop("checked", "checked");
                                    console.log("Sub Menu ID = " + $this.attr("value"));
                                    console.log("Sub Menu ID from DB = " + result.subMenus[i].id);
                                    
                                }
                            }
                        })
                    }else{
                        $("input[type='checkbox']").prop("checked", false);
                    }
                }
            },
            error: function(xhr, textStatus, error){
                    document.getElementById("name").value= "";
                    document.getElementById("icon").value= "";
                    document.getElementById("uri").value= "";
                    console.log(xhr.statusText);
                    console.log(textStatus);
                    console.log(error);
        }
    });
}