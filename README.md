# AlitaManager

### Description
Appointmetn Management tool for a small business.  Allows workers, that are assigned a role of _EMPLOYEE_, and system administrator(or a superviser), who is assigned a role of _ADMIN_, to view and manipulate scheduled appointments.  _ADMIN_ is also able manipulate the data considering services that business provides, and employee data.  All information is stored on a remote database, provided by __Mongo Atlas__

### Technology used
 - Java / Spring Boot (Spring Web, Spring Security through JWT Tokens)
 - Mongo Atlas
 - ReactJs
 - Hosted on <a href="https://alita-manager-app.herokuapp.com/home">__Heroku__</a>
 
 ### Views
 
 
 - Main page view (one button page, menue button in top-left corner).
 
 - Appointment Form view. All form fields except Name are required, and form will not be sent and a message 
   will appear if any of the fields have wrong or no input.
   
 - Login page and sidebar menue view for a logged out user.
 
 - _ADMIN_ user layout view and _ADMIN_ user sidebar menue. _ADMIN_ is able to change or delete appointments, services, employees, and clients.
 
 - _EMPLOYEE_ user layout view. _EMPLOYEE_ user sidebar menue is identical to the _ADMIN_ user's one but "ADMIN Page" is changed to "EMPLOYEE Page".
 
 - Extra information about the appointment could be viewed by clicking on the appointment's service name.
