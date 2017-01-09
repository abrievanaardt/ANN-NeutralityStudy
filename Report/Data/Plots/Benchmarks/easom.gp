set zrange [0: 1.2]
set format z "%.1f"
set xtic -10, 5, 10 offset -0.8
set ytic -10, 5, 10 offset 0.8
set xrange [-10:10]
set yrange [-10:10]
#set key bottom
set xlabel 'x_1'
set ylabel 'x_2'
set zlabel 'f(x_1, x_2)'
set terminal postscript eps enhanced color font 'Helvetica,20'
set output 'easom.eps'
set lmargin 5
unset colorbox
set palette grey
set isosamples 80,80

splot  1-(cos(x) * cos(y) * exp(-(x - pi)**2 - (y - pi)**2)) title '' with pm3d
