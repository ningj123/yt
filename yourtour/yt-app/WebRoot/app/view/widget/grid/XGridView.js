Ext.define('YourTour.view.widget.grid.XGridView', {
    extend: 'Ext.DataView',
    xtype: 'xgridview',
    mixins: ['Ext.mixin.Selectable'],
    requires: [
        'Ext.LoadMask',
        'Ext.data.StoreManager',
        'YourTour.view.widget.grid.component.Container',
        'YourTour.view.widget.grid.element.Container'
    ],

    config: {
        baseCls:Ext.baseCSSPrefix + 'xgridview',
        cols:3,
        size:9999,

        scrollable:null
    },

    initialize: function() {
        this.callParent();
        var me = this,
            container,
            triggerEvent = me.getTriggerEvent();

        me.on(me.getTriggerCtEvent(), me.onContainerTrigger, me);

        container = me.container = this.add(new YourTour.view.widget.grid[me.getUseComponents() ? 'component' : 'element'].Container({
            baseCls: this.getBaseCls(), cols : me.cols, size : me.size
        }));

        container.dataview = me;

        if (triggerEvent) {
            me.on(triggerEvent, me.onItemTrigger, me);
        }

        container.on({
            itemtouchstart: 'onItemTouchStart',
            itemtouchend: 'onItemTouchEnd',
            itemtap: 'onItemTap',
            itemtaphold: 'onItemTapHold',
            itemtouchmove: 'onItemTouchMove',
            itemsingletap: 'onItemSingleTap',
            itemdoubletap: 'onItemDoubleTap',
            itemswipe: 'onItemSwipe',
            scope: me
        });

        if (me.getStore()) {
            if (me.isPainted()) {
                me.refresh();
            }
            else {
                me.on({
                    painted: 'refresh',
                    single: true
                });
            }
        }
    },

    updateCols:function(cols){
        this.cols = cols;
    },

    updateSize:function(size){
        this.size = size;
    }
});

