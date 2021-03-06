Ext.define('YourTour.model.RouteScheduleModel', {
    extend: 'Ext.data.Model',
    requires: ['YourTour.model.RouteActivityModel'],
    config: {
        idProperty: 'id',

        fields: [
            {name: 'id', type: 'string'},
            {name: 'parentId', type: 'string'},
            {name: 'index', type: 'string'},
            {name: 'imageUrl', type: 'string'},
            {name: 'type', type: 'string'},

            {name: 'title', type: 'string'},
            {name: 'memo', type: 'string'},
            {name: 'date', type: 'string'},
            {name: 'startTime', type: 'string'},
            {name: 'endTime', type: 'string'},
            {name: 'days', type: 'string'},
            {name: 'resourceId', type: 'string'},
            {name: 'resourceType', type: 'string'},
            {name: 'placeIds', type: 'string'},
            {name: 'places', type: 'string'},
            {name: 'price', type: 'string'},
            {name: 'commentScore', type: 'string'},
            {name: 'thumbupNum', type: 'string'},
            {name: 'favoriteNum', type: 'string'},
            {name: 'shareNum', type: 'string'},
            {name: 'commentNum', type: 'string'},
            {name: 'status', type: 'string'},
            {name: 'viewhidden', type: 'boolean',defaultValue: false},
            {name: 'planhidden', type: 'boolean',defaultValue: false}
        ]
    }
});
