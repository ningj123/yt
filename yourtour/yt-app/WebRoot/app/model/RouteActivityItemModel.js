Ext.define('YourTour.model.RouteActivityItemModel', {
    extend: 'YourTour.model.BaseModel',
    config:{
	    fields:[
	    	{name:'id', type:'string'},
    		{name:'title', type:'string'},
    		{name:'option', type:'string'},
			{name:'imageUrl', type:'string'},
			{name:'memo', type:'string'},
			{name:'thumbupNum', type:'string'}
	    ]
    }
});
