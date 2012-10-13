PersonFinder
============

DESCRIPTION:

INSTALLATION GUIDE:



TO DO: - major stuff
============
1. Write description and installation guide
2. Make an overlay that will put bounding boxes around faces with the ability to add names/classes underneath the 
   bounding box
3. Make the recognition process happen in a concurrent loop
4. Implement more algorithms - fisherfaces, lbp, modular pca. 
5. Make the application wait between it's recognition processes
6. write a little algorithm to basically guess if a new bounding box is likely to be the same face as the last one.
   Basically this should just say if the box is a similar size and in a similar location it is likely to be the same
   person.
      With this, the application then would need a verificaition process every so many seconds.
7. Change the training to use incremental training


TO DO: - minor stuff
============
1. Give the application 2 icons, RCamera & RConfig 
2. Implement a face database viewer and the ability to add people/faces
3. Draw some custom icons to use for the app.
4. Add options menus
5. Make it so you can change the orientation.