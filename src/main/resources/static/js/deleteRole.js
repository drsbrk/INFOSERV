$(document).on('submit','#formDeleteRole', function(event){
    event.preventDefault();
        
        const data = {delRole:$("#delRole").val()};
        console.log(data);
        $.ajax({
            type:"POST",
            url:"/deleteRole",
            data:JSON.stringify(data),
            contentType:"application/json",
        }).done(
                (data) => {
                    console.log({data});
                }
            )
          .fail(
                (err) => {
                    console.log(err);
                }
          )
          .always(
                () => {
                    console.log({data});
                }
          )});
    

        
        
        