$(
    // change text color when hover over
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
$(
        // change text color when hover over
    function() {
        $("#ordrd li:last").hover (
            function() {
                $(this).addClass("green");
            },
            function() {
                $(this).removeClass("green");
            }
        )
    }
);
$("#sss input").keyup(function(e) {
    alert ( "hello" );
});