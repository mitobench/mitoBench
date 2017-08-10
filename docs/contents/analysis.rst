Analyses
=========

Pairwise Fst values
-------------------


.. note::
  This implementation is just a prototype. The results might be incorrect.

  > Analysis -> Calculate pairwise Fst

This calculates the pairwise Fst values based on the approach of Hudson et al.
Advanced settings are provided by the configuration dialogue.

.. image:: images/Fst_settings.png
   :align: center


* Distance method:
    * Pairwise difference
    * Jukes & Cantor
    * Kimura 2-parameters

* Gamma a value
    * This parameter is used to perform gamma correction on the distance measure

* Symbol for missing data
    * Specify symbol that is used for missing data in your samples

* allowed level of missing data
    * Specify allowed percentage of missing data per locus / position

* Slatkin's linearization
    * Linearize the Fst values with Slatkin's linearization (D = Fst / (1-Fst))

* Reynolds' distance
    * Linearize the Fst values with Reynolds' linearization (D = -ln(1-Fst))

* Save result
    * The result is displayed in the mitoBench and can be downloaded as txt file as well.
    The file location can be specified here.


Finally, the result is displayed a text format and the Fst values are
visualized as heatmap.



Haplogroups
-----------

.. note::
  Not working yet!

  > Analysis -> Calculate haplogroups

The Haplogroups are detemined by HaploGrep2 based on the FastA sequence. This
functionality is just provided in the beta version of Haplogrep2.

The calculated haplogroups are added as new column in the table view and can be
downloaded as hsd file as well.




PCA
----

  > Analysis -> PCA analysis

The principal component analysis requires a grouping of the data and the haplogroups. Both can be set in the configuration pane.
The colouring can either be set like the groups (each group gets one color)

.. image:: images/pcaConfig.png
   :align: center


or several groups can be assigned to one color

.. image:: images/pcaConfig2.png
   :align: center
