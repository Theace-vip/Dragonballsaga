/* ==========================================================================
   NROKuRA — TUTIEN.JS  (Huyet Nguyet Ma Dao: hieu ung 2D canvas + CSS3 3D)
   Khong doi noi dung trang. Moi tinh nang duoc boc try/catch rieng de neu 1
   phan loi thi giao dien van chay binh thuong.
   Canh bao trang thai ra window.__bmTheme de kiem tra.
   ========================================================================== */
(function () {
    "use strict";

    var state = {
        fx: false,
        embers: 0,
        tilt: 0,
        reveals: 0,
        rings: 0,
        errors: []
    };
    window.__bmTheme = state;

    // Cho phep BAT BUOC bat hieu ung (xem truoc animation khi he dieu hanh dang bat
    // "giam chuyen dong"): them ?bm=on vao URL, hoac localStorage.setItem('bmMotion','on')
    var buoc = false;
    try {
        buoc = /[?&]bm=on(&|$|=)/.test(location.search) || window.localStorage.getItem("bmMotion") === "on";
    } catch (e) { /* bo qua */ }

    var giam = false;
    try {
        giam = window.matchMedia && window.matchMedia("(prefers-reduced-motion: reduce)").matches;
    } catch (e) { /* bo qua */ }
    if (buoc) { giam = false; }
    state.hieu_ung = !giam;

    var mobile = false;
    try {
        mobile = window.matchMedia && window.matchMedia("(max-width: 768px)").matches;
    } catch (e) { /* bo qua */ }

    function log_err(ten, e) {
        state.errors.push(ten + ": " + (e && e.message ? e.message : e));
    }

    /* ------------------------------------------------ 1. TAO LOP HIEU UNG */
    function tao_lop_fx() {
        if (document.getElementById("bm-fx")) { return document.getElementById("bm-fx"); }
        var fx = document.createElement("div");
        fx.id = "bm-fx";
        fx.setAttribute("aria-hidden", "true");
        fx.innerHTML =
            '<div class="bm-moon"></div>' +
            '<div class="bm-mist bm-mist-1"></div>' +
            '<div class="bm-mist bm-mist-2"></div>' +
            '<canvas id="bm-2d"></canvas>' +
            '<div class="bm-veil"></div>' +
            '<div class="bm-vignette"></div>' +
            '<div class="bm-grain"></div>';
        document.body.insertBefore(fx, document.body.firstChild);
        state.fx = true;
        return fx;
    }

    /* ---------------------------------------- 2. TAN LUOI THAN (2D canvas)
       HIEU NANG: ve bang anh sprite da tao san (drawImage) thay vi tao
       radial-gradient moi khung hinh - re hon nhieu lan khi may khong co GPU. */
    function tao_sprite(mau_trong, mau_giua) {
        var c = document.createElement("canvas");
        c.width = c.height = 64;
        var g = c.getContext("2d");
        var grd = g.createRadialGradient(32, 32, 0, 32, 32, 32);
        grd.addColorStop(0, mau_trong);
        grd.addColorStop(0.35, mau_giua);
        grd.addColorStop(1, "rgba(0,0,0,0)");
        g.fillStyle = grd;
        g.fillRect(0, 0, 64, 64);
        return c;
    }

    function chay_tan_lua() {
        var cv = document.getElementById("bm-2d");
        if (!cv || !cv.getContext) { return; }
        var ctx = cv.getContext("2d");
        var sprite_kim = tao_sprite("rgba(255,229,170,1)", "rgba(224,180,88,0.5)");
        var sprite_huyet = tao_sprite("rgba(255,140,140,1)", "rgba(216,31,42,0.45)");
        // Che do nhe: ve o nua do phan giai roi keo gian bang CSS -> re hon 4 lan
        var dpr = state.che_do_nhe ? 0.5 : Math.min(window.devicePixelRatio || 1, mobile ? 1 : 1.25);
        var W = 0, H = 0;
        var hat = [];
        var so_hat = mobile ? 34 : (window.innerWidth < 1200 ? 60 : 84);

        function tao_hat(ngau_nhien_cao) {
            return {
                x: Math.random() * W,
                y: ngau_nhien_cao ? Math.random() * H : H + Math.random() * 60,
                r: 0.6 + Math.random() * 2.1,
                vy: 0.16 + Math.random() * 0.62,
                vx: (Math.random() - 0.5) * 0.34,
                a: 0.25 + Math.random() * 0.6,
                mau: Math.random(),
                nhip: Math.random() * Math.PI * 2
            };
        }

        function doi_kich_thuoc() {
            W = window.innerWidth;
            H = window.innerHeight;
            cv.width = Math.floor(W * dpr);
            cv.height = Math.floor(H * dpr);
            cv.style.width = W + "px";
            cv.style.height = H + "px";
            ctx.setTransform(dpr, 0, 0, dpr, 0, 0);
            hat = [];
            var giu = Math.max(10, Math.floor(so_hat * (state.he_so_hat || 1)));
            for (var i = 0; i < giu; i++) { hat.push(tao_hat(true)); }
            state.embers = hat.length;
        }

        // Cho phep lop 3D yeu cau giam bot tan lua khi may yeu (su kien bm-giam-tai)
        state.giam_hat = function (he_so) {
            state.he_so_hat = Math.min(state.he_so_hat || 1, he_so);
            var giu = Math.max(10, Math.floor(so_hat * state.he_so_hat));
            if (hat.length > giu) {
                hat.length = giu;
                state.embers = hat.length;
            }
        };

        state.doi_kich_thuoc = doi_kich_thuoc;

        var t = 0;
        function ve() {
            t += 0.016;
            ctx.clearRect(0, 0, W, H);
            ctx.globalCompositeOperation = "lighter";
            for (var i = 0; i < hat.length; i++) {
                var p = hat[i];
                p.x += p.vx + Math.sin(t * 0.8 + p.nhip) * 0.22;
                p.y -= p.vy;
                if (p.y < -20 || p.x < -40 || p.x > W + 40) { hat[i] = tao_hat(false); continue; }
                var nhap_nhay = 0.65 + 0.35 * Math.sin(t * 2.2 + p.nhip);
                var r = p.r * (1 + 0.25 * Math.sin(t * 1.5 + p.nhip)) * 7;
                ctx.globalAlpha = Math.min(1, p.a * nhap_nhay);
                ctx.drawImage(p.mau > 0.72 ? sprite_kim : sprite_huyet, p.x - r, p.y - r, r * 2, r * 2);
            }
            ctx.globalCompositeOperation = "source-over";
            ctx.globalAlpha = 1;

            // Do FPS thuc te: may yeu thi tu chuyen che do nhe (tat animation lien tuc)
            dem_khung++;
            var nay = performance.now();
            if (nay - moc_fps >= 1300) {
                var fps = dem_khung / ((nay - moc_fps) / 1000);
                dem_khung = 0;
                moc_fps = nay;
                state.fps_2d = Math.round(fps);
                state.so_lan_do_2d = (state.so_lan_do_2d || 0) + 1;
                if (state.so_lan_do_2d > 1 && fps < 26 && !state.che_do_nhe) { chuyen_che_do_nhe(fps); }
            }

            khung = requestAnimationFrame(ve);
        }

        var dem_khung = 0;
        var moc_fps = performance.now();

        var khung = null;
        doi_kich_thuoc();
        window.addEventListener("resize", function () {
            try { doi_kich_thuoc(); } catch (e) { log_err("embers-resize", e); }
        });
        document.addEventListener("visibilitychange", function () {
            if (document.hidden) {
                if (khung) { cancelAnimationFrame(khung); khung = null; }
            } else if (!khung && !giam) {
                khung = requestAnimationFrame(ve);
            }
        });
        if (giam) { ve(); if (khung) { cancelAnimationFrame(khung); khung = null; } }
        else { khung = requestAnimationFrame(ve); }
    }

    /* ------------------------------------------------ 3. VONG PHU VAN 3D */
    function tao_vong_phu_van() {
        var dich = document.querySelector(".container .p-xs.mb-3") ||
            document.querySelector(".container .text-center a[href='/']") ||
            document.querySelector(".container .ibox-content");
        if (!dich) { return; }
        var boc = document.createElement("div");
        boc.className = "bm-ring-wrap";
        boc.innerHTML = '<div class="bm-ring"></div><div class="bm-ring r2"></div><div class="bm-ring r3"></div>';
        if (getComputedStyle(dich).position === "static") { dich.style.position = "relative"; }
        dich.insertBefore(boc, dich.firstChild);
        state.rings = 3;
    }

    /* ---------------------------------------------- 4. THE NGHIENG 3D */
    function gan_nghieng_3d() {
        if (giam) { return; }
        if (!window.matchMedia || !window.matchMedia("(hover: hover) and (pointer: fine)").matches) { return; }
        var chon = ".alert, .ibox-content, .top-tab, .modal-content";
        var the = Array.prototype.slice.call(document.querySelectorAll(chon));
        if (the.length > 24) { the = the.slice(0, 24); }
        the.forEach(function (el) {
            el.classList.add("bm-tilt", "bm-tilt-glow");
            var raf = null, tx = 0, ty = 0, dang = false;

            function cap_nhat() {
                raf = null;
                var r = el.getBoundingClientRect();
                var x = (tx - r.left) / r.width;
                var y = (ty - r.top) / r.height;
                var rx = (0.5 - y) * 7;
                var ry = (x - 0.5) * 9;
                el.style.transform = "perspective(1000px) rotateX(" + rx.toFixed(2) + "deg) rotateY(" + ry.toFixed(2) +
                    "deg) translateY(-3px) scale(1.012)";
                el.style.setProperty("--bm-mx", (x * 100).toFixed(1) + "%");
                el.style.setProperty("--bm-my", (y * 100).toFixed(1) + "%");
            }

            el.addEventListener("pointermove", function (e) {
                if (!dang) { return; }
                tx = e.clientX; ty = e.clientY;
                if (!raf) { raf = requestAnimationFrame(cap_nhat); }
            });
            el.addEventListener("pointerenter", function () { dang = true; });
            el.addEventListener("pointerleave", function () {
                dang = false;
                if (raf) { cancelAnimationFrame(raf); raf = null; }
                el.style.transform = "";
                el.style.removeProperty("--bm-mx");
                el.style.removeProperty("--bm-my");
            });
            state.tilt++;
        });
    }

    /* ---------------------------------------------- 5. HIEN DAN KHI CUON */
    function hien_dan_khi_cuon() {
        var chon = ".alert, .ibox-content, .form-auth, .table, .top-tab, .content-wrapper, .moc-nap";
        var ds = Array.prototype.slice.call(document.querySelectorAll(chon));
        if (!("IntersectionObserver" in window)) {
            ds.forEach(function (el) { el.classList.add("bm-in"); });
            state.reveals = ds.length;
            return;
        }
        var io = new IntersectionObserver(function (vao) {
            vao.forEach(function (m) {
                if (m.isIntersecting) {
                    m.target.classList.add("bm-in");
                    io.unobserve(m.target);
                }
            });
        }, { rootMargin: "0px 0px -8% 0px", threshold: 0.06 });
        ds.forEach(function (el, i) {
            if (i < 40) {
                el.classList.add("bm-reveal");
                el.style.transitionDelay = Math.min(i * 45, 320) + "ms";
            }
            io.observe(el);
        });
        state.reveals = ds.length;
    }

    /* ---------------------- 6. DUA POPUP RA NGOAI <body> (chong lech dinh vi)
       Trong markup cu, .modal nam LONG ben trong .container (nav.php mo the div,
       foot.php moi dat popup). Neu mot phan tu cha co backdrop-filter/transform thi
       popup bi lech khoi man hinh va .modal-backdrop se chan toan bo click.
       Dua popup ve con truc tiep cua <body> la cach Bootstrap khuyen dung. */
    function dua_modal_ra_body() {
        var ds = document.querySelectorAll(".modal");
        var dem = 0;
        for (var i = 0; i < ds.length; i++) {
            if (ds[i].parentElement !== document.body) {
                document.body.appendChild(ds[i]);
                dem++;
            }
        }
        state.modal_ra_body = dem;
    }

    /* ----------------------------------- 7. DON HIEU UNG CU (particles vang) */
    function don_hieu_ung_cu() {
        var cu = document.getElementById("snow");
        if (cu) { cu.parentNode.removeChild(cu); }
    }

    /* -------------------------- CHE DO NHE: tat animation lien tuc ton CPU */
    function chuyen_che_do_nhe(ly_do) {
        state.che_do_nhe = ly_do;
        try {
            document.body.classList.add("bm-nhe");
        } catch (e) { /* bo qua */ }
        if (state.giam_hat) { state.giam_hat(0.15); }
        if (state.doi_kich_thuoc) { try { state.doi_kich_thuoc(); } catch (e) { /* bo qua */ } }
        document.dispatchEvent(new CustomEvent("bm-giam-tai", { detail: { cap: 3, ly_do: ly_do } }));
    }

    /* --------------------------------------------------------- KHOI CHAY */
    function chay() {
        try { tao_lop_fx(); } catch (e) { log_err("fx", e); }
        try { dua_modal_ra_body(); } catch (e) { log_err("modal", e); }
        try { don_hieu_ung_cu(); } catch (e) { log_err("don-cu", e); }
        try { tao_vong_phu_van(); } catch (e) { log_err("ring", e); }
        try { chay_tan_lua(); } catch (e) { log_err("embers", e); }
        try { hien_dan_khi_cuon(); } catch (e) { log_err("reveal", e); }
        try { gan_nghieng_3d(); } catch (e) { log_err("tilt", e); }
        document.addEventListener("bm-giam-tai", function (ev) {
            try {
                var cap = ev && ev.detail && ev.detail.cap ? ev.detail.cap : 2;
                state.cap_giam_tai = cap;
                if (cap >= 3) {
                    // Lop 3D bao may qua yeu -> chuyen che do nhe ngay, khong cho do FPS lai
                    if (!state.che_do_nhe) { chuyen_che_do_nhe("3D-bao"); }
                    return;
                }
                if (state.giam_hat) { state.giam_hat(0.6); }
            } catch (e) { log_err("giam-tai", e); }
        });
    }

    if (document.readyState === "loading") {
        document.addEventListener("DOMContentLoaded", chay);
    } else {
        chay();
    }
})();
