#set zrange [0: 1.2]
#set format z "%.1f"
#set xtic -100, 50, 100 offset -1
#set ytic -100, 50, 100 offset 1
#set ztic 0, 50, 200
#set xrange [-100:100]
#set yrange [-100:100]
#set key bottom
set xlabel 'Dimensions'
set ylabel 'Neutrality'
set terminal postscript eps enhanced color font 'Helvetica,20'
set output 'neutralityVsDimensions.eps'
unset colorbox
set palette grey

plot 'AlmostFlat.dat' title 'f_{IA}' with linespoints lc 'black' dt 1,\
'Table.dat' title 'f_{IT}' with linespoints lc 'black' dt 2,\
'Step 2.dat' title 'f_{{St}}_2' with linespoints lc 'black' dt 3,\
'Step 1.dat' title 'f_{{St}}_1' with linespoints lc 'black' dt 4,\
'AbsoluteValue.dat' title 'f_{AV}' with linespoints lc 'black' dt 5
