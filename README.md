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
 
  ![main_page_screenshot](https://github.com/warferton/AlitaManager/blob/master/alitascreens/Screenshot%202021-02-05%20at%2019.52.21.png)
 
 - Appointment Form view. All form fields except Name are required, and form will not be sent and a message 
   will appear if any of the fields have wrong or no input.
   
   ![add_appointmet_screenshot](https://github.com/warferton/AlitaManager/blob/master/alitascreens/Screenshot%202021-02-05%20at%2019.13.11.png)
   
 - Login page and sidebar menue view for a logged out user.
   ![login_and_sidebar_screenshot](https://github.com/warferton/AlitaManager/blob/master/alitascreens/Screenshot%202021-02-05%20at%2019.14.57.png)
 
 - _ADMIN_ user layout view and _ADMIN_ user sidebar menu. _ADMIN_ is able to change or delete appointments, services, employees, and clients.
  ![admin_layout_screenshot](https://github.com/warferton/AlitaManager/blob/master/alitascreens/Screenshot%202021-02-05%20at%2019.19.09.png)
  
  ![admin_sidebar_menu_screenshot](https://github.com/warferton/AlitaManager/blob/master/alitascreens/Screenshot%202021-02-05%20at%2019.20.29.png)
 
 - _EMPLOYEE_ user layout view. _EMPLOYEE_ user sidebar menue is identical to the _ADMIN_ user's one but "ADMIN Page" is changed to "EMPLOYEE Page".
  ![employee_layout_screenshot](https://github.com/warferton/AlitaManager/blob/master/alitascreens/Screenshot%202021-02-05%20at%2019.23.36.png)
 
 - Extra information about the appointment could be viewed by clicking on the appointment's service name.
  ![extra_info_screenshot](https://github.com/warferton/AlitaManager/blob/master/alitascreens/Screenshot%202021-02-05%20at%2019.40.07.png)
