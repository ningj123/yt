Ext.define('YourTour.store.AjaxStore', {
    extend: 'Ext.data.Store',
    
    getUrl:function(url){
    	return "" + url;
    }
});
