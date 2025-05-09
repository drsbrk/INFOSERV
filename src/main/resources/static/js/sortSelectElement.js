// Cette fx sert à ordonner les options d'un élement select
// On l'appelle et on lui passe le nom de l'élément select
function sortSelectJQuery(select) {
    var options = $(select.options).sort(function(a, b) {
        return a.text.localeCompare(b.text);
    });

    $(select).empty();
    options.each(function() {
        $(select).append(this);
    });
}