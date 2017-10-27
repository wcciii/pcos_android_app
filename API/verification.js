var bcrypt = require('bcrypt');

exports.verify = function (username,verifyCode,password,callback)
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
			var testTemp = username;
			var emailadd = "\'" + testTemp + "\'";
			if (err) throw err;
			connect.query("SELECT mobile_verification FROM pcos.security_user where email ="+emailadd,function(err,result,fields)
			{
				dbVerifyCode = getJsonValue(result,"mobile_verification");
				ifmatch = (dbVerifyCode == verifyCode);
				if(ifmatch){
					bcrypt.hash(password, 12, function(err, hash){
						var passwordadd = "\'" + hash + "\'";
						connect.query("UPDATE pcos.security_user SET password = " + passwordadd + " WHERE email = " + emailadd, 
						function(err,result,fields){});
						callback(ifmatch)
					});
					
				}
				else
					callback(ifmatch);
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
