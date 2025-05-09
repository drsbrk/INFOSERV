const hamBurger = document.querySelector("#toggle-btn");
const menuItem = document.querySelector(".sidebar-link");

hamBurger.addEventListener("click", function () {
    document.querySelector("#sidebar").classList.toggle("expand");
});

document.querySelectorAll('.sidebar-item').forEach(item => {
    item.addEventListener('click', event => {
        const dropdown = event.target.querySelector('.sidebar-dropdown');
        console.log("dropdown = " + dropdown);
        if (dropdown) {
            dropdown.classList.toggle('open');
        } else {
            console.log("else get clicked");
        }
    });
});