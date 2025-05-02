console.log("Script loaded");

// change theme work
let currentTheme = getTheme();
//initial -->

document.addEventListener("DOMContentLoaded", () => {
    changeTheme();
});

//TODO:
function changeTheme() {
    //set to web page

    changePageTheme(currentTheme, "");
    //set the listener to change theme button
    const changeThemeButton = document.querySelector("#dark_mode_button");

    changeThemeButton.addEventListener("click", (event) => {
        let oldTheme = currentTheme;
        console.log("change theme button clicked");
        if (currentTheme === "dark") {
            //theme ko light
            currentTheme = "light";
        } else {
            //theme ko dark
            currentTheme = "dark";
        }
        console.log(currentTheme);
        changePageTheme(currentTheme, oldTheme);
    });
}

//set theme to localstorage
function setTheme(theme) {
    localStorage.setItem("theme", theme);
}

//get theme from localstorage
function getTheme() {
    let theme = localStorage.getItem("theme");
    return theme ? theme : "dark";
}

//change current page theme
function changePageTheme(theme, oldTheme) {
    //localstorage mein update karenge
    setTheme(currentTheme);
    //remove the current theme

    if (oldTheme) {
        document.querySelector("html").classList.remove(oldTheme);
    }
    //set the current theme
    document.querySelector("html").classList.add(theme);

    // change the text of button
    document
        .querySelector("#dark_mode_button")
        .querySelector("span").textContent = theme === "light" ? "Dark" : "Light";
}
function submitForm(radio) {
    // Get the form
    const form = document.getElementById('selectBusinessForm');

    // Update the form action dynamically with the selected business ID


    // Submit the form
    form.submit();
}