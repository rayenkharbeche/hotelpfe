import express, { Request, Response } from 'express';
import * as dotenv from 'dotenv';
import { Router_category } from './routes/category.routes';
import { Router_admin } from './routes/admin.routes';
import { Router_service } from './routes/extra-service.routes';
import { Router_serviceProvider } from './routes/service-provider.routes';
import { Router_item } from './routes/item.routes';
import { Router_command } from './routes/command.routes';
import { Router_client } from './routes/client.routes';
import { RouterClientAuth } from './routes/passwordless-authentication.routes';
import { Router_history } from './routes/history.routes';
import { Router_authentication} from './routes/authentication.routes';
//import { Router_serviceProviderauth} from './routes/serviceprovider-auth.routes';
import cors from 'cors';



dotenv.config();

const app = express();
if (!process.env.PORT) {
  console.log("no port found!");
  process.exit(1);
}
const port = process.env.PORT;
const allowedOrigins = ['http://localhost:4200'];

const options: cors.CorsOptions = {
  origin: allowedOrigins
};

app.get('/', (req: Request, res: Response) => {
  res.send('back end server is running');
});
app.use(cors(options));
app.use(express.json())
app.use(express.urlencoded({ extended: true }))

app.listen(port, () => {
  console.log(`⚡️[server]: Server is running at http://localhost:${port}`);
});
app.use('/api/category', Router_category);
app.use('/api/admin', Router_admin);
app.use('/api/service', Router_service);
app.use('/api/serviceProvider', Router_serviceProvider);
app.use('/api/item', Router_item);
app.use('/api/client', Router_client);
app.use('/api/command', Router_command);
app.use('/api/client', RouterClientAuth);
app.use('/api/history', Router_history);
app.use('/api/account', Router_authentication);
//app.use('/api/account',  Router_serviceProviderauth);













