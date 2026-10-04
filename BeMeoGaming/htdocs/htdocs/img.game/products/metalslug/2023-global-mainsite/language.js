
var welcome = $(".input-region").eq(0).val();
var language = document.cookie.replace(/(?:(?:^|.*;\s*)language\s*\=\s*([^;]*).*$)|^.*$/, '$1');
var d = new Date();
d.setTime(d.getTime() + (30 * 24 * 60 * 60 * 1000));
var expires = 'expires=' + d.toUTCString();

jQuery(document).ready(function () {

    $(".choose-language .language__item").removeClass('active');
    if (language.length == 0) {
        document.cookie = 'language=' + welcome + ';' + expires + '; path=/';
        $('.choose-language .language__item[data-language=' + welcome + ']').addClass("active");
        renewChooseLanguage();
    }
    else {
        renewChooseLanguage();
    }
    // $('.dropdown-content .region').on('click', function (e) {
    //     e.preventDefault();
    //     welcome = $(this).data("region");
    //     document.cookie = 'language=' + welcome + ';' + expires + '; path=/';
    //     if (!$(this).parent().hasClass('active')) {
    //         //document.location.href="/";
    //         document.location.href= "https://metalslugawk.vnggames.com/index.html";
    //     }
    // });

});

function renewChooseLanguage() {
    var x = document.cookie.replace(/(?:(?:^|.*;\s*)language\s*\=\s*([^;]*).*$)|^.*$/, '$1');
    var chosenNationText = $('.choose-language .region[data-region=' + x + ']').eq(0).text();
    var chosenLang = x;
    $('.current').html(chosenNationText);
    $('.currentInput').attr('data-region', chosenLang);
    $('.choose-language .language__item[data-language=' + x + ']').addClass("active");
}
