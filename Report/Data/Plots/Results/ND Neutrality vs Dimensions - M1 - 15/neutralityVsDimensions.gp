bm = 0.15
lm = 0.12
rm = 0.95
gap = 0.03
size = 0.75
y1 = -0.02; y2 = 0.4; y3 = 0.92; y4 = 1.02

set terminal postscript eps enhanced color font 'Helvetica,20'
set output 'neutralityVsDimensionsM1.eps'

set multiplot
set xlabel 'Dimensions'
set border 1+2+8
set xtics nomirror
set ytics 0, 0.1, 0.4 nomirror
set lmargin at screen lm
set rmargin at screen rm
set bmargin at screen bm
set tmargin at screen bm + size * (abs(y2-y1) / (abs(y2-y1) + abs(y4-y3) ) )

set yrange [y1:y2]
plot 'Step 2.dat' title 'f_{{St}}_2' with linespoints lc 'black' dt 4 pt 4,\
'Step 1.dat' title 'f_{{St}}_1' with linespoints lc 'black' dt 5 pt 6,\
'AbsoluteValue.dat' title 'f_{AV}' with linespoints lc 'black' dt 6 pt 3

unset xtics
unset xlabel
set border 2+4+8
set bmargin at screen bm + size * (abs(y2-y1) / (abs(y2-y1) + abs(y4-y3) ) ) + gap
set tmargin at screen bm + size + gap
set yrange [y3:y4]
set ytic 0.96, 0.04, 1.0
set key outside above

set label 'M_1' at screen 0.03, bm + 0.5 * (size + gap) offset -1.5,-strlen("M_1")/4.0 rotate by 0

set arrow from screen lm - gap / 4.0, bm + size * (abs(y2-y1) / (abs(y2-y1)+abs(y4-y3) ) ) - gap / 4.0 to screen \
lm + gap / 4.0, bm + size * (abs(y2-y1) / (abs(y2-y1) + abs(y4-y3) ) ) + gap / 4.0 nohead

set arrow from screen lm - gap / 4.0, bm + size * (abs(y2-y1) / (abs(y2-y1)+abs(y4-y3) ) ) - gap / 4.0  + gap to screen \
lm + gap / 4.0, bm + size * (abs(y2-y1) / (abs(y2-y1) + abs(y4-y3) ) ) + gap / 4.0 + gap nohead

set arrow from screen rm - gap / 4.0, bm + size * (abs(y2-y1) / (abs(y2-y1)+abs(y4-y3) ) ) - gap / 4.0 to screen \
rm + gap / 4.0, bm + size * (abs(y2-y1) / (abs(y2-y1) + abs(y4-y3) ) ) + gap / 4.0 nohead

set arrow from screen rm - gap / 4.0, bm + size * (abs(y2-y1) / (abs(y2-y1)+abs(y4-y3) ) ) - gap / 4.0  + gap to screen \
rm + gap / 4.0, bm + size * (abs(y2-y1) / (abs(y2-y1) + abs(y4-y3) ) ) + gap / 4.0 + gap nohead

plot 'Flat.dat' title 'f_{Fl}' with linespoints lc 'black' dt 1 pt 8,\
'AlmostFlat.dat' title 'f_{IA}' with linespoints lc 'black' dt 2 pt 2,\
'Table.dat' title 'f_{IT}' with linespoints lc 'black' dt 3 pt 1

unset multiplot
