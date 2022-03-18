# ChameleonBusinessStudio

## Setting up the application

### Database

1. Run the following commands in mysql:

```
CREATE DATABASE chameleon_business_studio;
CREATE USER 'springuser'@'%' IDENTIFIED BY password;
GRANT ALL ON chameleon_business_studio.* TO 'springuser'@'%';
```

where ```password``` is a password of your choice (remember it for later!)

2. navigate to ./src/main/resources/, and copy application.properties.txt as application.properties
3. fill in the password field in the application.properties file