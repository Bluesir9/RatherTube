# RatherTube Server

## What?
All the code that powers the backend server used by the client application. Currently, given the limited number of
use cases, all of the code has been fit inside a single [`index.js`](index.js) file. 

## How do I run this?
1. After cloning the project locally, navigate to the `server` directory.
2. Create a file by the name `.env` at the root path. This file is meant to hold all the environment variables. Locally, the file would look something like this:
```
client_origin=http://localhost:8080
```
3. Now run the `npm install` command to install all the dependencies declared in the `package.json` file. Make sure to install any errors encountered while installing these dependencies.
4. Now run the `npm run start` command to run the backend application. 
5. If you see a message saying `RatherTube server running at http://localhost:3000` then the project was setup successfully.
6. Fin.
