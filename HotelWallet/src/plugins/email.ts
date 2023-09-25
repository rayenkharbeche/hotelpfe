import sendgrid from '@sendgrid/mail';

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
                    <p style="font-size: 18px; font-family: Helvetica, Arial, sans-serif; color: #ffffff; font-weight: bold; padding: 12px 18px; border: 1px solid #1F7F4C; display: inline-block;">Your login token: ${emailToken}</p>
                </td>
            </tr>
          </table>`,
    dynamicTemplateData: {
      name: 'John Doe',
      email: email,
      emailToken: emailToken,
    },
  };

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
    <div>In case you lose your credentials, please use the following JWT to access your account:</div>
    <p style="font-size: 18px; font-family: Helvetica, Arial, sans-serif; color: #000000; font-weight: bold; padding: 12px 18px; border: 1px solid #1F7F4C; display: inline-block;">JWT: ${jwt}</p>
    <p>Thank you!</p>`,
    description: `<p>Your account has been created successfully. To access the app, please use the provided JWT.</p>`,
    dynamicTemplateData: {
      email: email,
      jwt: jwt,
    },
  };

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
