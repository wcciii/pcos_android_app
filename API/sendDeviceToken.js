exports.sendDeviceToken = function (username, deviceToken, callback)
{
	var mysql = require('mysql');

	function connFunc(callback){
		var ifmatch = false;

		var connect = mysql.createConnection(
		{
			host:"localhost",
			user:"root",
			password:"123qweasd",
			database:"pcos"
		}
		);

		connect.connect(function(err)
		{
			
			var testTemp = username;
			var emailadd = "\'" + testTemp + "\'";
			var deviceTokenAdd = "\'" + deviceToken + "\'";
			if (err) throw err;
			connect.query("UPDATE pcos.security_user SET device_token = " + deviceTokenAdd + " WHERE email = " + emailadd,function(err,result,fields)
			{
				callback(true)
			}
			);
		}
		);
	}

	connFunc(function(res){
		callback(res);
	});

}

function getJsonValue (obj,name)
{
    var result = null;
    var value  = null;
    for(var key in obj)
    {        
        value = obj[key];
        if(key == name)
        {
        	return value;
        } 
        else 
        {
        	if( typeof value == "object" )
        	{
            	result = getJsonValue(value,name);
            }
        }
    }
    return result;
}