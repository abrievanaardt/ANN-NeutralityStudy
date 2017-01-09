set yrange [-0.02: 1.02]
set key outside above
set xlabel 'Dimensions'
set ylabel 'M_2' rotate by 0 offset 1.5
set ytics 0, 0.2, 1
set terminal postscript eps enhanced color font 'Helvetica,20'
set output 'neutralityVsDimensionsM2.eps'
unset colorbox
set palette negative grey

plot 'Flat.dat' title 'f_{Fl}' with linespoints lc 'black' dt 1 pt 8,\
'AlmostFlat.dat' title 'f_{IA}' with linespoints lc 'black' dt 2 pt 2,\
'Table.dat' title 'f_{IT}' with linespoints lc 'black' dt 3 pt 1,\
'Step 2.dat' title 'f_{{St}}_2' with linespoints lc 'black' dt 4 pt 4,\
'Step 1.dat' title 'f_{{St}}_1' with linespoints lc 'black' dt 5 pt 6,\
'AbsoluteValue.dat' title 'f_{AV}' with linespoints lc 'black' dt 6 pt 3
