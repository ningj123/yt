Ext.define('YourTour.util.Context', {
	singleton : true,
	alias:'Context',
	
    config : {
    	userKey:'user',

		//server:'192.168.1.174:8080'
    	server:'192.168.2.102:8080'
		//server:'120.55.76.201:8080'
    },
    
    constructor : function(config) {
        this.initConfig(config);
    },
    
    getContext:function(url){
    	var s = 'http://' + this.getServer() + '/yt-web/rest';
    	if(url.substr(0,1) != '/'){
    		s += '/';
    	}
    	
    	return s + url;
    },

	/**
	 * 获取图片资源
	 * @param url
	 * @param size
	 * @returns {string}
	 */
    getImageResource:function(url, size){
		if(size){
			var index = url.lastIndexOf('.');
			url = url.substr(0, index) + '_' + size + url.substr(index);
		}

    	var s = 'http://' + this.getServer() + '/yt-app';
    	if(url == null || url == '') return s;

    	if(url.substr(0,1) != '/'){
    		s += '/';
    	}

    	return s + url;
    }
});

