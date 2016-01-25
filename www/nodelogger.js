cordova.define("com.ionicsokolov.arcode.arcode", function(require, exports, module) { /*global cordova, module*/

module.exports = {
    /**
     * Send request to ARcode engine.
     *
     * @param {String} name       Place name
     * @param {String} category   Place category
     * @param {String} icon       Image icon url from map
     * @param {Float} latitude    Latitude
     * @param {Float} longitude   Longitude
     */
    arcodeview: function (data, successCallback) {
        if(data.length > 0){
            var sendData = [];
               for (var i=0; i<data.length; i++){
               sendData[i] = {};
               sendData[i].videoUrl = (data[i].videoUrl||'Unknown video');
			   sendData[i].setting = (data[i].setting||'Unknown video');
               sendData[i].mediaTrigger = (data[i].mediaTrigger||false);
               sendData[i].placename = (data[i].name||'Unknown Place');
               sendData[i].placecategory = (data[i].category||false);
               sendData[i].imgurl = (data[i].icon||'https://cdn1.iconfinder.com/data/icons/Map-Markers-Icons-Demo-PNG/256/Map-Marker-Marker-Inside-Chartreuse.png');
               sendData[i].lat = (data[i].latitude||55.655840);
               sendData[i].lng = (data[i].longitude||37.702510);
               }
            var senddata = JSON.stringify(sendData);
            cordova.exec(successCallback, null, "ARcode", "arcodeview", [senddata]);
        }

    }
};

});
