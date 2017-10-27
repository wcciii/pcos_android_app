// npm install nodemailer-smtp-transport --save
exports.mail = function test (target,patient,clin)
{
	var nodemailer = require('nodemailer');
	
	var smtpTransport = require('nodemailer-smtp-transport');
	var transporter = nodemailer.createTransport({
	    service: 'Gmail',
	    auth: {
	        user: 'info.pcosdatabase@gmail.com',
	                pass: 'pcos12345'
	    }
	});
	var content = '<b>Dear doctor '+clin+' : You have received a new appointment from patient Id: '+patient+'. Plese check it online</b>' 
	var mailOptions = {
	    from: 'PCOS ✔ <info.pcos@gmail.com>', 
	    to: target, 
	    subject: 'A new appointment notification', 
	    html: content 
	};
	 
	// send mail with defined transport object 
	transporter.sendMail(mailOptions, function(error, info){
	    if(error){
	        return console.log(error);
	    }
	    console.log('Message sent: ' + info.response);
	 
	});
}