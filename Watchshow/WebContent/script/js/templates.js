WS = window.WS || {};

WS.Template = function(){};

WS.Template.initMenu = function (id) {
	var $container = $('#'+id);
	if (!$container) return;
	$container.find("#main-nav li ul").hide(); // Hide all sub menus
	$container.find("#main-nav li a.current").parent().find("ul").slideToggle("slow"); // Slide down the current menu item's sub menu
	$container.find("#main-nav li a.nav-top-item").click( // When a top menu item is clicked...
		function () {
			$(this).parent().siblings().find("ul").slideUp("normal"); // Slide up all sub menus except the one clicked
			$(this).next().slideToggle("normal"); // Slide down the clicked sub menu
			return false;
		}
	);
	$container.find("#main-nav li a.no-submenu").click( // When a menu item with no sub menu is clicked...
		function () {
			window.location.href=(this.href); // Just open the link instead of a sub menu
			return false;
		}
	); 
    // Sidebar Accordion Menu Hover Effect:
	$container.find("#main-nav li .nav-top-item").hover(
		function () {
			$(this).stop().animate({ paddingRight: "25px" }, 200);
		}, 
		function () {
			$(this).stop().animate({ paddingRight: "15px" });
		}
	);
};

WS.Template.initBox = function (id) {
	var $container = $('#'+id);
	if (!$container) return;
	$container.find(".content-box-header h3").css({ "cursor":"s-resize" }); // Give the h3 in Content Box Header a different cursor
	$container.find(".closed-box .content-box-content").hide(); // Hide the content of the header if it has the class "closed"
	$container.find(".closed-box .content-box-tabs").hide(); // Hide the tabs in the header if it has the class "closed"
	$container.find(".content-box-header h3").click( // When the h3 is clicked...
		function () {
		  $(this).parent().next().toggle(); // Toggle the Content Box
		  $(this).parent().parent().toggleClass("closed-box"); // Toggle the class "closed-box" on the content box
		  $(this).parent().find(".content-box-tabs").toggle(); // Toggle the tabs
		}
	);
};

WS.Template.initTabBox = function (id) {
	var $container = $('#'+id);
	if (!$container) return;
	$container.find('.content-box .content-box-content div.tab-content').hide(); // Hide the content divs
	$container.find('ul.content-box-tabs li a.default-tab').addClass('current'); // Add the class "current" to the default tab
	$container.find('.content-box-content div.default-tab').show(); // Show the div with class "default-tab"
	$container.find('.content-box ul.content-box-tabs li a').click( // When a tab is clicked...
		function() { 
			$(this).parent().siblings().find("a").removeClass('current'); // Remove "current" class from all tabs
			$(this).addClass('current'); // Add class "current" to clicked tab
			var currentTab = $(this).attr('href'); // Set variable "currentTab" to the value of href of clicked tab
			$(currentTab).siblings().hide(); // Hide all content divs
			$(currentTab).show(); // Show the content div with the id equal to the id of clicked tab
			return false; 
		}
	);
};

WS.Template.initCloseButton= function (id) {
	var $container = $('#'+id);
	if (!$container) return;
	$container.find(".close").click(
			function () {
				$(this).parent().fadeTo(400, 0, function () { // Links with the class "close" will close parent
					$(this).slideUp(400);
				});
				return false;
			}
		);
};

WS.Template.initCheckAll= function (id) {
	var $container = $('#'+id);
	if (!$container) return;
	$container.find('.check-all').click(
		function(){
			$(this).parent().parent().parent().parent().find("input[type='checkbox']").attr('checked', $(this).is(':checked'));   
		}
	);
};

WS.Template.initFaceBox = function (id) {
	var $container = $('#'+id);
	if (!$container) return;
	$container.find('a[rel*=modal]').facebox();
};

WS.Template.initWYSIWYG = function (id) {
	var $container = $('#'+id);
	if (!$container) return;
	$container.find(".wysiwyg").wysiwyg(); // Applies WYSIWYG editor to any textarea with the class "wysiwyg"
};

WS.Template.init = function (id) {
	WS.Template.initMenu(id);
	WS.Template.initBox(id);
	WS.Template.initTabBox(id);
	WS.Template.initCloseButton(id);
	WS.Template.initCheckAll(id);
	WS.Template.initFaceBox(id);
	WS.Template.initWYSIWYG(id);
};
  
WS.setup = function () {
	// Open an external link in a new window
    $('a[href^="http://"]').filter(function () {
        return this.hostname && this.hostname !== location.hostname;
    }).attr('target', '_blank');
	
    // build animated dropdown navigation
	$('#menu ul').supersubs({ 
		minWidth:    12,   // minimum width of sub-menus in em units 
		maxWidth:    27,   // maximum width of sub-menus in em units 
		extraWidth:  1     // extra width can ensure lines don't sometimes turn over 
						   // due to slight rounding differences and font-family 
	}).superfish(); 
	
    // build an animated footer
    $('#animated').each(function () {
        $(this).hover(function () {
            $(this).stop().animate({
                opacity: 0.9
            }, 400);
        }, function () {
            $(this).stop().animate({
                opacity: 0.0
            }, 200);
        });
    });

    // scroll to top on request
    if ($("a#totop").length) scrollToTop("a#totop");

    // setup content boxes
    if ($(".content-box").length) {
        $(".content-box header").css({
            "cursor": "s-resize"
        });
        // Give the header in content-box a different cursor	
        $(".content-box header").click(
        function () {
            $(this).parent().find('section').toggle(); // Toggle the content
            $(this).parent().toggleClass("content-box-closed"); // Toggle the class "content-box-closed" on the content
        });
    }
	
	// custom tooltips to replace the default browser tooltips for <a title=""> <div title=""> and <span title="">
    $("a[title], div[title], span[title]").tipTip();
    
    function scrollToTop (e) {
        $(e).hide().removeAttr("href");
        if ($(window).scrollTop() != "0") {
            $(e).fadeIn("slow");
        }
        var scrollDiv = $(e);
        $(window).scroll(function () {
            if ($(window).scrollTop() == "0") {
                $(scrollDiv).fadeOut("slow");
            } else {
                $(scrollDiv).fadeIn("slow");
            }
        });
        $(e).click(function () {
            $("html, body").animate({
                scrollTop: 0
            }, "slow");
        });
    };
};

//progress() - animate a progress bar "el" to the value "val"
WS.progress = function (el, val, max) {
    var duration = 400;
    var span = $(el).find("span");
    var b = $(el).find("b");
    var w = Math.round((val / max) * 100);
    $(b).fadeOut('fast');
    $(span).animate({
        width: w + '%'
    }, duration, function () {
        $(el).attr("value", val);
        $(b).text(w + '%').fadeIn('fast');
    });
};

// videoSupport() - <video> tag support for older browsers through flash player embedding
WS.videoSupport = function (wrapper, videoURL, width, height) {
    var v = document.createElement("video"); // Are we dealing with a browser that supports <video> tag? 
    if (!v.play) { // If no, use Flash.
        var vobj = $('#' + wrapper).find('video');
        var poster = $(vobj).attr("poster");
        var params = {
            allowfullscreen: "true",
            allowscriptaccess: "always"
        };
        var flashvars = {
            file: videoURL,
            image: poster
        };
        swfobject.embedSWF("player.swf", wrapper, width, height, "9.0.0", "expressInstall.swf", flashvars, params);
    }
};