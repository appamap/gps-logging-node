               
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
               nodeloggerview: function(data, successCallback) {
               if (data.length > 0) {
               var sendData = [];
               for (var i = 0; i < data.length; i++) {
               sendData[i] = {};
               sendData[i].deviceID = (data[i].deviceID || 'Unknown ID');
               sendData[i].setting = (data[i].setting || 'Unknown');
               }
               var senddata = JSON.stringify(sendData);
               cordova.exec(successCallback, null, "NodeLogger", "nodeloggerview", [senddata]);
               }
               }
               };
               
              