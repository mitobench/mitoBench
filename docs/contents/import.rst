Data Import
===========


mitoBench provides different ways to import data.

Import via data upload
----------------------

mitoBench supports different file formats:

* Multi-FastA (.fasta, .fa, .fas)
* Arlequin (.arp)
* Haplogrep (.hsd)
* Excel (.xls)
* Generic file (.tsv)
* MitoProject (.mitoproj)

The uploaded data are represented in table format. Information that are in different files,
but belonging to one sample are merged into one row based on the sample name.

.. note::
   To merge information from different files, make sure that the samples have
   identical names.




Import from mitoDB
------------------

To import data from mitoDB, select *File -> Import Data from DB*. This opens a
tab in the main view where you have to enter your mitoDB login data.

.. image:: images/mitoDB_login.png
   :align: center

After login sucessfully, the user can set up a database query to specify which
data has to be loaded. More advanced user also can write their own query (SQL statement).

.. image:: images/mitoDB_query.png
   :align: center

The secified data will be loaded to the mitoBench in a separate table and can be added
to the main user table.
