# ChameleonBusinessStudio

You no longer need to run Angular & Spring separately, just use 
```gradle build``` in the Angular App folder, then run Spring.
App is now 100% hosted on localhost:8080

/logout to log out
see AuthenticationService for getting logged-in user
If Angular doesn't look like it's working, try rebuilding.
After logging in, you can check the logged in user in /auth/principal

## Setting up the application

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