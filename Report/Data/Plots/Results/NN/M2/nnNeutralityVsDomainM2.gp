set yrange [-0.02: 1.02]
set key outside above
set xlabel 'x^{max}'
set ylabel 'M_2' rotate by 0 offset 1.5
set ytics 0, 0.2, 1
set terminal postscript eps enhanced color font 'Helvetica,20'
set output 'nnNeutralityVsDomainM2.eps'
unset colorbox
set palette negative grey

plot 'Xor.dat' title 'XOR' with linespoints lc 'black' dt 1 pt 8,\
'Iris.dat' title 'Iris' with linespoints lc 'black' dt 2 pt 2,\
'Diabetes.dat' title 'Diabetes' with linespoints lc 'black' dt 3 pt 1,\
'Glass.dat' title 'Glass' with linespoints lc 'black' dt 4 pt 4,\
'Cancer.dat' title 'Cancer' with linespoints lc 'black' dt 5 pt 6,\
'Heart.dat' title 'Heart' with linespoints lc 'black' dt 6 pt 3
