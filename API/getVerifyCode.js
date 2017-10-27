exports.getVerify = function (username, callback)
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
			if (err) throw err;
			connect.query("SELECT mobile_verification FROM pcos.security_user where email ="+emailadd,function(err,result,fields)
			{
				verifyCode = getJsonValue(result,"mobile_verification");
				callback(verifyCode)
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