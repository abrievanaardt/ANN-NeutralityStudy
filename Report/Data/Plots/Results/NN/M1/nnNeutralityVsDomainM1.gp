set yrange [-0.02: 1.02]
set xrange [0.05:1000]
set logscale x
set key outside above
set xlabel 'log(x_{max})'
set ylabel 'M_1' rotate by 0 offset 1.5
set ytics 0, 0.2, 1
set terminal postscript eps enhanced color font 'Helvetica,20'
set output 'nnNeutralityVsDomainM1.eps'
unset colorbox
set palette negative grey

plot 'Xor.dat' using 1:2 title 'XOR' with linespoints lc 'black' lt 1 pt 8,\
'Iris.dat' using 1:2 title 'Iris' with linespoints lc 'black' lt 2 pt 2,\
'Diabetes.dat' using 1:2 title 'Diabetes' with linespoints lc 'black' lt 3 pt 1,\
'Glass.dat' using 1:2 title 'Glass' with linespoints lc 'black' lt 4 pt 4,\
'Cancer.dat' using 1:2 title 'Cancer' with linespoints lc 'black' lt 5 pt 6,\
'Heart.dat' using 1:2 title 'Heart' with linespoints lc 'black' lt 6 pt 3
