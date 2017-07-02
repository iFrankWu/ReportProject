require(["js/jquery/jquery-1.8.1.min", "js/jquery/jquery-ui-1.8.23.custom.min"], function() {
});

require(["js/ui/user_customize"]);

// Bootstrap dependencies
require(["js/bootstrap/js/bootstrap.min"]);
require(["js/bootstrap/js/bootstrap-modal"]);




// Angular dependencies
require(["angular/angular"], function() {
	require(["angular/i18n/angular-locale_nl-nl"]);
	require(["angular/i18n/angular-locale_en-us"]);
	require(["angular/angular-cookies.js"]);
	require(["angular/angular-cookies.min.js"]);
	require(["angular/angular-resource","angular/angular-ui", "angular/angular-ui-ieshiv"]);
	
	//resource dependencies
	require(["js/ui/resources"], function() {
		// Service depencendies
		require(["js/ui/services"], function() {
			//modules en controllers
			require(["js/ui/app", "js/ui/Controller"], function() {
                setTimeout(function() {
                    $("#container").removeClass("onzichtbaar");
                    $("#wait").addClass("onzichtbaar");
                }, 200);
            });
		});
	});
});