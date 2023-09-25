import sendgrid from '@sendgrid/mail'

export async function sendEmailToken(email: string, emailToken: string) {
  const fullUrl = `${process.env.URL}/client/auth/${email}/${emailToken}`;

  const msg = {
    to: email,
    from: process.env.SENDGRID_EMAIL_SENDER,
    heading: `<h1> Welcome Dear Guest </h1>`,
    subject: 'Login token for the modern backend API',
    html: `<table border="0" cellspacing="0" cellpadding="0">
            <tr>
                <td align="center" style="border-radius: 5px; background-color: #1F7F4C;">
                    <a rel="noopener" target="_blank" href="${fullUrl}" target="_blank" style="font-size: 18px; font-family: Helvetica, Arial, sans-serif; color: #ffffff; font-weight: bold; text-decoration: none;border-radius: 5px; padding: 12px 18px; border: 1px solid #1F7F4C; display: inline-block;">Click here to login &rarr;</a>
                </td>
            </tr>
          </table>`,
    dynamicTemplateData: {
      name: 'John Doe',
    },

    //`The login token for the API is: ${emailToken}`,
  };
  //register 
  registerSendGridKey();
  await sendgrid.send(msg);
}

export async function sendJwtToken(email: string, jwt: string) {
  const fullUrl = `${process.env.URL}/client/auth/${jwt}`;

  const msg = {
    to: email,
    from: process.env.SENDGRID_EMAIL_SENDER,
    heading: `<h1> Welcome Dear Guest </h1>`,
    subject: 'JWT',
    html: `
    <div>In case your loose your credentials, click on this button to redirect to your space.</div>
    <table border="0" cellspacing="0" cellpadding="0">
    <tr>
        <td align="center" style="border-radius: 5px; background-color: #1F7F4C;">
            <a rel="noopener" target="_blank" href="${fullUrl}" target="_blank" style="font-size: 18px; font-family: Helvetica, Arial, sans-serif; color: #ffffff; font-weight: bold; text-decoration: none;border-radius: 5px; padding: 12px 18px; border: 1px solid #1F7F4C; display: inline-block;">Click here to login &rarr;</a>
        </td>
    </tr>
  </table>`,
    description: `<p>your account has been created successfully. to access the app </p>`,
    authToken: jwt,
  };
  //register 
  registerSendGridKey();
  await sendgrid.send(msg);
}

function registerSendGridKey() {
  if (!process.env.SENDGRID_API_KEY) {
    console.log(
      `The SENDGRID_API_KEY env var must be set, otherwise the API won't be able to send emails.`,
      `Using debug mode which logs the email tokens instead.`
    );

  } else {
    sendgrid.setApiKey(process.env.SENDGRID_API_KEY);
  }
}
