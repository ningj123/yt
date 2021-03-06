Ext.define('YourTour.view.route.RouteSchedulePlanDataItem', {
    extend: 'Ext.dataview.component.DataItem',
    xtype: 'RouteSchedulePlanDataItem',
    requires:['Ext.Label','Ext.field.Select','Ext.Panel'],
    config: {
    	items:[
    		{
    			itemId:'preparePanel',
    			xtype:'panel',
    			layout:'hbox',
    			hidden:true,
    			cls:'schedule',
    			items:[
    				{
    					xtype:'label',
    					itemId:'title',
    					flex:1,
    					cls:'title strong',
						padding:'0 0 0 50'
    				}
    			]
    		},
    		
    		{
    			itemId:'prepareItemPanel',
    			xtype:'panel',
    			hidden:true,
    			layout:'hbox',
				cls:'scheduleItem',
    			items:[
					{
						xtype:'label',
						itemId:'title',
						cls:'title icon-todo',
						padding:'0 0 0 50'
					}
    			]
    		},
    		
    		{
    			itemId:'dayPanel',
    			xtype:'panel',
    			hidden:true,
    			layout:'hbox',
    			cls:'schedule',
    			items:[
    				{
    					xtype:'label',
    					itemId:'title',
    					cls:'title strong',
						padding:'0 0 0 50'
    				}
    			]
    		},
    		
    		{
    			itemId:'dayItemPanel',
    			xtype:'panel',
    			layout:'vbox',
    			hidden:true,
    			cls:'dayItem',
    			items:[
					{
						xtype:'label',
						itemId:'title',
						cls:'title',
						padding:'0 0 0 50'
					}
    			]
    		}
    	]
    },
    
   	/**
   	 * 
   	 * @param {} record
   	 */
    updateRecord: function(record) {
       var me = this;
       var dataview = this.dataview || this.getDataview();
       if(record){
    	   var panel;
       	   var type = record.get('type');
       		if(type == 'Provision'){
       			panel = me.down('#preparePanel');
       			panel.show();
       			
       			var title = panel.down('#title');
       			title.setHtml(record.get('title'));
				title.addCls('icon-prepare');
       		}else if(type == 'ProvisionItem'){
       			panel = me.down('#prepareItemPanel');
       			panel.show();
       			
       			var title = panel.down('#title');
       			title.setHtml(record.get('title'));
       		}else if(type == 'Schedule'){
       			panel = me.down('#dayPanel');
       			panel.show();
       			
       			var title = panel.down('#title');
       			title.setHtml(record.get('title'));
				title.addCls('icon-time-32');
       		}else{
       			panel = me.down('#dayItemPanel');
       			panel.show();

				var title = panel.down('#title');
				title.setHtml(record.get('title'));
				var resourceType = record.get('resourceType');
				if(resourceType == 'SCENE'){
					title.addCls('icon-scene');
				}else if(resourceType == 'HOTEL'){
					title.addCls('icon-hotel');
				}else if(resourceType == 'FOOD'){
					title.addCls('icon-food');
				}else if(resourceType == 'traffic'){
					title.addCls('icon_restaurant');
				}
       		}
       }
    }   
});

