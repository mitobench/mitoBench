Data Import
===========


mitoBench provides different ways to import data.

Import via data upload
----------------------



Import from mitoDB
------------------

To import data from mitoDB, select `File -> Import Data from DB`. This opens a
window where you have to enter your mitoDB login data.

.. image:: images/mitoDB_login.png
   :align: center

After login sucessfully, the user can set up a database query to specify which
data has to be loaded. More advanced user also can write their own query (SQL statement).

.. image:: images/mitoDB_query.png
   :align: center

.. note::
   Type * to get all data from the database.

The secified data are loaded to the mitoBench in a separate table and can be added
to the main user table.
