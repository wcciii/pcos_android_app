exports.Appointment = function (clinId,timeSlot,date,patient,callback)
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
			var ifmatch = false
			var dateinSql = "\'" + date + "\'";
			var time = "\'" + timeSlot + "\'";
			if (err) throw err;
			connect.query("select "+timeSlot+" from AppointClin where AppDate ="+dateinSql+"and ClinicianId = 4",function(err,result,fields)
			{
				dbStatus = getJsonValue(result,timeSlot);
				ifmatch = (dbStatus == null);
				//console.log(ifmatch)
				if (dbStatus == null) {
					connect.query("UPDATE `pcos`.`AppointClin` SET `"+timeSlot+"` = '"+patient+"' where `ClinicianId`='"+clinId+"' and `AppDate`="+dateinSql)
					var mail = require("./mail.js")
					//mail.mail('micshampoo@icloud.com','1','Li')
					connect.query("select email from pcos.security_user where id = (select user_id from pcos.clinician where id = "+clinId+")",function(err,result,fields)
					{
						var mailAdd = getJsonValue(result,"email");
						mail.mail(mailAdd,patient,clinId);
					})
					callback(ifmatch)
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
