	WS = window.WS || {};

WS.PlatformAdmin = function (id) {
    var _self = this,
          _state = null,
          _id = id,
          $container = $('#'+_id);
    
    function setState (state) {
    	var preState = _state;
    	_state = state;
    	onStateChanged (preState);
    }
    
    function onStateChanged (preState) {
    	switch (_state) {
    	case WS.PlatformAdmin.STATES.PROFILE:
    		$container.load('./platforms/PlatformProfile.html', onProfileLoaded);
    		break;
    	case WS.PlatformAdmin.STATES.APPROVED_STORES:
    	case WS.PlatformAdmin.STATES.APPROVING_STORES:
    		$container.load('./platforms/PlatformStores.html', onStoresLoaded);
    		break;
    	case WS.PlatformAdmin.STATES.PROCESSED_COMPLAINTS:
    	case WS.PlatformAdmin.STATES.PROCESSING_COMPLAINTS:
    		$container.load('./platforms/PlatformComplaints.html', onComplaintsLoaded);
    		break;
    	case WS.PlatformAdmin.STATES.NOTICES:
    	case WS.PlatformAdmin.STATES.ANNOUNCES:
    		$container.load('./platforms/PlatformPublications.html', onPublishLoaded);
    		break;
    	case WS.PlatformAdmin.STATES.VISIT_STATISTIC:
    		$container.load('./platforms/PlatformStatistic.html', onStatisticLoaded);
    		break;
    	case WS.PlatformAdmin.STATES.SYS_USER_AUTHORITY:
    	case WS.PlatformAdmin.STATES.COM_USER_AUTHORITY:
    		$container.load('./platforms/PlatformAuth.html', onAuthorityLoaded);
    		break;
    	case WS.PlatformAdmin.STATES.GENERAL_SETTINGS:
    	case WS.PlatformAdmin.STATES.SECURITY_SETTINGS:
    		$container.load('./platforms/PlatformSettings.html', onSettingsLoaded);
    		break;
    	case WS.PlatformAdmin.STATES.ADD_WATCH:
    		$container.load('./platforms/AddWatch.jsp', onAddWatchLoaded);
    		break;
    	case WS.PlatformAdmin.STATES.LOG:
    		$container.load('./platforms/PlatformLog.html', onLogLoaded);
    		break;
    	}
    }
    
    function onProfileLoaded () {
    	WS.Template.initBox(_id);
    	WS.Template.initTabBox(_id);
    	WS.Template.initFaceBox(_id);
    	$container.find('a[operation="'+WS.PlatformAdmin.STATES.APPROVED_STORES+'"]').bind('click', _self.approvedStores);
    	$container.find('a[operation="'+WS.PlatformAdmin.STATES.APPROVING_STORES+'"]').bind('click', _self.approvingStores);
    	$container.find('a[operation="'+WS.PlatformAdmin.STATES.PROCESSED_COMPLAINTS+'"]').bind('click', _self.processedComplaints);
    	$container.find('a[operation="'+WS.PlatformAdmin.STATES.PROCESSING_COMPLAINTS+'"]').bind('click', _self.processingComplaints);
    	$container.find('a[operation="'+WS.PlatformAdmin.STATES.PUBLISH+'"]').bind('click', _self.publish);
    	$container.find('a[operation="'+WS.PlatformAdmin.STATES.STATISTIC+'"]').bind('click', _self.statistic);
    	$container.find('a[operation="'+WS.PlatformAdmin.STATES.AUTHORITY+'"]').bind('click', _self.authority);
    }
    
    function onStoresLoaded () {
    	WS.Template.initFaceBox(_id);
    }
    
    function onComplaintsLoaded () {
    	
    }
    
    function onPublishLoaded () {
    	WS.Template.initFaceBox(_id);
    }
    
    function onStatisticLoaded () {
    	
    }
    
    function onSettingsLoaded () {
    	
    }

    function onAuthorityLoaded () {
    	
    }
    
    function onAddWatchLoaded () {
    	WS.Template.initWYSIWYG(_id);
    	WS.Template.initBox(_id);
    	$('#uploader').preview();
    	document.getElementById('register_watch_section').scrollTop = 0;
    	$('#register_watch_section').attr('scrollTop', 0);
    }
    
    function onLogLoaded () {
    	
    }
    
    this.profile = function () {
    	setState (WS.PlatformAdmin.STATES.PROFILE);
    };
    
    this.approvingStores = function () {
    	setState (WS.PlatformAdmin.STATES.APPROVING_STORES);
    };
    
    this.approvedStores = function () {
    	setState (WS.PlatformAdmin.STATES.APPROVED_STORES);
    };

    this.processingComplaints = function () {
    	setState (WS.PlatformAdmin.STATES.PROCESSING_COMPLAINTS);
    };
    
    this.processedComplaints = function () {
    	setState (WS.PlatformAdmin.STATES.PROCESSED_COMPLAINTS);
    };

    this.notices = function () {
    	setState (WS.PlatformAdmin.STATES.NOTICES);
    };
    
    this.announces = function () {
    	setState (WS.PlatformAdmin.STATES.ANNOUNCES);
    };

    this.visitStatistic = function () {
    	setState (WS.PlatformAdmin.STATES.VISIT_STATISTIC);
    };

    this.generalSettings = function () {
    	setState (WS.PlatformAdmin.STATES.GENERAL_SETTINGS);
    };
    
    this.securitySettings = function () {
    	setState (WS.PlatformAdmin.STATES.SECURITY_SETTINGS);
    };

    this.comAuthority = function () {
    	setState (WS.PlatformAdmin.STATES.COM_USER_AUTHORITY);
    };
    
    this.sysAuthority = function () {
    	setState (WS.PlatformAdmin.STATES.SYS_USER_AUTHORITY);
    };
    
    this.addWatch = function () {
    	setState (WS.PlatformAdmin.STATES.ADD_WATCH);
    };
    
    this.log = function () {
    	setState (WS.PlatformAdmin.STATES.LOG);
    };
    
    this.getState = function () {
        return _state;
    };
};

WS.PlatformAdmin.STATES = {
	PROFILE: 'profile',
	APPROVING_STORES: 'approving_stores',
	APPROVED_STORES: 'approved_stores',
	PROCESSING_COMPLAINTS: 'processing_complaints',
	PROCESSED_COMPLAINTS: 'processed_complaints',
	NOTICES: 'notices',
	ANNOUNCES: 'announces',
	VISIT_STATISTIC: 'visit_statistic',
	SYS_USER_AUTHORITY: 'sys_user_authority',
	COM_USER_AUTHORITY: 'com_user_authority',
	GENERAL_SETTINGS: 'general_settings',
	SECURITY_SETTINGS: 'security_settings',
	ADD_WATCH: 'add_watch',
	LOG: 'log'
  };