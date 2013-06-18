WS = window.WS || {}; //define WS namespace.

/**--- UA ----**/
WS.Browser = {
    ua: navigator.userAgent.toLowerCase(),
    msie: function() {
        if (this._msie === undefined) {
            var pos = this.ua.match(/msie ([\d]+).[\d].+/);
            if (!!pos) {
                this._msie = parseInt(pos[1], 10);
            } else {
                this._msie = false;
            }
        }
        return this._msie;
    },
    chrome: function() {
        if (this._chrome === undefined) {
            var pos = this.ua.match(/chrome\/([\d]+).[\d.]+/);
            if (!!pos) {
                this._chrome = parseInt(pos[1], 10);
            } else {
                this._chrome = false;
            }
        }
        return this._chrome;
    },
    mchrome: function() {
        if (this._mchrome === undefined) {
            var pos = this.ua.match(/crios\/([\d]+).[\d.]+.mobile/);
            if (!!pos) {
                this._mchrome = parseInt(pos[1], 10);
            } else {
                this._mchrome = false;
            }
        }
        return this._mchrome;
    },
    safari: function() {
        if (this._safari === undefined) {
            var pos = this.ua.match(/version\/([\d]+).[\d.]+.[^mobile]*safari/);
            if (!!pos) {
                this._safari = parseInt(pos[1], 10);
            } else {
                this._safari = false;
            }
        }
        return this._safari;
    },
    msafari: function() {
        if (this._msafari === undefined) {
            var pos = this.ua.match(/version\/([\d]+).[\d.]+.mobile.*safari/);
            if (!!pos) {
                this._msafari = parseInt(pos[1], 10);
            } else {
                this._msafari = false;
            }
        }
        return this._msafari;
    },
    ff: function() {
        if (this._ff === undefined) {
            var pos = this.ua.match(/firefox\/([\d]+).[\d.]+/);
            if (!!pos) {
                this._ff = parseInt(pos[1], 10);
            } else {
                this._ff = false;
            }
        }
        return this._ff;
    },
    opera: function() {
        if (this._opera === undefined) {
            var pos = this.ua.match(/opera.([\d]+).[\d.]+/);
            if (!!pos) {
                this._opera = parseInt(pos[1], 10);
            } else {
                this._opera = false;
            }
        }
        return this._opera;
    },
    webkit: function() {
        if (this._webkit === undefined) {
            var pos = this.ua.match(/webkit\/([\d.]+)/);
            if (!!pos) {
                this._webkit = pos[1];
            } else {
                this._webkit = false;
            }
        }
        return this._webkit;
    },
    mozilla: function() {
        if (this._mozilla === undefined) {
            var pos = this.ua.match(/gecko\/([\d.]+)/);
            if (!!pos && !this.webkit()) {
                this._mozilla = pos[1];
            } else {
                this._mozilla = false;
            }
        }
        return this._mozilla;
    },
    mac: function() {
        if (this._mac === undefined) {
            this._mac = (/mac/).test(this.ua) && !this.ipad();
        }
        return this._mac;
    },
    ipad: function() {
        if (this._ipad === undefined) {
            this._ipad = (/ipad/).test(this.ua);
        }
        return this._ipad;
    }
};

WS.reloadCode = function (id) {
	$('#'+id).attr('src', "randomCode.do?id="+id+"&random="+Math.round(new Date().getTime()));
	//document.getElementById("randomImage").src = "randomCode.do?random=Math.round(new Date().getTime())";
};

WS.format = function (x,y) {
    var z = {M:x.getMonth()+1,d:x.getDate(),h:x.getHours(),m:x.getMinutes(),s:x.getSeconds()};
    y = y.replace(/(M+|d+|h+|m+|s+)/g,function(v) {return ((v.length>1?"0":"")+eval('z.'+v.slice(-1))).slice(-2)});
    return y.replace(/(y+)/g,function(v) {return x.getFullYear().toString().slice(-v.length);});
};

WS.initValidator = function () {
	$.validator.addMethod("allLetters", function(value, element) {
		var letterExp = /^[a-zA-Z]+$/;
		return this.optional(element) || letterExp.test(value);
	});

	$.validator.addMethod("isTelephone", function(value, element) {
		var mobile = /^(\(?\+86\)? ?)?(((13[0-9]{1})|(15[0-9]{1})|(18[0-9]{1}))+\d{8})$/;
		var tel = /^\d{3,4}-?\d{7,9}$/;
		return this.optional(element) || (tel.test(value) || mobile.test(value));
	});

	$.validator.addMethod("isPostCode", function(value, element) {
	    var postCode = /^[0-9]{6}$/;
	    return this.optional(element) || postCode.test(value);
	});

};

WS.validateUrl = './remotecheck';