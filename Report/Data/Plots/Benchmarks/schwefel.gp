set format z "%.1f"
set xtic -1, 0.5, 1 offset -0.8
set ytic -1, 0.5, 1 offset 0.8
set ztic 0, 0.2, 1
#set zrange [0:1]
set xrange [-1:1]
set yrange [-1:1]
#set key bottom
set xlabel 'x_1'
set ylabel 'x_2'
set zlabel 'f(x_1, x_2)' offset -1,0
set terminal postscript eps enhanced color font 'Helvetica,20'
set output 'schwefel.eps'
set lmargin 5.2
unset colorbox
set palette grey
set isosamples 80,80

splot  abs(x) > abs(y) ? abs(x) : abs(y) title '' with pm3d
