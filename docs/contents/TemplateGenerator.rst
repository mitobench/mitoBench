.. _templategenerator-label:

TemplateGenerator
=================

How to prepare your data for database import
---------------------------------------------

The TemplateGenerator helps you to prepare your data for the database import.

Current version: :download:`TemplateGenerator v0.3 <jar/TemplateGenerator_v0.3.jar>`

Requirements: Java Version 1.8 or higher and JavaFX.

The tool can be run with

*java -jar TemplateGenerator.jar*

This will open a GUI where you can select all attributes tat you want to add to the database. The mandatory fields cannot
be deselected.
Hovering over the attribute names shows the corresponding description.


The tool creates a template based on the selected attributes. All values can be entered and (for the time being) sent to me (judith.neukamm@uzh.ch).
I will then take care of the maintenance of the database. The MT Sequence itself can also be copied into the template or handed in as (multi) fasta file.
In the case of the (multi)fasta file, the header line must match the name / value in the accession_ID column.



Previous releases: :download:`TemplateGenerator v0.1 <jar/TemplateGenerator.jar>`
:download:`TemplateGenerator v0.2 <jar/TemplateGenerator_v0.2.jar>`
