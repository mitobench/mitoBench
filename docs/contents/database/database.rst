.. _database-label:

Database
============

This database contains currently 22,880 complete modern and ancient published mitochondrial genomes from the 1000 Genome
Project and other published sources with the aim of continuous expansion
(see all publications `here <https://docs.google.com/spreadsheets/d/1AM9izcYYnGUoZrBaVGNWBQzldTgossOYX23fgJK474o/edit?usp=sharing>`_).

Access
-------

The database can be :ref:`accessed <databaseaccess-label>` via mitoBench.

Glossary
---------
In addition to the accession id and the actual sequence, the database contains 72 attributes describing the data such as
geographic location, sequencing technologies, and sequence quality information. A complete list describing each attribute in detail including
examples can be found `here <https://bit.ly/2WZtYWs>`_.

DataValidator
-------------
This tool validates the data intended to be uploaded to the database. See :ref:`DataValidator <datavalidator-label>` for
more details.

DataCompleter
-------------
DataCompleter is calculating several attributes automatically, such as numbers of Ns in the sequence. Moreover, it completes
geographic information. For example, if latitude and longitude of the sampling location is given, it determines the
city, country, region, and continent. For more information, see :ref:`DataCompleter <datacompleter-label>`.


.. note::
   DataValidator and DataCompleter can also called directly within mitoBench.



Future plans:
-------------
The user will be able to upload own data to the database via mitoBench. We will provide a template where the data can be
filled in (:download:`Download template <template.csv>`) and imported into mitoBench.


