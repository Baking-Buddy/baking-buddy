const close = document.querySelector(".close");
const toggleButton = document.querySelector(".toggle-button");
const slideNav = document.querySelector(".slide-nav");

// console.dir(backdrop.style['background-image']);

close.addEventListener("click", function() {
    // mobileNav.style.display = 'none';
    slideNav.classList.remove("open");
});

toggleButton.addEventListener("click", function() {
    // mobileNav.style.display = 'block';
    // backdrop.style.display = 'block';
    slideNav.classList.add("open");
});

const navbar = document.querySelector('nav')

window.onscroll = function() {

    // pageYOffset or scrollY
    if (window.pageYOffset > 0) {
        navbar.classList.add('scrolled')
    } else {
        navbar.classList.remove('scrolled')
    }
}