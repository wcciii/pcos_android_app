exports.notification = function (patientID, callback)
{
	var mysql = require('mysql');

	function connFunc(callback){

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
			var dbVerifyCode = ""
			var ifmatch = false
			var patientAdd = "\'" + patientID + "\'";
			if (err) throw err;
			connect.query("SELECT message FROM notification where patient_id = "+patientAdd,function(err,result,fields)
			{
				message = getJsonValue(result,"message")
				callback(message)
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
    var result = new Array();
    var value  = null;
    for(var key in obj)
    {        
        value = obj[key]
        if(key == name)
        {
        	result.push(value)
        } 
        else 
        {
        	if( typeof value == "object" )
        	{
            	result.push(getJsonValue(value,name))
            }
        }
    }
    return result;
}