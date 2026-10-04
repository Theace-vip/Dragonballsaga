function searchGame(e) {
    var s = $(e).val().toLowerCase(0),
        a = change_alias(s);
    a.length > 0 ? $(".game-list .game").each(function() {
        $(this).filter("[data-search-term *= " + a + "]").length > 0 || a.length < 1 ? $(this).show() : $(this).hide()
    }) : $(".game-list .game").show()
}

function change_alias(e) {
    var s = e;
    return s = s.replace(/à|á|ạ|ả|ã|â|ầ|ấ|ậ|ẩ|ẫ|ă|ằ|ắ|ặ|ẳ|ẵ/g, "a"), s = s.replace(/è|é|ẹ|ẻ|ẽ|ê|ề|ế|ệ|ể|ễ/g, "e"), s = s.replace(/ì|í|ị|ỉ|ĩ/g, "i"), s = s.replace(/ò|ó|ọ|ỏ|õ|ô|ồ|ố|ộ|ổ|ỗ|ơ|ờ|ớ|ợ|ở|ỡ/g, "o"), s = s.replace(/ù|ú|ụ|ủ|ũ|ư|ừ|ứ|ự|ử|ữ/g, "u"), s = s.replace(/ỳ|ý|ỵ|ỷ|ỹ/g, "y"), s = s.replace(/đ/g, "d"), s = s.replace(/À|Á|Ạ|Ả|Ã|Â|Ầ|Ấ|Ậ|Ẩ|Ẫ|Ă|Ằ|Ắ|Ặ|Ẳ|Ẵ/g, "A"), s = s.replace(/È|É|Ẹ|Ẻ|Ẽ|Ê|Ề|Ế|Ệ|Ể|Ễ/g, "E"), s = s.replace(/Ì|Í|Ị|Ỉ|Ĩ/g, "I"), s = s.replace(/Ò|Ó|Ọ|Ỏ|Õ|Ô|Ồ|Ố|Ộ|Ổ|Ỗ|Ơ|Ờ|Ớ|Ợ|Ở|Ỡ/g, "O"), s = s.replace(/Ù|Ú|Ụ|Ủ|Ũ|Ư|Ừ|Ứ|Ự|Ử|Ữ/g, "U"), s = s.replace(/Ỳ|Ý|Ỵ|Ỷ|Ỹ/g, "Y"), s = s.replace(/Đ/g, "D")
}

function show_search_box(e) {
    $(this).toggleClass("active"), $(".frm-search").show().find("input").focus(), $(e).find("img").attr("src", "/assets/v2/images/nav-search-active.svg"), $("html,body").animate({
        scrollTop: $(".block-game").offset().top - 70
    }, 300)
}

function back_step_previous(e) { 
    1 === e && ($("fieldset").hide(), $("#progressbar li").removeClass("active"), $("#progressbar li").eq(e - 1).addClass("active"), $("fieldset").eq(e - 1).show()), previous_fs = $("fieldset").eq(e - 1), current_fs = $("fieldset").eq(e), $("#progressbar li").eq(e).removeClass("active"), previous_fs.show(), current_fs.animate({
        opacity: 0
    }, {
        step: function(e) {
            opacity = 1 - e, current_fs.css({
                display: "none",
                position: "relative"
            }), previous_fs.css({
                opacity: opacity
            })
        },
        duration: 600
    })
}

function appendData() {
    for (var e = "", s = 0; 5 > s; s++) e += '<div class="news"> <div class="news-thumb"> <a href=""> <img src="/assets/v2/images/thumb.jpg" class="img-fluid"/> </a> </div><div class="news-meta"> <h4 class="news-title"> <a href="#"> Đón “cơn mưa quà tặng” cùng giải đấu “Huyền Thoại Bá Vương” </a> </h4> <div class="news-date"> 10:35 20/07/2020 </div></div></div>';
    $(".list-news").append(e)
}
$(document).ready(function() {
    var e, s, a;
    $("#msform").on("click", ".next", function() {
        e = $(this).parent().parent(), s = e.next(), console.log(s.attr("class")), $("#progressbar li").eq($("fieldset").index(s)).addClass("active"), s.show(), e.animate({
            opacity: 0
        }, {
            step: function(t) {
                a = 1 - t, e.css({
                    display: "none",
                    position: "relative"
                }), s.css({
                    opacity: a
                })
            },
            duration: 600
        }), $("html,body").scrollTop($("#msform").offset().top - 90)
    }), $(".radio-group .radio").click(function() {
        $(this).parent().find(".radio").removeClass("selected"), $(this).addClass("selected")
    }), $(".submit").click(function() {
        return !1
    }), $("select").select2(), $(".game-list .game").each(function() {
        $(this).attr("data-search-term", change_alias($(this).find(".game-name a").text().toLowerCase()))
    })
});