#set zrange [0: 1.2]
#set format z "%.0t%E%T"
set xtic -100, 40, 100 offset -1
set ytic -100, 40, 100 offset 1
set ztic 0, 5000, 20000
set xrange [-100:100]
set yrange [-100:100]
#set key bottom
set xlabel 'x_1'
set ylabel 'x_2'
set zlabel 'f(x_1, x_2)' offset -3
set terminal postscript eps enhanced color font 'Helvetica,20'
set output 'step1.eps'
set lmargin 8
unset colorbox
set palette grey
set isosamples 80,80

splot floor(x + 0.5)**2 + floor(y + 0.5)**2 title '' with pm3d
