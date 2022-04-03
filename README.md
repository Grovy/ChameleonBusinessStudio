# ChameleonBusinessStudio

You no longer need to run Angular & Spring separately, just use 
```gradle build``` in the Angular App folder, then run Spring.
App is now 100% hosted on localhost:8080

/logout to log out
see AuthenticationService for getting logged-in user
If Angular doesn't look like it's working, try rebuilding.
After logging in, you can check the logged in user in /auth/principal

## Setting up the application

### Angular CLI

1. Make sure you have Node.Js installed in your local machine

https://nodejs.org/en/

2. At the time of cloning the repository, if you go to ChameleonBusinessStudio/angular-app and there is no directory called /node_modules
    
    1. First change directory to /angular-app
    2. Run the command `> npm i`
    3. You should see the /node_modules installed in the directory
    4. Run the command `> npm install -g @angular/cli` to install Angular CLI globally
    5. Lastly, install typescript by running the command `> npm install -g typescript`

3. By then, hopefully all the error messages are gone now

4. Also, since we will be using most of the styles and component from @Angular/material it is important that we import and make use of the UI Package

    1. Once everything is installed, go to `/angular-app/` directory
    2. run the command `> npm install --save @angular/material @angular/cdk`
    3. Double check that under `/angular-app/angular.json` in line 32, the first stylesheet Angular is using is `"./node_modules/@angular/material/prebuilt-themes/indigo-pink.css"`, if its not, then you can go ahead and copy and paste it as the first element of the json array.

### Database


1. Run the following commands in mysql:

```
CREATE DATABASE chameleon_business_studio;
CREATE USER 'springuser'@'%' IDENTIFIED BY 'password';
GRANT ALL ON chameleon_business_studio.* TO 'springuser'@'%';
```

where ```password``` is a password of your choice (remember it for later!)

2. navigate to ./src/main/resources/, and copy application.properties.txt as application.properties
3. fill in the password field in the application.properties file

### Google API Credentials

1. create your Google API credentials for Login with Google
    a. https://developers.google.com/identity/gsi/web/guides/overview
    b. https://developers.google.com/identity/gsi/web/guides/get-google-api-clientid
add "http://localhost:8080" and "http://localhost" as authorized JS origins
add "http://localhost:8080/login/oauth2/code/google" as an authorized REDIRECT
be sure to update application.properties!

## Launching the application

To run the application, you'll need to make sure your Angular App is built, 
then run spring via



```gradle bootRun```
