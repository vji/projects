All files are included in zip file. run bTreeIndex.java

Done : BONUS Wildcard Search

input these one by one followed by enter:
1). single term -i used "new"
2). multiple terms
3). wildcard term

-----------------------------------------------
Output
-----------------------------------------------
ROOT term  -  new
 adding home to left of new
 adding sales to right of new
 adding top to right of sales
 adding forecasts to left of home
 adding rise to left of sales
 adding in to right of home
 adding july to right of in
 adding increase to left of july
------------------------------
Tree Index
-------------------------------
forecasts: ( 0)
home: ( 0 1 2 3)
in: ( 1 2)
increase: ( 2)
july: ( 1 2 3)
new: ( 0 3)
rise: ( 1 3)
sales: ( 0 1 2 3)
top: ( 0)
-------------------------------
SEARCH: 
Enter single term
top
[0]

enter multiple terms
home sales
[0, 1, 2, 3]
Enter wildcard term
ho

Wild Card Searched: ho
[0, 1, 2, 3]