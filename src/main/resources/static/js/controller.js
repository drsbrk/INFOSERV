$(document).ready(function(){
$("#clientsTable").DataTable({
    'ajax':'clients',
     "columns": [
          {'data':'tin'},
          {'data':'clientName'},
          {'data':'commercialName'},
          {'data':'contactNumber'},
          {'data':'contactName'}
        ]
});
});