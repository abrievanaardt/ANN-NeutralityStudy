set yrange [-0.02: 1.02]
set key outside above
set xlabel 'ln(x^{max})'
set ylabel 'M_2' rotate by 0 offset 1.5
set ytics 0, 0.2, 1
set terminal postscript eps enhanced color font 'Helvetica,20'
set output 'nnNeutralityVsDomainM2.eps'
unset colorbox
set palette negative grey

plot 'Xor.dat' using (log($1)):2 title 'XOR' with linespoints lc 'black' dt 1 pt 8,\
'Iris.dat' using (log($1)):2 title 'Iris' with linespoints lc 'black' dt 2 pt 2,\
'Diabetes.dat' using (log($1)):2 title 'Diabetes' with linespoints lc 'black' dt 3 pt 1,\
'Glass.dat' using (log($1)):2 title 'Glass' with linespoints lc 'black' dt 4 pt 4,\
'Cancer.dat' using (log($1)):2 title 'Cancer' with linespoints lc 'black' dt 5 pt 6,\
'Heart.dat' using (log($1)):2 title 'Heart' with linespoints lc 'black' dt 6 pt 3
