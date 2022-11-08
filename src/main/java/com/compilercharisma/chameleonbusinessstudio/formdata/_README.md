WebFlux appears to not support binding most data types directly to form data.
For example, I couldn't bind a string field to a single string in the controller
method.

However, WebFlux does support binding to objects with fields.
Go figure.