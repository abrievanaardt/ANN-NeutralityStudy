#set zrange [0: 1.2]
#set format z "%.0t%E%T"
set xtic -10, 5, 10 offset -1
set ytic -10, 5, 10 offset 1
set ztic 0, 50, 200
set xrange [-10:10]
set yrange [-10:10]
#set key bottom
set xlabel 'x_1'
set ylabel 'x_2'
set zlabel 'f(x_1, x_2)'
set terminal postscript eps enhanced color font 'Helvetica,20'
set output 'step2.eps'
set lmargin 5
unset colorbox
set palette grey
set isosamples 80,80

splot floor(x + 0.5)**2 + floor(y + 0.5)**2 title '' with pm3d
