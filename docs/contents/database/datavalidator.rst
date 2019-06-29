.. _datavalidator-label:

DataValidator
=================

How to validate your data for database import
---------------------------------------------

This tool validates the data before uploading to the mitoBench database. It checks, if

    * all mandatory fields are set
    * all values are in correct format
    * number of sequences and meta information are equal

The glossary contains a definition for each attribute: https://docs.google.com/spreadsheets/d/18BsU3wdWvpE5emqy7TUBUO5Si-m-X368D1b-E4s_n5g/edit?usp=sharing

How to run:

java -jar dataValidator_<version>.jar -i metaInfo.csv -f sequences.fasta

Output:

Report as text file. This report tells you if data can be uploaded ot not.


.. note::
   It is also possible so set a directory (-d) containing multiple pairs of fasta and csv files instead of specifying them
   individually.






