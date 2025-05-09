$(document).ready(function() {
    //$("#role").val("");
    $("#menus").change(function(e) {

        var id = JSON.stringify({ id: $("#menus").val() });
        if ($("#menus").val() == 0) {
            clearcontent('loading'); // because roleId = 0 does not exist
        } else {
            clearcontent('loading');
            // Send only when roleid is different from 0
            ajaxPost(id);
            
        }
    });


    $("#saveRoleDetails").click(function(event) {
        event.preventDefault();
        if ($("#role").val() == 0) {
            iziToast.destroy();
            iziToast.question({
                timeout: 20000,
                close: false,
                overlay: true,
                displayMode: 'once',
                id: 'question',
                zindex: 999,
                title: 'Hey',
                message: 'Voulez vous sauvegarder sans choisir un profil?',
                position: 'center',
                buttons: [
                    ['<button><b>YES</b></button>', function(instance, toast) {

                        instance.hide({ transitionOut: 'fadeOut' }, toast, 'button');

                    }, true],
                    ['<button>NO</button>', function(instance, toast) {

                        instance.hide({ transitionOut: 'fadeOut' }, toast, 'button');

                    }],
                ],
                onClosing: function(instance, toast, closedBy) {
                    console.info('Closing | closedBy: ' + closedBy);
                },
                onClosed: function(instance, toast, closedBy) {
                    console.info('Closed | closedBy: ' + closedBy);
                }
            });
            return;
        }
        if ($("#menus").val() == 0) {
            iziToast.destroy();
            iziToast.question({
                timeout: 20000,
                close: false,
                overlay: true,
                displayMode: 'once',
                id: 'question',
                zindex: 999,
                title: 'Hey',
                message: 'Voulez vous sauvegarder sans choisir un menu?',
                position: 'center',
                buttons: [
                    ['<button><b>YES</b></button>', function(instance, toast) {

                        instance.hide({ transitionOut: 'fadeOut' }, toast, 'button');

                    }, true],
                    ['<button>NO</button>', function(instance, toast) {

                        instance.hide({ transitionOut: 'fadeOut' }, toast, 'button');

                    }],
                ],
                onClosing: function(instance, toast, closedBy) {
                    console.info('Closing | closedBy: ' + closedBy);
                },
                onClosed: function(instance, toast, closedBy) {
                    console.info('Closed | closedBy: ' + closedBy);
                }
            });
            return;
        }
        let subMenus = [];
        $("input:checkbox[name=col3SubMenuId]:checked").each(function() {
            subMenus.push($(this).val());
        });

        let donnees = JSON.stringify({
            roleId: $("#role").val(),
            menuId: $("#menus").val(), subMenuList: subMenus
        });
        postRoleDetails(donnees);
    });

    $(document).on("change", "#role", function() {
        var selectedVal = $("#role").val();
        if (selectedVal == 0) {
            $('input:checkbox').prop('checked', false);
        }
        if (selectedVal != 0 && $("#menus").val() != 0) {
            var donnees = JSON.stringify({ role_id: $("#role").val(), menu_id: $("#menus").val() });
            searchRoleDetails(donnees);
        }
    });
});


function ajaxPost(id) {
    $.ajax({
        type: "POST",
        contentType: "application/json",
        url: "/mainmenu/roleDetails",
        data: id,
        dataType: "json",
        success: function(result) {
            if (jQuery.isEmptyObject(result[0])) {
                console.log("Menu ID does not exist");
                console.log(result);
                $('input:checkbox').prop('checked', false);

            } else {
                var fonctionnalites = "";
                fonctionnalites += "<div class='form-group' id='loading'><label class='col-form-label-sm'><b>Fonctionnalités:</b></label><div>";
                $.each(result, function(i, e) {

                    fonctionnalites += "<div class='text-left'><input type='checkbox'  name='col3SubMenuId' value = " + e.id + " checked class='m-2'/>" + e.displayName + "</div>";

                });
                fonctionnalites += "</div>";
                document.getElementById('loading').innerHTML = fonctionnalites;
            }
        },
        error: function(xhr, textStatus, error) {

        }
    });
}

function postRoleDetails(donnees) {
    $.ajax(
        {
            type: "POST",
            contentType: "application/json",
            url: "/roles/functions/save",
            data: donnees,
            dataType: "json",
            success: (result) => {
                console.log(result[0]);
                iziToast.destroy();
                iziToast.success({
                    title: "",
                    position: "center",
                    message: "Mise à jour réussie"
                });
            },
            error: function(xhr, status, error) {
                //                alert(xhr.responseText);
                console.log("No content returned");
                iziToast.destroy();
                iziToast.success({
                    title: "",
                    position: "center",
                    message: "Echec de mise à jour"
                });
            }
        }
    );
}

function searchRoleDetails(roleDetail) {
    $.ajax({
        type: "POST",
        contentType: "application/json",
        url: "/mainmenu/role/details/seach",
        data: roleDetail,
        dataType: "json",
        success: function(result) {
            if (jQuery.isEmptyObject(result[0])) {
                console.log("La requête n'a retourné aucune ligne de données'");
                $('input:checkbox').prop('checked', false);
            } else {
                console.log(result);
                //$('input:checkbox').prop('checked', false);
                $.each(result, function(i, e) {
                    $('input:checkbox[value="' + e.subMenuId + '"]').prop('checked', true);
                    console.log(e);
                });
            }
        },
    });
}

function clearcontent(elementID) {
    document.getElementById(elementID).innerHTML = "";
}
