const dateFormatter = () => {
    let choosenDate = $(this).val();
    let formattedDate = moment(choosenDate).format('dd-MM-yyyy');
    $(this).val(formattedDate);
};

