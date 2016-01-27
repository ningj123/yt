Ext.define('YourTour.controller.CommonMainCtrl', {
    extend: 'Ext.app.Controller',
    config: {
        refs: {
            contentReadView: '#ContentReadView',

            commentListView: '#CommentListView',
            commentList: '#CommentListView #commentList',

            timeSelectionView: '#TimeSelectionView'
        },

        control: {
            '#CommentListView #commentNum': {
                tap: 'onCommentFilterTap'
            },

            '#CommentListView #goodNum': {
                tap: 'onCommentFilterTap'
            },

            '#CommentListView #mediumNum': {
                tap: 'onCommentFilterTap'
            },

            '#CommentListView #badNum': {
                tap: 'onCommentFilterTap'
            },

            '#CommentListView #imageNum': {
                tap: 'onCommentFilterTap'
            },

            '#TimeSelectionView #btnNext': {
                tap: 'onTimeSelectionNextTapHandler'
            },

            '#TimeSelectionView #calendar': {
                itemtap: 'onTimeSelectionActiveItemChangeHandler'
            }
        }
    },

    showContentReadView: function (title, content) {
        Ext.ComponentManager.get('MainView').push(Ext.create('YourTour.view.common.ContentReadView'));

        var view = this.getContentReadView();

        var headerbar = view.down('#headerbar');
        headerbar.setHtml(title);

        var contentEl = view.down('#content');
        contentEl.setHtml(content);
    },

    showCommentListView: function (id, type, handler) {
        var me = this;
        Ext.ComponentManager.get('MainView').push(Ext.create('YourTour.view.common.CommentListView'));

        var params = [{name: 'id', value: id}, {name: 'type', value: type}, {
            name: 'nextCursor',
            value: 0
        }, {name: 'filter', value: 'commentNum'}];
        me.getCommentStore(params, handler);
    },

    onCommentFilterTap: function (field) {
        var itemId = field.getItemId();

        var view = this.getCommentListView();
        var filterPanel = view.down('#filterPanel');
        var items = filterPanel.getItems();
        items.each(function (item) {
            if (item instanceof YourTour.view.widget.XField) {
                var value = item.down('#value');
                if (item.getItemId() == itemId) {
                    value.addCls('active');
                } else {
                    value.removeCls('active');
                }
            }
        })

        var params = view.getData();
        params[2].value = 0;
        params[3].value = itemId;
        this.getCommentStore(params);
    },

    getCommentStore: function (params, handler) {
        var me = this;
        var options = {
            model: 'YourTour.model.CommentModel',
            url: '/comments',
            params: params,
            success: function (store) {
                var view = me.getCommentListView();

                if (handler) {
                    handler(view);
                }

                me.getCommentList().setStore(store);

                view.bindData(params);
            }
        };
        me.getApplication().query(options);
    },

    /*************************************************************************************************
     * 日历选择部分
     ************************************************************************************************/
    showTimeSelectionView: function (options, callback) {
        Ext.ComponentManager.get('MainView').push(Ext.create('YourTour.view.common.TimeSelectionView', {callback: callback}));
        var view = this.getTimeSelectionView();
        view.bindData(options);

        this.initializeTimeSelectionView(view);
    },

    initializeTimeSelectionView:function(view){
        var options = view.getData();
        var defaults = {
            date:new Date(),
            single:true,
            title:'行程日期安排'
        };
        Ext.applyIf(options, defaults);

        var date = options.date, year = date.getFullYear(), month = date.getMonth() + 1;
        var calendar = view.down('#calendar');
        calendar.setDate(year, month);

        var headerbar = view.down('#headerbar');
        headerbar.setTitle(options.title);

        if(options.single){
            view.down('#infoPanel').hide();
        }
    },

    onTimeSelectionNextTapHandler: function () {
        var me = this, view = me.getTimeSelectionView(), startDate = view.down('#startDate'), endDate = view.down('#endDate'), duration = view.down('#duration');

        var callback = view.getCallback();
        if (callback) {
            callback(startDate.getValue(), endDate.getValue(), duration);
        }
    },

    onTimeSelectionActiveItemChangeHandler: function (calendar, panel, item, date, active) {
        var me = this, view = me.getTimeSelectionView(), options = view.getData();
        if(options.single){
            return this.handleTimeSingleSelection(calendar, panel, item, date, active);
        }else{
            return this.handleTimeRangeSelection(calendar, panel, item, date, active);
        }
    },

    /**
     * 处理单个时间选择
     * @param calendar
     * @param panel
     * @param item
     * @param date
     * @param active
     * @returns {boolean}
     */
    handleTimeSingleSelection:function(calendar, panel, item, date, active){
        if(! item){
            return true;
        }

        if (active) {
            calendar.reset();
        }

        var me = this, view = me.getTimeSelectionView(), startDate = view.down('#startDate');
        startDate.setText(date);
        return true;
    },

    /**
     * 处理范围选择
     * @param calendar
     * @param panel
     * @param item
     * @param date
     * @param active
     * @returns {boolean}
     */
    handleTimeRangeSelection:function(calendar, panel, item, date, active){
        if(! item){
            return;
        }

        var me = this;
        if (active && calendar.getActiveItems().length >= 2) {
            me.getApplication().alert('只能选择一个时间范围，请先取消已选择的。');
            return false;
        }

        var view = me.getTimeSelectionView(), dDate = Ext.Date.format(new Date(date), 'Y-m-d');
        var startDate = view.down('#startDate'), endDate = view.down('#endDate'), dStart = startDate.getValue(), dEnd = endDate.getValue();
        var duration = view.down('#duration');

        var dDuration = 0;
        if (active == false) {
            var value = startDate.getValue();
            if (value == date) {
                startDate.setText(endDate.getText());
            }

            endDate.setText('');
        } else {
            if (dStart == null) {
                startDate.setText(date);
            } else {
                dStart = Ext.Date.format(new Date(dStart), 'Y-m-d');
                if (dDate > dStart) {
                    endDate.setText(date);
                } else {
                    endDate = startDate.getValue();
                    startDate.setText(date);
                }

                dStart = startDate.getValue() == '' ? null : new Date(startDate.getValue());
                dEnd = endDate.getValue() == '' ? null : new Date(endDate.getValue());
                dDuration = parseInt((new Date(dEnd) - new Date(dStart)) / (1000 * 60 * 60 * 24)) + 1;
            }
        }

        duration.setText(dDuration);

        return true;
    }

});
