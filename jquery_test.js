$(
    function() {
        $("#ordrd li:first").hover (
            function() {
                $(this).addClass("green");
            },
            function() {
                $(this).removeClass("green");
            }
        )
    }
);

    $("#sss").keypress(function(e) {
        alert ( e.keyCode );
    });